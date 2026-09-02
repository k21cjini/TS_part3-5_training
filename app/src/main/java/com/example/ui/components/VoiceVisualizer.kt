package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SpeechState

@Composable
fun VoiceVisualizer(
    speechState: SpeechState,
    rmsLevel: Float,
    isListening: Boolean,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.28f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = if (isListening) 0.1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAlpha"
    )

    val buttonColor by animateColorAsState(
        targetValue = when {
            speechState is SpeechState.Listening -> MaterialTheme.colorScheme.error
            speechState is SpeechState.Processing -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.primary
        },
        label = "buttonColor"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(110.dp)
        ) {
            // Ripple background layers when listening
            if (isListening) {
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .scale(pulseScale + (rmsLevel * 0.4f))
                        .clip(CircleShape)
                        .background(buttonColor.copy(alpha = waveAlpha))
                )
                Box(
                    modifier = Modifier
                        .size(85.dp)
                        .scale(1f + (rmsLevel * 0.3f))
                        .clip(CircleShape)
                        .background(buttonColor.copy(alpha = 0.25f))
                )
            }

            // Main Mic Button
            Surface(
                onClick = onMicClick,
                shape = CircleShape,
                color = buttonColor,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .testTag("mic_action_button")
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(68.dp)
                ) {
                    if (speechState is SpeechState.Processing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else if (isListening) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "녹음 정지 및 평가",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "음성으로 답변하기",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Status description
        val statusText = when (speechState) {
            is SpeechState.Listening -> "영어로 말씀해주세요... (탭하여 완료)"
            is SpeechState.Processing -> "음성을 분석하고 있습니다..."
            is SpeechState.PartialResult -> "\"${speechState.text}\""
            is SpeechState.Error -> speechState.message
            else -> "마이크를 눌러 영어로 말해보세요"
        }

        val statusColor = when (speechState) {
            is SpeechState.Listening -> MaterialTheme.colorScheme.error
            is SpeechState.Error -> MaterialTheme.colorScheme.error
            is SpeechState.PartialResult -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                if (isListening) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                    )
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isListening) FontWeight.Bold else FontWeight.Normal,
                        color = statusColor,
                        fontSize = 12.sp
                    ),
                    maxLines = 2
                )
            }
        }
    }
}
