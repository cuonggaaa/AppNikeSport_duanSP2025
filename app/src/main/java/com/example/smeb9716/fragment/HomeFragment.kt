package com.example.smeb9716.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.smeb9716.R
import com.example.smeb9716.adapter.HomeCategoryAdapter
import com.example.smeb9716.adapter.HomeProductAdapter
import com.example.smeb9716.adapter.HomeVoucherAdapter
import com.example.smeb9716.databinding.FragmentHomeBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.model.Category
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.viewmodel.HomeViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val userViewModel: HomeViewModel by viewModels()

    private val categoryAdapter by lazy { HomeCategoryAdapter() }

    private val recommendProductAdapter by lazy {
        HomeProductAdapter(
            viewType = HomeProductAdapter.ITEM_TYPE_PRODUCT_VERTICAL, maxItem = 12
        )
    }

    private val topSaleProductAdapter by lazy {
        HomeProductAdapter(
            viewType = HomeProductAdapter.ITEM_TYPE_PRODUCT_HORIZONTAL, maxItem = 6
        )
    }

    private val voucherAdapter by lazy { HomeVoucherAdapter() }

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews(binding: FragmentHomeBinding) {
        // Set up RecyclerView with FlexboxLayoutManager
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        binding.revCategory.layoutManager = flexboxLayoutManager
        binding.revCategory.adapter = categoryAdapter

        categoryAdapter.setOnCategoryClick {
            // Handle category click event
            Toasty.info(requireContext(), "Category clicked: ${it.name}", 1000, true).show()
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.revTopSaleProduct.adapter = topSaleProductAdapter
        binding.revTopSaleProduct.layoutManager = linearLayoutManager

        topSaleProductAdapter.setOnProductClick(::onProductClick)
        topSaleProductAdapter.setOnFavoriteClick(::onFavoriteClick)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.revRecommendProduct.adapter = recommendProductAdapter
        binding.revRecommendProduct.layoutManager = gridLayoutManager

        recommendProductAdapter.setOnProductClick(::onProductClick)
        recommendProductAdapter.setOnFavoriteClick(::onFavoriteClick)
        categoryAdapter.setOnCategoryClick(::onCategoryClick)
        voucherAdapter.setOnVoucherClick { voucher ->
        }

        val voucherLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.revTopVoucher.adapter = voucherAdapter
        binding.revTopVoucher.layoutManager = voucherLayoutManager

        refreshData()
    }

    override fun initEvents() {
        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.isRefreshing = false
            refreshData()
        }
        binding.headerHome.imvCart.setOnClickListener {

        }
        binding.headerHome.tvCartCount.setOnClickListener {

        }
        binding.headerHome.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val fragment = SearchFragment(ScreenMode.ProductSearch(query ?: ""))
                addFragment(R.id.frameLayoutContainer, fragment, true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun refreshData() {
        userViewModel.getBanners()
        userViewModel.getCategories()
        userViewModel.getProducts()
        userViewModel.getVouchers()
    }

    override fun initObservers() {
        lifecycleScope.launch {
            userViewModel.isLoading.collect {
                (requireActivity() as BaseActivity<*>).showLoading(it)
            }
        }
        lifecycleScope.launch {
            userViewModel.responseMessage.collect {
                (requireActivity() as BaseActivity<*>).showMessage(
                    requireContext(), it.message, it.bgType
                )
            }
        }
        userViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    val bannerList = it.map { banner ->
                        SlideModel(banner.image, banner.name ?: "", ScaleTypes.CENTER_CROP)
                    }
                    showImageSlider(bannerList)
                }

            }
        }

        userViewModel.categoryLiveData.observe(viewLifecycleOwner) {
            it?.let {
                categoryAdapter.setItems(it)
            }
        }

        userViewModel.products.observe(viewLifecycleOwner) {
            it?.let {
                topSaleProductAdapter.setItems(it)
                recommendProductAdapter.setItems(it)
            }
        }

        userViewModel.vouchers.observe(viewLifecycleOwner) {
            it?.let {
                voucherAdapter.setData(it)
            }
        }

        WholeApp.cartCount.observe(viewLifecycleOwner) {
            it?.let {
                binding.headerHome.tvCartCount.text = it.toString()
            }
        }
    }

    private fun showImageSlider(bannerList: List<SlideModel>) {
        try {
            binding.imageSlider.visibility = View.VISIBLE
            binding.imageSlider.setImageList(bannerList, ScaleTypes.FIT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onCategoryClick(category: Category) {
        // Handle category click event
        val fragment = SearchFragment(ScreenMode.CategoryFilter(category))
        addFragment(R.id.frameLayoutContainer, fragment, true)
    }

    private fun onProductClick(product: Product) {

    }

    private fun onFavoriteClick(product: Product, isFavorite: Boolean) {
        // Handle favorite click event

        if (isFavorite) {
            userViewModel.addFavoriteProduct(product)
        } else {
            userViewModel.removeFavoriteProduct(product)
        }
    }
}
