package com.example.smeb9716

import com.example.smeb9716.databinding.ActivityMainBinding
import com.example.smeb9716.foundation.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {


    }

    override fun initEvents() {

    }

    override fun initObservers() {

    }
}