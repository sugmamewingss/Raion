package com.example.raion.ui.features.profile

import androidx.compose.foundation.Image
import coil.compose.SubcomposeAsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialNickname: String,
    initialFullName: String,
    initialBirthDate: String,
    initialAvatarUrl: String = "",
    onBackClick: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
    onSaveSuccess: () -> Unit = {}
) {
    // Scaffold or Main Container for Edit Profile
    var nickname by remember(initialNickname) { mutableStateOf(initialNickname) }
    var fullName by remember(initialFullName) { mutableStateOf(initialFullName) }
    var birthDate by remember(initialBirthDate) { mutableStateOf(initialBirthDate) }
    var showDatePicker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            onSaveSuccess()
            onBackClick() // dismiss
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }

    if (showDatePicker) {
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
                        birthDate = formatter.format(Date(millis))
                    }
                }) {
                    Text("OK", color = DesignTokens.Colors.BrandPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { 
                    Text("Batal", color = Color.Gray) 
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DesignTokens.Colors.CreamBackground)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Space for system status bar
        Spacer(modifier = Modifier.height(32.dp))

        // 1. Header ("Edit Profil")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Edit Profil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        // Horizontal line separator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )

        // 2. Avatar Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFCEEBE1)) // Mint green background
        ) {
            val imageToLoad = if (initialAvatarUrl.isNotEmpty()) initialAvatarUrl else "https://nnloirkwladlazxgpgrm.supabase.co/storage/v1/object/public/avatars/dino_default.png"

            SubcomposeAsyncImage(
                model = imageToLoad,
                contentDescription = "Hero Avatar",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp), // Slightly drop it down
                contentScale = ContentScale.Fit,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = DesignTokens.Colors.OrangePrimary,
                            strokeWidth = 3.dp
                        )
                    }
                },
                error = {
                    Image(
                        painter = painterResource(id = R.drawable.img_dino_default),
                        contentDescription = "Hero Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            )
            // Bottom stroke border divider
            Box(
               modifier = Modifier
                   .align(Alignment.BottomCenter)
                   .fillMaxWidth()
                   .height(1.dp)
                   .background(Color.Black)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Form Section (Fields)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp), // Wider padding for input fields
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            EditableFieldCard(
                label = "Nama Pengguna",
                value = nickname,
                onValueChange = { /* Do nothing, it's read-only */ },
                themeColor = Color(0xFF2B705B), // Dark Teal
                readOnly = true,
                showEditIcon = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            EditableFieldCard(
                label = "Nama Lengkap",
                value = fullName,
                onValueChange = { fullName = it },
                themeColor = Color(0xFFD98A48) // Orange
            )

            Spacer(modifier = Modifier.height(24.dp))

            EditableFieldCard(
                label = "Tanggal Lahir",
                value = birthDate,
                onValueChange = { birthDate = it },
                themeColor = Color(0xFF9042F5), // Purple
                onClick = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 4. Save Button
            val isButtonLoading = uiState.isLoading
            SaveProfileButton(
                text = if (isButtonLoading) "MENYIMPAN..." else "SIMPAN",
                onClick = {
                    if (!isButtonLoading) {
                        viewModel.saveProfile(
                            newName = fullName,
                            newBirthDate = birthDate
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun EditableFieldCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    themeColor: Color,
    onClick: (() -> Unit)? = null,
    readOnly: Boolean = false,
    showEditIcon: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.DarkGray, // Or black matching design
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            // Main Input Box
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                readOnly = readOnly || onClick != null,
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = themeColor,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(themeColor),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, themeColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )

            if (onClick != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick
                        )
                )
            }

            // Floating Badge (Pencil Icon)
            if (showEditIcon) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 6.dp, y = 6.dp) // Offset to make it float outside
                        .size(24.dp)
                        .background(themeColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit $label",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SaveProfileButton(text: String = "SIMPAN", onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6BCCA3), // Solid Green/Teal
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp) // Reset padding for custom shadow sizing
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Inset bottom shadow simulation
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color(0xFF4C987D)) // Darker green bottom edge
            )

            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }
    }
}
