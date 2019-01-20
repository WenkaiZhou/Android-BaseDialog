/*
 * Copyright (c) 2019 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.FloatRange
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

/**
 * BaseDialog
 *
 * @author zwenkai@foxmail.com, Created on 2019-01-20 17:01:20
 *         Major Function：<b>BaseDialog</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */
abstract class BaseDialog : DialogFragment() {

    private var mGravity = Gravity.CENTER // 对话框的位置
    private var mCanceledOnTouchOutside = true // 是否触摸外部关闭
    private var mCanceledBack = true // 是否返回键关闭
    private var mWidth = 0.9f // 对话框宽度，范围：0-1；1整屏宽
    private var mOffsetY = 0f // Y方向偏移，范围：-1 ~ 1；1向下整屏幕
    private var mPadding: IntArray? = null // 对话框与屏幕边缘距离
    private var mAnimStyle: Int = 0 // 显示动画
    private var isDimEnabled = true // 边缘阴影
    private var mAlpha = 1f // 对话框透明度，范围：0-1；1不透明
    private var mX: Int = 0
    private var mY: Int = 0

    val isShowing: Boolean
        get() = dialog != null && dialog.isShowing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置 无标题 无边框
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        if (savedInstanceState != null) {
            mGravity = savedInstanceState.getInt(SAVED_GRAVITY)
            mCanceledOnTouchOutside = savedInstanceState.getBoolean(SAVED_TOUCH_OUT)
            mCanceledBack = savedInstanceState.getBoolean(SAVED_CANCELED_BACK)
            mWidth = savedInstanceState.getFloat(SAVED_WIDTH)
            mOffsetY = savedInstanceState.getFloat(SAVED_OFFSET_Y)
            mPadding = savedInstanceState.getIntArray(SAVED_PADDING)
            mAnimStyle = savedInstanceState.getInt(SAVED_ANIM_STYLE)
            isDimEnabled = savedInstanceState.getBoolean(SAVED_DIM_ENABLED)
            mAlpha = savedInstanceState.getFloat(SAVED_ALPHA)
            mX = savedInstanceState.getInt(SAVED_X)
            mY = savedInstanceState.getInt(SAVED_Y)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVED_GRAVITY, mGravity)
        outState.putBoolean(SAVED_TOUCH_OUT, mCanceledOnTouchOutside)
        outState.putBoolean(SAVED_CANCELED_BACK, mCanceledBack)
        outState.putFloat(SAVED_WIDTH, mWidth)
        outState.putFloat(SAVED_OFFSET_Y, mOffsetY)
        if (mPadding != null) {
            outState.putIntArray(SAVED_PADDING, mPadding)
        }
        outState.putInt(SAVED_ANIM_STYLE, mAnimStyle)
        outState.putBoolean(SAVED_DIM_ENABLED, isDimEnabled)
        outState.putFloat(SAVED_ALPHA, mAlpha)
        outState.putInt(SAVED_X, mX)
        outState.putInt(SAVED_Y, mY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = createView(context, inflater, container)
        view.alpha = mAlpha
        return view
    }

    override fun onStart() {
        val dialog = dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
            dialog.setCancelable(mCanceledBack)
            setDialogGravity(dialog) // 设置对话框布局
        }
        super.onStart()
    }

    /**
     * 设置对话框底部显示
     *
     * @param dialog
     */
    private fun setDialogGravity(dialog: Dialog) {
        // 设置宽度为屏宽、靠近屏幕底部
        val window = dialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val wlp = window.attributes
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm) // 获取屏幕宽
        wlp.width = (dm.widthPixels * mWidth).toInt() // 宽度按屏幕宽度的百分比设置
        wlp.gravity = mGravity
        wlp.x = mX
        wlp.y = mY
        if (mOffsetY != 0f) {
            wlp.y = (dm.heightPixels * mOffsetY).toInt() // 偏移按屏幕高度的百分比设置
        }
        // 边距
        if (mPadding != null) {
            val padding = mPadding!!
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT
            window.decorView.setPadding(
                scaleValue(padding[0]), scaleValue(padding[1]),
                scaleValue(padding[2]), scaleValue(padding[3])
            )
        }
        // 动画
        if (mAnimStyle != 0) {
            window.setWindowAnimations(mAnimStyle)
        }

        if (isDimEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        window.attributes = wlp
    }

    override fun show(manager: FragmentManager, tag: String) {
        if (!isAdded) {
            val transaction = manager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.add(this, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    fun remove() {
        val ft = fragmentManager!!.beginTransaction()
        ft.remove(this)
        ft.addToBackStack(null)
    }

    /**
     * 设置对话框位置
     * [默认][Gravity.CENTER]
     *
     * @param gravity 位置
     */
    protected fun setGravity(gravity: Int) {
        mGravity = gravity
    }

    /**
     * 设置对话框点击外部关闭
     *
     * @param cancel true允许
     */
    protected fun setCanceledOnTouchOutside(cancel: Boolean) {
        mCanceledOnTouchOutside = cancel
    }

    /**
     * 设置对话框返回键关闭关闭
     *
     * @param cancel true允许
     */
    protected fun setCanceledBack(cancel: Boolean) {
        mCanceledBack = cancel
    }

    /**
     * 设置对话框宽度
     *
     * @param width 0.0 ~ 1.0
     */
    protected fun setWidth(@FloatRange(from = 0.0, to = 1.0) width: Float) {
        mWidth = width
    }

    /**
     * 设置对话框宽度
     *
     * @param offsetY -1.0 ~ 1.0
     */
    protected fun setOffsetY(@FloatRange(from = -1.0, to = 1.0) offsetY: Float) {
        mOffsetY = offsetY
    }

    /**
     * 设置边距
     *
     * @param left   px
     * @param top    px
     * @param right  px
     * @param bottom px
     */
    protected fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mPadding = intArrayOf(left, top, right, bottom)
    }

    /**
     * 弹出对话框的动画
     *
     * @param animStyle StyleRes
     */
    protected fun setAnimations(animStyle: Int) {
        mAnimStyle = animStyle
    }

    /**
     * 设置背景是否昏暗，默认true
     *
     * @param dimEnabled true昏暗
     */
    protected fun setDimEnabled(dimEnabled: Boolean) {
        isDimEnabled = dimEnabled
    }

    /**
     * 设置对话框透明度
     *
     * @param alpha 0.0 - 1.0
     */
    protected fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        mAlpha = alpha
    }

    protected fun setX(x: Int) {
        mX = x
    }

    protected fun setY(y: Int) {
        mY = y
    }

    fun scaleValue(pxVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, pxVal.toFloat(),
            context!!.resources.displayMetrics
        ).toInt()
    }

    abstract fun createView(context: Context?, inflater: LayoutInflater, container: ViewGroup?): View

    companion object {

        private val SAVED_GRAVITY = "SAVED_GRAVITY"
        private val SAVED_TOUCH_OUT = "SAVED_TOUCH_OUT"
        private val SAVED_CANCELED_BACK = "SAVED_CANCELED_BACK"
        private val SAVED_WIDTH = "SAVED_WIDTH"
        private val SAVED_OFFSET_Y = "SAVED_OFFSET_Y"
        private val SAVED_PADDING = "SAVED_PADDING"
        private val SAVED_ANIM_STYLE = "SAVED_ANIM_STYLE"
        private val SAVED_DIM_ENABLED = "SAVED_DIM_ENABLED"
        private val SAVED_ALPHA = "SAVED_ALPHA"
        private val SAVED_X = "SAVED_X"
        private val SAVED_Y = "SAVED_Y"
    }
}
