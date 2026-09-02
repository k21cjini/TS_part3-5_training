package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.audio.SpeechState
import com.example.model.AnswerMode
import com.example.model.QuestionMode
import com.example.ui.components.AnswerDiffView
import com.example.ui.components.ModeSelectorDialog
import com.example.ui.components.VoiceVisualizer
import com.example.ui.components.WordScrambleHint
import com.example.ui.viewmodel.EngSpeakViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    viewModel: EngSpeakViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val practiceState by viewModel.practiceState.collectAsState()
    val sessionConfig by viewModel.sessionConfig.collectAsState()
    val speechState by viewModel.speechManager.state.collectAsState()
    val rmsLevel by viewModel.speechManager.rmsLevel.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showModeDialog by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var revealVoiceText by remember { mutableStateOf(false) }

    val practiceCategories = listOf(
        "전체",
        "YouTube 강의",
        "일상회화",
        "패턴영어",
        "영어식 사고훈련",
        "비즈니스",
        "여행",
        "내 문장장",
        "즐겨찾기",
        "복습필요"
    )

    // Permission launcher for STT
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceRecording()
        }
    }

    val handleMicClick: () -> Unit = {
        if (speechState is SpeechState.Listening) {
            viewModel.stopVoiceRecording()
        } else {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                viewModel.startVoiceRecording()
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    if (showModeDialog) {
        ModeSelectorDialog(
            currentConfig = sessionConfig,
            onDismiss = { showModeDialog = false },
            onSave = { updated ->
                viewModel.updateSessionConfig(
                    questionMode = updated.questionMode,
                    answerMode = updated.answerMode,
                    speechSpeed = updated.speechSpeed,
                    autoPlayEnglish = updated.autoPlayEnglishOnResult
                )
            }
        )
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWideScreen = maxWidth > 680.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top Mode & Category Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Category pill & Count
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { showCategoryMenu = true }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = selectedCategory,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "카테고리 변경",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = showCategoryMenu,
                                    onDismissRequest = { showCategoryMenu = false }
                                ) {
                                    practiceCategories.forEach { cat ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = cat,
                                                    fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (selectedCategory == cat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.selectCategory(cat)
                                                showCategoryMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (practiceState.totalCount > 0) {
                                Text(
                                    text = "${practiceState.currentIndex + 1} / ${practiceState.totalCount}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        // Mode Selector Quick Pill & Settings Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { showModeDialog = true }
                                    .testTag("mode_config_pill")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (sessionConfig.answerMode == AnswerMode.VOICE) Icons.Default.Mic else Icons.Default.Chat,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = when (sessionConfig.answerMode) {
                                            AnswerMode.VOICE -> "음성 답변"
                                            AnswerMode.TEXT -> "채팅 답변"
                                            AnswerMode.HYBRID -> "음성+채팅"
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 11.sp
                                        )
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "모드 변경",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.shuffleSentences() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shuffle,
                                    contentDescription = "랜덤 섞기",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Deck Progress Bar
                    if (practiceState.totalCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (practiceState.currentIndex + 1).toFloat() / practiceState.totalCount },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }

            // Main Practice Content
            if (practiceState.currentSentence == null) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "학습할 문장이 없습니다.",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "다른 카테고리를 선택하거나 새 문장을 추가해보세요!",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            } else {
                val sentence = practiceState.currentSentence!!

                if (isWideScreen) {
                    // iPad / Tablet Two-column layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Left Pane: Question Card & Hints
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            QuestionPromptCard(
                                sentence = sentence,
                                questionMode = sessionConfig.questionMode,
                                thinkingSeconds = practiceState.thinkingSeconds,
                                isVoicePlaying = false,
                                isEvaluated = practiceState.isEvaluated,
                                revealVoiceText = revealVoiceText,
                                onToggleReveal = { revealVoiceText = !revealVoiceText },
                                onPlayPrompt = { viewModel.speakKoreanPrompt() },
                                onCycleHint = { viewModel.cycleHint() },
                                onToggleBookmark = { viewModel.toggleBookmark(sentence) }
                            )

                            if (practiceState.hintLevel > 0) {
                                WordScrambleHint(
                                    sentence = sentence,
                                    hintLevel = practiceState.hintLevel,
                                    onWordChipClick = { word ->
                                        val current = practiceState.textInput
                                        val separator = if (current.isEmpty() || current.endsWith(" ")) "" else " "
                                        viewModel.onTextInputChange(current + separator + word)
                                    }
                                )
                            }
                        }

                        // Right Pane: Answering / Evaluation Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (practiceState.isEvaluated && practiceState.evaluationResult != null) {
                                AnswerDiffView(
                                    result = practiceState.evaluationResult!!,
                                    sentence = sentence,
                                    onPlayEnglish = { speed -> viewModel.speakEnglishAnswer(speed = speed) },
                                    onShadowingClick = {
                                        viewModel.startShadowingMode()
                                        handleMicClick()
                                    },
                                    onNextClick = { viewModel.nextSentence() },
                                    onRetryClick = { viewModel.retryCurrentSentence() },
                                    onToggleBookmark = { viewModel.toggleBookmark(sentence) },
                                    isShadowingActive = practiceState.isShadowingMode,
                                    shadowingResult = practiceState.shadowingEvaluation
                                )
                            } else {
                                AnswerInputCard(
                                    answerMode = sessionConfig.answerMode,
                                    textInput = practiceState.textInput,
                                    speechState = speechState,
                                    rmsLevel = rmsLevel,
                                    onTextChange = { viewModel.onTextInputChange(it) },
                                    onSubmit = { viewModel.submitAnswer(it, isVoice = false) },
                                    onMicClick = handleMicClick,
                                    onToggleMode = { viewModel.toggleAnswerMode() }
                                )
                            }
                        }
                    }
                } else {
                    // Smartphone Single Column layout
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Question Prompt Card
                        QuestionPromptCard(
                            sentence = sentence,
                            questionMode = sessionConfig.questionMode,
                            thinkingSeconds = practiceState.thinkingSeconds,
                            isVoicePlaying = false,
                            isEvaluated = practiceState.isEvaluated,
                            revealVoiceText = revealVoiceText,
                            onToggleReveal = { revealVoiceText = !revealVoiceText },
                            onPlayPrompt = { viewModel.speakKoreanPrompt() },
                            onCycleHint = { viewModel.cycleHint() },
                            onToggleBookmark = { viewModel.toggleBookmark(sentence) }
                        )

                        // 2. Hint View if toggled
                        if (practiceState.hintLevel > 0 && !practiceState.isEvaluated) {
                            WordScrambleHint(
                                sentence = sentence,
                                hintLevel = practiceState.hintLevel,
                                onWordChipClick = { word ->
                                    val current = practiceState.textInput
                                    val separator = if (current.isEmpty() || current.endsWith(" ")) "" else " "
                                    viewModel.onTextInputChange(current + separator + word)
                                }
                            )
                        }

                        // 3. Answer Input or Evaluated Diff View
                        if (practiceState.isEvaluated && practiceState.evaluationResult != null) {
                            AnswerDiffView(
                                result = practiceState.evaluationResult!!,
                                sentence = sentence,
                                onPlayEnglish = { speed -> viewModel.speakEnglishAnswer(speed = speed) },
                                onShadowingClick = {
                                    viewModel.startShadowingMode()
                                    handleMicClick()
                                },
                                onNextClick = { viewModel.nextSentence() },
                                onRetryClick = { viewModel.retryCurrentSentence() },
                                onToggleBookmark = { viewModel.toggleBookmark(sentence) },
                                isShadowingActive = practiceState.isShadowingMode,
                                shadowingResult = practiceState.shadowingEvaluation
                            )
                        } else {
                            AnswerInputCard(
                                answerMode = sessionConfig.answerMode,
                                textInput = practiceState.textInput,
                                speechState = speechState,
                                rmsLevel = rmsLevel,
                                onTextChange = { viewModel.onTextInputChange(it) },
                                onSubmit = { viewModel.submitAnswer(it, isVoice = false) },
                                onMicClick = handleMicClick,
                                onToggleMode = { viewModel.toggleAnswerMode() }
                            )
                        }

                        // Bottom Navigation arrows (Prev / Next)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { viewModel.previousSentence() },
                                modifier = Modifier.testTag("prev_button")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("이전 문장")
                            }

                            TextButton(
                                onClick = { viewModel.nextSentence() },
                                modifier = Modifier.testTag("skip_next_button")
                            ) {
                                Text("다음 문장")
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionPromptCard(
    sentence: com.example.model.Sentence,
    questionMode: QuestionMode,
    thinkingSeconds: Int,
    isVoicePlaying: Boolean,
    isEvaluated: Boolean,
    revealVoiceText: Boolean,
    onToggleReveal: () -> Unit,
    onPlayPrompt: () -> Unit,
    onCycleHint: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row: Thinking Stopwatch + Hint + Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Thinking Stopwatch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "사고 시간",
                        tint = if (thinkingSeconds > 7) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "생각 시간 ${thinkingSeconds}초",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (thinkingSeconds > 7) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Hint & Bookmark Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilledTonalButton(
                        onClick = onCycleHint,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("hint_button")
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("힌트", fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (sentence.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "북마크",
                            tint = if (sentence.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Korean Prompt Question Area
            if (questionMode == QuestionMode.VOICE_ONLY && !revealVoiceText && !isEvaluated) {
                // Hidden text mode for ear training
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onPlayPrompt() }
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = "듣기",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            text = "한국어 음성을 듣고 영어로 생각해보세요",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        TextButton(onClick = onToggleReveal) {
                            Icon(Icons.Outlined.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("한글 문장 확인하기", fontSize = 12.sp)
                        }
                    }
                }
            } else {
                // Display Korean Prompt with Audio Play button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Q. 다음 문장을 영어로 말해보세요",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sentence.korean,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 21.sp,
                                lineHeight = 28.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onPlayPrompt,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .testTag("play_korean_prompt_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "한국어 질문 다시 듣기",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerInputCard(
    answerMode: AnswerMode,
    textInput: String,
    speechState: SpeechState,
    rmsLevel: Float,
    onTextChange: (String) -> Unit,
    onSubmit: (String) -> Unit,
    onMicClick: () -> Unit,
    onToggleMode: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Header Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "A. 영어 답변 입력",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                TextButton(
                    onClick = onToggleMode,
                    modifier = Modifier.testTag("toggle_input_mode_button")
                ) {
                    Icon(
                        imageVector = if (answerMode == AnswerMode.VOICE) Icons.Outlined.Keyboard else Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (answerMode == AnswerMode.VOICE) "키보드 모드로 전환" else "음성 모드로 전환",
                        fontSize = 12.sp
                    )
                }
            }

            // Voice Visualizer or Text Input Box
            when (answerMode) {
                AnswerMode.VOICE -> {
                    VoiceVisualizer(
                        speechState = speechState,
                        rmsLevel = rmsLevel,
                        isListening = speechState is SpeechState.Listening,
                        onMicClick = onMicClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )

                    if (textInput.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "인식된 문장: \"$textInput\"",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                AnswerMode.TEXT -> {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = onTextChange,
                        label = { Text("영어로 문장을 작성해주세요") },
                        placeholder = { Text("Type English sentence here...") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("answer_text_field"),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            if (textInput.isNotBlank()) onSubmit(textInput)
                        }),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (textInput.isNotBlank()) onSubmit(textInput)
                                },
                                enabled = textInput.isNotBlank(),
                                modifier = Modifier.testTag("submit_text_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "답변 제출",
                                    tint = if (textInput.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                            }
                        }
                    )
                }

                AnswerMode.HYBRID -> {
                    // Hybrid mode provides both keyboard and mic seamlessly!
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = onTextChange,
                            label = { Text("영어로 말하거나 타이핑해주세요") },
                            placeholder = { Text("Speak or type in English...") },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("hybrid_answer_field"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                if (textInput.isNotBlank()) onSubmit(textInput)
                            }),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (textInput.isNotBlank()) onSubmit(textInput)
                                    },
                                    enabled = textInput.isNotBlank(),
                                    modifier = Modifier.testTag("hybrid_submit_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "제출",
                                        tint = if (textInput.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        )

                        VoiceVisualizer(
                            speechState = speechState,
                            rmsLevel = rmsLevel,
                            isListening = speechState is SpeechState.Listening,
                            onMicClick = onMicClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
