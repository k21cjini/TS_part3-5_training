package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentences")
data class Sentence(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "일상회화", "패턴영어", "영어식 사고훈련", "비즈니스", "여행", "내 문장장"
    val korean: String,
    val english: String,
    val acceptableAnswers: String = "", // Delimited alternative phrases (e.g. "I'm going to... | I will...")
    val patternTip: String = "", // Grammar/Thinking structure tip
    val difficulty: String = "초급", // "초급", "중급", "고급"
    val isBookmarked: Boolean = false,
    val practiceCount: Int = 0,
    val successCount: Int = 0,
    val lastPracticedAt: Long = 0L,
    val isCustom: Boolean = false
) {
    fun getAlternatives(): List<String> {
        val list = mutableListOf(english)
        if (acceptableAnswers.isNotBlank()) {
            val splits = acceptableAnswers.split("|").map { it.trim() }.filter { it.isNotEmpty() }
            list.addAll(splits)
        }
        return list.distinct()
    }

    val accuracyPercent: Int
        get() = if (practiceCount > 0) ((successCount.toDouble() / practiceCount) * 100).toInt() else 0
}
