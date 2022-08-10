@file:OptIn(DelicateCoroutinesApi::class)

package com.example.leagueoftbcoupon.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.activity.TicketActivity
import com.example.leagueoftbcoupon.adapter.OnSellAdapter
import com.example.leagueoftbcoupon.base.BaseViewModelFragment
import com.example.leagueoftbcoupon.constant.Constant.ITEM_CLICK_DATA

import com.example.leagueoftbcoupon.databinding.ActivityShopCarSuccessBinding
import com.example.leagueoftbcoupon.databinding.FragmentOnSellBinding
import com.example.leagueoftbcoupon.databinding.FragmentOnSellSuccessBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.view.ShopCarDialog
import com.example.leagueoftbcoupon.viewModel.OnSellViewModel
import com.example.leagueoftbcoupon.viewModel.ShopCarViewModel
import com.example.leagueoftbcoupon.viewModel.TicketViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.DelicateCoroutinesApi

class OnSellFragment : BaseViewModelFragment<OnSellViewModel>() {
    private lateinit var successBinding: FragmentOnSellSuccessBinding
    private lateinit var rootBinding: FragmentOnSellBinding
    private val ticketViewModel by lazy {
        TicketViewModel()
    }
    private val shopCarViewModel by lazy {
        ShopCarViewModel(requireActivity().application)
    }

    private val shopTitleList = mutableListOf<String>()
    override fun setSuccessView(contentContainer: FrameLayout): View {
        successBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_on_sell_success,
            contentContainer,
            false
        )
        return successBinding.root
    }

    private val onSellAdapter by lazy {
        OnSellAdapter()
    }

    private lateinit var shopCarDialog: ShopCarDialog
    override fun setRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        rootBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_on_sell,
            null,
            false
        )
        return rootBinding.root
    }

    override fun reLoadData() {
        currentViewModel.getOnSellList()
    }

    override fun setViewModelClass() = OnSellViewModel::class.java

    override fun initView() {
        super.initView()
        switchDisplayView(LoadViewStatus.SUCCESS)
        currentViewModel.getOnSellList()
        successBinding.onSellRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = onSellAdapter
        }

        successBinding.onSellSmartRefresh.apply {
            setRefreshHeader(MaterialHeader(requireContext()))
            setRefreshFooter(ClassicsFooter(requireContext()))
            setEnableHeaderTranslationContent(true)
        }

        shopCarDialog = ShopCarDialog(requireContext())

    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            var refreshListSize = 0
            onSellDataList.observe(this@OnSellFragment) {
                refreshListSize = it.size
                onSellAdapter.setData(it)
            }
            onSellLoadMoreList.observe(this@OnSellFragment) {
                onSellAdapter.setLoadMoreData(it)
                refreshListSize = it.size
            }
            onSellLoadStateList.observe(this@OnSellFragment) {
                when (it) {
                    OnSellViewModel.OnSellDataLoadStatus.LOADING -> {
                        switchDisplayView(LoadViewStatus.LOADING)
                    }

                    OnSellViewModel.OnSellDataLoadStatus.SUCCESS -> {
                        switchDisplayView(LoadViewStatus.SUCCESS)
                    }
                    OnSellViewModel.OnSellDataLoadStatus.EMPTY -> {
                        switchDisplayView(LoadViewStatus.EMPTY)
                    }
                    OnSellViewModel.OnSellDataLoadStatus.ERROR -> {
                        switchDisplayView(LoadViewStatus.ERROR)
                    }
                    OnSellViewModel.OnSellDataLoadStatus.LOAD_MORE_SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "成功加载${refreshListSize}条数据",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    OnSellViewModel.OnSellDataLoadStatus.LOAD_MORE_ERROR -> {
                        Toast.makeText(requireContext(), "加载失败TnT", Toast.LENGTH_SHORT).show()
                    }
                    OnSellViewModel.OnSellDataLoadStatus.REFRESH_SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "成功刷新${refreshListSize}条数据",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    OnSellViewModel.OnSellDataLoadStatus.REFRESH_ERROR -> {
                        Toast.makeText(requireContext(), "刷新失败o—O", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }

            shopCarViewModel.queryAllShopCarData().observe(this@OnSellFragment) { it ->
                shopTitleList.clear()
                it.forEach {
                    shopTitleList.add(it.title)
                }

            }
        }
    }

    override fun initListener() {
        super.initListener()
        successBinding.onSellSmartRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                currentViewModel.getOnSellRefreshList()
                refreshLayout.finishRefresh()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                currentViewModel.getOnSellLoadMoreList()
                refreshLayout.finishLoadMore()
            }
        })

        onSellAdapter.setOnItemClickListener(object : OnSellAdapter.OnItemClickListener {
            override fun onItemClick(data: BaseData) {
                val handleTickNeedData = HandlerTicketData.handleTickNeedData(data)
                ticketViewModel.getTicketData(handleTickNeedData)
                val intent = Intent(requireActivity(), TicketActivity::class.java)
                intent.putExtra(ITEM_CLICK_DATA, handleTickNeedData)
                startActivity(intent)
            }

            override fun onItemLongClick(data: BaseData) {
                shopCarDialog.apply {
                    if (shopTitleList.size == 0) {
                        updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                        setOnAddShopCarClickListener(object :
                            ShopCarDialog.OnAddShopCarClickListener {
                            override fun onAddShopCarClick() {
                                shopCarViewModel.insertShopCarData(data)
                                Toast.makeText(
                                    requireContext(),
                                    "添加成功",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } else {
                        if (shopTitleList.contains(data.title)) {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.IS_ADD)
                        } else {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                            setOnAddShopCarClickListener(object :
                                ShopCarDialog.OnAddShopCarClickListener {
                                override fun onAddShopCarClick() {
                                    shopCarViewModel.insertShopCarData(data)
                                    Toast.makeText(
                                        requireContext(),
                                     "添加成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                    }
                    show()
                }

            }

        })
    }
}