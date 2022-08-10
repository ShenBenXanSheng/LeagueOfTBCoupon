package com.example.leagueoftbcoupon.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.key
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.adapter.CurrencyDataAdapter
import com.example.leagueoftbcoupon.base.BaseViewModelActivity
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.databinding.ActivityShopCarBinding
import com.example.leagueoftbcoupon.databinding.ActivityShopCarSuccessBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.room.ShopCarData
import com.example.leagueoftbcoupon.utils.KeypadUtil
import com.example.leagueoftbcoupon.view.ShopCarDialog
import com.example.leagueoftbcoupon.viewModel.ShopCarViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

class ShopCarActivity : BaseViewModelActivity<ShopCarViewModel>() {
    companion object {
        const val TAG = "ShopCarActivity"
    }

    private val shopCarAdapter by lazy {
        CurrencyDataAdapter()
    }
    private lateinit var dataBinding: ActivityShopCarBinding
    private lateinit var successBinding: ActivityShopCarSuccessBinding
    private lateinit var shopCarDialog: ShopCarDialog
    override fun setRootView(): View {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_shop_car,
            null,
            false
        )
        return dataBinding.root
    }

    override fun setSuccessView(container: FrameLayout): View {
        successBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_shop_car_success,
            null,
            false
        )
        return successBinding.root
    }

    override fun reloadData() {
        //数据来自本地数据库，不存在网路问题
    }

    override fun setViewModelClass() = ShopCarViewModel::class.java

    @OptIn(DelicateCoroutinesApi::class)
    override fun initView() {
        super.initView()
        updateState(ActivityLoadState.SUCCESS)

        dataBinding.shopCarToolBar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.shop_delete -> {
                        shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.CLEAR)
                        shopCarDialog.setOnClearShopCarClickListener(object :
                            ShopCarDialog.OnClearShopCarClickListener {
                            override fun onClearShopCarClick() {
                                Toast.makeText(this@ShopCarActivity,"删除成功",Toast.LENGTH_SHORT).show()
                                currentViewModel.clearShopCarData()
                            }
                        })
                        shopCarDialog.show()
                    }
                }
                true
            }
            setNavigationOnClickListener {
                finish()
            }
            val findItem = menu.findItem(R.id.shop_Search)
            val searchView = findItem.actionView as SearchView
            searchView.queryHint = "输入商品"

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    val keyword = query.toString().trim()

                    if (!TextUtils.isEmpty(keyword)) {
                        KeypadUtil.hideKeypad(searchView)
                        currentViewModel.queryShopCarDataByKeyword(keyword)
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText == ""){
                        currentViewModel.queryAllShopCarData().observe(this@ShopCarActivity) {
                            shopCarAdapter.setData(it.reversed())
                        }

                    }
                    return true
                }

            })
        }

        successBinding.shopCarRV.apply {
            layoutManager = LinearLayoutManager(this@ShopCarActivity)
            adapter = shopCarAdapter
        }

        shopCarDialog = ShopCarDialog(this)
    }

    //  private val contentList = mutableListOf<BaseData>()
    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.queryAllShopCarData().observe(this) {
            shopCarAdapter.setData(it.reversed())
        }

        currentViewModel.queryShopList.observe(this) {
            shopCarAdapter.setSearchData(it)

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun initListener() {
        super.initListener()
        shopCarAdapter.setCurrencyItemClickListener(object :
            CurrencyDataAdapter.OnCurrencyItemClickListener {
            override fun onItemClickListener(baseData: BaseData) {
                val handleTickNeedData = HandlerTicketData.handleTickNeedData(baseData)
                val intent = Intent(this@ShopCarActivity, TicketActivity::class.java)
                intent.putExtra(Constant.ITEM_CLICK_DATA, handleTickNeedData)
                startActivity(intent)
            }

            override fun onItemLongClickListener(baseData: BaseData) {
                shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.DELETE)
                shopCarDialog.setOnDeleteShopCarClickListener(object :
                    ShopCarDialog.OnDeleteShopCarClickListener {
                    override fun onDeleteShopCarClick() {

                        currentViewModel.deleteShopCarData(baseData)
                        Toast.makeText(this@ShopCarActivity,"删除成功",Toast.LENGTH_SHORT).show()
                    }

                })
                shopCarDialog.show()
            }

        })
    }


}