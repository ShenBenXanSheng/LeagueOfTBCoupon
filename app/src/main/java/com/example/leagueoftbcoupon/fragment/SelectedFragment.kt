package com.example.leagueoftbcoupon.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.adapter.SelectedCategoryTitleAdapter
import com.example.leagueoftbcoupon.adapter.SelectedDataAdapter
import com.example.leagueoftbcoupon.base.BaseViewModelFragment
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.databinding.FragmentSelectedBinding
import com.example.leagueoftbcoupon.databinding.FragmentSelectedSuccessBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.SelectedCategories
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.view.ShopCarDialog
import com.example.leagueoftbcoupon.viewModel.SelectedViewModel
import com.example.leagueoftbcoupon.viewModel.ShopCarViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * 什本先生
 * 2022年5月27日
 * 精选界面
 *
 * 接口崩溃，该页面已经被废弃
 */
class SelectedFragment : BaseViewModelFragment<SelectedViewModel>() {
    companion object {
        const val TAG = "SelectedFragment"
    }

    private lateinit var successDataBinding: FragmentSelectedSuccessBinding
    private lateinit var dataBinding: FragmentSelectedBinding
    private lateinit var selectedContentStatusFm: FrameLayout
    private lateinit var loadingView: View
    private lateinit var successView: View
    private lateinit var errorView: View
    private lateinit var emptyView: View
    private lateinit var selectedContentRv: RecyclerView
    private lateinit var shopCarDialog: ShopCarDialog
    private val shopTitleList = mutableListOf<String>()

    private val leftAdapter by lazy {
        SelectedCategoryTitleAdapter()
    }
    private val rightAdapter by lazy {
        SelectedDataAdapter()
    }

    private val shopCarViewModel by lazy {
        ShopCarViewModel(requireActivity().application)
    }

    override fun setRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_selected,
            container,
            false
        )
        return dataBinding.root
    }

    override fun setSuccessView(contentContainer: FrameLayout): View {
        successDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_selected_success,
            contentContainer,
            false
        )
        return successDataBinding.root
    }


    override fun reLoadData() {
        currentViewModel.getSelectedCategory()
    }

    override fun setViewModelClass() = SelectedViewModel::class.java

    override fun initView() {
        super.initView()
        currentViewModel.getSelectedCategory()
        shopCarDialog = ShopCarDialog(requireContext())
        successDataBinding.apply {
            selectedContentStatusFm = successDataBinding.selectedStatusFm
            selectedCategoriesRv.apply {
                layoutManager = object : LinearLayoutManager(requireContext()) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                adapter = leftAdapter

            }
        }

        loadAllViewToContentFm()
        hideAllStateView()

        selectedContentRv = successView.findViewById(R.id.selectedSuccessRv)

        selectedContentRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rightAdapter
        }
    }


    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.repositoryIdList.observe(this) {
            leftAdapter.setData(it)
        }
        currentViewModel.selectedStatus.observe(this) {
            hideAllStateView()
            when (it) {
                SelectedViewModel.SelectedDataStatus.LOADING -> {
                    switchDisplayView(LoadViewStatus.LOADING)
                }

                SelectedViewModel.SelectedDataStatus.SUCCESS -> {
                    switchDisplayView(LoadViewStatus.SUCCESS)
                }

                SelectedViewModel.SelectedDataStatus.EMPTY -> {
                    switchDisplayView(LoadViewStatus.EMPTY)
                }

                SelectedViewModel.SelectedDataStatus.ERROR -> {
                    switchDisplayView(LoadViewStatus.ERROR)
                }

                SelectedViewModel.SelectedDataStatus.CONTENT_LOADING -> {
                    loadingView.visibility = View.VISIBLE
                }

                SelectedViewModel.SelectedDataStatus.CONTENT_SUCCESS -> {
                    successView.visibility = View.VISIBLE
                }

                SelectedViewModel.SelectedDataStatus.CONTENT_EMPTY -> {
                    emptyView.visibility = View.VISIBLE
                }

                SelectedViewModel.SelectedDataStatus.CONTENT_ERROR -> {
                    errorView.visibility = View.VISIBLE
                }
            }
        }

        currentViewModel.selectedDataList.observe(this) {
            rightAdapter.setData(it)
        }

        shopCarViewModel.queryAllShopCarData().observe(this) {
            shopTitleList.clear()
            it.forEach {
                shopTitleList.add(it.title)
            }
        }
    }
    private var categoryID = 0
    @OptIn(DelicateCoroutinesApi::class)
    override fun initListener() {
        super.initListener()
        leftAdapter.setOnItemClickCategoryIdListener(object :
            SelectedCategoryTitleAdapter.OnItemClickCategoryIdListener {
            override fun onItemClick(data: SelectedCategories.Data?) {
                if (data != null) {
                    currentViewModel.getSelectedContentByCategoryId(data.favoritesId)
                    categoryID = data.favoritesId
                }
                selectedContentRv.scrollToPosition(0)
            }
        })

        rightAdapter.setOnItemClickListener(object : SelectedDataAdapter.OnItemClickListener {
            override fun onItemClickListener(data: BaseData) {
             //   val handleTickNeedData = HandlerTicketData.handleTickNeedData(data)
             //   val bundle = Bundle()
             //   bundle.putParcelable(Constant.ITEM_CLICK_DATA, handleTickNeedData)
             //   findNavController().navigate(R.id.selected_to_ticket, bundle)
            }

            override fun onLongItemClickListener(data: BaseData, selectedShopBt: Button) {
                shopCarDialog.apply {
                    if (selectedShopBt.visibility == View.GONE) {
                        updateAddShopCarState(ShopCarDialog.AddShopCarState.DOT_HAVE_COUPON)
                    } else {
                        if (shopTitleList.size == 0) {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                            setOnAddShopCarClickListener(object :
                                ShopCarDialog.OnAddShopCarClickListener {
                                override fun onAddShopCarClick() {
                                    shopCarViewModel.insertShopCarData(data)
                                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }

                        if (shopTitleList.contains(data.title)) {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.IS_ADD)
                        } else {
                            updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                            setOnAddShopCarClickListener(object :
                                ShopCarDialog.OnAddShopCarClickListener {
                                override fun onAddShopCarClick() {
                                    shopCarViewModel.insertShopCarData(data)
                                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }

                    }
                    show()
                }
            }
        })
    }


    private fun hideAllStateView() {
        loadingView.visibility = View.GONE
        successView.visibility = View.GONE
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE

    }

    private fun loadAllViewToContentFm() {
        loadingView = initAllStateView(R.layout.state_loading)
        successView = initAllStateView(R.layout.fragment_selected_content_success)
        emptyView = initAllStateView(R.layout.state_empty)
        errorView = initAllStateView(R.layout.state_error)

        errorView.setOnClickListener {
            currentViewModel.getSelectedContentByCategoryId(categoryID)
        }

        selectedContentStatusFm.addView(loadingView)
        selectedContentStatusFm.addView(successView)
        selectedContentStatusFm.addView(emptyView)
        selectedContentStatusFm.addView(errorView)
    }

    private fun initAllStateView(layoutId: Int): View {
        return LayoutInflater.from(requireContext())
            .inflate(layoutId, selectedContentStatusFm, false)
    }
}