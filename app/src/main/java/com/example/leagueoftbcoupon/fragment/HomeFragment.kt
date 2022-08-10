package com.example.leagueoftbcoupon.fragment

import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.adapter.HomePageAdapter
import com.example.leagueoftbcoupon.base.BaseViewModelFragment
import com.example.leagueoftbcoupon.databinding.FragmentHomeBinding
import com.example.leagueoftbcoupon.databinding.FragmentHomeSuccessBinding
import com.example.leagueoftbcoupon.viewModel.HomeCategoryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 什本先生
 * 2022年5月24日
 * 首页界面，viewpager2和tabLayout联动
 */
class HomeFragment : BaseViewModelFragment<HomeCategoryViewModel>() {
    private lateinit var homeDataBinding: FragmentHomeBinding//该布局需要修改rootView，rootView的DataBinding
    private lateinit var homeDataSuccessBinding: FragmentHomeSuccessBinding//成功布局的DataBinding
    private val titleList = mutableListOf<String>()//tabLayout需要的标题容器
    private lateinit var ediator: TabLayoutMediator//tabLayout中介器，负责把viewapger2和tab联动起来

    //创建viewpager2的适配器
    private val homePageAdapter by lazy {
        HomePageAdapter(childFragmentManager, lifecycle)
    }

    //设置成功布局
    override fun setSuccessView(contentContainer: FrameLayout): View {
        homeDataSuccessBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_home_success,
            contentContainer,
            false
        )

        return homeDataSuccessBinding.root
    }

    //设置rootview
    override fun setRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        homeDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        return homeDataBinding.root
    }

    //发生错误重新加载数据
    override fun reLoadData() {
        currentViewModel.getCategoryList()
    }

    //设置当前viewModel
    override fun setViewModelClass() = HomeCategoryViewModel::class.java

    override fun initView() {
        super.initView()
        //viewmodel获取数据
        currentViewModel.getCategoryList()

        //搜索框的点击事件，跳转到搜索界面
        homeDataBinding.homeToSearchEd.setOnClickListener {
            //findNavController().navigate(R.id.fragment_search)
            activity?.mainBottomNavigation?.selectedItemId = R.id.fragment_search

        }

        //购物车点击事件
        homeDataBinding.homeShopCarIv.setOnClickListener {
            findNavController().navigate(R.id.home_to_shop)
        }
        homeDataSuccessBinding.apply {
            //关闭viewpage2的预加载
            homeVp2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            //设置适配器
            homeVp2.adapter = homePageAdapter
            //设置滑动监听
            homeVp2.registerOnPageChangeCallback(viewPager2Callback)
            //创建中介器,将page和tab传入
            ediator = TabLayoutMediator(
                homeTabLayout,
                homeVp2
            ) { tab, position ->
                //初始化tablayout的颜色，位置，以及根据position加载文字
                val textView = TextView(context)
                textView.text = titleList[position]
                textView.setTextColor(resources.getColor(R.color.white))
                textView.gravity = Gravity.CENTER

                //将设置好的textview给tab的customview
                tab.customView = textView
            }
            //确认绑定
            ediator.attach()
        }
    }

    override fun initDataListener() {
        super.initDataListener()
        //给tab标题容器赋值
        currentViewModel.categoryList.observe(this) { it ->
            it.forEach {
                titleList.add(it.title)
            }
            //给viewpager2传入该容器
            homePageAdapter.setData(it)

        }

        //状态处理
        currentViewModel.categoryLoadState.observe(this) {
            when (it) {
                HomeCategoryViewModel.LoadCategoryStatus.LOADING -> {
                    switchDisplayView(LoadViewStatus.LOADING)

                }

                HomeCategoryViewModel.LoadCategoryStatus.SUCCESS -> {
                    switchDisplayView(LoadViewStatus.SUCCESS)

                }

                HomeCategoryViewModel.LoadCategoryStatus.EMPTY -> {
                    switchDisplayView(LoadViewStatus.EMPTY)
                }

                HomeCategoryViewModel.LoadCategoryStatus.ERROR -> {
                    switchDisplayView(LoadViewStatus.ERROR)
                }
                else -> {}
            }
        }
    }

    //viewpager2的监听器
    private val viewPager2Callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            //循环标题容器
            for (i in 0 until titleList.size) {
                //拿到所有的tab
                val tabAt = homeDataSuccessBinding.homeTabLayout.getTabAt(i)
                //将tab转为textview
                if (tabAt != null) {
                    val textView = tabAt.customView as TextView
                    //如果所有的tab里有一个的position等于viewpage的position，及选中，修改颜色，否则还是默认颜色
                    if (tabAt.position == position) {
                        textView.setTextColor(resources.getColor(R.color.black))

                    } else {
                        textView.setTextColor(resources.getColor(R.color.white))
                    }
                }

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        homeDataSuccessBinding.homeVp2.unregisterOnPageChangeCallback(viewPager2Callback)
        ediator.detach()
    }
}