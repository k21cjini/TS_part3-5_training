package com.example.data

import com.example.model.HistoryRecord
import com.example.model.Sentence
import kotlinx.coroutines.flow.Flow

class SentenceRepository(
    private val sentenceDao: SentenceDao,
    private val historyDao: HistoryDao
) {
    val allSentences: Flow<List<Sentence>> = sentenceDao.getAllSentences()
    val bookmarkedSentences: Flow<List<Sentence>> = sentenceDao.getBookmarkedSentences()
    val weakSentences: Flow<List<Sentence>> = sentenceDao.getWeakSentences()
    val recentHistory: Flow<List<HistoryRecord>> = historyDao.getRecentHistory()
    val totalAttempts: Flow<Int> = historyDao.getTotalAttemptsCount()
    val averageAccuracy: Flow<Double?> = historyDao.getAverageAccuracy()

    fun getSentencesByCategory(category: String): Flow<List<Sentence>> =
        sentenceDao.getSentencesByCategory(category)

    fun searchSentences(query: String): Flow<List<Sentence>> =
        sentenceDao.searchSentences(query)

    suspend fun getSentenceById(id: Long): Sentence? =
        sentenceDao.getSentenceById(id)

    suspend fun insertSentence(sentence: Sentence): Long =
        sentenceDao.insert(sentence)

    suspend fun updateSentence(sentence: Sentence) =
        sentenceDao.update(sentence)

    suspend fun deleteSentence(sentence: Sentence) =
        sentenceDao.delete(sentence)

    suspend fun toggleBookmark(id: Long, isBookmarked: Boolean) =
        sentenceDao.updateBookmark(id, isBookmarked)

    suspend fun recordAttempt(sentenceId: Long, accuracy: Int, korean: String, expected: String, userAnswer: String, modeUsed: String, durationSec: Int) {
        val isSuccess = if (accuracy >= 70) 1 else 0
        sentenceDao.recordPracticeResult(sentenceId, isSuccess)
        historyDao.insertRecord(
            HistoryRecord(
                sentenceId = sentenceId,
                korean = korean,
                expectedEnglish = expected,
                userAnswer = userAnswer,
                accuracy = accuracy,
                modeUsed = modeUsed,
                durationSeconds = durationSec
            )
        )
    }

    suspend fun ensureDefaultSentencesLoaded() {
        val defaultList = DefaultSentences.getList()
        if (sentenceDao.getCount() == 0) {
            sentenceDao.insertAll(defaultList)
        } else {
            // Remove deprecated YouTube preset sentences if present
            sentenceDao.deleteByTipPattern("김재우")
            sentenceDao.deleteByTipPattern("Capture")
            sentenceDao.deleteByTipPattern("vGsfHHJeak8")
            // Ensure any missing default sentences (like new YouTube lecture sentences) are inserted
            for (sentence in defaultList) {
                if (sentenceDao.countByKorean(sentence.korean) == 0) {
                    sentenceDao.insert(sentence)
                }
            }
        }
    }

    suspend fun resetOrSyncDefaultSentences() {
        // Remove deprecated sentences
        sentenceDao.deleteByTipPattern("김재우")
        sentenceDao.deleteByTipPattern("Capture")
        sentenceDao.deleteByTipPattern("vGsfHHJeak8")
        val defaultList = DefaultSentences.getList()
        for (sentence in defaultList) {
            if (sentenceDao.countByKorean(sentence.korean) == 0) {
                sentenceDao.insert(sentence)
            }
        }
    }
}
