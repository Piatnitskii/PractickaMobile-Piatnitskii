package com.example.practice_mobile.ui.screen

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.up_piatnitskii.R

// Регулярка для email: name@domain.ru (только маленькие латинские буквы и цифры,
// TLD минимум 3 символа)
private val EMAIL_REGEX = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

private fun isEmailValid(email: String): Boolean = EMAIL_REGEX.matches(email)

// Регистрация
@Composable
fun SignUpScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    val name = remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }          // <‑‑ исправлено
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val agreementChecked = remember { mutableStateOf(false) }

    val showEmailErrorDialog = remember { mutableStateOf(false) }
    val disabledColor = Color(0xFF2B6B8B)
    val enabledColor = Color(0xFF48B2E7)

    var emailError by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {

        if (showEmailErrorDialog.value) {
            AlertDialog(
                onDismissRequest = { showEmailErrorDialog.value = false },
                confirmButton = {
                    Button(onClick = { showEmailErrorDialog.value = false }) {
                        Text("Ок")
                    }
                },
                title = { Text("Ошибка") },
                text = {
                    Text(
                        "Некорректный email. Используйте формат name@domain.ru (только строчные буквы и цифры, домен не короче 3 символов)."
                    )
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            // Круглая кнопка "назад"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp),
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
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Регистрация",
                    color = Color(0xFF2B2B2B),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Заполните Свои данные",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF707B81),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(32.dp))

                // Колонка с полями, выровненными слева
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Имя
                    Text(
                        text = "Ваше имя",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2B2B2B),
                        fontSize = 16.sp
                    )
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        placeholder = {
                            Text(
                                "xxxxxxxx",
                                color = Color(0xFF6A6A6A),
                                fontSize = 14.sp
                            )
                        },
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Email
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2B2B2B),
                        fontSize = 16.sp
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { newValue ->
                            email = newValue
                            emailError = !EMAIL_REGEX.matches(newValue)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        isError = emailError,
                        placeholder = {
                            Text(
                                "name@domain.ru",
                                color = Color(0xFF6A6A6A),
                                fontSize = 14.sp
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Пароль
                    Text(
                        text = "Пароль",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2B2B2B),
                        fontSize = 16.sp
                    )
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        visualTransformation = if (passwordVisible.value)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
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
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Чекбокс согласия
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (agreementChecked.value)
                                    R.drawable.vector
                                else
                                    R.drawable.policy_check
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
                            fontSize = 18.sp,
                            text = "Даю согласие на обработку\nперсональных данных",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (!isEmailValid(email)) {
                                showEmailErrorDialog.value = true
                            } else {
                                onRegisterClick(name.value, email, password.value)
                            }
                        },
                        enabled = agreementChecked.value &&
                                name.value.isNotBlank() &&
                                email.isNotBlank() &&
                                password.value.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = enabledColor,
                            disabledContainerColor = disabledColor,
                            contentColor = Color.White,
                            disabledContentColor = Color.White.copy(alpha = 0.7f)
                        )
                    ) {
                        Text("Зарегистрироваться")
                    }
                }
            }

            // Нижняя строка по центру
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Есть аккаунт? ",
                    color = Color(0xFF707B81)
                )
                Text(
                    text = "Войти",
                    color = Color(0xFF2B2B2B),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
