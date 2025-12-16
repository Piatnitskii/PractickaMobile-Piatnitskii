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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.viewModel.SignInViewModel
import com.example.up_piatnitskii.ui.theme.BackgroundColor
import com.example.up_piatnitskii.ui.theme.HintColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography
import com.example.up_piatnitskii.ui.theme.SubTextDarkColor
import com.example.up_piatnitskii.ui.theme.TextColor

private val emailRegex = Regex("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$")

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onBackClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onSignInClick: () -> Unit = {} //
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    var showDialogAlert by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 66.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Круглая кнопка "назад"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Назад",
                        tint = Color.Black
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Привет!",
                style = RalewayTypography.headingRegular32,
                color = TextColor,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Заполните Свои Данные",
                color = SubTextDarkColor,
                style = RalewayTypography.bodyRegular16
            )

            Spacer(Modifier.height(54.dp))

            // Колонка с полями, выровненными слева
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Email
                Text(
                    text = "Email",
                    style = RalewayTypography.bodyMedium16,
                    color = TextColor,
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { newEmail ->
                        email = newEmail
                        emailError = !emailRegex.matches(newEmail)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    placeholder = {
                        Text(
                            "xyz@gmail.com",
                            color = HintColor,
                            style = RalewayTypography.bodyRegular14
                        )
                    },
                    isError = emailError,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedContainerColor = BackgroundColor,
                        unfocusedContainerColor = BackgroundColor,
                        disabledContainerColor = BackgroundColor
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(Modifier.height(30.dp))

                // Пароль
                Text(
                    text = "Пароль",
                    style = RalewayTypography.bodyMedium16,
                    color = TextColor,
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    placeholder = {
                        Text(
                            "*********",
                            color = HintColor,
                            style = RalewayTypography.bodyRegular14
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedContainerColor = BackgroundColor,
                        unfocusedContainerColor = BackgroundColor,
                        disabledContainerColor = BackgroundColor
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
            }

            Spacer(Modifier.height(24.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF48B2E7),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF2B6B8B),
                    disabledContentColor = Color.White
                ),
                enabled = email.isNotBlank()  && password.isNotBlank(),
                onClick = {
                    viewModel.email = email
                    viewModel.password = password

                    viewModel.signIn(
                        onSuccess = {
                            onSignInClick()
                        },
                        onError = { error ->
                            errorMessage = error
                            showDialogAlert = true
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Войти",
                    color = BackgroundColor,
                    style = RalewayTypography.bodyRegular14
                )
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Вы впервые? ",
                    color = HintColor,
                    style = RalewayTypography.bodyRegular16,
                )
                Text(
                    text = "Создать",
                    color = TextColor,
                    style = RalewayTypography.bodyRegular16,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }

        // Диалог ошибки
        if (showDialogAlert) {
            AlertDialog(
                onDismissRequest = { showDialogAlert = false },
                confirmButton = {
                    Button(onClick = { showDialogAlert = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Ошибка") },
                text = { Text(errorMessage) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
//    SignInScreen(
//        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
//        onBackClick = {},
//        onRegisterClick = {},
//        onSignInClick = {}
//    )
}
