@file:OptIn(DelicateCoroutinesApi::class)

package com.example.leagueoftbcoupon.fragment

import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.activity.TicketActivity
import com.example.leagueoftbcoupon.adapter.CurrencyDataAdapter
import com.example.leagueoftbcoupon.base.BaseViewModelFragment
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.databinding.FragmentSearchBinding
import com.example.leagueoftbcoupon.databinding.FragmentSearchSuccessBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.utils.KeypadUtil
import com.example.leagueoftbcoupon.view.SearchFlowLayout
import com.example.leagueoftbcoupon.view.ShopCarDialog
import com.example.leagueoftbcoupon.viewModel.SearchViewModel
import com.example.leagueoftbcoupon.viewModel.ShopCarViewModel
import com.example.leagueoftbcoupon.viewModel.TicketViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_search_success.view.*
import kotlinx.coroutines.DelicateCoroutinesApi

class SearchFragment : BaseViewModelFragment<SearchViewModel>(),
    SearchFlowLayout.OnFlowItemClickListener {
    private lateinit var successBinding: FragmentSearchSuccessBinding
    private lateinit var rootBinding: FragmentSearchBinding
    private val searchAdapter by lazy {
        CurrencyDataAdapter()
    }
    private val ticketViewModel by lazy {
        ViewModelProvider(this)[TicketViewModel::class.java]
    }

    private val shopCarViewModel by lazy {
        ViewModelProvider(this)[ShopCarViewModel::class.java]
    }

    private lateinit var shopCarDialog: ShopCarDialog
    override fun setSuccessView(contentContainer: FrameLayout): View {
        successBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_search_success,
            contentContainer,
            false
        )
        return successBinding.root
    }

    override fun setRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        rootBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_search,
            null,
            false
        )
        return rootBinding.root
    }

    override fun reLoadData() {
        currentViewModel.getRecommendKeyWord()
    }

    override fun setViewModelClass() = SearchViewModel::class.java

    override fun initView() {
        super.initView()
        switchDisplayView(LoadViewStatus.SUCCESS)
        currentViewModel.getRecommendKeyWord()

        successBinding.apply {
            searchContentRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchAdapter
            }

            searchSmart.apply {
                setEnableRefresh(false)
                setRefreshFooter(ClassicsFooter(requireContext()))

            }
        }

        currentViewModel.getSearchHistory()

        shopCarDialog = ShopCarDialog(requireContext())
    }

    private val recommendList = mutableListOf<String>()

    private val shopCarTitleList = mutableListOf<String>()
    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {
            recommendDataList.observe(this@SearchFragment) {
                recommendList.clear()
                it.forEach {
                    recommendList.add(it.keyword)
                }
                successBinding.searchRecommendFlow.setData(recommendList)
            }

            searchDataStatusList.observe(this@SearchFragment) {

                when (it) {
                    SearchViewModel.SearchDataStatus.LOADING -> {
                        switchDisplayView(LoadViewStatus.LOADING)
                    }

                    SearchViewModel.SearchDataStatus.SUCCESS -> {
                        switchDisplayView(LoadViewStatus.SUCCESS)
                        KeypadUtil.hideKeypad(rootBinding.root)
                    }

                    SearchViewModel.SearchDataStatus.EMPTY -> {
                        switchDisplayView(LoadViewStatus.EMPTY)
                    }

                    SearchViewModel.SearchDataStatus.ERROR -> {
                        switchDisplayView(LoadViewStatus.ERROR)
                    }

                    SearchViewModel.SearchDataStatus.LOADED_MORE_SUCCESS -> {
                        Toast.makeText(requireContext(), "加载成功", Toast.LENGTH_SHORT).show()
                    }

                    SearchViewModel.SearchDataStatus.LOADED_MORE_ERROR -> {
                        Toast.makeText(requireContext(), "加载错误", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            }

            searchDataList.observe(this@SearchFragment) {
                if (it.isNotEmpty()) {
                    searchAdapter.setData(it)
                }
            }

            searchLoadMoreDataList.observe(this@SearchFragment) {
                searchAdapter.setLoadMoreData(it)
            }

            historyList.observe(this@SearchFragment) {

                if (it != null && it.isNotEmpty()) {
                    successBinding.searchHistoryFlow.setData(it)
                    successBinding.searchHistoryContainer.visibility = View.VISIBLE
                    successBinding.searchRecommendContainer.visibility = View.GONE
                } else {
                    successBinding.searchHistoryContainer.visibility = View.GONE
                    successBinding.searchRecommendContainer.visibility = View.VISIBLE
                }

            }
        }

        shopCarViewModel.queryAllShopCarData().observe(this) { it ->
            shopCarTitleList.clear()
            it.forEach {
                shopCarTitleList.add(it.title)
            }
        }
    }

    override fun initListener() {
        super.initListener()
        rootBinding.apply {
            searchInputKeyEd.apply {
                setOnEditorActionListener(object : TextView.OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if (v != null) {
                            if (v.text.isNotEmpty()) {
                                KeypadUtil.hideKeypad(searchInputKeyEd)
                                currentViewModel.getSearchDataList(v.text.toString())
                                successBinding.searchContentRv.scrollToPosition(0)
                                currentViewModel.getSearchHistory()
                                successBinding.searchFlowContainer.visibility = View.GONE
                                successBinding.searchSmart.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(requireContext(), "请输入内容后再搜索", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        return true
                    }

                })

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }


                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s != null) {
                            if (s.isNotEmpty()) {
                                searchClearEditIv.visibility = View.VISIBLE
                                KeypadUtil.showKeypad(searchInputKeyEd)
                                setSelection(s.toString().length)
                            } else {
                                searchClearEditIv.visibility = View.GONE


                            }
                        }

                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }
            successBinding.apply {
                searchClearEditIv.setOnClickListener {
                    searchInputKeyEd.setText("")
                    searchClearEditIv.visibility = View.GONE
                    searchFlowContainer.visibility = View.VISIBLE
                    searchSmart.visibility = View.GONE
                    KeypadUtil.showKeypad(searchInputKeyEd)
                }

                searchCancelTv.setOnClickListener {
                    KeypadUtil.hideKeypad(rootBinding.root)

                }

                searchRecommendFlow.setOnFlowItemClickListener(this@SearchFragment)

                searchHistoryFlow.setOnFlowItemClickListener(this@SearchFragment)

                searchClearHistory.setOnClickListener {
                    shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.CLEAR_HISTORY)
                    shopCarDialog.setOnClearHistoryClickListener(object :
                        ShopCarDialog.OnClearHistoryClickListener {
                        override fun onClearHistoryClick() {
                            currentViewModel.clearHistory()
                            currentViewModel.getSearchHistory()
                        }

                    })
                    shopCarDialog.show()
                }

                searchSmart.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                    override fun onRefresh(refreshLayout: RefreshLayout) {

                    }

                    override fun onLoadMore(refreshLayout: RefreshLayout) {
                        currentViewModel.getSearchLoadMoreDataList()
                        refreshLayout.finishLoadMore()
                    }

                })
            }
        }
        searchAdapter.setCurrencyItemClickListener(object :
            CurrencyDataAdapter.OnCurrencyItemClickListener {
            override fun onItemClickListener(baseData: BaseData) {
                val handleTickNeedData = HandlerTicketData.handleTickNeedData(baseData)
                ticketViewModel.getTicketData(handleTickNeedData)
                val intent = Intent(requireActivity(), TicketActivity::class.java)
                intent.putExtra(Constant.ITEM_CLICK_DATA, handleTickNeedData)
                startActivity(intent)
            }

            override fun onItemLongClickListener(baseData: BaseData) {
                shopCarDialog.apply {


                    if (shopCarTitleList.size == 0) {
                        updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                        setOnAddShopCarClickListener(object :
                            ShopCarDialog.OnAddShopCarClickListener {
                            override fun onAddShopCarClick() {
                                shopCarViewModel.insertShopCarData(baseData)
                            }

                        })
                    } else {
                        if (!shopCarTitleList.contains(baseData.title)) {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                            setOnAddShopCarClickListener(object :
                                ShopCarDialog.OnAddShopCarClickListener {
                                override fun onAddShopCarClick() {
                                    shopCarViewModel.insertShopCarData(baseData)
                                }

                            })
                        } else {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.IS_ADD)
                        }
                    }
                    show()
                }
            }
        })

    }

    override fun flowItemClick(keyword: String) {
        rootBinding.apply {
            successBinding.apply {
                searchInputKeyEd.setText(keyword)
                searchInputKeyEd.isFocusable = true
                searchInputKeyEd.isFocusableInTouchMode = true
                searchInputKeyEd.requestFocus()
                KeypadUtil.showKeypad(searchInputKeyEd)
                searchFlowContainer.visibility = View.GONE
                searchSmart.visibility = View.VISIBLE
                currentViewModel.getSearchDataList(keyword)
                currentViewModel.getSearchHistory()
                searchContentRv.scrollToPosition(0)
            }

        }

    }

}