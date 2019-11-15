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
import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorInt
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

    private var gravity = Gravity.CENTER // 对话框的位置
    private var canceledOnTouchOutside = true // 是否触摸外部关闭
    private var canceledBack = true // 是否返回键关闭
    private var width = 0.9f // 对话框宽度，范围：0-1；1整屏宽
    private var height = 0.0f // 对话框宽度，范围：0-1；1整屏高，0默认包裹内容
    private var offsetY = 0f // Y方向偏移，范围：-1 ~ 1；1向下整屏幕
    private var padding: IntArray? = null // 对话框与屏幕边缘距离
    private var animStyle: Int = 0 // 显示动画
    private var dimEnabled = true // 边缘阴影
    private var backgroundColor = Color.TRANSPARENT; // 对话框的背景色
    private var radius = 0 // 圆角半径
    private var alpha = 1f // 对话框透明度，范围：0-1；1不透明
    private var x: Int = 0
    private var y: Int = 0

    val isShowing: Boolean
        get() = dialog != null && dialog.isShowing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置 无标题 无边框
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        if (savedInstanceState != null) {
            gravity = savedInstanceState.getInt(SAVED_GRAVITY)
            canceledOnTouchOutside = savedInstanceState.getBoolean(SAVED_TOUCH_OUT)
            canceledBack = savedInstanceState.getBoolean(SAVED_CANCELED_BACK)
            width = savedInstanceState.getFloat(SAVED_WIDTH)
            height = savedInstanceState.getFloat(SAVED_HEIGHT)
            offsetY = savedInstanceState.getFloat(SAVED_OFFSET_Y)
            padding = savedInstanceState.getIntArray(SAVED_PADDING)
            animStyle = savedInstanceState.getInt(SAVED_ANIM_STYLE)
            dimEnabled = savedInstanceState.getBoolean(SAVED_DIM_ENABLED)
            backgroundColor = savedInstanceState.getInt(SAVED_BACKGROUND_COLOR);
            radius = savedInstanceState.getInt(SAVED_RADIUS);
            alpha = savedInstanceState.getFloat(SAVED_ALPHA)
            x = savedInstanceState.getInt(SAVED_X)
            y = savedInstanceState.getInt(SAVED_Y)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVED_GRAVITY, gravity)
        outState.putBoolean(SAVED_TOUCH_OUT, canceledOnTouchOutside)
        outState.putBoolean(SAVED_CANCELED_BACK, canceledBack)
        outState.putFloat(SAVED_WIDTH, width)
        outState.putFloat(SAVED_HEIGHT, height)
        outState.putFloat(SAVED_OFFSET_Y, offsetY)
        if (padding != null) {
            outState.putIntArray(SAVED_PADDING, padding)
        }
        outState.putInt(SAVED_ANIM_STYLE, animStyle)
        outState.putBoolean(SAVED_DIM_ENABLED, dimEnabled)
        outState.putInt(SAVED_BACKGROUND_COLOR, backgroundColor);
        outState.putInt(SAVED_RADIUS, radius);
        outState.putFloat(SAVED_ALPHA, alpha)
        outState.putInt(SAVED_X, x)
        outState.putInt(SAVED_Y, y)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = createView(context, inflater, container)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = CircleDrawable(backgroundColor, radius);
        } else {
            view.setBackgroundDrawable(CircleDrawable(backgroundColor, radius))
        }
        view.alpha = alpha
        return view
    }

    override fun onStart() {
        val dialog = dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            dialog.setCancelable(canceledBack)
            setDialog(dialog)
        }
        super.onStart()
    }

    private fun setDialog(dialog: Dialog) {
        // 设置宽度为屏宽、靠近屏幕底部
        val window = dialog.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val wlp = window.attributes
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm) // 获取屏幕宽
        wlp.width = (dm.widthPixels * width).toInt() // 宽度按屏幕宽度的百分比设置
        if (height > 0) {
            wlp.height = (dm.heightPixels * height).toInt() // 高度按屏幕宽度的百分比设置
        }
        wlp.gravity = gravity
        wlp.x = x
        wlp.y = y
        if (offsetY != 0f) {
            wlp.y = (dm.heightPixels * offsetY).toInt() // 偏移按屏幕高度的百分比设置
        }
        // 边距
        if (padding != null) {
            val padding = padding!!
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT
            window.decorView.setPadding(
                scaleValue(padding[0]), scaleValue(padding[1]),
                scaleValue(padding[2]), scaleValue(padding[3])
            )
        }
        // 动画
        if (animStyle != 0) {
            window.setWindowAnimations(animStyle)
        }

        if (dimEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        window.attributes = wlp
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded) {
            val transaction = manager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.add(this, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    fun remove() {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.remove(this)
        transaction?.addToBackStack(null)
    }

    /**
     * 设置对话框位置
     *
     * @param gravity 位置
     */
    fun setGravity(gravity: Int): BaseDialog {
        this.gravity = gravity
        return this
    }

    /**
     * 设置对话框点击外部关闭
     *
     * @param cancel true允许
     */
    fun setCanceledOnTouchOutside(cancel: Boolean): BaseDialog {
        canceledOnTouchOutside = cancel
        return this
    }

    /**
     * 设置对话框返回键关闭关闭
     *
     * @param cancel true允许
     */
    fun setCanceledBack(cancel: Boolean): BaseDialog {
        canceledBack = cancel
        return this
    }

    /**
     * 设置对话框宽度
     *
     * @param width 0.0 ~ 1.0
     */
    fun setWidth(@FloatRange(from = 0.0, to = 1.0) width: Float): BaseDialog {
        this.width = width
        return this
    }

    /**
     * 设置对话框高度
     *
     * @param height 0.0 ~ 1.0 0:默认包裹内容 1:整屏高
     */
    fun setHeight(@FloatRange(from = 0.0, to = 1.0) height: Float): BaseDialog {
        this.height = height
        return this
    }

    /**
     * 设置对话框偏移
     *
     * @param offsetY -1.0 ~ 1.0
     */
    fun setOffsetY(@FloatRange(from = -1.0, to = 1.0) offsetY: Float): BaseDialog {
        this.offsetY = offsetY
        return this
    }

    /**
     * 设置边距
     *
     * @param left   px
     * @param top    px
     * @param right  px
     * @param bottom px
     */
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int): BaseDialog {
        padding = intArrayOf(left, top, right, bottom)
        return this
    }

    /**
     * 弹出对话框的动画
     *
     * @param animStyle StyleRes
     */
    fun setAnimations(animStyle: Int): BaseDialog {
        this.animStyle = animStyle
        return this
    }

    /**
     * 设置背景是否阴影，默认true
     *
     * @param dimEnabled true阴影
     */
    fun setDimEnabled(dimEnabled: Boolean): BaseDialog {
        this.dimEnabled = dimEnabled
        return this
    }

    /**
     * 设置背景颜色
     *
     * @color 背景颜色
     */
    fun setBackgroundColor(@ColorInt color: Int): BaseDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置对话框圆角
     *
     * @param radius 半径
     */
    fun setRadius(radius: Int): BaseDialog {
        this.radius = radius
        return this
    }

    /**
     * 设置对话框透明度
     *
     * @param alpha 0.0 - 1.0
     */
    fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): BaseDialog {
        this.alpha = alpha
        return this
    }

    fun setX(x: Int): BaseDialog {
        this.x = x
        return this
    }

    fun setY(y: Int): BaseDialog {
        this.y = y
        return this
    }

    private fun scaleValue(pxVal: Int): Int {
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
        private val SAVED_HEIGHT = "SAVED_HEIGHT"
        private val SAVED_OFFSET_Y = "SAVED_OFFSET_Y"
        private val SAVED_PADDING = "SAVED_PADDING"
        private val SAVED_ANIM_STYLE = "SAVED_ANIM_STYLE"
        private val SAVED_DIM_ENABLED = "SAVED_DIM_ENABLED"
        private val SAVED_BACKGROUND_COLOR = "SAVED_BACKGROUND_COLOR"
        private val SAVED_RADIUS = "SAVED_RADIUS"
        private val SAVED_ALPHA = "SAVED_ALPHA"
        private val SAVED_X = "SAVED_X"
        private val SAVED_Y = "SAVED_Y"
    }
}
