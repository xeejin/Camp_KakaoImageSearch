package com.limheejin.kakaoimagesearch.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.limheejin.kakaoimagesearch.R
import com.limheejin.kakaoimagesearch.databinding.ActivityMainBinding
import com.limheejin.kakaoimagesearch.view.fragment.ImageSearchFragment
import com.limheejin.kakaoimagesearch.view.fragment.MyStorageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment(ImageSearchFragment())
        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    replaceFragment(ImageSearchFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.action_storage -> {
                    replaceFragment(MyStorageFragment())
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainerView.id, fragment)
        fragmentTransaction.commit()
    }
}