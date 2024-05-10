package com.limheejin.kakaoimagesearch.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.limheejin.kakaoimagesearch.databinding.FragmentImageSearchBinding
import com.limheejin.kakaoimagesearch.repository.ImageSearchRepositoryImpl
import com.limheejin.kakaoimagesearch.viewmodel.ImageSearchViewModelProviderFactory

class ImageSearchFragment : Fragment() {
    companion object {
        fun newInstance() = ImageSearchFragment()
    }

    private var _binding: FragmentImageSearchBinding? = null

    private val binding get() = _binding!!

    // ViewModel 초기화
    private val viewModel: SearchViewModel by viewModels {
        ImageSearchViewModelProviderFactory(
            ImageSearchRepositoryImpl(requireActivity())
        )
    }

    private val searchListAdapter by lazy {
        SearchListAdapter(
            itemClickListener = {
                // 이미지 아이템 클릭시
                viewModel.updateItem(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateSavedStatus()
    }

    private fun initView() {
        initSearchView()

        initRecyclerView()
    }

    private fun initRecyclerView() = with(binding) {
        recyclerSearch.adapter = searchListAdapter

        recyclerSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerSearch.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    viewModel.plusPageCount()
                }
            }
        })
    }

    private fun initViewModel() = with(viewModel) {
        searchResult.observe(viewLifecycleOwner) {
            // RecyclerView 데이터셋 업데이트
            searchListAdapter.submitList(it.list)

            // 아이템 추가, 삭제시 스낵바 메세지 표시
            if (it.showSnackMessage) {
                it.snackMessage?.let { resId ->
                    showSnackBar(resId)
                }
            }
        }

        // 저장 되어 있는 검색 단어 불러오기
        searchWord.observe(viewLifecycleOwner) {
            binding.searchView.setQuery(it, false)
        }

        // 스크롤 끝 감지시 페이지 수 + 1
        pageCounts.observe(viewLifecycleOwner) {
            val query = binding.searchView.query.toString()
            viewModel.searchCombinedResults(query)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onStop() {
        // 검색 된 단어 저장
        val query = binding.searchView.query.toString()
        viewModel.saveStorageSearchWord(query)
        super.onStop()
    }

    // 이미지 검색을 위한 SearchView 생성
    private fun initSearchView() {
        binding.searchView.isSubmitButtonEnabled = true // 검색 버튼 활성화
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 입력시 호출
                if (query != null) {
                    viewModel.resetPageCount()
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 텍스트 입력, 수정시 호출
                return false
            }
        })

        binding.searchView.setQuery(loadData(), false)
    }

    // 이미지 클릭시 스낵바를 사용하여 메세지 표시
    private fun showSnackBar(resId: Int) {
        Snackbar.make(
            binding.searchFragment,
            getString(resId),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    // 플로팅 버튼 클릭시 리스트의 최상단으로 이동
    fun smoothScrollToTop() =
        binding.recyclerSearch.smoothScrollToPosition(0)
}