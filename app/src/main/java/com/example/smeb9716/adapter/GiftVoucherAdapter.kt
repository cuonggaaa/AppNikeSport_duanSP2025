package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemVoucherListBinding
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.utils.ext.toDMYFormat

class GiftVoucherAdapter(val onVoucherClick: (Voucher) -> Unit) : RecyclerView.Adapter<GiftVoucherAdapter.ViewHolder>() {
    private val data = mutableListOf<Voucher>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Voucher>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemVoucherListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Voucher) {
            binding.title.text = (item.description ?: "").capitalizeWords()
            Glide.with(binding.root.context).load(item.image).centerCrop().into(binding.image)

            binding.discountValue.text = "Giảm ngay ${item.discountValue.toString()} VND"
            binding.endDate.text = item.endDate?.toDMYFormat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVoucherListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onVoucherClick(item)
        }
    }
}
