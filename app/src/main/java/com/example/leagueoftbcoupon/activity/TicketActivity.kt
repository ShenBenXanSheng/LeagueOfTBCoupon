package com.example.leagueoftbcoupon.activity

import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.base.BaseViewModelActivity
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.databinding.ActivityTicketBinding
import com.example.leagueoftbcoupon.databinding.ActivityTicketSuccessBinding
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.domain.dataBean.TicketDataBean
import com.example.leagueoftbcoupon.viewModel.TicketViewModel

/**
 * 什本先生
 * 2022年5月26日
 * 领券界面
 */
class TicketActivity : BaseViewModelActivity<TicketViewModel>() {

    private lateinit var successDataBinding: ActivityTicketSuccessBinding
    private lateinit var dataBinding: ActivityTicketBinding
    private var hasTaoBao = false

    companion object {
        @JvmStatic
        @BindingAdapter("ticketCover")
        fun setTicketImageView(imageView: ImageView, cover: Any) {
            Glide.with(imageView).load(cover).into(imageView)
        }
    }

    override fun setRootView(): View {
        dataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.activity_ticket,
                null,
                false
            )
        return dataBinding.root
    }

    override fun setSuccessView(container: FrameLayout): View {
        successDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_ticket_success,
            container,
            false
        )
        return successDataBinding.root
    }

    override fun reloadData() {
        handlerCurrentTicketData()
    }

    override fun setViewModelClass() = TicketViewModel::class.java

    override fun initView() {
        super.initView()

        dataBinding.ticketToolbar.setNavigationOnClickListener {
            finish()
        }

        successDataBinding.apply {

            ticketTitleTv.isSelected = true

            handlerCurrentTicketData()

            //cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
            val packageManager = packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    val packageInfo = packageManager.getPackageInfo(
                        "com.taobao.taobao",
                        PackageManager.MATCH_UNINSTALLED_PACKAGES
                    )
                    hasTaoBao = packageInfo != null
                } catch (e: Exception) {
                    hasTaoBao = false
                    e.printStackTrace()
                }
            }

            if (!hasTaoBao) {
                toTaobaoOrPasteTicketBt.text = "复制口令到粘贴板"
            } else {
                toTaobaoOrPasteTicketBt.text = "跳转到淘宝领券"
            }
        }

    }

    private fun handlerCurrentTicketData() {
        val intent = intent
        val parcelableExtra =
            intent.getParcelableExtra<HandlerTicketData.IsHandleTickNeedData>(Constant.ITEM_CLICK_DATA)
        if (parcelableExtra != null) {
            val cover = if (parcelableExtra.picUrl.isNotBlank()) {
                parcelableExtra.picUrl
            } else {
                R.mipmap.paimeng
            }
            successDataBinding.ticketData =
                TicketDataBean(parcelableExtra.title, cover)
            currentViewModel.getTicketData(parcelableExtra)
        }
    }

    override fun initDataListener() {
        super.initDataListener()

        currentViewModel.ticketData.observe(this) {

            successDataBinding.apply {
                val ticketModel = it.substring(2, it.indexOf('h'))
                Log.d("优惠码",":${it}")
                ticketTv.text = ticketModel
            }

        }

        currentViewModel.tickState.observe(this) {
            when (it) {
                TicketViewModel.RequestTickState.LOADING -> {
                    updateState(ActivityLoadState.LOADING)
                }

                TicketViewModel.RequestTickState.SUCCESS -> {
                    updateState(ActivityLoadState.SUCCESS)
                }

                TicketViewModel.RequestTickState.EMPTY -> {
                    updateState(ActivityLoadState.EMPTY)
                }

                TicketViewModel.RequestTickState.ERROR -> {
                    updateState(ActivityLoadState.ERROR)
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()
        successDataBinding.toTaobaoOrPasteTicketBt.setOnClickListener {
            val ticket = successDataBinding.ticketTv.text.toString().trim()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("shenben_taobao_ticket", ticket)
            clipboardManager.setPrimaryClip(clipData)

            if (clipboardManager.primaryClip != null) {
                if (!hasTaoBao) {
                    Toast.makeText(this, "复制成功，快去领券联吧！", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent()
                    val componentName =
                        ComponentName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome")
                    intent.component = componentName
                    intent.`package` = packageName
                    startActivity(intent)
                }
            }

        }
    }
}