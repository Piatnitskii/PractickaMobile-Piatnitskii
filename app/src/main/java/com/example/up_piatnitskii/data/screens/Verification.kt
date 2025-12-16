package com.example.up_piatnitskii.data.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Surface
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.delay

private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

// СОЗДАНИЕ ЭКРАНА ВВОДА КОДА ОТР, Томин Андрей, 15.12.2025
@Composable
fun Verfication() {

    var code by remember { mutableStateOf("") }
    val codeLength = 6

    var timeLeft by remember { mutableStateOf(30) }  // 30 секунд
    var isTimerRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isTimerRunning) {
        if (!isTimerRunning) return@LaunchedEffect
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        isTimerRunning = false    // остановили, 0 секунд
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "OTP Проверка",
                style = MaterialTheme.typography.headlineMedium,

                )
            Text(
                text = "                Пожалуйста, Проверьте Свою\n      " +
                        "Электронную Почту, Чтобы Увидеть Код\n                   " +
                        "          Подтверждения",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(32.dp))

            // OTP код
            Column(Modifier.padding(16.dp)) {
                Text("OTP Код", style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(12.dp))

                OtpField(
                    value = code,
                    onValueChange = { new ->
                        val filtered = new.filter { it.isDigit() }.take(codeLength)
                        code = filtered
                    },
                    length = codeLength
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isTimerRunning) {
                        Text(
                            text = "Отправить заново",
                            textDecoration = TextDecoration.Underline,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = TextDecoration.None,
                                color = Color.Gray
                            ),
                            modifier = Modifier
                                .clickable {
                                    // TODO: запросить новый код
                                    timeLeft = 30
                                    isTimerRunning = true
                                }
                        )
                    } else {
                        Spacer(Modifier)
                    }

                    Text(
                        text = "00:${timeLeft.toString().padStart(2, '0')}",
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 35.dp),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// СОЗДАНИЕ ПОЛЯ OTP, Томин Андрей, 16.12.2025
@Composable
fun OtpField(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int
) {
    // Скрытое поле, которое принимает ввод
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(length) { index ->
                    val char = value.getOrNull(index)?.toString() ?: ""
                    val isCurrent = index == value.length && value.length < length

                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isCurrent) Color.Transparent
                                else Color(0xFFF3F3F3)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isCurrent) Color.Red else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (char.isEmpty()) "" else char,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerficationScreenPreview() {

}