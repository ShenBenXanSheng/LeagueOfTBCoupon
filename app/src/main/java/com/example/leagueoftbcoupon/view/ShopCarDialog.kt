package com.example.leagueoftbcoupon.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.compose.ui.layout.Layout
import androidx.databinding.DataBindingUtil
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.databinding.DialogAddShopCarBinding

/**
 * 什本先生
 * 2022年5月27日
 * 添加商品，已经添加过商品，删除商品的dialog
 */
class ShopCarDialog : Dialog, View.OnClickListener {
    private lateinit var onClearHistoryClickListener: OnClearHistoryClickListener
    private lateinit var onClearShopCarClickListener: OnClearShopCarClickListener
    private lateinit var onDeleteShopCarClickListener: OnDeleteShopCarClickListener
    private lateinit var onAddShopCarClickListener: OnAddShopCarClickListener
    private var dialogView: DialogAddShopCarBinding

    enum class AddShopCarState {
        ADD, IS_ADD, DELETE, CLEAR, DOT_HAVE_COUPON,CLEAR_HISTORY
    }

    fun updateAddShopCarState(addShopCarState: AddShopCarState) {
        hideAllView()
        when (addShopCarState) {
            AddShopCarState.ADD -> {
                dialogView.dialogAddShopCarContainer.visibility = View.VISIBLE
            }

            AddShopCarState.IS_ADD -> {
                dialogView.dialogIsAddShopCarContainer.visibility = View.VISIBLE
            }
            AddShopCarState.DELETE -> {
                dialogView.dialogDeleteShopCarContainer.visibility = View.VISIBLE
            }

            AddShopCarState.CLEAR -> {
                dialogView.dialogClearShopCarContainer.visibility = View.VISIBLE
            }

            AddShopCarState.DOT_HAVE_COUPON -> {
                dialogView.dialogDtCouponShopCarContainer.visibility = View.VISIBLE
            }

            AddShopCarState.CLEAR_HISTORY->{
                dialogView.dialogClearHistoryContainer.visibility = View.VISIBLE
            }
        }
    }

    constructor(context: Context) : this(context, 0)
    constructor(context: Context, defaultAttr: Int) : super(context, defaultAttr) {
        dialogView = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_add_shop_car,
            null,
            false
        )
        hideAllView()
        initView()

        setContentView(dialogView.root)
    }

    private fun initView() {
        dialogView.apply {
            dialogConfirmAddTv.setOnClickListener {
                onAddShopCarClickListener.onAddShopCarClick()
                dismiss()
            }

            dialogConfirmDeleteTv.setOnClickListener {
                onDeleteShopCarClickListener.onDeleteShopCarClick()
                dismiss()
            }

            dialogConfirmClearTv.setOnClickListener {
                onClearShopCarClickListener.onClearShopCarClick()
                dismiss()
            }

            dialogConfirmClearHisTv.setOnClickListener {
                onClearHistoryClickListener.onClearHistoryClick()
                dismiss()
            }
            dialogGiveUpAddTv.setOnClickListener(this@ShopCarDialog)
            dialogIsAddShopCarContainer.setOnClickListener(this@ShopCarDialog)
            dialogGiveUpDeleteTv.setOnClickListener(this@ShopCarDialog)
            dialogGiveUpClearTv.setOnClickListener(this@ShopCarDialog)
            dialogDtCouponTv.setOnClickListener(this@ShopCarDialog)
            dialogGiveUpClearHisTv.setOnClickListener(this@ShopCarDialog)
        }
    }

    private fun hideAllView() {
        dialogView.apply {
            dialogAddShopCarContainer.visibility = View.GONE
            dialogIsAddShopCarContainer.visibility = View.GONE
            dialogDeleteShopCarContainer.visibility = View.GONE
            dialogClearShopCarContainer.visibility = View.GONE
            dialogDtCouponShopCarContainer.visibility = View.GONE
            dialogClearHistoryContainer.visibility = View.GONE
        }
    }

    fun setOnAddShopCarClickListener(onAddShopCarClickListener: OnAddShopCarClickListener) {
        this.onAddShopCarClickListener = onAddShopCarClickListener
    }

    fun setOnDeleteShopCarClickListener(onDeleteShopCarClickListener: OnDeleteShopCarClickListener) {
        this.onDeleteShopCarClickListener = onDeleteShopCarClickListener
    }

    fun setOnClearShopCarClickListener(onClearShopCarClickListener: OnClearShopCarClickListener) {
        this.onClearShopCarClickListener = onClearShopCarClickListener
    }

    fun setOnClearHistoryClickListener(onClearHistoryClickListener: OnClearHistoryClickListener){
        this.onClearHistoryClickListener = onClearHistoryClickListener
    }

    interface OnAddShopCarClickListener {
        fun onAddShopCarClick()
    }


    interface OnDeleteShopCarClickListener {
        fun onDeleteShopCarClick()
    }

    interface OnClearShopCarClickListener {
        fun onClearShopCarClick()
    }

    interface OnClearHistoryClickListener{
        fun onClearHistoryClick()
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}