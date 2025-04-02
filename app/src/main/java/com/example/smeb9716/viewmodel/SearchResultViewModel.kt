package com.example.smeb9716.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.fragment.FilterType
import com.example.smeb9716.model.Category
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.filter.ProductSortBy
import com.example.smeb9716.model.filter.ProductSortMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.ParseException
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products get() = _products

    val page = MutableLiveData(1)
    private val pageLimit = 10

    fun searchProduct(keyword: String? = "", filterType: FilterType, category: Category? = null) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val name = if (keyword.isNullOrEmpty()) null else keyword
            showLoading(true)
            var sortBy: ProductSortBy? = null
            var sortMode: ProductSortMode? = null
            when (filterType) {
                FilterType.ALL -> {
                    sortBy = ProductSortBy.NAME
                    sortMode = ProductSortMode.ASC
                }

                FilterType.NEW -> {
                    sortMode = null
                    sortBy = null
                }

                FilterType.SALE -> {
                    sortBy = ProductSortBy.DISCOUNT
                    sortMode = ProductSortMode.DESC
                }

                FilterType.RATING -> {
                    sortMode = null
                    sortBy = null
                }

                FilterType.PRICE -> {
                    sortBy = ProductSortBy.PRICE
                    sortMode = ProductSortMode.ASC
                }
            }
            val response = apiRepository.getProducts(
                name = name,
                page = page.value ?: 1,
                limit = pageLimit,
                sort = sortBy,
                order = sortMode
            )

            handleResponse(response, onSuccess = {
                showLoading(false)
                val products = it?.data?.data ?: emptyList()
                getFavoriteProductIds(products, filterType, category)
            }, onError = {

            })
        }
    }

    private fun applyFilter(products: List<Product>, filterType: FilterType) {
        when (filterType) {
            FilterType.ALL -> {
                Timber.d("Filter All")
                _products.postValue(products)
            }

            FilterType.NEW -> {
                Timber.d("Filter New")
                val sortedProducts = products.sortedWith { p1, p2 ->
                    compareDates(p1.updatedAt ?: "", p2.updatedAt ?: "")
                }
                _products.postValue(sortedProducts)
            }

            FilterType.SALE -> {
                Timber.d("Filter Sale")
                val sortedProducts = products.sortedByDescending { it.discount }
                _products.postValue(sortedProducts)
            }

            FilterType.RATING -> {
                Timber.d("Filter Rating")
                val sortedProducts = products.sortedByDescending { it.reviewValue ?: 0.0 }
                _products.postValue(sortedProducts)
            }

            FilterType.PRICE -> {
                Timber.d("Filter Price")
                _products.postValue(products)
            }
        }
    }

    private fun compareDates(date1: String, date2: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return try {
            val parsedDate1: Date = dateFormat.parse(date1)
            val parsedDate2: Date = dateFormat.parse(date2)
            parsedDate1.compareTo(parsedDate2)
        } catch (e: ParseException) {
            e.printStackTrace()
            0 // Return 0 if there is a parsing error
        }
    }

    private fun getFavoriteProductIds(
        products: List<Product>,
        filterType: FilterType,
        category: Category?,
    ) {
        val productList = products.toMutableList()
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getFavoriteProducts(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                it?.data?.forEach { favoriteProduct ->
                    productList.find { it.id == favoriteProduct.product.id }?.isFavorite = true
                }

                val products = productList.filter { product ->
                    category == null || product.category?.id == category.id
                }
                applyFilter(products, filterType)
            }, onError = { errorMsg ->
                val products = productList.filter { product ->
                    category == null || product.category?.id == category.id
                }
                applyFilter(products, filterType)
            })
        }
    }

    fun addFavoriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.addFavoriteProduct(WholeApp.USER_ID, product.id)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                product.isFavorite = true
                _products.value?.find { it.id == product.id }?.isFavorite = true
                _products.postValue(_products.value)

            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    fun removeFavoriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)

            val response = apiRepository.removeFavoriteProduct(
                userId = WholeApp.USER_ID, productId = product.id
            )

            showLoading(false)
            handleResponse(response = response, onSuccess = {
                product.isFavorite = false
                _products.value?.find { it.id == product.id }?.isFavorite = false
                _products.postValue(_products.value)
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    fun getCartCount() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getCarts(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                // Handle success response
                Timber.d("Cartcount: ${it?.data?.size}")
                WholeApp.cartCount.postValue(it?.data?.size ?: 0)
            }, onError = { errorMsg ->
                // Handle error response
                WholeApp.cartCount.postValue(0)
            })
        }
    }
}