package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemProductOrderBinding
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.CartOrder
import com.example.smeb9716.utils.ext.toMoneyFormat

data class OrderProductAdapter(val carts: List<CartOrder>) :
    RecyclerView.Adapter<OrderProductAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemProductOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cart: CartOrder) {
            binding.tvName.text = cart.productId.name ?: ""
            binding.tvPrice.text = cart.productId.getDiscountedPrice().toString().toMoneyFormat()

            Glide.with(binding.root.context).load(cart.productId.image?.first()).centerCrop()
                .into(binding.imvThumbnail)

            binding.tvQuantity.text = "x${cart.quantity}"
            binding.tvSize.text = "Size: ${cart.size}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(ItemProductOrderBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carts[position])
    }
}
