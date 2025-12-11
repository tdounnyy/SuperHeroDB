package felix.duan.superherodb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import felix.duan.superherodb.model.SuperHeroData
import felix.duan.superherodb.repo.SuperHeroRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeroListViewModel : ViewModel() {

    private val _superHeroes = MutableStateFlow<List<SuperHeroData>>(emptyList())
    val superHeroes: StateFlow<List<SuperHeroData>> = _superHeroes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadHeroes(keyword: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val heroes = if (keyword.isNullOrEmpty()) {
                    SuperHeroRepo.getAll()
                } else {
                    SuperHeroRepo.searchLocal(keyword)
                }

                _superHeroes.value = heroes
                _isLoading.value = false
            } catch (e: Exception) {
                // TODO: 2025/12/11 (duanyufei) user friendly description
                _error.value = e.message ?: "Unknown error occurred"
                _isLoading.value = false
            }
        }
    }

    fun clearData() {
        _superHeroes.value = emptyList()
    }
}