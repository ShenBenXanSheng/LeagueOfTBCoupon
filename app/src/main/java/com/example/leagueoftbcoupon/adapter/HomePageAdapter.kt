package com.example.leagueoftbcoupon.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.leagueoftbcoupon.domain.Data
import com.example.leagueoftbcoupon.fragment.HomeContentFragment

class HomePageAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private val fragmentMap = mutableMapOf<Int, Fragment>()
    private val dataList = mutableListOf<Data>()
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        val data = dataList[position]
        var fragment = fragmentMap[position]
        if (fragment != null) {
            return fragment
        } else {
            fragment = HomeContentFragment(data)
            fragmentMap[position] = fragment
        }

        return fragment
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<Data>?) {
        dataList.clear()
        if (it != null) {
            dataList.addAll(it)
        }
        notifyDataSetChanged()
    }

}