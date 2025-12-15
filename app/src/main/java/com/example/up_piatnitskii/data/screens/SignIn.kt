package com.example.up_piatnitskii.data.screens



import androidx.compose.material3.Surface
import androidx.compose.foundation.clickable
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



private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")


@Composable
fun SignInScreen(onBackClick: () -> Unit = {},) {
    var email by remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Привет!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 32.sp

            )
            Text(
                text = "Заполните Свои данные",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(32.dp))

            // Email
            Text(text = "Email", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !emailRegex.matches(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("xyz@gmail.com") },
                isError = emailError,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3),
                    disabledContainerColor = Color(0xFFF3F3F3)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(16.dp))

            // Пароль
            Text(text = "Пароль", style = MaterialTheme.typography.bodyMedium)
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
                                    R.drawable.eye_open     // иконка «глаз закрыт»
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


            Spacer(Modifier.height(24.dp))

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
                Text("Войти")
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Вы здесь впервые? ")
                Text(
                    text = "Создать",
                    color = Color(0xFF000000),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { /* TODO: навигация к логину */ }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {

}

