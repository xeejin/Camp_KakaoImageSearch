package com.limheejin.kakaoimagesearch.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.limheejin.kakaoimagesearch.api.RetrofitInstance
import com.limheejin.kakaoimagesearch.model.ImageDocument
import com.limheejin.kakaoimagesearch.model.ImageSearchResponse
import com.limheejin.kakaoimagesearch.model.SearchModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response


//실질적으로 통신하는 공간인 레포지토리를 생성 (페이지, 사이즈 파라미터는 그냥 하드코딩)
class Repository {
    suspend fun searchImage(query : String, sort : String) : Response<ImageSearchResponse> {
        return RetrofitInstance.api.searchImage(query = query, sort = sort, page = 1, size = 5)
    }
}

interface ImageSearchRepository {

    suspend fun searchImage(
        query: String,
        sort: String = SORT_TYPE,
        page: Int,
        size: Int = MAX_SIZE_IMAGE
    ): SearchResponse<ImageDocument>

    suspend fun saveStorageItem(searchModel: SearchModel)

    suspend fun removeStorageItem(searchModel: SearchModel)

    suspend fun getStorageItems(): List<SearchModel>

    suspend fun searchResults(
        query: String,
        imagePage: Int
    ): SearchUiState

    suspend fun saveSearchData(searchWord: String)

    suspend fun loadSearchData(): String?
}


class ImageSearchRepositoryImpl(context: Context) : ImageSearchRepository {
    override suspend fun searchImage(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): SearchResponse<ImageDocument> {
        return NetWorkClient.ImageNetWork.searchImage(query, sort, page, size)
    }

    private val pref: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, 0)

    /**
     * 이미지 검색 화면에서 이미지를 클릭하면 보관함에 저장하기 위한 함수
     * id를 비교해서 존재하지 않을 경우에만 아이템을 추가한다.
     */
    override suspend fun saveStorageItem(searchModel: SearchModel) {
        val favoriteItems = getPrefsStorageItems().toMutableList()
        val findItem = favoriteItems.find { it.id == searchModel.id }

        if (findItem == null) {
            favoriteItems.add(searchModel)
            savePrefsStorageItems(favoriteItems)
        }
    }

    /**
     * 보관함에 저장된 이미지를 삭제하기 위한 함수
     * 해당 아이템이 보관함에 존재하면 아이템을 삭제한다.
     */
    override suspend fun removeStorageItem(searchModel: SearchModel) {
        val favoriteItems = getPrefsStorageItems().toMutableList()
        favoriteItems.removeAll { it.id == searchModel.id }
        savePrefsStorageItems(favoriteItems)
    }

    override suspend fun searchResults(
        query: String,
        imagePage: Int
    ): SearchUiState = coroutineScope {
        val imageDeferred = async {
            try {
                val response = searchImage(query = query, page = imagePage)
                SearchUiState(list = response.documents?.map {
                    SearchModel(
                        thumbnailUrl = it.thumbnailUrl,
                        siteName = it.displaySiteName,
                        datetime = it.dateTime,
                        itemType = SearchListType.IMAGE
                    )
                } ?: emptyList())
            } catch (e: Exception) {
                throw e
            }
        }

        imageDeferred.await()
    }

    /**
     * 보관함에 저장되어 있는 아이템을 리스트 목록으로 가져온다.
     */
    override suspend fun getStorageItems(): List<SearchModel> {
        return getPrefsStorageItems()
    }

    private fun getPrefsStorageItems(): List<SearchModel> {
        val jsonString = pref.getString(STORAGE_ITEMS, "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            /**
             * Gson()을 사용하여 Json 문자열을 SearchModel 객체로 변환
             */
            Gson().fromJson(jsonString, object : TypeToken<List<SearchModel>>() {}.type)
        }
    }

    /**
     * SearchModel 객체 아이템을 Json 문자열로 변환한 후 저장
     */
    private fun savePrefsStorageItems(items: List<SearchModel>) {
        val jsonString = Gson().toJson(items)
        pref.edit().putString(STORAGE_ITEMS, jsonString).apply()
    }

    /**
     * 검색 된 단어 저장
     */
    override suspend fun saveSearchData(searchWord: String) {
        pref.edit {
            putString(Constants.SEARCH_WORD, searchWord)
        }
    }

    /**
     * 검색 된 단어 불러오기
     */
    override suspend fun loadSearchData(): String? =
        pref.getString(Constants.SEARCH_WORD, "")

}