package com.eungb.cleanarchapp.presentation.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eungb.cleanarchapp.databinding.ItemProductBinding
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import java.text.DecimalFormat

class ProductListAdapter:RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private var products = mutableListOf<ProductEntity>()

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product:ProductEntity) = with(binding) {
            tvProductName.text = product.productName
            tvProductPrice.text = DecimalFormat("#,##0").format(product.price.toLong())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        products[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAllProducts(list: List<ProductEntity>) {
        this.products = list.toMutableList()
        notifyDataSetChanged()
    }
}