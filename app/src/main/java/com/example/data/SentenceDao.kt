package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.Sentence
import kotlinx.coroutines.flow.Flow

@Dao
interface SentenceDao {
    @Query("SELECT * FROM sentences ORDER BY isCustom DESC, id ASC")
    fun getAllSentences(): Flow<List<Sentence>>

    @Query("SELECT * FROM sentences WHERE category = :category ORDER BY id ASC")
    fun getSentencesByCategory(category: String): Flow<List<Sentence>>

    @Query("SELECT * FROM sentences WHERE isBookmarked = 1 ORDER BY id DESC")
    fun getBookmarkedSentences(): Flow<List<Sentence>>

    @Query("SELECT * FROM sentences WHERE practiceCount > 0 AND (CAST(successCount AS REAL) / practiceCount) < 0.7 ORDER BY (CAST(successCount AS REAL) / practiceCount) ASC")
    fun getWeakSentences(): Flow<List<Sentence>>

    @Query("SELECT * FROM sentences WHERE id = :id LIMIT 1")
    suspend fun getSentenceById(id: Long): Sentence?

    @Query("SELECT COUNT(*) FROM sentences")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM sentences WHERE korean = :korean")
    suspend fun countByKorean(korean: String): Int

    @Query("DELETE FROM sentences WHERE isCustom = 0")
    suspend fun deletePresetSentences()

    @Query("DELETE FROM sentences WHERE patternTip LIKE '%' || :pattern || '%'")
    suspend fun deleteByTipPattern(pattern: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sentences: List<Sentence>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sentence: Sentence): Long

    @Update
    suspend fun update(sentence: Sentence)

    @Delete
    suspend fun delete(sentence: Sentence)

    @Query("UPDATE sentences SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Long, isBookmarked: Boolean)

    @Query("UPDATE sentences SET practiceCount = practiceCount + 1, successCount = successCount + :isSuccess, lastPracticedAt = :timestamp WHERE id = :id")
    suspend fun recordPracticeResult(id: Long, isSuccess: Int, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM sentences WHERE korean LIKE '%' || :query || '%' OR english LIKE '%' || :query || '%'")
    fun searchSentences(query: String): Flow<List<Sentence>>
}
