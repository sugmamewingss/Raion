package com.example.raion.ui.features.story

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.raion.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiEpisode(
    val id: String,
    val number: Int,
    val title: String,
    val coverImageUrl: String?,
    val contentImageUrl: String?,
    val isLocked: Boolean,
    val isPremium: Boolean,
    val isCompleted: Boolean
)

data class UiChapter(
    val id: String,
    val number: Int,
    val title: String,
    val episodes: List<UiEpisode>
)

data class StoryUiState(
    val isLoading: Boolean = true,
    val chapters: List<UiChapter> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryUiState())
    val uiState: StateFlow<StoryUiState> = _uiState.asStateFlow()

    init {
        loadStoryData()
        viewModelScope.launch {
            repository.refreshTrigger.collect {
                loadStoryData()
            }
        }
    }

    private fun loadStoryData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Fetch in sequence or parallel (for simplicity, sequence here as it's quick)
                val chaptersResult = repository.getActiveChapters()
                val episodesResult = repository.getAllEpisodes()
                val progressResult = repository.getUserProgress()

                if (chaptersResult.isSuccess && episodesResult.isSuccess && progressResult.isSuccess) {
                    val rawChapters = chaptersResult.getOrNull() ?: emptyList()
                    val rawEpisodes = episodesResult.getOrNull() ?: emptyList()
                    val rawProgress = progressResult.getOrNull() ?: emptyList()

                    val completedEpisodeIds = rawProgress.filter { it.isCompleted }.map { it.episodeId }.toSet()

                    val uiChapters = mutableListOf<UiChapter>()
                    var previousCompleted = true // Global tracker for previous episode completion

                    rawChapters.sortedBy { it.chapterNumber }.forEach { chapter ->
                        // Filter episodes for this chapter and sort
                        val chapterEpisodes = rawEpisodes
                            .filter { it.chapterId == chapter.id }
                            .sortedBy { it.episodeNumber }

                        val uiEpisodes = chapterEpisodes.mapIndexed { index, episode ->
                            val isCompleted = completedEpisodeIds.contains(episode.id)
                            Log.d("StoryState", "Chapter ${chapter.chapterNumber} Ep ${episode.episodeNumber} isCompleted: $isCompleted, previousCompleted: $previousCompleted")
                            
                            // Determine if locked
                            // An episode is locked if the previous episode globally is NOT completed.
                            // However, if it's the very first episode in the game (Ch 1, Ep 1), it's always unlocked.
                            // ADDITIONALLY, if the episode has no content yet, it's always locked (Coming Soon)
                            val isLocked = if (episode.contentImageUrl == null) {
                                true
                            } else if (chapter.chapterNumber == 1 && episode.episodeNumber == 1) {
                                false // Always unlock Chapter 1 Episode 1 if it has content
                            } else {
                                !previousCompleted
                            }
                            
                            // Update tracking variable for the NEXT iteration with THIS episode's status
                            previousCompleted = isCompleted

                            UiEpisode(
                                id = episode.id,
                                number = episode.episodeNumber,
                                title = episode.title,
                                coverImageUrl = episode.coverImageUrl,
                                contentImageUrl = episode.contentImageUrl,
                                isLocked = isLocked,
                                isPremium = episode.isPremium,
                                isCompleted = isCompleted
                            )
                        }

                        uiChapters.add(
                            UiChapter(
                                id = chapter.id,
                                number = chapter.chapterNumber,
                                title = "Bab ${chapter.chapterNumber} : ${chapter.title}",
                                episodes = uiEpisodes
                            )
                        )
                    }

                    _uiState.value = StoryUiState(
                        isLoading = false,
                        chapters = uiChapters,
                        error = null
                    )
                } else {
                    _uiState.value = StoryUiState(isLoading = false, error = "Failed to load story data")
                }
            } catch (e: Exception) {
                Log.e("StoryViewModel", "Error loading story data", e)
                _uiState.value = StoryUiState(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    suspend fun markEpisodeCompleted(episodeId: String): Boolean {
        Log.d("StoryViewModel", "Attempting to mark episode $episodeId as completed")
        val result = repository.markEpisodeCompleted(episodeId)
        return if (result.isSuccess) {
            Log.d("StoryViewModel", "Successfully marked $episodeId as completed.")
            true
        } else {
            Log.e("StoryViewModel", "Failed to mark as completed", result.exceptionOrNull())
            false
        }
    }
}
