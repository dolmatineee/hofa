package com.example.hofa.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hofa.R
import com.example.hofa.data.entities.MeditationState
import com.example.hofa.data.entities.Settings
import com.example.hofa.viewmodel.MeditationScreenViewModel
import com.example.hofa.viewmodel.SettingsScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(
    viewModel: MeditationScreenViewModel = hiltViewModel(),
    settings: Settings,
    onExitClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val totalTime = when (state) {
        is MeditationState.Preparation -> settings.preparationTime
        is MeditationState.Breathing -> settings.breathsCount
        is MeditationState.BreathHoldAfterExhale -> settings.breathHoldDuration
        is MeditationState.BreathHoldAfterInhale -> settings.breathHoldDuration
        else -> 0
    }
    val progress = if (totalTime > 0) (totalTime - timeLeft).toFloat() / totalTime.toFloat() else 0f

    Scaffold(
        topBar = {
            val roundText = when (state) {
                is MeditationState.Preparation -> "Подготовка"
                is MeditationState.Breathing -> "Раунд ${(state as MeditationState.Breathing).round}"
                is MeditationState.DeepExhale -> "Раунд ${(state as MeditationState.DeepExhale).round}"
                is MeditationState.BreathHoldAfterExhale -> "Раунд ${(state as MeditationState.BreathHoldAfterExhale).round}"
                is MeditationState.DeepInhale -> "Раунд ${(state as MeditationState.DeepInhale).round}"
                is MeditationState.BreathHoldAfterInhale -> "Раунд ${(state as MeditationState.BreathHoldAfterInhale).round}"
                is MeditationState.Paused -> "Пауза" // Добавлено состояние паузы
                else -> "Медитация"
            }
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = roundText,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        }
    ) { paddingValues ->

        when (state) {
            is MeditationState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            viewModel.startMeditation()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Начать",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is MeditationState.Finished -> {
                viewModel.finishMeditation()
                onExitClick()
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                ) {

                    when (state) {
                        is MeditationState.Paused -> {
                            Icon(
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.Center),
                                painter = painterResource(id =  R.drawable.baseline_pause_24),
                                contentDescription = "Pause",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        else -> {
                            if (state != MeditationState.Idle) {
                                MeditationStagesText(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    currentState = state
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(200.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    progress = progress,
                                    modifier = Modifier.size(200.dp),
                                    strokeWidth = 10.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                                    strokeCap = StrokeCap.Round
                                )
                                Text(
                                    modifier = Modifier.zIndex(1f),
                                    text = when (state) {
                                        is MeditationState.Preparation -> "$timeLeft сек"
                                        is MeditationState.Breathing -> "$timeLeft вдохов"
                                        is MeditationState.BreathHoldAfterExhale -> "$timeLeft сек"
                                        is MeditationState.BreathHoldAfterInhale -> "$timeLeft сек"
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))


                    MeditationController(
                        isPlaying = when (state) {
                            is MeditationState.Paused -> {
                                false
                            }

                            else -> {
                                true
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .zIndex(1f),
                        onPausePlayClick = {
                            when (state) {
                                is MeditationState.Paused -> {
                                    viewModel.resumeMeditation() // Если состояние паузы, возобновляем медитацию
                                }

                                else -> {
                                    viewModel.pauseMeditation() // В любом другом состоянии ставим на паузу
                                }
                            }
                        },
                        onExitClick = {
                            viewModel.finishMeditation()
                            onExitClick()
                        },
                        onForwardClick = { viewModel.skipStage() }
                    )
                }
            }


        }

    }
}

@Composable
fun MeditationStagesText(
    currentState: MeditationState,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        "Расслабьтесь", // Индекс 0
        "Дышите", // Индекс 1
        "Сделайте глубокий выдох", // Индекс 2
        "Задержите дыхание", // Индекс 3
        "Сделайте глубокий вдох", // Индекс 4
        "Задержите дыхание" // Индекс 5
    )

    // Определяем индекс текущего этапа
    val currentStageIndex = when (currentState) {
        is MeditationState.Preparation -> 0 // "Расслабьтесь"
        is MeditationState.Breathing -> 1 // "Дышите"
        is MeditationState.DeepExhale -> 2 // "Сделайте глубокий выдох"
        is MeditationState.BreathHoldAfterExhale -> 3 // "Задержите дыхание"
        is MeditationState.DeepInhale -> 4 // "Сделайте глубокий вдох"
        is MeditationState.BreathHoldAfterInhale -> 5 // "Задержите дыхание"
        else -> 0 // По умолчанию "Расслабьтесь"
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображение текущего этапа
        Text(
            text = stages[currentStageIndex],
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Отображение следующих двух этапов
        if (currentStageIndex < stages.size - 1) {
            val nextStages = stages.subList(currentStageIndex + 1, stages.size).take(2)
            nextStages.forEachIndexed { index, stage ->
                Text(
                    text = stage,
                    style = MaterialTheme.typography.bodyLarge,
                    color =
                    if (index == 0) MaterialTheme.colorScheme.onBackground.copy(0.4f)
                    else MaterialTheme.colorScheme.onBackground.copy(0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun MeditationStages(
    currentState: MeditationState,
    modifier: Modifier = Modifier
) {
    val stages = listOf(
        "Дышите",
        "Сделайте глубокий выдох",
        "Задержите дыхание",
        "Сделайте глубокий вдох",
        "Задержите дыхание"
    )

    // Определяем индекс текущего этапа
    val currentStageIndex = when (currentState) {
        is MeditationState.Breathing -> 0
        is MeditationState.DeepExhale -> 1
        is MeditationState.BreathHoldAfterExhale -> 2
        is MeditationState.DeepInhale -> 3
        is MeditationState.BreathHoldAfterInhale -> 4
        else -> -1 // Если этап не активен
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображение текущего этапа под строкой
        if (currentStageIndex != -1) {
            Text(
                text = stages[currentStageIndex],
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Text(
                text = "Расслабьтесь",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            stages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (index <= currentStageIndex) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                        )
                        .size(width = 64.dp, height = 32.dp)
                )
            }
        }


    }
}


@Composable
fun MeditationController(
    isPlaying: Boolean, // Это значение теперь управляется извне
    modifier: Modifier,
    onPausePlayClick: () -> Unit, // Колбэк для изменения состояния
    onExitClick: () -> Unit,
    onForwardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Первая треть строки для паузы/плей
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onPausePlayClick() // Вызываем колбэк для изменения состояния
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                )
            }

            // Первая разделительная линия
            Divider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Вторая треть строки для выхода
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onExitClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_exit_to_app_24),
                    contentDescription = "Exit",
                )
            }

            // Вторая разделительная линия
            Divider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Третья треть строки для перемотки
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onForwardClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_skip_next_24),
                    contentDescription = "Forward",
                )
            }
        }
    }
}