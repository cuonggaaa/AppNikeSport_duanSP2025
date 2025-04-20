package com.example.smeb9716.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.smeb9716.R
import com.example.smeb9716.adapter.ProductReviewAdapter
import com.example.smeb9716.adapter.capitalizeWords
import com.example.smeb9716.databinding.FragmentProductDetailBinding
import com.example.smeb9716.foundation.BaseActivity
import com.example.smeb9716.foundation.BaseFragment
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.utils.ext.addFragment
import com.example.smeb9716.utils.ext.goBackFragment
import com.example.smeb9716.utils.ext.toMoneyFormat
import com.example.smeb9716.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    private val viewModel by viewModels<ProductDetailViewModel>()
    private val PRODUCT_ID_ARG = "PRODUCT_ID_ARG"

    private val productReviewAdapter by lazy { ProductReviewAdapter() }

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ): FragmentProductDetailBinding {
        return FragmentProductDetailBinding.inflate(inflater, container, false)
    }

    override fun initEvents() {
        binding.imvBack.setOnClickListener {
            goBackFragment()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goBackFragment()
            }
        })

        binding.btnFavorite.setOnClickListener {
            viewModel.product.value?.let {
                if (it.isFavorite == true) {
                    viewModel.removeFavoriteProduct(it)
                } else {
                    viewModel.addFavoriteProduct(it)
                }
            }
        }

//        binding.btnDecreaseQuantity.setOnClickListener {
//            viewModel.decreaseQuantity()
//        }
//
//        binding.btnIncreaseQuantity.setOnClickListener {
//            viewModel.increaseQuantity()
//        }

        binding.imvCart.setOnClickListener {
            val fragment = CartsFragment()
            addFragment(
                R.id.frameLayoutContainer, fragment, true, CartsFragment::class.java.simpleName
            )
        }

        binding.btnAddToCart.setOnClickListener {
//            viewModel.product.value?.let {
//                showConfirmAlert(
//                    "Thêm vào giỏ hàng", "Bạn có chắc chắn muốn thêm sản phẩm này vào giỏ hàng?"
//                ) {
//                    viewModel.addCart(it, viewModel.quantity)
//                }
//            }
            viewModel.product.value?.let {
                val fragment = AddCartSheet(viewModel.product.value!!)
                fragment.show(childFragmentManager, fragment.tag)
            }
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

        viewModel.product.observe(viewLifecycleOwner) {
            it?.let {
                showProductDetail(it)
            }
        }

        viewModel.reviews.observe(viewLifecycleOwner) {
            it?.let {
                productReviewAdapter.setData(it)
            }
        }

//        viewModel.quantityLiveData.observe(viewLifecycleOwner) {
//            binding.tvQuantity.text = it.toString()
//        }

        WholeApp.cartCount.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvCartCount.text = it.toString()
            }
        }

        viewModel.addCartSuccess.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as BaseActivity<*>).showMessage(
                    requireContext(), "Thêm vào giỏ hàng thành công", BGType.BG_TYPE_SUCCESS
                )
                exit()
                viewModel.addCartSuccess.postValue(false)
            }
        }
    }

    private fun showProductDetail(product: Product) {
        Timber.d("Product: $product")

        product.image?.let {
            binding.sliderProductImage.setImageList(product.image.map {
                SlideModel(it, null, ScaleTypes.CENTER_CROP)
            })
        }

        binding.tvProductName.text = (product.name ?: "").capitalizeWords()

        val originalPrice = product.price ?: 0L
        val discountValue = product.discount ?: 0L

        val discountedPrice = (originalPrice - discountValue).coerceAtLeast(0)

        if (discountValue > 0) {
            binding.tvProductDiscountedPrice.text = discountedPrice.toString().toMoneyFormat()
            binding.tvProductOriginalPrice.text = originalPrice.toString().toMoneyFormat()
        } else {
            binding.tvProductDiscountedPrice.text = originalPrice.toString().toMoneyFormat()
            binding.tvProductOriginalPrice.visibility = View.GONE
        }

        binding.tvCategory.text = (product.category?.name ?: "").capitalizeWords()
        binding.tvDescription.text = product.description ?: ""

        val rating = product.reviewValue?.toFloat() ?: 0f
        binding.ratingBar.rating = rating
        binding.tvRating.text = "$rating (${product.reviewCount ?: 0})"

        binding.btnFavorite.setImageResource(
            if (product.isFavorite == true) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        )

//        binding.tvQuantity.text = viewModel.quantity.toString()
//        binding.tvSize.text = "Kích thước: " + product.size ?: ""
    }

    override fun initViews(binding: FragmentProductDetailBinding) {

        arguments?.getString(PRODUCT_ID_ARG)?.let {
            viewModel.getProductDetail(it)
            viewModel.getProductReviews(it)
            viewModel.getCartCount()
        }

        viewModel.quantity = 1

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.reverseLayout = false
        layoutManager.isAutoMeasureEnabled = true
        binding.revReview.layoutManager = layoutManager
        binding.revReview.setHasFixedSize(true)
        binding.revReview.adapter = productReviewAdapter
        binding.revReview.isNestedScrollingEnabled = false
    }

    fun exit() {
        goBackFragment()
    }

    companion object {
        fun newInstance(productId: String? = "") = ProductDetailFragment().apply {
            arguments = Bundle().apply {
                putString(PRODUCT_ID_ARG, productId)
            }
        }
    }
}
