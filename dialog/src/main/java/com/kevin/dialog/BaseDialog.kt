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
import android.support.annotation.IntRange
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.DisplayMetrics
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
    private var width = -1 // 对话框宽度
    private var height = -1 // 对话框高度
    private var widthRatio = 0.9f // 对话框宽度比例，范围：0-1；1整屏宽
    private var heightRatio = 0.0f // 对话框宽度比例，范围：0-1；1整屏高，0默认包裹内容
    private var offsetY = 0f // Y方向偏移百分比，范围：-1 ~ 1；1向下整屏幕
    private var padding: IntArray? = null // 对话框与屏幕边缘距离
    private var animStyle: Int = 0 // 显示动画
    private var dimEnabled = true // 背景阴影
    private var backgroundColor = Color.TRANSPARENT // 对话框的背景色
    private var leftTopRadius = 0 // 左上角圆角半径
    private var rightTopRadius = 0 // 右上角圆角半径
    private var leftBottomRadius = 0 // 左下角圆角半径
    private var rightBottomRadius = 0 // 右下角圆角半径
    private var alpha = 1f // 对话框透明度，范围：0-1；1不透明
    private var x: Int = 0 // X方向偏移量
    private var y: Int = 0 // Y方向偏移量

    val isShowing: Boolean
        get() = dialog != null && dialog.isShowing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置 无标题 无边框
        setStyle(STYLE_NO_TITLE, 0)
        if (savedInstanceState != null) {
            gravity = savedInstanceState.getInt(SAVED_GRAVITY)
            canceledOnTouchOutside = savedInstanceState.getBoolean(SAVED_TOUCH_OUT)
            canceledBack = savedInstanceState.getBoolean(SAVED_CANCELED_BACK)
            width = savedInstanceState.getInt(SAVED_WIDTH)
            height = savedInstanceState.getInt(SAVED_HEIGHT)
            widthRatio = savedInstanceState.getFloat(SAVED_WIDTH_RATIO)
            heightRatio = savedInstanceState.getFloat(SAVED_HEIGHT_RATIO)
            offsetY = savedInstanceState.getFloat(SAVED_OFFSET_Y)
            padding = savedInstanceState.getIntArray(SAVED_PADDING)
            animStyle = savedInstanceState.getInt(SAVED_ANIM_STYLE)
            dimEnabled = savedInstanceState.getBoolean(SAVED_DIM_ENABLED)
            backgroundColor = savedInstanceState.getInt(SAVED_BACKGROUND_COLOR)
            leftTopRadius = savedInstanceState.getInt(SAVED_LEFT_TOP_RADIUS)
            rightTopRadius = savedInstanceState.getInt(SAVED_RIGHT_TOP_RADIUS)
            leftBottomRadius = savedInstanceState.getInt(SAVED_LEFT_BOTTOM_RADIUS)
            rightBottomRadius = savedInstanceState.getInt(SAVED_RIGHT_BOTTOM_RADIUS)
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
        outState.putInt(SAVED_WIDTH, width)
        outState.putInt(SAVED_HEIGHT, height)
        outState.putFloat(SAVED_WIDTH_RATIO, widthRatio)
        outState.putFloat(SAVED_HEIGHT_RATIO, heightRatio)
        outState.putFloat(SAVED_OFFSET_Y, offsetY)
        if (padding != null) {
            outState.putIntArray(SAVED_PADDING, padding)
        }
        outState.putInt(SAVED_ANIM_STYLE, animStyle)
        outState.putBoolean(SAVED_DIM_ENABLED, dimEnabled)
        outState.putInt(SAVED_BACKGROUND_COLOR, backgroundColor)
        outState.putInt(SAVED_LEFT_TOP_RADIUS, leftTopRadius)
        outState.putInt(SAVED_RIGHT_TOP_RADIUS, rightTopRadius)
        outState.putInt(SAVED_LEFT_BOTTOM_RADIUS, leftBottomRadius)
        outState.putInt(SAVED_RIGHT_BOTTOM_RADIUS, rightBottomRadius)
        outState.putFloat(SAVED_ALPHA, alpha)
        outState.putInt(SAVED_X, x)
        outState.putInt(SAVED_Y, y)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = NoLeakDialog(requireActivity(), theme)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = createView(context, inflater, container)
        val background = CircleDrawable(
            backgroundColor = backgroundColor,
            leftTopRadius = leftTopRadius,
            rightTopRadius = rightTopRadius,
            leftBottomRadius = leftBottomRadius,
            rightBottomRadius = rightBottomRadius
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = background
        } else {
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(background)
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
        if (width == -1) {
            wlp.width = (dm.widthPixels * widthRatio).toInt() // 宽度按屏幕宽度的百分比设置
        } else {
            wlp.width = width
        }
        if (height == -1) {
            if (heightRatio > 0) {
                wlp.height = (dm.heightPixels * heightRatio).toInt() // 高度按屏幕宽度的百分比设置
            }
        } else {
            wlp.height = height
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
            window.decorView.setPadding(padding[0], padding[1], padding[2], padding[3])
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

    override fun dismiss() {
        dismissAllowingStateLoss()
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
     * @param width  > 0
     */
    fun setWidth(@IntRange(from = 0) width: Int): BaseDialog {
        this.width = width
        return this
    }

    /**
     * 设置对话框高度
     *
     * @param height > 0
     */
    fun setHeight(@IntRange(from = 0) height: Int): BaseDialog {
        this.height = height
        return this
    }

    /**
     * 设置对话框宽度比例
     *
     * @param widthRatio 0.0 ~ 1.0
     */
    fun setWidthRatio(@FloatRange(from = 0.0, to = 1.0) widthRatio: Float): BaseDialog {
        this.widthRatio = widthRatio
        return this
    }

    /**
     * 设置对话框高度比例
     *
     * @param heightRatio 0.0 ~ 1.0 0:默认包裹内容 1:整屏高
     */
    fun setHeightRatio(@FloatRange(from = 0.0, to = 1.0) heightRatio: Float): BaseDialog {
        this.heightRatio = heightRatio
        return this
    }

    /**
     * 设置对话框Y方向偏移百分比
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
     * @param color 背景颜色
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
        setLeftTopRadius(radius)
        setRightTopRadius(radius)
        setLeftBottomRadius(radius)
        setRightBottomRadius(radius)
        return this
    }

    /**
     * 设置对话框左上角圆角
     *
     * @param radius 半径
     */
    fun setLeftTopRadius(radius: Int): BaseDialog {
        this.leftTopRadius = radius
        return this
    }

    /**
     * 设置对话框右上角圆角
     *
     * @param radius 半径
     */
    fun setRightTopRadius(radius: Int): BaseDialog {
        this.rightTopRadius = radius
        return this
    }

    /**
     * 设置对话框左上角圆角
     *
     * @param radius 半径
     */
    fun setLeftBottomRadius(radius: Int): BaseDialog {
        this.leftBottomRadius = radius
        return this
    }

    /**
     * 设置对话框右上角圆角
     *
     * @param radius 半径
     */
    fun setRightBottomRadius(radius: Int): BaseDialog {
        this.rightBottomRadius = radius
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

    /**
     * 设置对话框在X方向偏移量
     *
     * @param x
     */
    fun setX(x: Int): BaseDialog {
        this.x = x
        return this
    }

    /**
     * 设置对话框在Y方向偏移量
     *
     * @param y
     */
    fun setY(y: Int): BaseDialog {
        this.y = y
        return this
    }

    abstract fun createView(
        context: Context?,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View

    companion object {
        private const val SAVED_GRAVITY = "SAVED_GRAVITY"
        private const val SAVED_TOUCH_OUT = "SAVED_TOUCH_OUT"
        private const val SAVED_CANCELED_BACK = "SAVED_CANCELED_BACK"
        private const val SAVED_WIDTH = "SAVED_WIDTH"
        private const val SAVED_HEIGHT = "SAVED_HEIGHT"
        private const val SAVED_WIDTH_RATIO = "SAVED_WIDTH_RATIO"
        private const val SAVED_HEIGHT_RATIO = "SAVED_HEIGHT_RATIO"
        private const val SAVED_OFFSET_Y = "SAVED_OFFSET_Y"
        private const val SAVED_PADDING = "SAVED_PADDING"
        private const val SAVED_ANIM_STYLE = "SAVED_ANIM_STYLE"
        private const val SAVED_DIM_ENABLED = "SAVED_DIM_ENABLED"
        private const val SAVED_BACKGROUND_COLOR = "SAVED_BACKGROUND_COLOR"
        private const val SAVED_LEFT_TOP_RADIUS = "SAVED_LEFT_TOP_RADIUS"
        private const val SAVED_RIGHT_TOP_RADIUS = "SAVED_RIGHT_TOP_RADIUS"
        private const val SAVED_LEFT_BOTTOM_RADIUS = "SAVED_LEFT_BOTTOM_RADIUS"
        private const val SAVED_RIGHT_BOTTOM_RADIUS = "SAVED_RIGHT_BOTTOM_RADIUS"
        private const val SAVED_ALPHA = "SAVED_ALPHA"
        private const val SAVED_X = "SAVED_X"
        private const val SAVED_Y = "SAVED_Y"
    }
}
