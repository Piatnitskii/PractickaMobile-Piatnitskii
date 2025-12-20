package com.example.up_piatnitskii.data.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.viewModel.VerificationViewModel
import com.example.up_piatnitskii.ui.theme.BackgroundColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography
import com.example.up_piatnitskii.ui.theme.SubTextDarkColor
import com.example.up_piatnitskii.ui.theme.TextColor

import kotlinx.coroutines.delay

private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

// 23. Создать экран «Verification», как на макете
@Composable
fun Verfication(
    onBackClick: () -> Unit = {},
    onCodeVerified: () -> Unit = {},          // коллбек при успешной верификации
    viewModel: VerificationViewModel = viewModel()
) {

    //24. Экран «Verification». Реализовать возможность повторного запроса кода по
    //истечению таймера 01:00.
    var code by remember { mutableStateOf("") }
    val codeLength = 6
    var timeLeft by remember { mutableStateOf(60) }  // 30 секунд
    var isTimerRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isTimerRunning) {
        if (!isTimerRunning) return@LaunchedEffect
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        isTimerRunning = false    // остановили, 0 секунд
    }

    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }   // все квадраты красные при ошибке
    var isLoading by remember { mutableStateOf(false) } // отправка на сервер

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(62.dp))
            // Круглая кнопка "назад"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    modifier = Modifier.size(width = 44.dp, height = 44.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = BackgroundColor // HEX F7F7F9
                    ),

                    ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Назад",
                        tint = Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.otp_verification),
                style = RalewayTypography.headingRegular32,
                color = TextColor
                )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.check_email_for_code),
                style = RalewayTypography.bodyRegular16,
                color = SubTextDarkColor,
                textAlign = TextAlign.Center
            )

            // OTP код
            Column(Modifier.padding(16.dp)) {

                Text(stringResource(id = R.string.otp_code),
                    style = RalewayTypography.bodyMedium16,
                    color = TextColor,
                )

                Spacer(Modifier.height(20.dp))

                OtpField(
                    value = code,
                    onValueChange = { new ->
                        val filtered = new.filter { it.isDigit() }.take(codeLength)
                        code = filtered
                    },
                    length = codeLength
                )


                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isTimerRunning) {
                        Text(
                            text = "Отправить заново",
                            textDecoration = TextDecoration.Underline,
                            style = RalewayTypography.bodyRegular12.copy(
                                color = SubTextDarkColor
                            ),
                            modifier = Modifier
                                .clickable {
                                    viewModel.requestNewCode(
                                        onSuccess = {
                                            timeLeft = 60
                                            isTimerRunning = true
                                            isError = false
                                            code = ""
                                        },
                                        onError = {
                                                error ->
                                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                        })
                                }
                        )
                    } else {
                        Spacer(Modifier)
                    }

                    Text(
                        text = "00:${timeLeft.toString().padStart(2, '0')}",
                        style = RalewayTypography.bodyRegular12,
                        color = SubTextDarkColor,


                    )
                }
            }
        }
    }
}

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
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(length) { index ->
                    val char = value.getOrNull(index)?.toString() ?: ""
                    val isCurrent = index == value.length && value.length < length

                    Box(
                        modifier = Modifier
                            .size(width = 46.dp, height = 99.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCurrent) Color.Transparent
                                else Color(0xFFF3F3F3)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isCurrent) Color.Red else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
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