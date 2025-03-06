package com.example.smeb9716.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemHomeVoucherBinding
import com.example.smeb9716.model.Voucher

class HomeVoucherAdapter : RecyclerView.Adapter<HomeVoucherAdapter.ViewHolder>() {

    private var onVoucherClick: ((Voucher) -> Unit)? = null
    private val data = mutableListOf<Voucher>()

    fun setData(vouchers: List<Voucher>) {
        data.clear()
        data.addAll(vouchers)
        notifyDataSetChanged()
    }

    fun setOnVoucherClick(onVoucherClick: ((Voucher) -> Unit)) {
        this.onVoucherClick = onVoucherClick
    }

    class ViewHolder(val binding: ItemHomeVoucherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(voucher: Voucher) {
            // Bind data to view
            binding.tvVoucherName.text = voucher.description?.capitalizeWords()
            Glide.with(binding.root.context).load(voucher.image).into(binding.ivVoucherImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomeVoucherBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = data[position]
        holder.bind(voucher)
        holder.itemView.setOnClickListener {
            onVoucherClick?.invoke(voucher)
        }
    }
}