package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemEmptyBinding
import com.example.smeb9716.databinding.ItemFavoriteProductBinding
import com.example.smeb9716.model.Product
import com.example.smeb9716.utils.ext.invisible
import com.example.smeb9716.utils.ext.toMoneyFormat

class FavoriteProductAdapter : RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder>() {
    private val listProduct = mutableListOf<Product>()
    private var onProductClick: ((Product) -> Unit)? = null
    private var onFavoriteRemoveClick: ((Product) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Product>) {
        listProduct.clear()
        listProduct.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnProductClick(onProductClick: ((Product) -> Unit)) {
        this.onProductClick = onProductClick
    }

    fun setOnFavoriteRemoveClick(onFavoriteRemoveClick: ((Product) -> Unit)) {
        this.onFavoriteRemoveClick = onFavoriteRemoveClick
    }

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            product: Product? = null,
            onProductClick: ((Product) -> Unit)?,
            onFavoriteRemoveClick: ((Product) -> Unit),
        ) {
            if (product == null) {
                binding as ItemEmptyBinding
            } else {
                binding as ItemFavoriteProductBinding
                Glide.with(binding.root.context).load(product.image?.firstOrNull() ?: "")
                    .centerCrop().into(binding.imvThumbnail)
                binding.tvName.text = product.name ?: ""

                if (product.discount != null && product.discount > 0) {
                    val discounted = product.getDiscountedPrice().toString().toMoneyFormat()
                    val original = (product.price ?: 0L).toString().toMoneyFormat()

                    binding.tvDiscountPrice.text = "$discounted"
                    binding.tvOriginalPrice.text = "$original"
                    binding.tvOriginalPrice.paintFlags =
                        binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.tvOriginalPrice.text = (product.price ?: 0L).toString().toMoneyFormat()
                    binding.tvDiscountPrice.invisible()
                }

                val rating = product.reviewValue ?: 0.0
                binding.tvRating.text = "$rating/5"

                binding.llInfo.setOnClickListener {
                    onProductClick?.invoke(product)
                }
                binding.imvThumbnail.setOnClickListener {
                    onProductClick?.invoke(product)
                }
                binding.imvRemoveFavorite.setOnClickListener {
                    onFavoriteRemoveClick.invoke(product)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 0) {
            val binding = ItemEmptyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }
        val binding = ItemFavoriteProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (listProduct.isEmpty()) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return if (listProduct.isEmpty()) 1 else listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listProduct.isEmpty()) {
            holder.bind(null, onProductClick, onFavoriteRemoveClick!!)
            return
        }
        holder.bind(listProduct[position], onProductClick, onFavoriteRemoveClick!!)
    }
}
