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

// Регулярка для email: name@domain.ru (только маленькие латинские буквы и цифры,
// TLD минимум 3 символа)
private val EMAIL_REGEX = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")



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

    val disabledColor = Color(0xFF2B6B8B)
    val enabledColor = Color(0xFF48B2E7)

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
                    contentPadding = PaddingValues(0.dp)
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
                    text = "Регистрация",
                    color = TextColor,
                    style = RalewayTypography.headingRegular32,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Заполните Свои Данные",
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
                        text = "Ваше имя",
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
                    Text(
                        text = "Пароль",
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
                            text = "Даю согласие на обработку\nперсональных данных",
                            color = HintColor,
                            style = RalewayTypography.bodyRegular16.copy(
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.email = email
                            viewModel.password = password

                            viewModel.signUp(
                                onSuccess = onRegistrationSuccess,
                                onError = { error -> Toast.makeText(context, error, Toast.LENGTH_LONG).show() }
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
                            containerColor = if (agreementChecked.value &&
                                name.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank()) enabledColor else disabledColor,
                            contentColor = Color.White,
                            disabledContentColor = Color.White.copy(alpha = 0.7f)
                        )

                    ) {
                        Text(
                            "Зарегистрироваться",
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
                    text = "Есть аккаунт? ",
                    color = HintColor,
                    style = RalewayTypography.bodyRegular16,
                )
                Text(
                    text = "Войти",
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

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    //SignUpScreen()
}
