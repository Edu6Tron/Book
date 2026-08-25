package com.edu6tron.spiritualcompanion.nativepreview.media

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AartiAudioAssociationCodecTest {
  @Test
  fun `round trip preserves a valid local document association without separator ambiguity`() {
    val associations = mapOf(
      "sukhkarta-dukhharta" to AartiAudioAssociation(
        uri = "content://com.android.providers.media.documents/document/audio%3A7?album=morning",
        label = "Sukhkarta; personal recording, v1",
      ),
    )

    val encoded = AartiAudioAssociationCodec.encode(associations)

    assertEquals(associations, AartiAudioAssociationCodec.decode(encoded))
  }

  @Test
  fun `codec rejects non-document and malformed association values`() {
    val invalid = AartiAudioAssociation(uri = "https://example.com/media.mp3", label = "Online media")

    assertFalse(AartiAudioAssociationCodec.isValid("sukhkarta-dukhharta", invalid))
    assertTrue(AartiAudioAssociationCodec.decode("bad=not-base64,also-not-base64").isEmpty())
  }

  @Test
  fun `codec ignores malformed entries while retaining bounded valid local associations`() {
    val valid = AartiAudioAssociation(uri = "content://audio/1", label = "Local audio")
    val encoded = AartiAudioAssociationCodec.encode(mapOf("vakratunda" to valid))

    assertEquals(mapOf("vakratunda" to valid), AartiAudioAssociationCodec.decode("bad=oops,also-oops;$encoded"))
  }
}
