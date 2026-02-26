package com.example.raion.ui.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raion.R
import com.example.raion.ui.theme.RaionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onLoginSubmit: () -> Unit = {}
) {
    // State untuk menyimpan input pengguna
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    // Warna dari desain
    val CreamBackground = Color(0xFFFFFBE6)
    val TealColor = Color(0xFF6AC9AB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            // Menambahkan scroll agar aman di layar HP kecil
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER (Tombol Back & Judul) ---
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black
                )
            }

            // Spacer fleksibel + Teks di tengah + Spacer fleksibel
            // Ini trik agar teks benar-benar ada di tengah layar
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Masukkan detailmu",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                ),
                // Digeser sedikit ke kiri untuk mengimbangi lebar tombol panah
                modifier = Modifier.offset(x = (-24).dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(48.dp))

        // --- ILUSTRASI DINO ---
        // Catatan: Pastikan kamu menambahkan gambar dino ini ke folder drawable ya!
        // Saya asumsikan nama filenya 'dino_writing'
        Image(
            painter = painterResource(id = R.drawable.dinomenulis),
            contentDescription = "Dino Menulis",
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(48.dp))

        // --- FORM EMAIL ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Nama atau email",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Nama Kamu") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email Icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealColor,
                    unfocusedBorderColor = TealColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FORM PASSWORD ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Kata sandi",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color.Gray
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealColor,
                    unfocusedBorderColor = TealColor,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CHECKBOX INGAT AKU ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = TealColor,
                    uncheckedColor = TealColor,
                    checkmarkColor = Color.White
                )
            )
            Text(
                text = "Ingat Aku",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- TOMBOL MASUK ---
        Button(
            onClick = onLoginSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealColor)
        ) {
            Text(
                text = "MASUK",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // --- FOOTER TEKS ---
        Text(
            text = "lorem ipsum dolor sir amet ini cuman tulisan\nkebijakna dan privasi",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    RaionTheme {
        LoginScreen()
    }
}