package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnswerMode
import com.example.model.QuestionMode
import com.example.model.SessionModeConfig

@Composable
fun ModeSelectorDialog(
    currentConfig: SessionModeConfig,
    onDismiss: () -> Unit,
    onSave: (SessionModeConfig) -> Unit
) {
    var selectedQuestionMode by remember { mutableStateOf(currentConfig.questionMode) }
    var selectedAnswerMode by remember { mutableStateOf(currentConfig.answerMode) }
    var selectedSpeed by remember { mutableFloatStateOf(currentConfig.speechSpeed) }
    var autoPlayEnglish by remember { mutableStateOf(currentConfig.autoPlayEnglishOnResult) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "모드 설정",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "학습 모드 설정",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Question Presentation Mode
                Text(
                    text = "1. 한글 질문 출제 방식",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuestionModeOptionCard(
                        title = "자동 음성 + 텍스트",
                        description = "한글 음성(TTS)으로 먼저 읽어주고 텍스트 표시",
                        icon = Icons.Default.VolumeUp,
                        isSelected = selectedQuestionMode == QuestionMode.VOICE_AUTO,
                        onClick = { selectedQuestionMode = QuestionMode.VOICE_AUTO }
                    )

                    QuestionModeOptionCard(
                        title = "음성 듣기 집중 모드",
                        description = "텍스트를 가리고 귀로만 듣고 영어로 생각하기",
                        icon = Icons.Default.Headphones,
                        isSelected = selectedQuestionMode == QuestionMode.VOICE_ONLY,
                        onClick = { selectedQuestionMode = QuestionMode.VOICE_ONLY }
                    )

                    QuestionModeOptionCard(
                        title = "텍스트 카드 전용",
                        description = "음성 없이 조용히 화면 텍스트로만 학습",
                        icon = Icons.Default.Chat,
                        isSelected = selectedQuestionMode == QuestionMode.TEXT_ONLY,
                        onClick = { selectedQuestionMode = QuestionMode.TEXT_ONLY }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 2. Answer Input Mode
                Text(
                    text = "2. 영어 답변 제출 방식",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AnswerModeOptionCard(
                        title = "음성으로 말하기 (STT)",
                        description = "마이크를 누르고 영어로 직접 발화하여 체크",
                        icon = Icons.Default.Mic,
                        isSelected = selectedAnswerMode == AnswerMode.VOICE,
                        onClick = { selectedAnswerMode = AnswerMode.VOICE }
                    )

                    AnswerModeOptionCard(
                        title = "채팅/텍스트 타이핑",
                        description = "키보드로 영작하여 메시지처럼 입력",
                        icon = Icons.Outlined.Keyboard,
                        isSelected = selectedAnswerMode == AnswerMode.TEXT,
                        onClick = { selectedAnswerMode = AnswerMode.TEXT }
                    )

                    AnswerModeOptionCard(
                        title = "음성 + 텍스트 자유 조합",
                        description = "말하기와 타이핑을 원하는 대로 번갈아 사용",
                        icon = Icons.Default.GraphicEq,
                        isSelected = selectedAnswerMode == AnswerMode.HYBRID,
                        onClick = { selectedAnswerMode = AnswerMode.HYBRID }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 3. Audio & Pronunciation Settings
                Text(
                    text = "3. 영어 음성 발음 속도",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val speeds = listOf(0.8f to "0.8x 느리게", 1.0f to "1.0x 보통", 1.2f to "1.2x 빠르게")
                    speeds.forEach { (speed, label) ->
                        FilterChip(
                            selected = selectedSpeed == speed,
                            onClick = { selectedSpeed = speed },
                            label = { Text(label, fontSize = 12.sp) },
                            leadingIcon = if (selectedSpeed == speed) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                // 4. Auto English Playback
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "정답 확인 시 영어 발음 자동 재생",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = "원어민 억양과 발음을 즉시 청취",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                    Switch(
                        checked = autoPlayEnglish,
                        onCheckedChange = { autoPlayEnglish = it },
                        modifier = Modifier.testTag("autoplay_switch")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        currentConfig.copy(
                            questionMode = selectedQuestionMode,
                            answerMode = selectedAnswerMode,
                            speechSpeed = selectedSpeed,
                            autoPlayEnglishOnResult = autoPlayEnglish
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.testTag("save_mode_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("설정 완료")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
private fun QuestionModeOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
private fun AnswerModeOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
