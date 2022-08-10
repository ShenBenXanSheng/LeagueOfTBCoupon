package com.example.leagueoftbcoupon.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.adapter.CurrencyDataAdapter
import com.example.leagueoftbcoupon.base.BaseViewModelFragment
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.databinding.FragmentHomeContentBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.Data
import com.example.leagueoftbcoupon.domain.HomeContentList
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.room.ShopCarData
import com.example.leagueoftbcoupon.utils.SizeUtil
import com.example.leagueoftbcoupon.view.ShopCarDialog
import com.example.leagueoftbcoupon.view.IndicationLayout
import com.example.leagueoftbcoupon.viewModel.HomeContentViewModel
import com.example.leagueoftbcoupon.viewModel.ShopCarViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_home_content.*
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * 什本先生
 * 2022年5月24日
 * 首页内容
 * @param categoryData 根据tabLayout,返回首页推荐id
 */
class HomeContentFragment(private val categoryData: Data?) :
    BaseViewModelFragment<HomeContentViewModel>() {
    constructor() : this(null)

    private lateinit var homeContentDataBinding: FragmentHomeContentBinding  //首页内容的dataBinding

    private lateinit var shopCarDialog: ShopCarDialog

    private val currencyDataAdapter by lazy { //首页内容的rv
        CurrencyDataAdapter()
    }

    private val shopCarViewModel by lazy {
        ShopCarViewModel(requireActivity().application)
    }

    //将整个布局作为"成功"返回给baseFragment
    override fun setSuccessView(contentContainer: FrameLayout): View {
        homeContentDataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.fragment_home_content,
                contentContainer,
                false
            )

        return homeContentDataBinding.root
    }

    //网络错误，重新加载数据
    override fun reLoadData() {
        if (categoryData != null) {
            currentViewModel.getHomeContentData(categoryData.id)
        }

    }

    //设置viewModel
    override fun setViewModelClass() = HomeContentViewModel::class.java

    override fun initView() {
        super.initView()
        if (categoryData != null) {
            //将id传给viewModel
            currentViewModel.getHomeViewPagerData(categoryData.id)
            homeContentDataBinding.apply {

                //修改标题
                homeContentTitleLayout.findViewById<TextView>(R.id.homeContentTitle).text =
                    categoryData.title

                //设置rv的属性
                homeContentRecycler.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = currencyDataAdapter
                }

                //设置smartRefresh的头尾，头部下沉
                homeContentSmartRefresh.apply {
                    setRefreshHeader(MaterialHeader(context))
                    setRefreshFooter(ClassicsFooter(context))
                    setEnableHeaderTranslationContent(true)
                }
                //将布局传给nv
                homeContentNested.getSmart(homeContentDataBinding.root)
            }
        }

        shopCarDialog = ShopCarDialog(requireContext())
    }

    //轮播图的数据
    private val pageDatList = mutableListOf<HomeContentList.Data>()


    private val avoidRepetitionList = mutableListOf<String>()

    @OptIn(DelicateCoroutinesApi::class)
    override fun initListener() {
        super.initListener()
        homeContentDataBinding.apply {
            //全局布局
            homeContentContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    //那到头部高度，将它传给nv
                    val headHeight =
                        homeContentViewPager.measuredHeight + homeContentTitleLayout.measuredHeight
                    homeContentNested.getHeadViewHeight(
                        headHeight + SizeUtil.dip2px(21f),
                    )

                    //拿到rv的layoutParams，将整个布局的高度设置给rv，解决rv的复用问题
                    val rvLayoutParams = homeContentRecycler.layoutParams
                    rvLayoutParams.height = homeContentContainer.measuredHeight
                    homeContentRecycler.layoutParams = rvLayoutParams

                    //如果rv接收到了高度，将全局布局移除
                    if (rvLayoutParams.height != 0) {
                        homeContentContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    }
                }

            })

            if (categoryData != null) {
                //设置smartRefresh的刷新加载事件
                homeContentSmartRefresh.setOnRefreshLoadMoreListener(object :
                    OnRefreshLoadMoreListener {
                    override fun onRefresh(refreshLayout: RefreshLayout) {
                        currentViewModel.getHomeRefreshData(categoryData.id)
                        homeContentSmartRefresh.finishRefresh()
                    }

                    override fun onLoadMore(refreshLayout: RefreshLayout) {
                        currentViewModel.getHomeLoadMoreData(categoryData.id)
                        homeContentSmartRefresh.finishLoadMore()
                    }

                })
            }

            homeContentViewPager.setOnPageClickListener(object :
                IndicationLayout.OnPageClickListener {
                override fun onPageClick(baseData: BaseData) {
                    val handleTickNeedData = HandlerTicketData.handleTickNeedData(baseData)
                    //ticketViewModel.getTicketData(handleTickNeedData)
                    val bundle = Bundle()
                    bundle.putParcelable(Constant.ITEM_CLICK_DATA, handleTickNeedData)
                    findNavController().navigate(R.id.home_to_ticket, bundle)
                }

                //viewpage添加到购物车
                override fun onPageLongClick(baseData: BaseData) {
                    val data = handlerShopCarData(baseData)

                    if (avoidRepetitionList.size == 0) {
                        shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                        shopCarDialog.setOnAddShopCarClickListener(object :
                            ShopCarDialog.OnAddShopCarClickListener {
                            override fun onAddShopCarClick() {
                                Toast.makeText(requireContext(),"添加成功",Toast.LENGTH_SHORT).show()
                                shopCarViewModel.insertShopCarData(data)
                            }
                        })
                    }


                        if (!avoidRepetitionList.contains(data.title) ) {
                            //  shopCarViewModel.insertShopCarData(baseData)
                            shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                            shopCarDialog.setOnAddShopCarClickListener(object :
                                ShopCarDialog.OnAddShopCarClickListener {
                                override fun onAddShopCarClick() {
                                    Toast.makeText(requireContext(),"添加成功",Toast.LENGTH_SHORT).show()
                                    shopCarViewModel.insertShopCarData(data)
                                }
                            })
                        } else {
                            shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.IS_ADD)
                        }

                    shopCarDialog.show()
                }
            })

            currencyDataAdapter.setCurrencyItemClickListener(object :
                CurrencyDataAdapter.OnCurrencyItemClickListener {
                override fun onItemClickListener(baseData: BaseData) {
                    val handleTickNeedData = HandlerTicketData.handleTickNeedData(baseData)
                    // ticketViewModel.getTicketData(handleTickNeedData)
                    val bundle = Bundle()
                    bundle.putParcelable("clickData", handleTickNeedData)
                    findNavController().navigate(R.id.home_to_ticket, bundle)
                }

                //recyclerview添加购物车
                override fun onItemLongClickListener(baseData: BaseData) {
                    val data = handlerShopCarData(baseData)

                    if (avoidRepetitionList.size == 0) {
                        shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                        shopCarDialog.setOnAddShopCarClickListener(object :
                            ShopCarDialog.OnAddShopCarClickListener {
                            override fun onAddShopCarClick() {
                                Toast.makeText(requireContext(),"添加成功",Toast.LENGTH_SHORT).show()
                                shopCarViewModel.insertShopCarData(data)
                            }
                        })
                    }

                    if (!avoidRepetitionList.contains(data.title)) {
                        shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.ADD)
                        shopCarDialog.setOnAddShopCarClickListener(object :
                            ShopCarDialog.OnAddShopCarClickListener {
                            override fun onAddShopCarClick() {
                                Toast.makeText(requireContext(),"添加成功",Toast.LENGTH_SHORT).show()
                                shopCarViewModel.insertShopCarData(data)
                            }
                        })

                    } else {
                        shopCarDialog.updateAddShopCarState(ShopCarDialog.AddShopCarState.IS_ADD)
                    }
                    shopCarDialog.show()
                }
            })
        }
    }

    private fun handlerShopCarData(baseData: BaseData): ShopCarData {
        return ShopCarData(
            baseData.title,
            baseData.zkFinalPrice,
            baseData.pictUrl,
            baseData.couponAmount,
            baseData.volume,
            baseData.clickUrl,
            baseData.couponClickUrl
        )
    }

    override fun initDataListener() {
        super.initDataListener()
        currentViewModel.apply {

            var loadListSize = 0
            contentDataList.observe(this@HomeContentFragment) {
                //如果数据不为空
                if (it.isNotEmpty()) {

                    //将请求回来的数据穿给rv的适配器
                    currencyDataAdapter.setData(it)
                    //刷新和正常加载共用一个liveData，避免正常加载直接弹出toast
                    if (loadListSize != 0) {
                        Toast.makeText(context, "成功刷新${loadListSize}条数据", Toast.LENGTH_SHORT).show()
                    } else {
                        loadListSize = it.size
                    }
                } else {
                    Toast.makeText(context, "刷新失败", Toast.LENGTH_SHORT).show()
                }
            }

            contentLoadMoreList.observe(this@HomeContentFragment) {
                //没有对加载更多做状态处理，直接根据list的大小来判断
                if (it.isNotEmpty()) {
                    currencyDataAdapter.setLoadMoreData(it)
                    Toast.makeText(context, "成功加载${it.size}条数据", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "加载失败!", Toast.LENGTH_SHORT).show()
                }

            }
            contentViewPageList.observe(this@HomeContentFragment){
                Log.d("TAG",it.size.toString())
                if (it.isNotEmpty()) {
                    if (categoryData != null) {
                        currentViewModel.getHomeContentData(categoryData.id)
                    }

                    //取数据的后四位，传给轮播图布局
                    for (i in it.size - 4 until it.size) {
                        pageDatList.add(it[i])
                    }
                    homeContentViewPager.setData(pageDatList)
                }
            }

            contentLoadStatus.observe(this@HomeContentFragment) {
                //请求数据的状态处理
                when (it) {
                    HomeContentViewModel.HomeContentLoadStatus.LOADING -> {
                        switchDisplayView(LoadViewStatus.LOADING)
                    }

                    HomeContentViewModel.HomeContentLoadStatus.SUCCESS -> {
                        switchDisplayView(LoadViewStatus.SUCCESS)
                    }

                    HomeContentViewModel.HomeContentLoadStatus.EMPTY -> {
                        switchDisplayView(LoadViewStatus.EMPTY)
                    }

                    HomeContentViewModel.HomeContentLoadStatus.ERROR -> {
                        switchDisplayView(LoadViewStatus.ERROR)
                    }
                    else -> {}
                }
            }
        }

        shopCarViewModel.queryAllShopCarData().observe(this) {
            avoidRepetitionList.clear()
            it.forEach {
                avoidRepetitionList.add(it.title)
            }

        }
    }


}