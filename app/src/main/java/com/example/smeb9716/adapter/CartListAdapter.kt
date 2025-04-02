package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemCartBinding
import com.example.smeb9716.databinding.ItemEmptyBinding
import com.example.smeb9716.model.Cart

class CartListAdapter : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    private val cartList = mutableListOf<Cart>()
    private var onSelectListener: (Cart, Boolean) -> Unit = { cart, selected -> }
    private var onIncreaseListener: (Cart) -> Unit = { cart -> }
    private var onDecreaseListener: (Cart) -> Unit = { cart -> }
    private var onDeleteListener: (Cart) -> Unit = { cart -> }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Cart>) {
        cartList.clear()
        cartList.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnSelectListener(listener: (Cart, Boolean) -> Unit) {
        onSelectListener = listener
    }

    fun setOnIncreaseListener(listener: (Cart) -> Unit) {
        onIncreaseListener = listener
    }

    fun setOnDecreaseListener(listener: (Cart) -> Unit) {
        onDecreaseListener = listener
    }

    fun setOnDeleteListener(listener: (Cart) -> Unit) {
        onDeleteListener = listener
    }

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            cart: Cart? = null,
            onSelectListener: (Cart, Boolean) -> Unit,
            onIncreaseListener: (Cart) -> Unit,
            onDecreaseListener: (Cart) -> Unit,
            onDeleteListener: (Cart) -> Unit,
        ) {
            if (cart == null) {
                binding as ItemEmptyBinding
            } else {
                binding as ItemCartBinding
                binding.tvName.text = cart.productId.name ?: ""
                binding.tvPrice.text = cart.productId.getDiscountedPrice().toString()
                Glide.with(binding.root.context).load(cart.productId.image?.first()).centerCrop()
                    .into(binding.imvThumbnail)

                binding.tvQuantity.text = cart.quantity.toString()
                binding.cbSelect.isChecked = cart.selected ?: false

                binding.cbSelect.setOnClickListener {
                    onSelectListener(cart, binding.cbSelect.isChecked)
                }

                binding.imvIncrease.setOnClickListener {
                    onIncreaseListener(cart)
                }

                binding.imvDecrease.setOnClickListener {
                    onDecreaseListener(cart)
                }

                binding.imvDelete.setOnClickListener {
                    onDeleteListener(cart)
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
        val inflater = parent.context.getSystemService(LayoutInflater::class.java)
        val binding = ItemCartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (cartList.isEmpty()) 1 else cartList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (cartList.isEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (cartList.isEmpty()) {
            holder.bind(
                null,
                onSelectListener,
                onIncreaseListener,
                onDecreaseListener,
                onDeleteListener
            )
        } else {
            holder.bind(
                cartList[position],
                onSelectListener,
                onIncreaseListener,
                onDecreaseListener,
                onDeleteListener
            )
        }
    }
}
