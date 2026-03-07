package com.example.raion.ui.features.auth.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.raion.R
import com.example.raion.ui.features.auth.components.AuthPrimaryButton
import com.example.raion.ui.features.auth.components.DinoDialogBox
import com.example.raion.ui.features.auth.components.RegisterHeader
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onBackToAuthSelection: () -> Unit = {},
    onFinishRegister: () -> Unit = {}
) {
    val step by viewModel.currentStep.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    val progress = when(step) {
        1, 2 -> 0f
        3 -> 0.14f
        4 -> 0.28f
        5 -> 0.42f
        6 -> 0.60f
        7 -> 0.80f
        8 -> 1f
        else -> 0f
    }

    val buttonText = when {
        step == 7 -> "DAFTAR SEKARANG"
        step == 8 -> "SELESAI"
        else -> "LANJUTKAN"
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge, vertical = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                RegisterHeader(
                    progress = progress,
                    showBackButton = step < 8,
                    onBackClick = {
                        if (step == 1) {
                            onBackToAuthSelection()
                        } else {
                            viewModel.previousStep()
                        }
                    }
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge, vertical = 24.dp)
            ) {
                val isButtonLoading = uiState.isLoading || uiState.isCheckingUsername
                AuthPrimaryButton(
                    text = if (uiState.isCheckingUsername) "MEMERIKSA..." else if (uiState.isLoading) "MEMPROSES..." else if(step == 8) "SELESAI" else "LANJUTKAN",
                    onClick = {
                        if (isButtonLoading) return@AuthPrimaryButton
                        if (step == 7) {
                            viewModel.submitRegistration()
                        } else if (step == 8) {
                            onFinishRegister()
                        } else {
                            viewModel.nextStep()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        },
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
    
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()

            AnimatedContent(
            targetState = step,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally(animationSpec = tween(300)) { width -> width } + fadeIn(animationSpec = tween(300))).togetherWith(slideOutHorizontally(animationSpec = tween(300)) { width -> -width } + fadeOut(animationSpec = tween(300)))
                } else {
                    (slideInHorizontally(animationSpec = tween(300)) { width -> -width } + fadeIn(animationSpec = tween(300))).togetherWith(slideOutHorizontally(animationSpec = tween(300)) { width -> width } + fadeOut(animationSpec = tween(300)))
                }
            },
            label = "register_step_animation"
        ) { currentStep ->
            val dialogText = when(currentStep) {
                1 -> "Halo, Teman!\nAku Gobi, sahabatmu di\npetualangan ini!"
                2 -> "Ayo siapkan akunmu\nsebelum memulai\npetualangan seru!"
                3 -> "Halo! Siapa nama kamu?"
                4 -> "Kapan hari ulang\ntahunmu?"
                5 -> "Wah nama yang bagus!\nSekarang buat\nNama Panggilan unik,\nkamu!"
                6 -> "Ayo buat kata sandi\nrahasia!"
                7 -> "Masukkan sandi yang\nbaru kamu buat."
                8 -> "Yey! Semuanya\nSelesai!"
                else -> ""
            }

            val dinoImageRes = when(currentStep) {
                1 -> R.drawable.dino_menyapa
                2 -> R.drawable.dino_aha
                3 -> R.drawable.dino_tanya
                4 -> R.drawable.dino_ultah
                5 -> R.drawable.dino_cool
                6 -> R.drawable.dino_ssst
                7 -> R.drawable.dino_ssstt
                8 -> R.drawable.dino_yeay
                else -> R.drawable.dino_menyapa
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier.heightIn(min = 140.dp).fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    DinoDialogBox(text = dialogText)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxWidth().offset(y = (-20).dp)
                ) {
                    Image(
                        painter = painterResource(id = dinoImageRes),
                        contentDescription = "Dino Illustration",
                        modifier = Modifier
                            .fillMaxWidth(if(currentStep == 8) 0.85f else 0.7f)
                            .height(if(currentStep == 8) 280.dp else 220.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (currentStep in 3..7) {
                    var passwordVisible by remember { mutableStateOf(false) }
                    var showDatePicker by remember { mutableStateOf(false) }

                    if (showDatePicker && currentStep == 4) {
                        val datePickerState = rememberDatePickerState(
                            selectableDates = object : SelectableDates {
                                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                    return utcTimeMillis <= System.currentTimeMillis()
                                }
                            }
                        )
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = { 
                                    showDatePicker = false
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
                                        viewModel.updateBirthDate(formatter.format(Date(millis)))
                                    }
                                }) {
                                    Text("OK", color = DesignTokens.Colors.BrandPrimary)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) { 
                                    Text("Batal", color = DesignTokens.Colors.TextSecondary) 
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = when(currentStep) {
                                3 -> uiState.name
                                4 -> uiState.birthDate
                                5 -> uiState.username
                                6 -> uiState.password
                                7 -> uiState.confirmPassword
                                else -> ""
                            },
                            onValueChange = { newValue ->
                                when(currentStep) {
                                    3 -> viewModel.updateName(newValue)
                                    4 -> viewModel.updateBirthDate(newValue)
                                    5 -> viewModel.updateUsername(newValue)
                                    6 -> viewModel.updatePassword(newValue)
                                    7 -> viewModel.updateConfirmPassword(newValue)
                                }
                            },
                            placeholder = {
                                val placeholderText = when(currentStep) {
                                    3 -> "Masukkan nama kamu"
                                    4 -> "Masukkan tanggal lahir kamu"
                                    5 -> "Masukkan nama panggilan kamu"
                                    6 -> "Masukkan kata sandinya"
                                    7 -> "Masukkan sekali lagi kata sandinya"
                                    else -> ""
                                }
                                Text(text = placeholderText, style = MaterialTheme.typography.bodyLarge)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium),
                            singleLine = true,
                            readOnly = (currentStep == 4),
                            visualTransformation = if (currentStep in 6..7 && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardOptions = when (currentStep) {
                                3 -> KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next)
                                5 -> KeyboardOptions(capitalization = KeyboardCapitalization.None, imeAction = ImeAction.Next)
                                6 -> KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next)
                                7 -> KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
                                else -> KeyboardOptions.Default
                            },
                            keyboardActions = KeyboardActions(
                                onNext = { viewModel.nextStep() },
                                onDone = {
                                    if (step == 7) viewModel.submitRegistration()
                                }
                            ),
                            leadingIcon = if (currentStep == 3) {
                                { Icon(Icons.Default.Person, contentDescription = "Nama") }
                            } else null,
                            trailingIcon = {
                                if (currentStep == 4) {
                                    Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal")
                                } else if (currentStep in 6..7) {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = if (passwordVisible) "Sembunyikan Sandi" else "Lihat Sandi"
                                        )
                                    }
                                }
                            },
                            isError = when(currentStep) {
                                3 -> uiState.nameError != null
                                4 -> uiState.birthDateError != null
                                5 -> uiState.usernameError != null
                                6 -> uiState.passwordError != null
                                7 -> uiState.confirmPasswordError != null
                                else -> false
                            },
                            supportingText = {
                                val errorMsg = when(currentStep) {
                                    3 -> uiState.nameError
                                    4 -> uiState.birthDateError
                                    5 -> uiState.usernameError
                                    6 -> uiState.passwordError
                                    7 -> uiState.confirmPasswordError
                                    else -> null
                                }
                                if (errorMsg != null) {
                                    Text(
                                        text = errorMsg, 
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.offset(x = (-16).dp)
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DesignTokens.Colors.BrandPrimary,
                                unfocusedBorderColor = Color(0xFFBDBDBD),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = DesignTokens.Colors.BrandPrimary,
                                errorBorderColor = Color.Red,
                                errorCursorColor = Color.Red,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedPlaceholderColor = Color(0xFFBDBDBD),
                                unfocusedPlaceholderColor = Color(0xFFBDBDBD),
                                focusedLeadingIconColor = DesignTokens.Colors.BrandPrimary,
                                unfocusedLeadingIconColor = Color(0xFFBDBDBD),
                                errorLeadingIconColor = Color.Red,
                                focusedTrailingIconColor = DesignTokens.Colors.BrandPrimary,
                                unfocusedTrailingIconColor = Color(0xFFBDBDBD),
                                errorTrailingIconColor = Color.Red
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.Black
                            )
                        )
                        
                        if (currentStep == 4) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) { showDatePicker = true }
                            )
                        }
                    }
                }
                
                if (currentStep !in 3..7) {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
        } // End of outer Box wrapping Background and Content
    }
}
