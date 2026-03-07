package com.example.raion.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.features.auth.components.AuthPrimaryButton
import com.example.raion.ui.features.auth.components.WaveBackground
import com.example.raion.ui.theme.DesignTokens
import com.example.raion.ui.theme.RaionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onLoginSubmit: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSubmit()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge, vertical = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.offset(x = (-12).dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = (-8).dp)
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Masukkan detailmu",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            ),
                            modifier = Modifier.offset(x = (-16).dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            WaveBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = DesignTokens.Dimensions.PaddingLarge)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.dino_writing),
            contentDescription = "Dino Menulis",
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Nama Pengguna",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DesignTokens.Colors.TextSecondary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { viewModel.updateUsername(it) },
                placeholder = { Text("Masukkan nama pengguna", style = MaterialTheme.typography.bodyLarge) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "User Icon"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next
                ),
                isError = uiState.usernameError != null,
                supportingText = { uiState.usernameError?.let { err -> Text(err, color = MaterialTheme.colorScheme.error, modifier = Modifier.offset(x = (-16).dp)) } },
                shape = RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Black
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DesignTokens.Colors.BrandPrimary,
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
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
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Kata Sandi",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DesignTokens.Colors.TextSecondary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = { Text("Masukkan kata sandi", style = MaterialTheme.typography.bodyLarge) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Lock Icon"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan Sandi" else "Lihat Sandi"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { viewModel.submitLogin(rememberMe) }),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.passwordError != null,
                supportingText = { uiState.passwordError?.let { err -> Text(err, color = MaterialTheme.colorScheme.error, modifier = Modifier.offset(x = (-16).dp)) } },
                shape = RoundedCornerShape(DesignTokens.Dimensions.CornerRadiusMedium),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Black
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DesignTokens.Colors.BrandPrimary,
                    unfocusedBorderColor = Color(0xFFBDBDBD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
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
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-12).dp, y = (-4).dp), // Negatif offset X (menarik ke kiri untuk menghilangkan padding default), dan Y (mendekatkan ke password)
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = DesignTokens.Colors.BrandPrimary,
                    uncheckedColor = DesignTokens.Colors.BrandPrimary,
                    checkmarkColor = Color.White
                )
            )
            Text(
                text = "Simpan info login",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = DesignTokens.Colors.TextSecondary,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.offset(x = (-4).dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        AuthPrimaryButton(
            text = if (uiState.isLoading) "MEMPROSES..." else "MASUK",
            onClick = {
                if (uiState.isLoading) return@AuthPrimaryButton
                viewModel.submitLogin(rememberMe)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        val annotatedString = buildAnnotatedString {
            append("Belum punya akun? ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = DesignTokens.Colors.BrandPrimary)) {
                append("Daftar")
            }
        }
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
                color = DesignTokens.Colors.TextSecondary
            ),
            modifier = Modifier.clickable { onBackClick() },
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))
        }
        } // End of outer Box wrapping Background and Content
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    RaionTheme {
        LoginScreen()
    }
}