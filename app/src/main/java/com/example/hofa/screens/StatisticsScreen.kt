package com.example.hofa.screens

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hofa.R
import com.example.hofa.customs.Picker
import com.example.hofa.customs.rememberPickerState
import com.example.hofa.data.entities.BarType
import com.example.hofa.data.entities.ChartData
import com.example.hofa.data.entities.Statistics
import com.example.hofa.viewmodel.StatisticsScreenViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.round

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    onBackPressed: () -> Unit,
    viewModel: StatisticsScreenViewModel = hiltViewModel(),
) {

    val statistics by viewModel.statistics.collectAsState()
    val targetSessions by viewModel.targetSessions.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    val chartData = viewModel.getChartData(statistics)
    val showGoalPicker by viewModel.showGoalPicker.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Статистика",
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                DateNavigation(
                    currentDate,
                    onPrevious = { viewModel.previousDate() },
                    onNext = { viewModel.nextDate() })
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                BarGraph(chartData = chartData)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {

                GoalCard(
                    targetSessions = targetSessions ?: 0,
                    totalSessions = statistics?.totalSessions ?: 0,
                    onChangeGoal = {
                        viewModel.showGoalPicker()
                    }
                )

            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }


            item {
                StatisticsCards(
                    totalDuration = statistics?.totalDuration ?: 0,
                    totalRounds = statistics?.totalRounds ?: 0,
                    totalBreaths = statistics?.totalBreaths ?: 0,
                    totalBreathHoldDuration = statistics?.totalBreathHoldDuration ?: 0
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }


        }

        if (showGoalPicker) {
            targetSessions?.let {
                GoalSelectionBottomSheet(
                    onDismiss = { viewModel.hideGoalPicker() },
                    onConfirm = { newGoal ->
                        viewModel.updateGoal(newGoal)
                        viewModel.hideGoalPicker()
                    },
                    targetSessions = it
                )
            }
        }
    }

}


@Composable
fun StatisticsCards(
    totalRounds: Int,
    totalDuration: Int,
    totalBreaths: Int,
    totalBreathHoldDuration: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TotalDurationCard(
                modifier = Modifier.weight(1f),
                totalDuration = totalDuration
            )
            Spacer(modifier = Modifier.width(16.dp)) // Отступ между карточками
            TotalRoundsCard(
                modifier = Modifier.weight(1f),
                totalRounds = totalRounds
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TotalBreathsCard(
                modifier = Modifier.weight(1f),
                totalBreaths = totalBreaths
            )
            Spacer(modifier = Modifier.width(16.dp)) // Отступ между карточками
            TotalBreathHoldDurationCard(
                modifier = Modifier.weight(1f),
                totalBreathHoldDuration = totalBreathHoldDuration
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateNavigation(currentDate: LocalDate, onPrevious: () -> Unit, onNext: () -> Unit) {
    val today = LocalDate.now()
    val isToday = currentDate.isEqual(today)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onPrevious,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "Previous"
            )
        }

        Text(
            text = currentDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )


            IconButton(
                onClick = onNext,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (!isToday) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
                    contentColor = if (!isToday) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.background
                ),
                enabled = !isToday
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = "Next"
                )
            }


    }
}

@Composable
fun TotalBreathHoldDurationCard(
    totalBreathHoldDuration: Int,
    modifier: Modifier = Modifier
) {
    val durationMinute = totalBreathHoldDuration / 60
    val durationSecond = totalBreathHoldDuration % 60

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Круг с иконкой
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.lungs_7c6g93wzdt98),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Отображение времени
            if (totalBreathHoldDuration >= 60) {
                // Если время больше или равно 60 секундам, отображаем минуты и секунды
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                            )
                        ) {
                            append("$durationMinute")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                            )
                        ) {
                            append("мин")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                            )
                        ) {
                            append("$durationSecond")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                            )
                        ) {
                            append("сек")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            } else {
                // Если время меньше 60 секунд, отображаем только секунды
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                            )
                        ) {
                            append("$totalBreathHoldDuration")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                            )
                        ) {
                            append("сек")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Текст
            Text(
                text = "Задержка\n" +
                        "дыхания",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun TotalRoundsCard(
    totalRounds: Int,
    modifier: Modifier = Modifier
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Круг с иконкой
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.fire_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                        )
                    ) {
                        append("$totalRounds")
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                        )
                    ) {
                        append("раунд")
                    }
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Текст
            Text(
                text = "Количество\n" +
                        "раундов",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }


    }
}


@Composable
fun TotalBreathsCard(
    totalBreaths: Int,
    modifier: Modifier = Modifier
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Круг с иконкой
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.group__10_),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                        )
                    ) {
                        append("$totalBreaths")
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                        )
                    ) {
                        append("вдох")
                    }
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Текст
            Text(
                text = "Количество\n" +
                        "вдохов",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }


    }
}


@Composable
fun TotalDurationCard(
    totalDuration: Int,
    modifier: Modifier = Modifier
) {
    val durationMinute = totalDuration / 60
    val durationSecond = totalDuration % 60

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Круг с иконкой
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_access_time_filled_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Отображение времени
            if (totalDuration >= 60) {
                // Если время больше или равно 60 секундам, отображаем минуты и секунды
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                            )
                        ) {
                            append("$durationMinute")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                            )
                        ) {
                            append("мин")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                            )
                        ) {
                            append("$durationSecond")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                            )
                        ) {
                            append("сек")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            } else {
                // Если время меньше 60 секунд, отображаем только секунды
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                            )
                        ) {
                            append("$totalDuration")
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                            )
                        ) {
                            append("сек")
                        }
                    },
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Текст
            Text(
                text = "Общая \n" +
                        "длительность",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GoalCard(
    targetSessions: Int,
    totalSessions: Int,
    onChangeGoal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Количество сеансов",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = totalSessions.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Цель: $targetSessions сеанс",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onChangeGoal()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Изменить цель",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BarGraph(
    chartData: ChartData,
    height: Dp = 200.dp,
    roundType: BarType = BarType.TOP_CURVED,
    barWidth: Dp = 8.dp,
    barColor: Color = MaterialTheme.colorScheme.primary,
    barArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    var typeface = Typeface.create("montserrat", Typeface.NORMAL)
    val sessionsByHour = chartData.sessionsByHour
    val yAxisLabels = chartData.yAxisLabels
    val xAxisLabels = chartData.xAxisLabels

    // Преобразуем данные для графика, учитывая все 24 часа
    val graphBarData = xAxisLabels.map { hour ->
        sessionsByHour[hour]?.toFloat() ?: 0f
    }

    // Часы, которые будут отображаться на оси X
    val visibleHours = listOf(0, 4, 8, 12, 16, 20, 24)

    // Остальной код остается практически без изменений, за исключением использования graphBarData и xAxisLabels

    val barData by remember {
        mutableStateOf(graphBarData.map { it.toInt() })
    }

    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp

    val xAxisScaleHeight = 40.dp

    val yAxisScaleSpacing by remember {
        mutableStateOf(100f)
    }
    val yAxisTextWidth by remember {
        mutableStateOf(100.dp)
    }

    val barShap = when (roundType) {
        BarType.CIRCULAR_TYPE -> CircleShape
        BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
    }
    // Применяем шрифт из стиля
    val context = LocalContext.current
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            // Устанавливаем цвет текста
            color = colorScheme.onSecondaryContainer.toArgb()

            // Выравнивание текста по центру
            textAlign = Paint.Align.CENTER

            // Получаем стиль текста из темы
            val textStyle = typography.labelMedium

            // Применяем размер текста из стиля
            textSize = with(density) { textStyle.fontSize.toPx() }


            typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)

            letterSpacing = textStyle.letterSpacing.value


        }
    }

    val yCoordinates = mutableListOf<Float>()
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {

        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Canvas(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxSize()
            ) {

                // Фиксированные значения для оси Y
                val yAxisScaleText = yAxisLabels
                val maxYValue = yAxisScaleText.maxOrNull() ?: 0

                // Отрисовка текста и линий для оси Y
                yAxisScaleText.forEachIndexed { i, value ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            value.toString(), // Целое число
                            30f,
                            size.height - yAxisScaleSpacing - i * size.height / (yAxisScaleText.size - 1),
                            textPaint
                        )
                    }
                    yCoordinates.add(size.height - yAxisScaleSpacing - i * size.height / (yAxisScaleText.size - 1))
                }

                // Отрисовка горизонтальных линий для оси Y
                yAxisScaleText.forEachIndexed { i, _ ->
                    drawLine(
                        start = Offset(x = yAxisScaleSpacing, y = yCoordinates[i]),
                        end = Offset(x = size.width, y = yCoordinates[i]),
                        color = colorScheme.surface,
                        strokeWidth = 5f,
                        pathEffect = pathEffect
                    )
                }

            }

        }

        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                modifier = Modifier
                    .width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {

                graphBarData.forEachIndexed { index, value ->

                    var animationTriggered by remember {
                        mutableStateOf(false)
                    }
                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        )
                    )
                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShap)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(barShap)
                                    .fillMaxWidth()
                                    .fillMaxHeight(graphBarHeight / (yAxisLabels.maxOrNull() ?: 1))
                                    .background(barColor)
                            )
                        }

                        // Отображаем текст только для выбранных часов
                        if (xAxisLabels[index] in visibleHours) {
                            Column(
                                modifier = Modifier
                                    .height(xAxisScaleHeight),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = CenterHorizontally
                            ) {


                                Text(
                                    modifier = Modifier.padding(bottom = 3.dp, top = 8.dp),
                                    text = xAxisLabels[index].toString(),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelMedium
                                )

                            }
                        }

                    }

                }

            }

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSelectionBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    targetSessions: Int
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        GoalPicker(
            onConfirm = { selectedGoal ->
                onConfirm(selectedGoal)
                coroutineScope.launch { sheetState.hide() }
            },
            targetSessions = targetSessions
        )
    }

    // Показываем BottomSheet при вызове
    LaunchedEffect(Unit) {
        sheetState.show()
    }
}

@Composable
fun GoalPicker(
    onConfirm: (Int) -> Unit,
    targetSessions: Int
) {
    var pickerValue by remember { mutableStateOf(targetSessions) }

    // Создаем список чисел от 0 до 100
    val numbers = (0..100).map { it.toString() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Цель",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Внедряем Picker
        Picker(
            items = numbers,
            state = rememberPickerState(),
            modifier = Modifier.fillMaxWidth(),
            startIndex = pickerValue,
            visibleItemsCount = 5,
            textStyle = MaterialTheme.typography.titleLarge,
            dividerColor = MaterialTheme.colorScheme.secondaryContainer,
            onItemSelected = { index ->
                pickerValue = index // Обновляем pickerValue при изменении выбора
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onConfirm(pickerValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Выбрать",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}