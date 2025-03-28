package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smeb9716.R
import com.example.smeb9716.adapter.FavoriteProductAdapter
import com.example.smeb9716.databinding.FragmentFavoriteBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    private val viewModel: FavoriteViewModel by viewModels()
    private val adapter: FavoriteProductAdapter by lazy { FavoriteProductAdapter() }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        adapter.setOnProductClick {
            val fragment = ProductDetailFragment.newInstance(it.id)
            addFragment(
                R.id.frameLayoutContainer,
                fragment,
                true,
            )
        }
        adapter.setOnFavoriteRemoveClick {
            viewModel.removeFavoriteProduct(it)
        }
        binding.imvCart.setOnClickListener {

        }
    }

    override fun initObservers() {
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                (requireActivity() as BaseActivity<*>).showLoading(it)
            }
        }
        lifecycleScope.launch {
            viewModel.responseMessage.collect {
                (requireActivity() as BaseActivity<*>).showMessage(
                    requireContext(), it.message, it.bgType
                )
            }
        }
        viewModel.products.observe(viewLifecycleOwner) {
            val data = (it ?: emptyList()).filter { it.isFavorite == true }
            adapter.setData(data)
        }
        WholeApp.cartCount.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvCartCount.text = it.toString()
            }
        }
    }

    override fun initViews(binding: FragmentFavoriteBinding) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.revFavorite.layoutManager = layoutManager
        binding.revFavorite.adapter = adapter

        viewModel.getProducts()
    }

}
