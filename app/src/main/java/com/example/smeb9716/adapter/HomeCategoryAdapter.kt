package com.example.smeb9716.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smeb9716.databinding.ItemCategoryHomeBinding
import com.example.smeb9716.model.Category
import java.util.Locale

class HomeCategoryAdapter : RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>() {
    private val items = mutableListOf<Category>()
    private var onCategoryClick: ((Category) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Category>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnCategoryClick(onCategoryClick: ((Category) -> Unit)) {
        this.onCategoryClick = onCategoryClick
    }

    class ViewHolder(val binding: ItemCategoryHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            Glide.with(binding.root.context).load(category.image).into(binding.ivCategory)
            binding.tvCategory.text = category.name?.capitalizeWords()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(ItemCategoryHomeBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return if (items.size <= 10) items.size else 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = items[position]
        holder.bind(category)
        holder.itemView.setOnClickListener {
            onCategoryClick?.invoke(category)
        }
    }

}
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }
