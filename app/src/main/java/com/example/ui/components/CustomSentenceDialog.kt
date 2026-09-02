package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Sentence

@Composable
fun CustomSentenceDialog(
    initialSentence: Sentence? = null,
    onDismiss: () -> Unit,
    onSave: (korean: String, english: String, patternTip: String, alternatives: String) -> Unit
) {
    var korean by remember { mutableStateOf(initialSentence?.korean.orEmpty()) }
    var english by remember { mutableStateOf(initialSentence?.english.orEmpty()) }
    var patternTip by remember { mutableStateOf(initialSentence?.patternTip.orEmpty()) }
    var alternatives by remember { mutableStateOf(initialSentence?.acceptableAnswers.orEmpty()) }

    var isKoreanError by remember { mutableStateOf(false) }
    var isEnglishError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialSentence == null) "새 문장 추가하기" else "문장 수정하기",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = korean,
                    onValueChange = {
                        korean = it
                        if (it.isNotBlank()) isKoreanError = false
                    },
                    label = { Text("한국어 질문 문장 *") },
                    placeholder = { Text("예: 나 지금 출발하려고 해.") },
                    isError = isKoreanError,
                    supportingText = if (isKoreanError) { { Text("한글 문장을 입력해주세요.") } } else null,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_korean_input")
                )

                OutlinedTextField(
                    value = english,
                    onValueChange = {
                        english = it
                        if (it.isNotBlank()) isEnglishError = false
                    },
                    label = { Text("영어 모범 답변 *") },
                    placeholder = { Text("예: I'm about to leave now.") },
                    isError = isEnglishError,
                    supportingText = if (isEnglishError) { { Text("영어 문장을 입력해주세요.") } } else null,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_english_input")
                )

                OutlinedTextField(
                    value = alternatives,
                    onValueChange = { alternatives = it },
                    label = { Text("기타 인정 답변 (선택, | 로 구분)") },
                    placeholder = { Text("예: I am leaving now. | I'm heading out now.") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = patternTip,
                    onValueChange = { patternTip = it },
                    label = { Text("영어식 사고 팁 / 핵심 패턴 (선택)") },
                    placeholder = { Text("예: be about to + 동사 = 막 ~하려던 참이다") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val k = korean.trim()
                    val e = english.trim()
                    if (k.isBlank()) {
                        isKoreanError = true
                        return@Button
                    }
                    if (e.isBlank()) {
                        isEnglishError = true
                        return@Button
                    }
                    onSave(k, e, patternTip.trim(), alternatives.trim())
                    onDismiss()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("save_custom_sentence_button")
            ) {
                Text("저장하기")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
