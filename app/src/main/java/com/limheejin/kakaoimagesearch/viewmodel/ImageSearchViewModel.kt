package com.limheejin.kakaoimagesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.limheejin.kakaoimagesearch.model.SearchModel
import com.limheejin.kakaoimagesearch.repository.ImageSearchRepository
import kotlinx.coroutines.launch

class ImageSearchViewModel(
    private val imageSearchRepository: ImageSearchRepository
) : ViewModel() {

    private val _searchResult = MutableLiveData(SearchUiState.init())
    val searchResult: LiveData<SearchUiState> get() = _searchResult

    private var _pageCounts = MutableLiveData(SearchPageCountUiState.init())
    val pageCounts: LiveData<SearchPageCountUiState> get() = _pageCounts

    private val _searchWord = MutableLiveData<String>()
    val searchWord: LiveData<String> get() = _searchWord

    init {
        // 저장 되어 있는 검색 단어를 불러온다.
        getStorageSearchWord()
    }

    // 검색 된 단어를 저장한다.
    fun saveStorageSearchWord(query: String) = viewModelScope.launch {
        imageSearchRepository.saveSearchData(query)
        _searchWord.value = query
    }

    private fun getStorageSearchWord() = viewModelScope.launch {
        _searchWord.value = imageSearchRepository.loadSearchData() ?: ""
    }

    /**
     * viewModelScope.launch를 사용하여 네트워크 요청을 비동기적으로 수행한다.
     * 검색을 통해 수신된 데이터를 리스트로 보여주기 위해 LiveData를 업데이트한다.
     */
    fun searchResults(query: String) = viewModelScope.launch {
        try {
            val pageCounts = _pageCounts.value ?: SearchPageCountUiState.init()

            val imageResponse =
                imageSearchRepository.searchResults(
                    query = query,
                    imagePage = pageCounts.imagePageCount,
                )

            _searchResult.value = SearchUiState(
                list = imageResponse.list.sortedByDescending { it.datetime }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
     * 검색하기 화면으로 돌아왔을 때 보관홤 화면에서 변경 된 값이 있는지 확인 후 업데이트
     */
    fun reloadStorageItems() = viewModelScope.launch {
        val storageItems = imageSearchRepository.getStorageItems()

        _searchResult.value = _searchResult.value?.copy(
            showSnackMessage = false,
            list = _searchResult.value?.list?.map { currentItem ->
                currentItem.copy(isSaved = storageItems.any { it.id == currentItem.id })
            } ?: emptyList()
        )
    }

    /*
     * 검색하기 화면에서 이미지 아이템을 클릭하면 내 보관함에 이미지 데이터를 저장한다.
     */
    private fun saveStorageImage(searchModel: SearchModel) = viewModelScope.launch {
        imageSearchRepository.saveStorageItem(searchModel)

//        updateSnackMessage(R.string.snack_image_save) // 아이템 저장 메세지
    }

    /*
     * 보관함에 있는 아이템을 한 번 더 클릭하면 보관함에서 이미지 데이터를 삭제한다.
     */
    private fun removeStorageItem(searchModel: SearchModel) = viewModelScope.launch {
        imageSearchRepository.removeStorageItem(searchModel)

//        updateSnackMessage("아이템 삭제") // 아이템 삭제 메세지
    }

    // 스낵바 메세지 업데이트
//    private fun updateSnackMessage(snackMessage: Int) {
//        _searchResult.value = _searchResult.value?.copy(
//            showSnackMessage = true,
//            snackMessage = snackMessage
//        )
//    }

    /*
     * 아이템을 클릭할 때 호출되는 함수로 아이템을 보관함에 저장할 때는 saveStorageImage,
     * 저장 된 아이템을 삭제할 때 removeStorageItem 함수가 수행된다.
     */
    fun updateStorageItem(searchModel: SearchModel) {
        val updatedItem = searchModel.copy(isSaved = !searchModel.isSaved)

        viewModelScope.launch {
            if (updatedItem.isSaved) {
                saveStorageImage(updatedItem)
            } else {
                removeStorageItem(updatedItem)
            }

            _searchResult.value = _searchResult.value?.copy(
                list = _searchResult.value?.list?.map {
                    if (it.id == updatedItem.id) updatedItem else it
                } ?: emptyList()
            )
        }
    }

    // 페이지 카운트 초기화
    fun resetPageCount() {
        _pageCounts.value = SearchPageCountUiState.init()
    }

    /*
     * api에서 검색 가능한 페이지 수가 정해져있으므로 최대 페이지 수를 넘기지 않도록 한다.
     * 최대 페이지 수 일 경우에는 다시 1페이지부터 시작한다.
     */
    fun plusPageCount() {
        val currentCounts = _pageCounts.value ?: SearchPageCountUiState.init()

        val imageCount = if (currentCounts.imagePageCount < MAX_PAGE_COUNT_IMAGE)
            currentCounts.imagePageCount + 1
        else 1

        _pageCounts.value = SearchPageCountUiState(
            imagePageCount = imageCount,
        )
    }

}

/*
 * SearchViewModel에서 초기값으로 imageSearchRepository를 전달 받기 위해 Factory 생성
 * imageSearchRepository를 초기값으로 전달 받아서 SearchViewModel을 반환하는 ViewModelProviderFactory 생성
 */
class ImageSearchViewModelProviderFactory(
    private val imageSearchRepository: ImageSearchRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageSearchViewModel::class.java)) {
            return ImageSearchViewModel(
                imageSearchRepository
            ) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}