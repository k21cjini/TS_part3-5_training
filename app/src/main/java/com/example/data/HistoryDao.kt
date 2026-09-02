package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.model.HistoryRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_records ORDER BY timestamp DESC LIMIT 100")
    fun getRecentHistory(): Flow<List<HistoryRecord>>

    @Insert
    suspend fun insertRecord(record: HistoryRecord): Long

    @Query("SELECT COUNT(*) FROM history_records")
    fun getTotalAttemptsCount(): Flow<Int>

    @Query("SELECT AVG(accuracy) FROM history_records")
    fun getAverageAccuracy(): Flow<Double?>

    @Query("DELETE FROM history_records")
    suspend fun clearHistory()
}
