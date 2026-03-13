package com.example.raion.ui.features.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.raion.ui.features.onboarding.components.PageIndicator
import com.example.raion.ui.features.onboarding.components.PrimaryButton
import com.example.raion.ui.features.onboarding.components.SecondaryButton
import com.example.raion.ui.theme.DesignTokens
import kotlinx.coroutines.launch
import com.example.raion.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onNavigateNext: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val navigateToAuth by viewModel.navigateToAuth.collectAsState()
    
    if (navigateToAuth) {
        onNavigateNext()
        viewModel.onEvent(OnboardingEvent.NavigationHandled)
    }
    
    val pages = listOf(
        OnboardingPageInfo(
            title = "Sampah Bisa\nMerusak Bumi!",
            description = "Yuk, bantu Dino menjaga bumi agar tetap bersih dan sehat!",
            imageRes = R.drawable.img_onboarding_1,
            imageScale = 1.0f,
            imageOffsetY = 40.dp
        ),
        OnboardingPageInfo(
            title = "Buang Sampah Pada\nTempatnya Itu Keren",
            description = "Bersama Dino, kita jaga bumi agar kembali bersih dan asri!",
            imageRes = R.drawable.img_onboarding_2,
            imageScale = 1.15f,
            imageOffsetY = 0.dp
        ),
        OnboardingPageInfo(
            title = "Ayo Jadi Pahlawan\nLingkungan!",
            description = "Selesaikan misi, kumpulkan poin, dan selamatkan bumi bersama Dino!",
            imageRes = R.drawable.img_onboarding_3,
            imageScale = 1.15f,
            imageOffsetY = 0.dp
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }

    val backgroundBrush = Brush.verticalGradient(
        0.0f to DesignTokens.Colors.BackgroundGradientStart,
        0.37f to Color(0xFFFBFBFB),
        0.72f to Color(0xFFF7F7F7),
        1.0f to DesignTokens.Colors.BackgroundGradientEnd
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
            },
            modifier = Modifier.fillMaxSize(),
            label = "OnboardingTransition"
        ) { position ->
            val page = pages[position]
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = painterResource(id = page.imageRes),
                        contentDescription = "Onboarding Image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(page.imageScale)
                            .offset(y = page.imageOffsetY)
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = DesignTokens.Colors.CardBackground,
                    shape = RoundedCornerShape(
                        topStart = DesignTokens.Dimensions.CornerRadiusLarge,
                        topEnd = DesignTokens.Dimensions.CornerRadiusLarge
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 24.dp,
                                vertical = 32.dp
                            )
                    ) {
                        val eyebrow = if (position == 0) "Tahukah Kamu?" 
                                      else if (position == 1) "Yuk Buktikan!" 
                                      else "Sudah Siap?"
                        
                        Text(
                            text = eyebrow,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Normal,
                            color = DesignTokens.Colors.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = page.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = DesignTokens.Colors.BrandDark,
                            lineHeight = MaterialTheme.typography.headlineMedium.lineHeight
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = page.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DesignTokens.Colors.TextSecondary
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PageIndicator(
                                pageCount = pages.size,
                                currentPage = currentPage
                            )

                            PrimaryButton(
                                text = if (currentPage == pages.size - 1) "Mulai" else "Lanjut",
                                onClick = {
                                    if (currentPage < pages.size - 1) {
                                        currentPage++
                                        viewModel.onEvent(OnboardingEvent.NextClicked)
                                    } else {
                                        viewModel.onEvent(OnboardingEvent.GetStartedClicked)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentPage > 0) {
                SecondaryButton(
                    text = "Kembali",
                    onClick = { currentPage-- }
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_app),
                        contentDescription = "BinGo App Icon",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BinGo",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            if (currentPage < pages.size - 1) {
                SecondaryButton(
                    text = "Lewati",
                    onClick = { viewModel.onEvent(OnboardingEvent.SkipClicked) }
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
