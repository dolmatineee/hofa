package com.example.hofa.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultStrokeLineWidth
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hofa.data.entities.Settings
import com.example.hofa.viewmodel.HomeScreenViewModel
import com.example.hofa.viewmodel.SettingsScreenViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    settings: Settings
) {

    val isLoading by viewModel.isLoading.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Настройки",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        }
    ) { paddingValues ->


        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {


            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Подготовка",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                PreparationTimeCard(
                    initialValue = settings.preparationTime,
                    onValueChange = { newPreparationTime ->
                        viewModel.updateSettings(settings.copy(preparationTime = newPreparationTime))
                    }
                )
            }


            item {
                RoundsCard(
                    initialValue = settings.rounds,
                    onValueChange = { newRounds ->
                        viewModel.updateSettings(settings.copy(rounds = newRounds))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Сеанс",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                BreathingSpeedCard(
                    initialValue = settings.breathingSpeed,
                    onValueChange = { newSpeed ->
                        viewModel.updateSettings(settings.copy(breathingSpeed = newSpeed))
                    }
                )
            }

            item {
                BreathsCountCard (
                    initialValue = settings.breathsCount,
                    onValueChange = { newBreathsCount ->
                        viewModel.updateSettings(settings.copy(breathsCount = newBreathsCount))
                    }
                )
            }

            item {
                BreathHoldDurationCard(
                    initialValue = settings.breathHoldDuration,
                    onValueChange = { newBreathsHoldDuration ->
                        viewModel.updateSettings(settings.copy(breathHoldDuration = newBreathsHoldDuration))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Дополнительно",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                AccompanimentCard(
                    initialValue = settings.accompaniment,
                    onValueChange = { newAccompaniment ->
                        viewModel.updateSettings(settings.copy(accompaniment = newAccompaniment))
                    }
                )
            }

            item {
                BackgroundSoundCard (
                    initialValue = settings.backgroundSound,
                    onValueChange = { newBackgroundSound ->
                        viewModel.updateSettings(settings.copy(backgroundSound = newBackgroundSound))
                    }
                )
            }

            item {
                SoundVolumeCard (
                    initialValue = settings.soundVolume,
                    onValueChange = { newSoundVolume ->
                        viewModel.updateSettings(settings.copy(soundVolume = newSoundVolume))
                    }
                )
            }

            item {
                VoiсeCard (
                    initialValue = settings.voice,
                    onValueChange = { newVoiсe ->
                        viewModel.updateSettings(settings.copy(voice = newVoiсe))
                    }
                )
            }
        }

    }
}


@Composable
fun Modifier.shimmer(isLoading: Boolean): Modifier = composed {
    if (isLoading) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition()
        val translateAnim = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset.Zero,
                end = Offset(x = translateAnim.value, y = translateAnim.value)
            )
        )
    } else {
        this
    }
}


@Composable
fun PreparationTimeCard(
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialValue.toFloat()) }

    LaunchedEffect(sliderValue) {
        onValueChange(sliderValue.toInt())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Время подготовки",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${sliderValue.toInt()} сек",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    onValueChange(newValue.toInt())
                },
                valueRange = 0f..120f,
                steps = 0,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun RoundsCard(
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialValue.toFloat()) }

    LaunchedEffect(sliderValue) {
        onValueChange(sliderValue.toInt())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Верхняя строка с текстом и Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Число раундов",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${sliderValue.toInt()} раунда",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Прогресс бар
            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    onValueChange(newValue.toInt())
                },
                valueRange = 1f..10f,
                steps = 10,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun BreathingSpeedCard(
    initialValue: String, // Начальное значение скорости дыхания (строка)
    onValueChange: (String) -> Unit // Callback для обновления значения
) {
    // Преобразуем начальное значение строки в числовое значение для слайдера
    val initialSliderValue = when (initialValue) {
        "Медленная" -> 0f
        "Обычная" -> 1f
        "Быстрая" -> 2f
        else -> 1f // По умолчанию "Обычная"
    }

    // Состояние для хранения текущего значения слайдера
    var sliderValue by remember { mutableStateOf(initialSliderValue) }

    // Функция для преобразования значения слайдера в текст
    val speedLabel = when (sliderValue) {
        0f -> "Медленная"
        1f -> "Обычная"
        2f -> "Быстрая"
        else -> "Обычная"
    }

    // Обновляем значение, если оно изменилось
    LaunchedEffect(sliderValue) {
        onValueChange(speedLabel)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Верхняя строка с текстом и Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Скорость дыхания",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = speedLabel,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Слайдер с тремя шагами
            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                },
                valueRange = 0f..2f, // Диапазон от 0 до 2 (три шага)
                steps = 1, // Два шага между значениями (0, 1, 2)
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun BreathsCountCard(
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialValue.toFloat()) }

    LaunchedEffect(sliderValue) {
        onValueChange(sliderValue.toInt())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Количество вдохов",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${sliderValue.toInt()} вдохов",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    onValueChange(newValue.toInt())
                },
                valueRange = 10f..100f,
                steps = 0,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BreathHoldDurationCard(
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialValue.toFloat()) }

    LaunchedEffect(sliderValue) {
        onValueChange(sliderValue.toInt())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Задержка дыхания",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${sliderValue.toInt()} сек",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    onValueChange(newValue.toInt())
                },
                valueRange = 10f..120f,
                steps = 0,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AccompanimentCard(
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    val options = listOf("Дыхание", "Дыхание и голос", "Ничего")
    var selectedOption by remember { mutableStateOf(initialValue) }

    LaunchedEffect(initialValue) {
        selectedOption = initialValue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp),
                text = "Сопровождение",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = option
                            onValueChange(option)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = {
                            selectedOption = option
                            onValueChange(option)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun BackgroundSoundCard(
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    val options = listOf("Утренние трели", "Шёпот ветра", "Журчание ручья", "Вечерние цикады", "Живой лес", "Ничего")
    var selectedOption by remember { mutableStateOf(initialValue) }

    LaunchedEffect(initialValue) {
        selectedOption = initialValue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp),
                text = "Фоновый звук",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = option
                            onValueChange(option)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = {
                            selectedOption = option
                            onValueChange(option)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun VoiсeCard(
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    val options = listOf("Мужской", "Женский")
    var selectedOption by remember { mutableStateOf(initialValue) }

    LaunchedEffect(initialValue) {
        selectedOption = initialValue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp),
                text = "Голос",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = option
                            onValueChange(option)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = {
                            selectedOption = option
                            onValueChange(option)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Composable
fun SoundVolumeCard(
    initialValue: Int,
    onValueChange: (Int) -> Unit
) {
    var sliderValue by remember { mutableStateOf(initialValue.toFloat()) }

    LaunchedEffect(sliderValue) {
        onValueChange(sliderValue.toInt())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Громкость фонового звука",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${sliderValue.toInt()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    onValueChange(newValue.toInt())
                },
                valueRange = 0f..100f,
                steps = 0,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}





