package com.example.raion.ui.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ExplanationItem(
    val id: Int,
    val question: String,
    val answer: String,
    val explanation: String
)

@Composable
fun QuizExplanationScreen(
    onNavigateHome: () -> Unit,
    onRetryQuiz: () -> Unit
) {
    val creamBgColor = Color(0xFFFCFDF2)
    val darkGreenColor = Color(0xFF1D5C42)
    val lightGreenColor = Color(0xFFEAF5EA)

    val explanations = listOf(
        ExplanationItem(
            1,
            "Mengapa T-Rex di dalam cerita disebut merusak keindahan hutan?",
            "Karena membuang sampah kaleng sembarangan",
            "T-Rex membuang sampah kaleng sembarangan di hutan salju, yang merusak keindahan tempat tersebut dan mencemari alam."
        ),
        ExplanationItem(
            2,
            "Sampah kaleng yang dibuang T-Rex terbuat dari logam. Berapa lama yang dibutuhkan alam untuk menghancurkan kaleng?",
            "50 sampai 100 tahun",
            "Kaleng logam sulit terurai secara alami dan membutuhkan waktu yang sangat lama, sekitar 50 hingga 100 tahun."
        ),
        ExplanationItem(
            3,
            "Hutan salju yang bersih menghasilkan udara segar. Gas apa yang paling kita butuhkan dari udara segar tersebut?",
            "Oksigen",
            "Pohon menghasilkan oksigen yang sangat penting bagi makhluk hidup untuk bernapas."
        ),
        ExplanationItem(
            4,
            "Apa yang akan terjadi jika sampah kaleng menumpuk di hutan dan tertutup salju?",
            "Tanah menjadi tercemar dan hewan bisa terluka",
            "Sampah logam dapat melukai hewan dan bahan kimianya dapat meresap membuat tanah tercemar."
        ),
        ExplanationItem(
            5,
            "Bagaimana sikapmu jika melihat temanmu membuang sampah sembarangan di hutan?",
            "Menegur dengan sopan dan mengajaknya mencari tempat sampah",
            "Menegur dengan sopan adalah cara terbaik untuk mengingatkan teman tanpa memicu pertengkaran."
        ),
        ExplanationItem(
            6,
            "Aku adalah julukan untuk benda yang bisa dipakai kembali agar tidak jadi sampah, seperti botol minum. Siapakah aku?",
            "Reusable (Guna Ulang)",
            "Barang reusable (guna ulang) didesain agar bisa dipakai berkali-kali untuk mengurangi jumlah sampah plastik."
        ),
        ExplanationItem(
            7,
            "Sampah kaleng dan botol kaca sebaiknya dibuang ke tempat sampah berwarna apa?",
            "Kuning (Daur ulang)",
            "Tempat sampah kuning biasanya ditujukan untuk menampung sampah anorganik yang dapat didaur ulang seperti plastik, kaca, dan kaleng."
        ),
        ExplanationItem(
            8,
            "Pohon di hutan salju membantu mendinginkan bumi. Apa nama peristiwa memanasnya suhu bumi akibat hutan yang rusak?",
            "Pemanasan Global",
            "Pemanasan global adalah meningkatnya suhu rata-rata atmosfer bumi akibat efek rumah kaca, sering dipicu oleh kerusakan hutan."
        ),
        ExplanationItem(
            9,
            "Di dunia nyata, kaleng bekas bisa dilebur untuk dibuat menjadi benda baru. Apa nama proses ini?",
            "Daur Ulang (Recycle)",
            "Daur ulang adalah proses mengubah bahan bekas menjadi material baru untuk mencegah pemborosan dan pencemaran."
        ),
        ExplanationItem(
            10,
            "Apa pesan moral utama dari komik petualangan Dino tadi?",
            "Menjaga kebersihan adalah tugas kita bersama, bukan hanya Dino",
            "Kebersihan lingkungan merupakan tanggung jawab kolektif semua makhluk, bukan hanya perorangan."
        ),
        ExplanationItem(
            11,
            "Jika seekor Brontosaurus membuang aku ke tanah pada zaman purba jutaan tahun lalu, mungkin sampai hari ini aku masih ada dan belum hancur. Aku tidak bisa dimakan oleh tanah (tidak bisa membusuk). Siapakah aku?",
            "Plastik",
            "Plastik adalah bahan buatan yang sangat sulit terurai secara alami, sehingga umurnya dapat mencapai ribuan hingga jutaan tahun."
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        com.example.raion.ui.features.auth.components.WaveBackground()
        Scaffold(
            containerColor = Color.Transparent,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pembahasan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Text(
                    text = "Episode 1: Si Trex",
                    fontSize = 16.sp,
                    color = darkGreenColor
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onNavigateHome,
                    colors = ButtonDefaults.buttonColors(containerColor = darkGreenColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Kembali ke Beranda",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onRetryQuiz,
                    border = BorderStroke(1.dp, darkGreenColor),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = darkGreenColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Kerjakan Ulang",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(explanations) { index, item ->
                ExplanationCard(item = item, darkGreenColor = darkGreenColor, lightGreenColor = lightGreenColor)
            }
        }
    }
    }
}

@Composable
fun ExplanationCard(item: ExplanationItem, darkGreenColor: Color, lightGreenColor: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, darkGreenColor),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Number Circle and Question text
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(lightGreenColor, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.id.toString(),
                        fontWeight = FontWeight.ExtraBold,
                        color = darkGreenColor,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Soal",
                        fontWeight = FontWeight.Bold,
                        color = darkGreenColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = item.question,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                        lineHeight = 20.sp
                    )
                }
            }

            // Answer Segment
            Text(
                text = "Jawaban",
                fontWeight = FontWeight.Bold,
                color = darkGreenColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 44.dp)
            )
            Text(
                text = item.answer,
                fontSize = 14.sp,
                color = darkGreenColor, // Correct answer in slightly bold color 
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 44.dp, top = 4.dp, bottom = 12.dp)
            )

            // Explanation Segment
            Text(
                text = "Pembahasan",
                fontWeight = FontWeight.Bold,
                color = darkGreenColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 44.dp)
            )
            Text(
                text = item.explanation,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 44.dp, top = 4.dp)
            )
        }
    }
}
