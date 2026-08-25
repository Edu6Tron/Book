package com.edu6tron.spiritualcompanion.nativepreview.media

import java.util.Base64

/**
 * A deliberately chosen association between one bundled Aarti identifier and one user-selected
 * local audio document. This remains only in the app's private on-device database; it is never
 * logged, uploaded, or used for provider media.
 */
data class AartiAudioAssociation(
  val uri: String,
  val label: String,
)

/**
 * Bounded local-only persistence format for per-Aarti audio choices. URI and label fields are
 * Base64-url encoded so separator characters in document URIs or file names cannot corrupt data.
 */
object AartiAudioAssociationCodec {
  private const val MAX_ASSOCIATIONS = 80
  private const val MAX_URI_LENGTH = 8_192
  private const val MAX_LABEL_LENGTH = 160
  private val aartiIdPattern = Regex("[a-z0-9-]+")

  fun encode(associations: Map<String, AartiAudioAssociation>): String? = associations
    .asSequence()
    .filter { (aartiId, association) -> isValid(aartiId, association) }
    .sortedBy { it.key }
    .take(MAX_ASSOCIATIONS)
    .joinToString(separator = ";") { (aartiId, association) ->
      "$aartiId=${encodeField(association.uri)},${encodeField(association.label)}"
    }
    .takeIf { it.isNotBlank() }

  fun decode(stored: String?): Map<String, AartiAudioAssociation> = stored
    ?.split(';')
    ?.asSequence()
    ?.mapNotNull { entry ->
      val separator = entry.indexOf('=')
      if (separator <= 0) return@mapNotNull null
      val aartiId = entry.substring(0, separator)
      val fields = entry.substring(separator + 1).split(',', limit = 2)
      if (fields.size != 2) return@mapNotNull null
      val association = AartiAudioAssociation(
        uri = decodeField(fields[0]) ?: return@mapNotNull null,
        label = decodeField(fields[1]) ?: return@mapNotNull null,
      )
      if (isValid(aartiId, association)) aartiId to association else null
    }
    ?.take(MAX_ASSOCIATIONS)
    ?.toMap()
    .orEmpty()

  fun isValid(aartiId: String, association: AartiAudioAssociation): Boolean =
    aartiId.matches(aartiIdPattern) &&
      association.uri.startsWith("content://") &&
      association.uri.length <= MAX_URI_LENGTH &&
      association.label.isNotBlank() &&
      association.label.length <= MAX_LABEL_LENGTH

  private fun encodeField(value: String): String = Base64.getUrlEncoder().withoutPadding()
    .encodeToString(value.toByteArray(Charsets.UTF_8))

  private fun decodeField(value: String): String? = runCatching {
    String(Base64.getUrlDecoder().decode(value), Charsets.UTF_8)
  }.getOrNull()
}
