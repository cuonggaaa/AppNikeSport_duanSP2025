package com.example.smeb9716

import androidx.fragment.app.Fragment
import com.example.smeb9716.databinding.ActivityMainBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.fragment.HomeFragment
import com.example.smeb9716.utils.ext.replaceFragment
import com.example.smeb9716.utils.views.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        // Init HomeFragment
        setCurrentFragment(HomeFragment())

        //Set up BottomNavigationView
        binding.bottomNavigation.itemIconTintList = null

        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this)
        }

    }

    override fun initEvents() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottomBarHome -> {
                    setCurrentFragment(HomeFragment())
                    true
                }

                R.id.bottomBarGift -> {
                    true
                }

                R.id.bottomBarFavorite -> {
                    true
                }

                R.id.bottomBarProfile -> {
                    true
                }

                else -> false
            }
        }
    }

    override fun initObservers() {

    }

    private fun setCurrentFragment(fragment: Fragment) {
        replaceFragment(R.id.frameLayoutContainer, fragment, false)
    }

}