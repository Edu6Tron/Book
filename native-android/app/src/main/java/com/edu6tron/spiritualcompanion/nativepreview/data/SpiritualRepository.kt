package com.edu6tron.spiritualcompanion.nativepreview.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class DailyPractice(
  val id: String,
  val title: String,
  val detail: String,
  val completed: Boolean,
)

data class StoredSpiritualState(
  val practices: List<DailyPractice> = emptyList(),
  val favouriteIds: Set<String> = emptySet(),
  val japaCount: Int = 0,
  val ritualAlarms: List<RitualAlarmEntity> = emptyList(),
)

@Singleton
class SpiritualRepository @Inject constructor(
  private val dailyPracticeDao: DailyPracticeDao,
  private val appStateDao: AppStateDao,
  private val ritualAlarmDao: RitualAlarmDao,
) {
  private val defaults = listOf(
    DailyPractice("pause", "Pause for one minute", "Settle your attention with three unhurried breaths.", false),
    DailyPractice("reading", "Read one meaningful line", "Choose an Aarti or a festival reflection that feels relevant today.", false),
    DailyPractice("kindness", "Offer one act of kindness", "Make your practice visible through a small, deliberate act.", false),
  )

  private val japaKey = "japa_count"

  fun observeDailyPractices(): Flow<List<DailyPractice>> =
    dailyPracticeDao.observeAll().map { stored ->
      val storedById = stored.associateBy { it.id }
      defaults.map { default ->
        val saved = storedById[default.id]
        default.copy(completed = saved?.completed ?: default.completed)
      }
    }

  fun observeState(): Flow<StoredSpiritualState> = combine(
    observeDailyPractices(),
    appStateDao.observeFavouriteIds(),
    appStateDao.observeLong(japaKey),
    ritualAlarmDao.observeAll(),
  ) { practices, favourites, japa, alarms ->
    StoredSpiritualState(
      practices = practices,
      favouriteIds = favourites.toSet(),
      japaCount = (japa ?: 0L).toInt().coerceAtLeast(0),
      ritualAlarms = alarms,
    )
  }

  suspend fun togglePractice(id: String) {
    val definition = defaults.firstOrNull { it.id == id } ?: return
    val saved = dailyPracticeDao.findById(id)
    dailyPracticeDao.upsert(
      DailyPracticeEntity(
        id = definition.id,
        title = definition.title,
        completed = !(saved?.completed ?: definition.completed),
        updatedAt = System.currentTimeMillis(),
      ),
    )
  }

  suspend fun toggleFavourite(aartiId: String) {
    if (appStateDao.isFavourite(aartiId)) {
      appStateDao.removeFavourite(aartiId)
    } else {
      appStateDao.saveFavourite(FavouriteAartiEntity(aartiId = aartiId))
    }
  }

  suspend fun incrementJapa() {
    val current = appStateDao.getLong(japaKey) ?: 0L
    appStateDao.savePreference(AppPreferenceEntity(japaKey, current + 1L))
  }

  suspend fun resetJapa() {
    appStateDao.savePreference(AppPreferenceEntity(japaKey, 0L))
  }

  suspend fun saveAlarm(alarm: RitualAlarmEntity) {
    ritualAlarmDao.upsert(alarm.copy(updatedAt = System.currentTimeMillis()))
  }

  suspend fun deleteAlarm(alarm: RitualAlarmEntity) {
    ritualAlarmDao.delete(alarm)
  }
}
