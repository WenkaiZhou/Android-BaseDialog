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
package com.kevin.dialog.sample

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kevin.dialog.BaseDialog

/**
 * CommonDialog
 *
 * @author zwenkai@foxmail.com, Created on 2019-12-28 20:16:46
 *         Major Function：<b>CommonDialog</b>
 *
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */
class CommonDialog : BaseDialog() {

    private var titleView: TextView? = null
    private var contentView: TextView? = null
    private var negativeButton: TextView? = null
    private var positiveButton: TextView? = null
    private var dividerView: View? = null
    private lateinit var builder: Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        this.isCancelable = true
        this.retainInstance = true
        super.onCreate(savedInstanceState)
    }

    override fun createView(
        context: Context?,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val layoutRes = builder.getLayoutRes() ?: R.layout.layout_common_dialog
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        builder.getBindViewCallback()?.invoke(this, view)

        if (builder.getTitle() != null) {
            titleView?.text = Html.fromHtml(builder.getTitle())
        } else {
            titleView?.visibility = View.GONE
        }
        if (builder.getContent() != null) {
            contentView?.text = Html.fromHtml(builder.getContent())
        } else {
            contentView?.visibility = View.GONE
        }
        if (builder.getNegativeButton() != null) {
            negativeButton?.text = builder.getNegativeButton()
            negativeButton?.setOnClickListener {
                builder.getOnNegativeClickListener()?.invoke(this)
            }
        } else {
            negativeButton?.visibility = View.GONE
            dividerView?.visibility = View.GONE
        }
        if (builder.getPositiveButton() != null) {
            positiveButton?.text = builder.getPositiveButton()
            positiveButton?.setOnClickListener {
                builder.getOnPositiveClickListener()?.invoke(this)
            }
        } else {
            positiveButton?.visibility = View.GONE
            dividerView?.visibility = View.GONE
        }
    }

    private fun initViews(view: View) {
        titleView = view.findViewById(R.id.common_dialog_title)
        contentView = view.findViewById(R.id.common_dialog_content)
        negativeButton = view.findViewById(R.id.common_dialog_negative)
        positiveButton = view.findViewById(R.id.common_dialog_positive)
        dividerView = view.findViewById(R.id.common_dialog_negative_positive_divider)
    }

    fun show(): CommonDialog {
        if (!isAdded) {
            show(builder.getActivity().supportFragmentManager, TAG)
        }
        return this
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        builder.getOnDismissListener()?.invoke(this)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        builder.getOnCancelListener()?.invoke(this)
    }

    class Builder(private var activity: FragmentActivity) {
        @LayoutRes
        private var layoutRes: Int? = null
        private var titleText: String? = null
        private var contentText: String? = null
        private var negativeButtonText: String? = null
        private var positiveButtonText: String? = null
        private var onPositiveClickListener: ((dialog: CommonDialog) -> Unit)? = null
        private var onNegativeClickListener: ((dialog: CommonDialog) -> Unit)? = null
        private var onDismissListener: ((dialog: CommonDialog) -> Unit)? = null
        private var onCancelListener: ((dialog: CommonDialog) -> Unit)? = null
        private var bindViewCallback: ((dialog: CommonDialog, view: View) -> Unit)? = null

        private var gravity = Gravity.CENTER // 对话框的位置
        private var canceledOnTouchOutside = true // 是否触摸外部关闭
        private var canceledBack = true // 是否返回键关闭
        private var width = 0.9f // 对话框宽度，范围：0-1；1整屏宽
        private var height = 0.0f // 对话框宽度，范围：0-1；1整屏高，0默认包裹内容
        private var offsetY = 0f // Y方向偏移，范围：-1 ~ 1；1向下整屏幕
        private var padding: IntArray? = null // 对话框与屏幕边缘距离
        private var animStyle: Int = 0 // 显示动画
        private var dimEnabled = true // 边缘阴影
        private var backgroundColor = Color.WHITE // 对话框的背景色
        private var radius = 0 // 圆角半径
        private var alpha = 1f // 对话框透明度，范围：0-1；1不透明
        private var x: Int = 0
        private var y: Int = 0

        init {
            radius = (10 * activity.resources.displayMetrics.density).toInt()
        }

        fun setLayoutRes(@LayoutRes layoutRes: Int): Builder {
            this.layoutRes = layoutRes
            return this
        }

        fun getLayoutRes() = layoutRes

        fun setTitle(title: String): Builder {
            titleText = title
            return this
        }

        fun getTitle() = titleText

        fun setTitle(title: Int): Builder {
            titleText = activity.getString(title)
            return this
        }

        fun setContent(content: String): Builder {
            contentText = content
            return this
        }

        fun setContent(content: Int): Builder {
            contentText = activity.getString(content)
            return this
        }

        fun getContent() = contentText

        @JvmOverloads
        fun setNegativeButton(
            negativeButtonText: String,
            listener: ((dialog: CommonDialog) -> Unit)? = null
        ): Builder {
            this.negativeButtonText = negativeButtonText
            this.onNegativeClickListener = listener
            return this
        }

        @JvmOverloads
        fun setNegativeButton(
            negativeButtonText: Int,
            listener: ((dialog: CommonDialog) -> Unit)? = null
        ): Builder {
            this.negativeButtonText = activity.getString(negativeButtonText)
            this.onNegativeClickListener = listener
            return this
        }

        fun getNegativeButton() = negativeButtonText

        @JvmOverloads
        fun setPositiveButton(
            positiveButtonText: Int,
            listener: ((dialog: CommonDialog) -> Unit)? = null
        ): Builder {
            this.positiveButtonText = activity.getString(positiveButtonText)
            this.onPositiveClickListener = listener
            return this
        }

        @JvmOverloads
        fun setPositiveButton(
            positiveButtonText: String,
            listener: ((dialog: CommonDialog) -> Unit)? = null
        ): Builder {
            this.positiveButtonText = positiveButtonText
            this.onPositiveClickListener = listener
            return this
        }

        fun getPositiveButton() = positiveButtonText

        fun getOnPositiveClickListener() = onPositiveClickListener

        fun getOnNegativeClickListener() = onNegativeClickListener

        fun setOnDismissListener(listener: (dialog: CommonDialog) -> Unit): Builder {
            this.onDismissListener = onDismissListener
            return this
        }

        fun getOnDismissListener() = onDismissListener

        fun setOnCancelListener(listener: (dialog: CommonDialog) -> Unit): Builder {
            onCancelListener = listener
            return this
        }

        fun getOnCancelListener() = onCancelListener

        fun getActivity() = activity

        /**
         * 设置对话框位置
         *
         * @param gravity 位置
         */
        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }

        /**
         * 设置对话框点击外部关闭
         *
         * @param cancel true允许
         */
        fun setCanceledOnTouchOutside(cancel: Boolean): Builder {
            canceledOnTouchOutside = cancel
            return this
        }

        /**
         * 设置对话框返回键关闭关闭
         *
         * @param cancel true允许
         */
        fun setCanceledBack(cancel: Boolean): Builder {
            canceledBack = cancel
            return this
        }

        /**
         * 设置对话框宽度
         *
         * @param width 0.0 ~ 1.0
         */
        fun setWidth(@FloatRange(from = 0.0, to = 1.0) width: Float): Builder {
            this.width = width
            return this
        }

        /**
         * 设置对话框高度
         *
         * @param height 0.0 ~ 1.0 0:默认包裹内容 1:整屏高
         */
        fun setHeight(@FloatRange(from = 0.0, to = 1.0) height: Float): Builder {
            this.height = height
            return this
        }

        /**
         * 设置对话框偏移
         *
         * @param offsetY -1.0 ~ 1.0
         */
        fun setOffsetY(@FloatRange(from = -1.0, to = 1.0) offsetY: Float): Builder {
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
        fun setPadding(left: Int, top: Int, right: Int, bottom: Int): Builder {
            padding = intArrayOf(left, top, right, bottom)
            return this
        }

        /**
         * 弹出对话框的动画
         *
         * @param animStyle StyleRes
         */
        fun setAnimations(animStyle: Int): Builder {
            this.animStyle = animStyle
            return this
        }

        /**
         * 设置背景是否阴影，默认true
         *
         * @param dimEnabled true阴影
         */
        fun setDimEnabled(dimEnabled: Boolean): Builder {
            this.dimEnabled = dimEnabled
            return this
        }

        /**
         * 设置背景颜色
         *
         * @color 背景颜色
         */
        fun setBackgroundColor(@ColorInt color: Int): Builder {
            this.backgroundColor = color
            return this
        }

        /**
         * 设置对话框圆角
         *
         * @param radius 半径
         */
        fun setRadius(radius: Int): Builder {
            this.radius = radius
            return this
        }

        /**
         * 设置对话框透明度
         *
         * @param alpha 0.0 - 1.0
         */
        fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): Builder {
            this.alpha = alpha
            return this
        }

        /**
         * 设置对话框在X方向偏移量
         *
         * @param x
         */
        fun setX(x: Int): Builder {
            this.x = x
            return this
        }

        /**
         * 设置对话框在Y方向偏移量
         *
         * @param y
         */
        fun setY(y: Int): Builder {
            this.y = y
            return this
        }

        /**
         * 设置初始化View的监听回调
         */
        fun onBindView(bindViewCallback: (dialog: CommonDialog, view: View) -> Unit): Builder {
            this.bindViewCallback = bindViewCallback
            return this
        }

        fun getBindViewCallback() = bindViewCallback

        fun build(): CommonDialog {
            val dialog = CommonDialog()
            dialog.setGravity(gravity)
                .setCanceledBack(canceledBack)
                .setCanceledOnTouchOutside(canceledOnTouchOutside)
                .setWidth(width)
                .setHeight(height)
                .setOffsetY(offsetY)
                .setAnimations(animStyle)
                .setDimEnabled(dimEnabled)
                .setBackgroundColor(backgroundColor)
                .setRadius(radius)
                .setAlpha(alpha)
                .setX(x)
                .setY(y)
            if (padding != null) {
                dialog.setPadding(padding!![0], padding!![1], padding!![2], padding!![3])
            }
            dialog.builder = this
            return dialog
        }
    }

    companion object {
        private const val TAG = "CommonDialog"
    }
}
