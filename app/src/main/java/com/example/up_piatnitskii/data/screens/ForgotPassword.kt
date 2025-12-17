package com.example.up_piatnitskii.data.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.viewModel.ForgotPasswordViewModel
import com.example.up_piatnitskii.ui.theme.BackgroundColor
import com.example.up_piatnitskii.ui.theme.HintColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography
import com.example.up_piatnitskii.ui.theme.SubTextDarkColor
import com.example.up_piatnitskii.ui.theme.TextColor

//18. Создать экран «Forgot Password», как на макете.
private val EMAIL_REGEX = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

@Composable
fun ForgotPassword(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onOTPClick: () -> Unit = {},
    viewModel: ForgotPasswordViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 66.dp)
        ) {
            // Круглая кнопка "назад"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) { //21. Экран «Forgot Password». Реализовать возможность перехода на экран «Sign
                //In» при нажатии на кнопку «Назад».
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


            // Центральная часть
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(id = R.string.forgot_password),
                    color = TextColor,
                    style = RalewayTypography.headingRegular32,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.enter_email_to_reset),
                    style = RalewayTypography.subtitleRegular16,
                    color = SubTextDarkColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(54.dp))

                // Колонка с полями, выровненными слева
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Email
                    Text(
                        text = stringResource(id = R.string.email),
                        style = RalewayTypography.bodyMedium16,
                        color = TextColor,
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { newValue ->
                            email = newValue
                            emailError = !EMAIL_REGEX.matches(newValue)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        isError = emailError,
                        placeholder = {
                            Text(
                                "xyz@gmail.com",
                                color = HintColor,
                                style = RalewayTypography.bodyRegular14,
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            focusedContainerColor = BackgroundColor,
                            unfocusedContainerColor = BackgroundColor,
                            disabledContainerColor = BackgroundColor
                        ),
                    )

                    Spacer(Modifier.height(24.dp))

                    //20. Экран «Forgot Password». При нажатии на диалоговое окно, осуществить
                    //переход на экран «OTP Verification».
                    if (showSuccessDialog) {
                        ForgotPasswordSuccessDialog(
                            onClick = {
                                showSuccessDialog = false
                                onOTPClick()           // переход на экран OTP Verification
                            },
                            onDismiss = { showSuccessDialog = false }
                        )
                    }
                    // Кнопка "Отправить"
                    Button(
                        onClick = {
                            // 22. Отправка запроса на сервер для получения кода
                            viewModel.forgotPassword(
                                email = email,
                                onSuccess = {
                                    showSuccessDialog = true   // 19: показать диалог
                                },
                                onError = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                }
                            )
                        },

                        enabled = email.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF48B2E7),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF2B6B8B),
                            disabledContentColor = Color.White.copy(alpha = 0.7f)
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.send),
                            style = RalewayTypography.bodyRegular14,
                        )
                    }
                }
            }
        }
    }
}

//19. Экран «Forgot Password». При нажатии на кнопку «Отправить», при наличии
//в поле ввода корректного e-mail, отобразить диалоговое окно, как на макете.
@Composable
fun ForgotPasswordSuccessDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            // Кнопка нам не нужна, кликаем по всей карточке
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Иконка сверху
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF48B2E7),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.group_1000000818), // своя иконка конверта
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Text(
                    text = "Проверьте ваш Email",
                    style = RalewayTypography.bodyMedium16,
                    color = TextColor
                )

                Text(
                    text = "Мы отправили код восстановления пароля на вашу электронную почту.",
                    style = RalewayTypography.bodyRegular14,
                    color = SubTextDarkColor,
                    textAlign = TextAlign.Center
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White
    )
}
