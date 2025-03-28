package com.example.smeb9716.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smeb9716.databinding.ItemProductReviewBinding
import com.example.smeb9716.model.response.ProductReview

class ProductReviewAdapter : RecyclerView.Adapter<ProductReviewAdapter.ViewHolder>() {
    private val productReviews = mutableListOf<ProductReview>()

    fun setData(productReviews: List<ProductReview>) {
        this.productReviews.clear()
        this.productReviews.addAll(productReviews)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemProductReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productReview: ProductReview) {
            binding.ratingBar.rating = productReview.rating?.toFloat() ?: 0f
            binding.tvReviewComment.text = productReview.reviewText ?: ""
            binding.tvUserName.text = productReview.userId?.name ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = parent.context.getSystemService(LayoutInflater::class.java)
        return ViewHolder(ItemProductReviewBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int {
        return productReviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productReviews[position])
    }


}
