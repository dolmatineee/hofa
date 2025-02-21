package com.example.hofa.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hofa.R
import com.example.hofa.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onSettingsClickListener: () -> Unit,
    onStatisticsClickListener: () -> Unit,
    onSeanceClickListener: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val days by viewModel.days.collectAsState()
    val quote by viewModel.quote.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Дыши Контролируй \n Управляй",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onSettingsClickListener()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onStatisticsClickListener()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Statistics",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StreakCard(days, context)

                QuoteCard(quote)


            }

            Spacer(modifier = Modifier.height(16.dp))

            SeanceCard(onSeanceClickListener)
        }
    }
}



@Composable
fun RowScope.StreakCard(
    days: Int,
    context: Context
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp)
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Твой прогресс",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
               verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = days.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = painterResource(R.drawable.fire_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "день подряд",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Я пользуюсь приложением для медитации Вима Хофа уже $days день подряд! Скачай его по ссылке: https://www.rustore.ru/catalog/app/com.example.hofa") // Текст для отправки
                    }

                    // Запускаем системное окно для выбора приложения
                    context.startActivity(Intent.createChooser(shareIntent, "Поделиться через"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Поделиться",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



@Composable
fun RowScope.QuoteCard(
    quote: String
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp)
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    // Стиль для открывающей кавычки (bodyLarge)
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append("\"")
                    }
                    // Стиль для текста цитаты (labelLarge)
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append(quote)
                    }
                    // Стиль для закрывающей кавычки (bodyLarge)
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append("\"")
                    }
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "- Вим Хоф",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun SeanceCard(
    onSeanceClickListener: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme // Получаем colorScheme вне Canvas

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSeanceClickListener() }
            .height(200.dp)
        ,
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surface)
        ) {
            // Рисуем круг с помощью Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val radius = size.height // Радиус круга равен высоте карточки
                val circleCenter = Offset(x = size.width / 2, y = size.height / 2)

                // Рисуем круг
                drawCircle(
                    color = colorScheme.primaryContainer, // Используем colorScheme, полученный выше
                    radius = radius,
                    center = circleCenter.copy(x = circleCenter.x + radius) // Сдвигаем центр круга вправо
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Первая половина карточки (текст)
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 32.dp)
                    ) {
                        Text(
                            text = "Начать сеанс",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Перед началом появится короткая реклама",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Вторая половина карточки (иконка внутри круга)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(R.drawable.breath_icon),
                        contentDescription = "Breath Icon",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(150.dp) // Размер иконки
                            .align(Alignment.CenterEnd) // Выравниваем иконку по центру конца
                            .offset(x = (30).dp) // Сдвигаем иконку влево, чтобы она была внутри круга
                    )
                }
            }
        }
    }
}