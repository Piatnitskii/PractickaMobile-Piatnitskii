package com.example.practice_mobile.ui.screen

import SignUpViewModel
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.ui.theme.BackgroundColor
import com.example.up_piatnitskii.ui.theme.HintColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography
import com.example.up_piatnitskii.ui.theme.SubTextDarkColor
import com.example.up_piatnitskii.ui.theme.TextColor
import androidx.compose.material.AlertDialog



//2. Создать экран «Register Account», как на макете.
//3. Экран «Register Account». Реализовать проверку email на корректность
//(соответствие паттерну «name@domenname.ru», где имя и доменное имя может состоять
//только из маленьких букв и цифр, старший домен только из символов количеством больше
private val EMAIL_REGEX = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}$")

// Регистрация
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegistrationSuccess: () -> Unit = {}
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val passwordVisible = remember { mutableStateOf(false) }
    val agreementChecked = remember { mutableStateOf(false) }


    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

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

            // Центральная часть
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(id = R.string.register),
                    color = TextColor,
                    style = RalewayTypography.headingRegular32,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.details),
                    style = RalewayTypography.subtitleRegular16,
                    color = SubTextDarkColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(54.dp))

                // Колонка с полями, выровненными слева
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Имя
                    Text(
                        text = stringResource(id = R.string.name),
                        style = RalewayTypography.bodyMedium16,
                        color = TextColor,
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        placeholder = {
                            Text(
                                "xxxxxxxx",
                                color = HintColor,
                                style = RalewayTypography.bodyRegular14,
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            // Прозрачные границы
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            // Цвета фона
                            focusedContainerColor = BackgroundColor,
                            unfocusedContainerColor = BackgroundColor,
                            disabledContainerColor = BackgroundColor
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // Email
                    Text(
                        text = "Email",
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
                            // Цвета фона
                            focusedContainerColor = BackgroundColor,
                            unfocusedContainerColor = BackgroundColor,
                            disabledContainerColor = BackgroundColor
                        ),
                    )

                    Spacer(Modifier.height(12.dp))

                    // Пароль
                    // 4. Экран «Register Account». Реализовать возможность отображения пароля.
                    //  5. Экран «Register Account». Реализовать корректное отображение иконки при
                    //  отображении и скрытии пароля.
                    Text(
                        text = stringResource(id = R.string.pass),
                        style = RalewayTypography.bodyMedium16,
                        color = TextColor,
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        visualTransformation = if (passwordVisible.value)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        placeholder = {
                            Text(
                                "********",
                                color = HintColor,
                                style = RalewayTypography.bodyRegular14,
                            )
                        },
                        trailingIcon = {
                            androidx.compose.material3.IconButton(
                                onClick = { passwordVisible.value = !passwordVisible.value }
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = if (passwordVisible.value)
                                            R.drawable.eye_open
                                        else
                                            R.drawable.eye_close
                                    ),
                                    contentDescription = if (passwordVisible.value)
                                        "Скрыть пароль"
                                    else
                                        "Показать пароль"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            // Прозрачные границы
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            // Цвета фона
                            focusedContainerColor = BackgroundColor,
                            unfocusedContainerColor = BackgroundColor,
                            disabledContainerColor = BackgroundColor
                        ),
                    )

                    Spacer(Modifier.height(12.dp))

                    // Чекбокс согласия
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (agreementChecked.value)
                                    R.drawable.frame_1000000814
                                else
                                    R.drawable.frame_1000
                            ),
                            contentDescription = if (agreementChecked.value)
                                "Отмечено"
                            else
                                "Не отмечено",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    agreementChecked.value = !agreementChecked.value
                                }
                        )

                        Text(
                            text = stringResource(id = R.string.agree),
                            color = HintColor,
                            style = RalewayTypography.bodyRegular16.copy(
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    //  9. В случае получения ошибки от сервера или отсутствия соединения с сетью
                    //  Интернет, отобразить соответствующий текст ошибки в диалоговом окне.
                    //  6. Экран «Register Account». Кнопка «Зарегистрироваться» должна быть
                    //  активна только при согласии с Условиями и политикой конфиденциальности.
                    // 8. Экран «Register Account». При нажатии на кнопку «Зарегистрироваться»
                    // реализовать отправку запроса на сервер для регистрации с помощью почты и пароля.
                    if (showErrorDialog) {
                        ErrorDialog(errorMessage = errorMessage, onDismiss = { showErrorDialog = false })
                    }

                    Button(
                        onClick = {
                            viewModel.email = email
                            viewModel.password = password

                            viewModel.signUp(
                                onSuccess = onRegistrationSuccess,
                                onError = { error ->
                                    errorMessage = error
                                    showErrorDialog = true }
                            )
                        },
                        enabled = agreementChecked.value &&
                                name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF48B2E7),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF2B6B8B),
                            disabledContentColor = Color.White
                        ),

                    ) {
                        Text(
                            stringResource(id = R.string.sign_up),
                            style = RalewayTypography.bodyRegular14,
                        )
                    }
                }
            }

            // Нижняя строка по центру
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 47.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(

                    text = stringResource(id = R.string.have_acc),
                    color = HintColor,
                    style = RalewayTypography.bodyRegular16,
                )
                Text(
                    text = stringResource(id = R.string.sign_in),
                    color = TextColor,
                    style = RalewayTypography.bodyRegular16,
                    modifier = Modifier.clickable {
                        onLoginClick()
                    }
                )
            }
        }
    }
}
@Composable
fun ErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ошибка") },
        text = { Text(errorMessage) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    //SignUpScreen()
}
