package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.smeb9716.R
import com.example.smeb9716.adapter.HomeProductAdapter
import com.example.smeb9716.databinding.FragmentSearchBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.Category
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.utils.ext.gone
import com.example.smeb9716.utils.ext.visible
import com.example.smeb9716.viewmodel.SearchResultViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment(private val screenMode: ScreenMode) : BaseFragment<FragmentSearchBinding>() {
    val viewModel: SearchResultViewModel by viewModels()
    val adapter: HomeProductAdapter by lazy {
        HomeProductAdapter(
            viewType = HomeProductAdapter.ITEM_TYPE_PRODUCT_VERTICAL
        )
    }

    var mFilterType: FilterType = FilterType.ALL
    var mQuery: String = ""

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        binding.tblCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    mFilterType = when (tab.position) {
                        0 -> FilterType.ALL
                        1 -> FilterType.NEW
                        2 -> FilterType.SALE
                        3 -> FilterType.RATING
                        4 -> FilterType.PRICE
                        else -> FilterType.ALL
                    }
                    searchProduct(mQuery, mFilterType)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.imvBack.setOnClickListener {
            goBackFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackFragment()
            }
        })

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchProduct(query, mFilterType)
                mQuery = query ?: ""
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchProduct(newText, mFilterType)
                mQuery = newText ?: ""
                return false
            }
        })
        binding.imvCart.setOnClickListener {
            val fragment = CartsFragment()
            addFragment(R.id.frameLayoutContainer, fragment, true)
        }
        binding.tvCartCount.setOnClickListener {
            val fragment = CartsFragment()
            addFragment(R.id.frameLayoutContainer, fragment, true)
        }
    }

    private fun onProductClick(product: Product) {
        val fragment = ProductDetailFragment.newInstance(product.id)
        addFragment(R.id.frameLayoutContainer, fragment, true)
    }

    private fun onFavoriteClick(product: Product, isFavorite: Boolean) {
        // Handle favorite click event

        if (isFavorite) {
            viewModel.addFavoriteProduct(product)
        } else {
            viewModel.removeFavoriteProduct(product)
        }
    }

    private fun searchProduct(query: String? = null, filterType: FilterType) {
        if (screenMode is ScreenMode.ProductSearch) {
            viewModel.searchProduct(query, filterType, null)
        } else if (screenMode is ScreenMode.CategoryFilter) {
            screenMode
            viewModel.searchProduct(query, filterType, screenMode.category)
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
            it?.let {
                adapter.setItems(it)
            }
        }
        WholeApp.cartCount.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvCartCount.text = it.toString()
            }
        }
    }

    override fun initViews(binding: FragmentSearchBinding) {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.revProduct.layoutManager = gridLayoutManager
        binding.revProduct.adapter = adapter

        adapter.setOnProductClick { product ->
            onProductClick(product)
        }

        adapter.setOnFavoriteClick { product, isFavorite ->
            onFavoriteClick(product, isFavorite)
        }

        when (screenMode) {
            is ScreenMode.ProductSearch -> {
                initProductSearch(binding)
                viewModel.searchProduct(screenMode.query, screenMode.filterType)
                mQuery = screenMode.query
                mFilterType = screenMode.filterType
            }

            is ScreenMode.CategoryFilter -> {
                initCategoryFilter(binding)
                viewModel.searchProduct(
                    screenMode.query, screenMode.filterType, screenMode.category
                )
                mQuery = screenMode.query
                mFilterType = screenMode.filterType
            }
        }
    }

    private fun initProductSearch(binding: FragmentSearchBinding) {
        binding.cardCategory.gone()
        screenMode as ScreenMode.ProductSearch
        screenMode.filterList.forEach {
            binding.tblCategory.addTab(binding.tblCategory.newTab().setText(it.getFilterName()))
        }
        binding.svSearch.setQuery(screenMode.query, false)
    }

    private fun initCategoryFilter(binding: FragmentSearchBinding) {
        binding.cardCategory.visible()
        screenMode as ScreenMode.CategoryFilter
        screenMode.filterList.forEach {
            binding.tblCategory.addTab(binding.tblCategory.newTab().setText(it.getFilterName()))
        }

        binding.tvCategory.text = screenMode.category.name
        Glide.with(binding.ivCategory).load(screenMode.category.image).centerCrop()
            .into(binding.ivCategory)
        binding.svSearch.setQuery(screenMode.query, false)
    }
}

sealed class ScreenMode {
    data class ProductSearch(
        val query: String = "",
        val filterType: FilterType = FilterType.ALL,
        val filterList: List<FilterType> = listOf(
            FilterType.ALL, FilterType.NEW, FilterType.SALE, FilterType.RATING, FilterType.PRICE
        ),
    ) : ScreenMode()

    data class CategoryFilter(
        val category: Category,
        val query: String = "",
        val filterType: FilterType = FilterType.ALL,
        val filterList: List<FilterType> = listOf(
            FilterType.ALL, FilterType.NEW, FilterType.SALE, FilterType.RATING, FilterType.PRICE
        ),
    ) : ScreenMode()
}

enum class FilterType {
    ALL, NEW, SALE, RATING, PRICE
}

fun FilterType.getFilterName(): String {
    return when (this) {
        FilterType.ALL -> "Phổ biến"
        FilterType.NEW -> "Mới nhất"
        FilterType.SALE -> "Giảm giá"
        FilterType.RATING -> "Đánh giá"
        FilterType.PRICE -> "Giá"
    }
}
