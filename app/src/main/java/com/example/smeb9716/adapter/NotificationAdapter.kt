package com.example.smeb9716.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemNotificationBinding
import com.example.smeb9716.model.Notification

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var onNotificationClick: ((Notification) -> Unit)? = null
    private val data = mutableListOf<Notification>()

    fun setData(notifications: List<Notification>) {
        data.clear()
        data.addAll(notifications)
        notifyDataSetChanged()
    }

    fun setOnNotificationClick(onNotificationClick: ((Notification) -> Unit)) {
        this.onNotificationClick = onNotificationClick
    }

    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            // Bind data to view
            binding.tvTitleNotification.text = notification.content
            binding.tvStatusNotification.text = if (notification.status == 2) "Đã đọc" else "Chưa đọc"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotificationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = data[position]
        holder.bind(notification)
        holder.itemView.setOnClickListener {
            onNotificationClick?.invoke(notification)
        }
    }
}