package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.smeb9716.R
import com.example.smeb9716.databinding.ItemEmptyBinding
import com.example.smeb9716.databinding.ItemProductHorizontalBinding
import com.example.smeb9716.databinding.ItemProductVerticalBinding
import com.example.smeb9716.model.Product
import com.example.smeb9716.utils.ext.toMoneyFormat
import timber.log.Timber

class HomeProductAdapter(val viewType: Int = ITEM_TYPE_PRODUCT_VERTICAL, val maxItem: Int = -1) :
    RecyclerView.Adapter<HomeProductAdapter.ViewHolder>() {
    private val items = mutableListOf<Product>()
    private var onProductClick: ((Product) -> Unit)? = null
    private var onFavoriteClick: ((product: Product, isFavorite: Boolean) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Product>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnProductClick(onProductClick: ((Product) -> Unit)) {
        this.onProductClick = onProductClick
    }

    fun setOnFavoriteClick(onFavoriteClick: ((product: Product, isFavorite: Boolean) -> Unit)) {
        this.onFavoriteClick = onFavoriteClick
    }

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product? = null, viewType: Int) {
            Timber.d("Product: $product")

            if (product == null) {
                binding as ItemEmptyBinding
                return
            }

            val discount = product.discount ?: 0
            val origin = product.price ?: 0
            val price = if (origin > discount) origin - discount else origin

            val binding = when (viewType) {
                ITEM_TYPE_PRODUCT_HORIZONTAL -> binding as ItemProductHorizontalBinding
                ITEM_TYPE_PRODUCT_VERTICAL -> binding as ItemProductVerticalBinding
                -1 -> binding as ItemEmptyBinding
                else -> binding as ItemProductVerticalBinding
            }

            when (viewType) {
                ITEM_TYPE_PRODUCT_HORIZONTAL -> {
                    binding as ItemProductHorizontalBinding

                    if (discount > 0) {
                        binding.tvDiscountPrice.text = price.toString().toMoneyFormat()
                        binding.tvOriginalPrice.text = origin.toString().toMoneyFormat()
                        binding.tvOriginalPrice.paintFlags =
                            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        binding.tvDiscountPrice.text = price.toString().toMoneyFormat()
                        binding.tvOriginalPrice.visibility = View.GONE
                    }

                    binding.tvProductName.text = product.name

                    Glide.with(binding.root.context).load(product.image?.firstOrNull()).centerCrop()
                        .error(
                            R.drawable.ic_avatar
                        ).into(binding.ivProductImage)

                    binding.btnFavorite.setImageResource(
                        if (product.isFavorite == true) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                    )
                }

                -1 -> {
                    binding as ItemEmptyBinding
                }

                else -> {
                    binding as ItemProductVerticalBinding

                    if (discount > 0) {
                        binding.tvDiscountPrice.text = price.toString().toMoneyFormat()
                        binding.tvOriginalPrice.text = origin.toString().toMoneyFormat()
                        binding.tvOriginalPrice.paintFlags =
                            binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        binding.tvDiscountPrice.text = price.toString().toMoneyFormat()
                        binding.tvOriginalPrice.visibility = View.GONE
                    }

                    binding.tvProductName.text = product.name
                    Glide.with(binding.root.context).load(product.image?.firstOrNull()).centerCrop()
                        .error(
                            R.drawable.ic_avatar
                        ).into(binding.ivProductImage)

                    binding.btnFavorite.setImageResource(
                        if (product.isFavorite == true) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
                    )

                    val rating = product.reviewValue ?: 0.0
                    val reviewCount = product.reviewCount ?: 0
                    if (reviewCount > 0) {
                        binding.tvRating.text = "$rating ($reviewCount)"
                    } else {
                        binding.tvRating.visibility = View.GONE
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when (viewType) {
            ITEM_TYPE_PRODUCT_HORIZONTAL -> ViewHolder(
                ItemProductHorizontalBinding.inflate(
                    inflater, parent, false
                )
            )

            ITEM_TYPE_PRODUCT_VERTICAL -> ViewHolder(
                ItemProductVerticalBinding.inflate(
                    inflater, parent, false
                )
            )

            -1 -> ViewHolder(ItemEmptyBinding.inflate(inflater, parent, false))

            else -> ViewHolder(ItemProductVerticalBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        if (items.isEmpty()) {
            return 1
        }

        return if (maxItem == -1) {
            items.size
        } else {
            if (items.size > maxItem) maxItem else items.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isEmpty()) {
            holder.bind(null, -1)
            return
        }

        val product = items[position]
        holder.bind(product, viewType)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(product)
        }

        when (viewType) {
            ITEM_TYPE_PRODUCT_HORIZONTAL -> {
                val binding = holder.binding as ItemProductHorizontalBinding
                binding.btnFavorite.setOnClickListener {
                    val isFavorite = product.isFavorite == true
                    onFavoriteClick?.invoke(product, !isFavorite)
                }
            }

            ITEM_TYPE_PRODUCT_VERTICAL -> {
                val binding = holder.binding as ItemProductVerticalBinding
                binding.btnFavorite.setOnClickListener {
                    val isFavorite = product.isFavorite == true
                    onFavoriteClick?.invoke(product, !isFavorite)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) -1 else viewType
    }

    companion object {
        const val ITEM_TYPE_PRODUCT_HORIZONTAL = 0
        const val ITEM_TYPE_PRODUCT_VERTICAL = 1
    }
}

