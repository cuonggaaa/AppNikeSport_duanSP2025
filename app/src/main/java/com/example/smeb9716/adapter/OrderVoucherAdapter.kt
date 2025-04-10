package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.smeb9716.R
import com.example.smeb9716.databinding.ItemVoucherOrderBinding
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.utils.ext.toDMYFormat

class OrderVoucherAdapter : RecyclerView.Adapter<OrderVoucherAdapter.ViewHolder>() {
    private val vouchers = mutableListOf<Voucher>()
    private var onVoucherSelectedListener: ((Voucher, Boolean) -> Unit)? = { voucher, selected -> }

    @SuppressLint("NotifyDataSetChanged")
    fun setVouchers(vouchers: List<Voucher>) {
        this.vouchers.clear()
        this.vouchers.addAll(vouchers)
        notifyDataSetChanged()
    }

    fun setOnVoucherSelectedListener(listener: (Voucher, Boolean) -> Unit) {
        onVoucherSelectedListener = listener
    }

    class ViewHolder(val binding: ItemVoucherOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            voucher: Voucher, onVoucherSelectedListener: ((Voucher, Boolean) -> Unit)? = null
        ) {
            binding.tvVoucherName.text = voucher.description ?: ""
            binding.tvVoucherEndDate.text = (voucher.endDate ?: "").toDMYFormat()
            binding.cbSelect.setOnCheckedChangeListener { _, isChecked ->
                voucher.selected = isChecked
            }
            if (voucher.selected == true) {
                binding.root.background = AppCompatResources.getDrawable(
                    binding.root.context, R.drawable.bg_selected_voucher
                )
            } else {
                binding.root.background = AppCompatResources.getDrawable(
                    binding.root.context, R.drawable.bg_unselected_voucher
                )
            }

            binding.cbSelect.isChecked = voucher.selected ?: false

            binding.root.isClickable = true

            binding.root.setOnClickListener {
                onVoucherSelectedListener?.invoke(voucher, !(voucher.selected ?: false))
            }

            binding.cbSelect.setOnCheckedChangeListener { _, isChecked ->
                onVoucherSelectedListener?.invoke(voucher, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemVoucherOrderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vouchers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(vouchers[position], onVoucherSelectedListener)
    }
}