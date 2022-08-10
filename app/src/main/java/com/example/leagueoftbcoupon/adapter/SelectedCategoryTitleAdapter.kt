package com.example.leagueoftbcoupon.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.databinding.ItemSelectedCategoryBinding
import com.example.leagueoftbcoupon.domain.SelectedCategories

class SelectedCategoryTitleAdapter :
    RecyclerView.Adapter<SelectedCategoryTitleAdapter.InnerHolder>() {
    private lateinit var onItemClickCategoryIdListener: OnItemClickCategoryIdListener
    private val selectedCategoriesList = mutableListOf<SelectedCategories.Data?>()

    class InnerHolder(itemView: View, val dataBinding: ItemSelectedCategoryBinding) :
        RecyclerView.ViewHolder(itemView) {

    }

    companion object {
        const val TAG = "SelectedCategoryAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val dataBinding = DataBindingUtil.inflate<ItemSelectedCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_selected_category,
            null,
            false
        )
        return InnerHolder(dataBinding.root, dataBinding)
    }

    var currentPosition = 0
    private var isFirst = true

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val data = selectedCategoriesList[position]


        holder.dataBinding.apply {
            itemSelectedCategoryTitle.text = data?.favoritesTitle
            if (currentPosition == position) {
                if (isFirst) {
                    onItemClickCategoryIdListener.onItemClick(data)
                }
                itemSelectedCategoryTitle.isSelected = true
                itemSelectedCategoryTitle.isEnabled = false
            } else {
                itemSelectedCategoryTitle.isSelected = false
                itemSelectedCategoryTitle.isEnabled = true
            }

            itemSelectedCategoryLayout.setOnClickListener {
                if (currentPosition != position) {
                    isFirst = false
                    currentPosition = position
                    onItemClickCategoryIdListener.onItemClick(data)
                    notifyDataSetChanged()
                }
            }
        }
    }


    override fun getItemCount() = selectedCategoriesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<SelectedCategories.Data>?) {
        selectedCategoriesList.clear()
        if (it != null) {
            selectedCategoriesList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun setOnItemClickCategoryIdListener(onItemClickCategoryIdListener: OnItemClickCategoryIdListener) {
        this.onItemClickCategoryIdListener = onItemClickCategoryIdListener
    }

    interface OnItemClickCategoryIdListener {
        fun onItemClick(data: SelectedCategories.Data?)
    }
}