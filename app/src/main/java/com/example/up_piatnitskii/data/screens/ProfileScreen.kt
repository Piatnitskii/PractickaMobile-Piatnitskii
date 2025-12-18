package com.example.up_piatnitskii.data.screens

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil3.compose.rememberAsyncImagePainter
import com.example.up_piatnitskii.R
import com.example.up_piatnitskii.data.components.DisableButton
import com.example.up_piatnitskii.data.viewModel.ProfileViewModel
import com.example.up_piatnitskii.data.viewModel.SupabaseClient
import com.example.up_piatnitskii.ui.theme.BackgroundColor
import com.example.up_piatnitskii.ui.theme.RalewayTypography
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.isNotEmpty
import com.example.up_piatnitskii.data.Model.Profile

import com.example.up_piatnitskii.data.viewModel.ProfileState
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val profileState: ProfileState by viewModel.profileState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadProfile(context)
    }

    // Проверка, изменились ли данные
    val hasChanges by remember(name, lastName, address, phone) {
        derivedStateOf {
            name != "" || lastName != "" || address != "" || phone != ""
        }
    }

    // РАБОТА С ФОТО
    var tempFhotoFile by remember { mutableStateOf<File?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // запускает системное приложение камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                tempFhotoFile?.let { file ->
                    selectedImageUri = Uri.fromFile(file)
                }
            } else {
                Toast.makeText(context, "Ошибка при съёмке фото", Toast.LENGTH_SHORT).show()
                selectedImageUri = null
            }
        }
    )
    // создает временный файл для фото с timestamp в названии
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss", java.util.Locale.getDefault()).format(Date())
        val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDirectory).apply {
            createNewFile()
        }
    }
    // подготавливает URI и запускает камеру
    fun openCamera() {
        try {
            val photoFile = createImageFile()
            tempFhotoFile = photoFile
            val photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            cameraLauncher.launch(photoUri)
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка при съёмке фото\n"+e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    // запрашивает разрешение CAMERA
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isSuccess ->
            if (isSuccess) {
                openCamera()
            } else {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    )
    // проверяет/запрашивает доступ к камере
    fun checkCameraPermissonAndOpen(){
        val permission = Manifest.permission.CAMERA
        cameraPermissionLauncher.launch(permission)
    }

    when (val state = profileState) {
        is ProfileState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ProfileState.Success -> {


            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Верхняя часть с заголовком и кнопкой редактирования
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.size(40.dp))
                        Text(
                            text = stringResource(id = R.string.profile),
                            style = RalewayTypography.headingSemiBold16,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (isEditing) {
                                    name = state.profile.firstname ?: ""
                                    lastName = state.profile.lastname ?: ""
                                    address = state.profile.address ?: ""
                                    phone = state.profile.phone ?: ""
                                }
                                isEditing = !isEditing
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = if (isEditing) R.drawable.edit else R.drawable.edit),
                                contentDescription = if (isEditing) "Отмена" else "Редактировать",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Аватар по центру
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0))
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(148.dp)
                                    .height(123.dp)
                                    .padding(bottom = 7.dp)
                                    .clickable { checkCameraPermissonAndOpen() }
                                    .clip(RoundedCornerShape(60.dp)),
                                painter = if (selectedImageUri != null) {
                                    rememberAsyncImagePainter(selectedImageUri)
                                } else {
                                    painterResource(id = R.drawable.group_1)
                                },
                                contentDescription = "Фото пациента",
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "$name $lastName",
                            style = RalewayTypography.bodyRegular20
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    BarcodeCard(
                        onClick = {
                            // TODO: действие по нажатию на штрих-код
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Поля профиля
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isEditing) {
                            EditableField(
                                label = stringResource(id = R.string.your_name),
                                value = name,
                                onValueChange = { name = it }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            EditableField(
                                label = stringResource(id = R.string.last_name),
                                value = lastName,
                                onValueChange = { lastName = it }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            EditableField(
                                label = stringResource(id = R.string.address),
                                value = address,
                                onValueChange = { address = it }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            EditableField(
                                label = stringResource(id = R.string.phone_number),
                                value = phone,
                                onValueChange = { phone = it }
                            )
                        } else {
                            InputField(label = stringResource(id = R.string.your_name), value = name)
                            Spacer(modifier = Modifier.height(16.dp))
                            InputField(label = stringResource(id = R.string.last_name), value = lastName)
                            Spacer(modifier = Modifier.height(16.dp))
                            InputField(label = stringResource(id = R.string.address), value = address)
                            Spacer(modifier = Modifier.height(16.dp))
                            InputField(label = stringResource(id = R.string.phone_number), value = phone)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Кнопка сохранения (только в режиме редактирования)
                    if (isEditing) {
                        DisableButton(
                            text = "Сохранить",
                            onClick = {
                                val updatedProfile = Profile(
                                    firstname = name,
                                    lastname = lastName,
                                    address = address,
                                    phone = phone
                                )
                                viewModel.updateProfile(context, updatedProfile)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = hasChanges
                        )
                    }
                }
            }
        }
        is ProfileState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Ошибка: ${state.message}")
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { viewModel.loadProfile(context) }) {
                        Text("Повторить")
                    }
                }
            }
        }
    }

    // Диалог ошибки
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Ошибка") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun BarcodeCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F7FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable._ae2187166e1c92b6c12b24707d7e7e7_1),
                contentDescription = "Штрих-код",
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = RalewayTypography.bodyMedium16.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = BackgroundColor,
            border = BorderStroke(
                width = 1.dp,
                color = Color.White
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (value.isNotEmpty()) value else "Не указано",
                    style = RalewayTypography.bodyRegular16.copy(
                        color = if (value.isNotEmpty()) Color.Black else Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = RalewayTypography.bodyMedium16.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = RalewayTypography.bodyRegular16,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedContainerColor = BackgroundColor,
                unfocusedContainerColor = BackgroundColor,
                disabledContainerColor = BackgroundColor
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    //ProfileScreen(viewModel = ProfileViewModel(SupabaseClient()))
}
