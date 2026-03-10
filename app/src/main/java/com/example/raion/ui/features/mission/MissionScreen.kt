package com.example.raion.ui.features.mission

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.data.model.MissionStep

@Composable
fun MissionScreen(
    viewModel: MissionViewModel = hiltViewModel(),
    onBackWithResult: (gainedXp: Int, gainedCoins: Int, newProgress: Int, isComplete: Boolean) -> Unit,
    onMysteryBoxClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    // Wrapper: back tanpa result (XP/coins = 0, progress tetap)
    val onSimpleBack = {
        onBackWithResult(
            state.totalGainedXp,
            state.totalGainedCoins,
            state.scannedCount,
            state.isMissionComplete
        )
    }

    if (state.isLoading && state.step == MissionStep.JOURNEY) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    AnimatedContent(
        targetState = state.step,
        transitionSpec = {
            if (targetState.ordinal > initialState.ordinal) {
                // Going forward: slide in from right
                (slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300)))
            } else {
                // Going back: slide in from left
                (slideInHorizontally(tween(300)) { -it } + fadeIn(tween(300)))
                    .togetherWith(slideOutHorizontally(tween(300)) { it } + fadeOut(tween(300)))
            }
        },
        label = "MissionStep"
    ) { step ->
        when (step) {
            MissionStep.JOURNEY -> JourneyContent(
                state = state,
                onStartCollecting = { viewModel.startCollecting() },
                onBack = onSimpleBack,
                onMysteryBoxClick = onMysteryBoxClick
            )
            MissionStep.INTRO -> IntroContent(
                onNext = { viewModel.goToSelectType() },
                onBack = { viewModel.goBack() }
            )
            MissionStep.SELECT_TYPE -> SelectTypeContent(
                onSelect = { viewModel.selectType(it) },
                onBack = { viewModel.goBack() }
            )
            MissionStep.SELECT_SUBTYPE -> SelectSubtypeContent(
                categories = state.categories,
                selectedType = state.selectedType ?: "",
                onSelect = { viewModel.selectSubtype(it) },
                onBack = { viewModel.goBack() }
            )
            MissionStep.SELECT_LOCATION -> SelectLocationContent(
                onSelect = { viewModel.selectLocation(it) },
                onBack = { viewModel.goBack() }
            )
            MissionStep.SELECT_QUANTITY -> SelectQuantityContent(
                isLoading = state.isLoading,
                onSelect = { viewModel.selectQuantityAndSubmit(it) },
                onBack = { viewModel.goBack() }
            )
            MissionStep.RESULT -> ResultContent(
                state = state,
                onContinue = { viewModel.continueMission() },
                onExit = onSimpleBack
            )
        }
    }
}
