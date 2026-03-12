package com.example.raion.ui.features.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.local.UserPreferences
import com.example.raion.data.model.UserProfile
import com.example.raion.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.raion.data.model.quiz.QuizChapter
import com.example.raion.data.model.quiz.UserChapterProgress
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.raion.R
import com.example.raion.ui.theme.DesignTokens
import androidx.lifecycle.SavedStateHandle
import com.example.raion.data.model.quiz.QuizEpisode
import com.example.raion.data.model.quiz.UserQuizProgress
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.raion.data.model.quiz.QuizQuestionDto
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay
import com.example.raion.data.model.quiz.QuizResultResponse

data class QuizPrepUiState(
    val isLoading: Boolean = true,
    val chapter: QuizChapter? = null,
    val episode: QuizEpisode? = null,
    val questionsCount: Int = 0,
    val errorMessage: String? = null
)

@HiltViewModel
class QuizPrepViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val episodeId: String = checkNotNull(savedStateHandle["episodeId"])

    private val _uiState = MutableStateFlow(QuizPrepUiState())
    val uiState: StateFlow<QuizPrepUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            // For now, fetch all chapters and episodes to find the specific ones. 
            // In a larger app we'd have a getEpisodeById and getChapterById Repository function
            val chaptersResult = homeRepository.getQuizChapters()
            var matchedEpisode: QuizEpisode? = null
            var matchedChapter: QuizChapter? = null

            val chapters = chaptersResult.getOrNull() ?: emptyList()
            for (ch in chapters) {
                val epsResult = homeRepository.getQuizEpisodes(ch.id)
                val epsList = epsResult.getOrNull() ?: emptyList()
                val foundEp = epsList.find { it.id == episodeId }
                if (foundEp != null) {
                    matchedEpisode = foundEp
                    matchedChapter = ch
                    break
                }
            }

            // Get questions count
            val questionsResult = homeRepository.getQuizQuestions(episodeId)
            val questionsCount = questionsResult.getOrNull()?.size ?: 0

            if (matchedEpisode != null && matchedChapter != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        chapter = matchedChapter,
                        episode = matchedEpisode,
                        questionsCount = questionsCount
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat detail persiapan kuis"
                    )
                }
            }
        }
    }
}

data class QuizQuestionUiState(
    val isLoading: Boolean = true,
    val questions: List<QuizQuestionDto> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect: Boolean = false,
    val correctAnswersCount: Int = 0,
    val showPopup: Boolean = false,
    val timeLeft: Int = 3,
    val isQuizFinished: Boolean = false,
    val isHalfwayBreak: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class QuizQuestionViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val episodeId: String = checkNotNull(savedStateHandle["episodeId"])

    private val _uiState = MutableStateFlow(QuizQuestionUiState())
    val uiState: StateFlow<QuizQuestionUiState> = _uiState.asStateFlow()
    
    // Gunakan job agar timer bisa dibatalkan saat berpindah
    private var timerJob: kotlinx.coroutines.Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val questionsResult = homeRepository.getQuizQuestions(episodeId)

            if (questionsResult.isSuccess) {
                val questions = questionsResult.getOrDefault(emptyList())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        questions = questions.sortedBy { q -> q.orderIndex }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat soal kuis"
                    )
                }
            }
        }
    }
    
    fun onAnswerSelected(index: Int) {
        val currentState = _uiState.value
        if (currentState.showPopup || currentState.questions.isEmpty()) return // Cegah klik double

        val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
        val isCorrect = index == currentQuestion.correctAnswerIndex

        _uiState.update {
            it.copy(
                selectedAnswerIndex = index,
                isAnswerCorrect = isCorrect,
                showPopup = true,
                timeLeft = 3
            )
        }
        
        startPopupTimer()
    }

    private fun startPopupTimer() {
        timerJob?.cancel() // Batalkan timer sebelumnya jika ada
        timerJob = viewModelScope.launch {
            var timeLeft = 3
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
                _uiState.update { it.copy(timeLeft = timeLeft) }
            }
            // Timer selesai
            handleNextQuestion()
        }
    }

    private fun handleNextQuestion() {
        val currentState = _uiState.value
        val questions = currentState.questions
        
        // Tally score
        val newCorrectCount = if (currentState.isAnswerCorrect) {
            currentState.correctAnswersCount + 1
        } else {
            currentState.correctAnswersCount
        }

        val halfwayIndex = if (questions.size > 1) (questions.size / 2) - 1 else -1

        if (currentState.currentQuestionIndex < questions.size - 1) {
            val nextIndex = currentState.currentQuestionIndex + 1
            if (currentState.currentQuestionIndex == halfwayIndex) {
                 _uiState.update {
                    it.copy(
                        currentQuestionIndex = nextIndex,
                        correctAnswersCount = newCorrectCount,
                        selectedAnswerIndex = null,
                        showPopup = false,
                        isHalfwayBreak = true
                    )
                }
            } else {
                 _uiState.update {
                    it.copy(
                        currentQuestionIndex = nextIndex,
                        correctAnswersCount = newCorrectCount,
                        selectedAnswerIndex = null,
                        showPopup = false
                    )
                }
            }
        } else {
            // Selesai
             _uiState.update {
                it.copy(
                    correctAnswersCount = newCorrectCount,
                    isQuizFinished = true,
                    selectedAnswerIndex = null,
                    showPopup = false
                )
            }
        }
    }

    fun dismissHalfwayBreak() {
        _uiState.update { it.copy(isHalfwayBreak = false) }
    }
    
    // Optional: untuk meng-hide popup jika user klik secara manual
    fun dismissPopupFast() {
        timerJob?.cancel()
        handleNextQuestion()
    }
}

data class QuizUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val userRank: Int = 0,
    val chapters: List<QuizChapter> = emptyList(),
    val chapterProgress: List<UserChapterProgress> = emptyList(),
    val currentTargetProgress: Int = 0,
    val currentTargetMax: Int = 10,
    val errorMessage: String? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuizData()
    }

    private fun loadQuizData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val profileResult = homeRepository.getUserProfile()
            // Fetch top 100 to determine user's rank
            val leaderboardResult = homeRepository.getTopPlayerProfiles(limit = 100)
            
            // Fetch Chapters & Progress
            val chaptersResult = homeRepository.getQuizChapters()
            var chaptersProgressResult: Result<List<UserChapterProgress>>? = null
            
            val profile = profileResult.getOrNull()
            if (profile != null) {
                chaptersProgressResult = homeRepository.getUserChapterProgress(profile.id)
            }

            if (profileResult.isSuccess && leaderboardResult.isSuccess && chaptersResult.isSuccess) {
                val leaderboard = leaderboardResult.getOrDefault(emptyList())
                val rank = leaderboard.indexOfFirst { it.id == profile?.id } + 1
                
                val chapters = chaptersResult.getOrDefault(emptyList())
                val progress = chaptersProgressResult?.getOrDefault(emptyList()) ?: emptyList()

                // Opsi C: Dynamic Target Calculation based on Active Chapter
                var activeChapter: QuizChapter? = chapters.sortedBy { it.chapterNumber }.firstOrNull()
                for (chapter in chapters.sortedBy { it.chapterNumber }) {
                    val prevChapter = chapters.find { it.chapterNumber == chapter.chapterNumber - 1 }
                    val isPrevCompleted = if (prevChapter == null) true else progress.find { it.chapterId == prevChapter.id }?.isCompleted == true
                    val isThisCompleted = progress.find { it.chapterId == chapter.id }?.isCompleted == true
                    
                    if (isPrevCompleted && !isThisCompleted) {
                        activeChapter = chapter
                        break
                    }
                }
                
                if (activeChapter == null && chapters.isNotEmpty()) {
                     activeChapter = chapters.last() // Or keep it at the latest unlocked
                }

                var currentProgress = 0
                var currentMax = 10

                if (activeChapter != null && profile != null) {
                    val maxQuestionsResult = homeRepository.getTotalQuestionsByChapter(activeChapter.id)
                    currentMax = maxQuestionsResult.getOrNull() ?: 10
                    if (currentMax == 0) currentMax = 10 

                    val allQuizProgressResult = homeRepository.getUserQuizProgress(profile.id)
                    val allQuizProgress = allQuizProgressResult.getOrNull() ?: emptyList()
                    
                    val chapterEpisodesResult = homeRepository.getQuizEpisodes(activeChapter.id)
                    val chapterEpisodes = chapterEpisodesResult.getOrNull() ?: emptyList()

                    val episodeIds = chapterEpisodes.map { it.id }
                    val relevantProgress = allQuizProgress.filter { it.episodeId in episodeIds }

                    currentProgress = relevantProgress.sumOf { it.score }
                    if (currentProgress > currentMax) currentProgress = currentMax
                }
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userProfile = profile,
                        userRank = if (rank > 0) rank else 0,
                        chapters = chapters,
                        chapterProgress = progress,
                        currentTargetProgress = currentProgress,
                        currentTargetMax = currentMax
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat data histori kuis"
                    )
                }
            }
        }
    }
}

data class QuizEpisodeUiState(
    val isLoading: Boolean = true,
    val chapter: QuizChapter? = null,
    val episodes: List<QuizEpisode> = emptyList(),
    val episodeProgress: List<UserQuizProgress> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class QuizEpisodeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chapterId: String = checkNotNull(savedStateHandle["chapterId"])

    private val _uiState = MutableStateFlow(QuizEpisodeUiState())
    val uiState: StateFlow<QuizEpisodeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            // 1. Get Chapter Details
            val chaptersResult = homeRepository.getQuizChapters()
            val chapter = chaptersResult.getOrNull()?.find { it.id == chapterId }
            
            // 2. Get Episodes
            val episodesResult = homeRepository.getQuizEpisodes(chapterId)
            
            // 3. Get User Profile -> Progress
            val profileResult = homeRepository.getUserProfile()
            var progressResult: Result<List<UserQuizProgress>>? = null
            
            val profile = profileResult.getOrNull()
            if (profile != null) {
                progressResult = homeRepository.getUserQuizProgress(profile.id)
            }

            if (episodesResult.isSuccess) {
                val episodes = episodesResult.getOrDefault(emptyList())
                val progress = progressResult?.getOrDefault(emptyList()) ?: emptyList()
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        chapter = chapter,
                        episodes = episodes,
                        episodeProgress = progress
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat data episode"
                    )
                }
            }
        }
    }
}

data class QuizCompleteUiState(
    val isLoading: Boolean = true,
    val result: QuizResultResponse? = null,
    val totalQuestions: Int = 0,
    val errorMessage: String? = null
)

@HiltViewModel
class QuizCompleteViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // These parameters must match the navArgument names in AppNavigation.kt
    private val episodeId: String = checkNotNull(savedStateHandle["episodeId"])
    private val correctCount: Int = checkNotNull(savedStateHandle["correctCount"])

    private val _uiState = MutableStateFlow(QuizCompleteUiState())
    val uiState: StateFlow<QuizCompleteUiState> = _uiState.asStateFlow()

    init {
        submitResult()
    }

    private fun submitResult() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            // 1. Submit result
            val submitResult = homeRepository.submitQuizResult(episodeId, correctCount)
            
            // 2. Get total questions for UI calculation
            val questionsResult = homeRepository.getQuizQuestions(episodeId)
            val totalQuestions = questionsResult.getOrNull()?.size ?: correctCount // fallback to correctCount

            if (submitResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        result = submitResult.getOrNull(),
                        totalQuestions = totalQuestions
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memproses hasil kuis: ${submitResult.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }
}

data class QuizExplanationUiState(
    val isLoading: Boolean = true,
    val questions: List<com.example.raion.data.model.quiz.QuizQuestionDto> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class QuizExplanationViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val episodeId: String = checkNotNull(savedStateHandle["episodeId"])

    private val _uiState = MutableStateFlow(QuizExplanationUiState())
    val uiState: StateFlow<QuizExplanationUiState> = _uiState.asStateFlow()

    init {
        loadExplanations()
    }

    private fun loadExplanations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val questionsResult = homeRepository.getQuizQuestions(episodeId)
            
            if (questionsResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        questions = questionsResult.getOrDefault(emptyList()).sortedBy { q -> q.orderIndex }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat pembahasan"
                    )
                }
            }
        }
    }
}
