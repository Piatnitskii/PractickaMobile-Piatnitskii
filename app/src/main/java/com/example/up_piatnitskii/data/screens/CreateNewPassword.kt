package com.example.up_piatnitskii.data.screens


import androidx.compose.material3.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.up_piatnitskii.ui.theme.UPPiatnitskiiTheme


private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

// СОЗДАНИЕ ЭКРАНА СОЗДАНИЯ НОВГО ПАРОЛЯ, Щедрин Артем, 16.12.2025
@Composable
fun CreateNewPassword(

) {
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val re_password = remember { mutableStateOf("") }
    val re_passwordVisible = remember { mutableStateOf(false) }

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
                text = "Задать Новый Пароль",
                style = MaterialTheme.typography.headlineMedium,

                )
            Text(
                text = "Установите Новый Пароль Для Входа В\n         " +
                        "        Вашу Учетную Запись",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(32.dp))

            // Пароль
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Пароль", style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3),
                    disabledContainerColor = Color(0xFFF3F3F3)
                ),
                visualTransformation = if (passwordVisible.value)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
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
                                "Показать пароль",
                            tint = Color.Unspecified
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(16.dp))

            // Подтверждение пароля
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Подтверждение пароля", style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = re_password.value,
                onValueChange = { re_password.value = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3),
                    disabledContainerColor = Color(0xFFF3F3F3)
                ),
                visualTransformation = if (passwordVisible.value)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            painter = painterResource(
                                id = if (re_passwordVisible.value)
                                    R.drawable.eye_open
                                else
                                    R.drawable.eye_close
                            ),
                            contentDescription = if (re_passwordVisible.value)
                                "Скрыть пароль"
                            else
                                "Показать пароль",
                            tint = Color.Unspecified
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(40.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF48B2E7),
                    contentColor = Color.White,

                    disabledContainerColor = Color(0xFF2B6B8B),
                    disabledContentColor = Color.White
                ),
                onClick = { /* TODO: регистрация */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Сохранить")
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateNewPasswordScreenPreview() {

}