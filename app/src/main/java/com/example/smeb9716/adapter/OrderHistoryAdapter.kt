package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.smeb9716.databinding.ItemEmptyBinding
import com.example.smeb9716.databinding.ItemOrderHistoryBinding
import com.example.smeb9716.model.Order
import com.example.smeb9716.utils.ext.gone
import com.example.smeb9716.utils.ext.toDMYFormat
import com.example.smeb9716.utils.ext.toMoneyFormat
import com.example.smeb9716.utils.ext.visible

class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    private val orders = mutableListOf<Order>()
    private var onReviewClick: (Order) -> Unit = {}
    private var onCancelOrder: (Order) -> Unit = {}

    @SuppressLint("NotifyDataSetChanged")
    fun setOrders(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        notifyDataSetChanged()
    }

    fun setOnReviewClick(onReviewClick: (Order) -> Unit) {
        this.onReviewClick = onReviewClick
    }

    fun setOnCancelOrder(onCancelOrder: (Order) -> Unit) {
        this.onCancelOrder = onCancelOrder
    }

    class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order? = null, onReviewClick: (Order) -> Unit = {}, onCancelOrder: (Order) -> Unit = {}) {
            if (order == null) {
                binding as ItemEmptyBinding
            } else {
                binding as ItemOrderHistoryBinding

                val idSize = order.id.length
                val subStringId = order.id.substring(idSize - 6, idSize)

                binding.tvOrderId.text = "#$subStringId"
                binding.tvOrderDate.text = (order.createdAt ?: "").toDMYFormat()
                val productAdapter = OrderProductAdapter(order.cartData ?: mutableListOf())

                binding.rvOrderDetail.isNestedScrollingEnabled = true
                binding.rvOrderDetail.setHasFixedSize(true)
                binding.rvOrderDetail.adapter = productAdapter

                binding.tvOrderTotal.text = "${order.totalAmount.toString().toMoneyFormat()} VND"
                binding.tvOrderStatus.text = order.status

                val address = if (order.address.isNullOrEmpty()) (order.userId?.address
                    ?: "") else order.address
                binding.tvOrderAddress.text = address

                if (order.status == "Delivered" || order.status == "Returned") {
                    binding.btnReview.visible()
                } else {
                    binding.btnReview.gone()
                }

                if (order.status == "Cancelled") {
                    binding.btnCancel.gone()
                }

                if (order.status == "Delivered") {
                    binding.tvOrderStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_green_dark))
                } else if (order.status == "Cancelled") {
                    binding.tvOrderStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_red_dark))
                } else if (order.status == "Returned") {
                    binding.tvOrderStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_orange_dark))
                } else if (order.status == "Pending") {
                    binding.tvOrderStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_blue_dark))
                } else if (order.status == "Shipping") {
                    binding.tvOrderStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_orange_dark))
                } else {
                    binding.tvOrderStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_blue_dark))
                }

                binding.btnReview.setOnClickListener {
                    onReviewClick(order)
                }

                binding.btnCancel.setOnClickListener {
                    onCancelOrder(order)
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

        val binding = ItemOrderHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (orders.isEmpty()) 1 else orders.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (orders.isEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(if (orders.isEmpty()) null else orders[position], onReviewClick, onCancelOrder)
    }
}