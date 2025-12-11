package felix.duan.superherodb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import felix.duan.superherodb.model.SuperHeroData
import felix.duan.superherodb.repo.SuperHeroRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeroPagingListViewModel : ViewModel() {

    private val _superHeroes = MutableStateFlow<List<SuperHeroData>>(emptyList())
    val superHeroes: StateFlow<List<SuperHeroData>> = _superHeroes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // paging
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _currentKeyword = MutableStateFlow<String?>(null)
    val currentKeyword: StateFlow<String?> = _currentKeyword.asStateFlow()

    private val pageSize = 20

    /**
     * load all heroes
     */
    fun loadHeroes(keyword: String? = null) {
        refreshHeroes(keyword)
    }

    fun clearData() {
        _superHeroes.value = emptyList()
    }

    /**
     * paging loading
     */
    fun loadMoreHeroes() {
        if (_isLoadingMore.value || !_hasMore.value) {
            return
        }

        viewModelScope.launch {
            try {
                _isLoadingMore.value = true
                _error.value = null

                val nextPage = _currentPage.value + 1
                val heroes = SuperHeroRepo.loadMore(
                    page = nextPage,
                    pageSize = pageSize,
                    keyword = _currentKeyword.value
                )

                if (heroes.isNotEmpty()) {
                    val currentHeroes = _superHeroes.value.toMutableList()
                    currentHeroes.addAll(heroes)
                    _superHeroes.value = currentHeroes
                    _currentPage.value = nextPage
                }

                _hasMore.value = heroes.size == pageSize
                _isLoadingMore.value = false

            } catch (e: Exception) {
                _error.value = e.message ?: "Error while load more"
                _isLoadingMore.value = false
            }
        }
    }

    fun refreshHeroes(keyword: String? = null) {
        _currentPage.value = 0
        _hasMore.value = true
        _currentKeyword.value = keyword
        _superHeroes.value = emptyList()
        loadMoreHeroes()
    }
}