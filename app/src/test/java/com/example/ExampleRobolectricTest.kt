package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.DiffStatus
import com.example.model.MatchGrade
import com.example.util.SentenceEvaluator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("EngSpeak", appName)
  }

  @Test
  fun `test sentence evaluator perfect match`() {
    val result = SentenceEvaluator.evaluate(
      userAnswer = "I'm going to cook dinner tonight.",
      primaryTarget = "I am going to cook dinner tonight."
    )
    assertEquals(MatchGrade.PERFECT, result.grade)
    assertTrue(result.accuracy >= 95)
  }

  @Test
  fun `test sentence evaluator contraction expansion`() {
    val result = SentenceEvaluator.evaluate(
      userAnswer = "Why don't we take a short break?",
      primaryTarget = "Why do not we take a short break?"
    )
    assertTrue(result.accuracy >= 90)
  }

  @Test
  fun `test sentence evaluator alternative acceptance`() {
    val result = SentenceEvaluator.evaluate(
      userAnswer = "I'll buy you coffee",
      primaryTarget = "Let me buy you a cup of coffee.",
      alternatives = listOf("I'll buy you coffee.", "Let me buy you coffee.")
    )
    assertEquals(MatchGrade.PERFECT, result.grade)
  }
}
