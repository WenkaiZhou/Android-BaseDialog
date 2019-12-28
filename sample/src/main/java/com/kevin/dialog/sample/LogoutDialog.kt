package com.kevin.dialog.sample

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kevin.dialog.BaseDialog

/**
 * LogoutDialog
 *
 * @author zwenkai@foxmail.com, Created on 2019-01-20 17:13:12
 *         Major Function：<b></b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */
class LogoutDialog : BaseDialog() {

    private lateinit var builder: Builder

    override fun createView(context: Context?, inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = LogoutDialogBinding.inflate(LayoutInflater.from(getContext()), container, false)
        binding.view = this
        return binding.root
    }

    fun show(): LogoutDialog {
        super.show(builder.activity.supportFragmentManager, TAG)
        return this
    }

    fun onLogoutClicked(view: View) {
        builder.logoutListener?.invoke(this, view)
    }

    fun onCancelClicked(view: View) {
        dismiss()
    }

    class Builder(val activity: FragmentActivity) {
        var logoutListener: ((dialog: LogoutDialog, view: View) -> Unit)? = null
            private set

        fun setOnLogoutListener(logoutListener: (dialog: LogoutDialog, view: View) -> Unit): Builder {
            this.logoutListener = logoutListener
            return this
        }

        fun build(): LogoutDialog {
            val dialog = LogoutDialog()
            dialog.setCanceledBack(false) // 设置屏蔽返回键
                .setCanceledOnTouchOutside(false) // 设置屏蔽对话框点击外部关闭
                .setGravity(Gravity.BOTTOM) // 设置对话框在底部
                .setWidth(1f) // 设置宽度为屏幕宽度
                .setDimEnabled(false) // 设置无黑色透明背景
                .setAnimations(android.R.style.Animation_InputMethod)
                .setRadius(8)
            dialog.builder = this
            return dialog
        }
    }

    companion object {
        private const val TAG = "LogoutDialog"
    }
}
