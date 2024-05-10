package com.limheejin.kakaoimagesearch.view.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.limheejin.kakaoimagesearch.R
import com.limheejin.kakaoimagesearch.viewmodel.MyStorageViewModel

class MyStorageFragment : Fragment() {

    companion object {
        fun newInstance() = MyStorageFragment()
    }

    private val viewModel: MyStorageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_storage, container, false)
    }
}