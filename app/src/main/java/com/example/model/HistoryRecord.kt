package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_records")
data class HistoryRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sentenceId: Long,
    val korean: String,
    val expectedEnglish: String,
    val userAnswer: String,
    val accuracy: Int,
    val modeUsed: String, // "VOICE", "TEXT"
    val durationSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
