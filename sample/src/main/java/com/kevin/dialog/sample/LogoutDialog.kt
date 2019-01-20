package com.kevin.dialog.sample

import android.app.Dialog
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

    private var mListener: LogoutDialog.OnLogoutListener? = null

    override fun createView(context: Context?, inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = LogoutDialogBinding.inflate(LayoutInflater.from(getContext()), container, false)
        binding.setView(this)
        return binding.getRoot()
    }

    fun show(activity: FragmentActivity, listener: LogoutDialog.OnLogoutListener): LogoutDialog {
        this.mListener = listener
        super.show(activity.supportFragmentManager, TAG)
        return this
    }

    fun onLogoutClicked(view: View) {
        mListener!!.onLogout(view, getDialog())
    }

    fun onCancelClicked(view: View) {
        dismiss()
    }

    interface OnLogoutListener {
        fun onLogout(view: View, dialog: Dialog)
    }

    companion object {

        private val TAG = "LogoutDialog"

        @JvmStatic
        val instance: LogoutDialog
            get() {
                val dialog = LogoutDialog()
                dialog.setCanceledBack(false) // 设置屏蔽返回键
                dialog.setCanceledOnTouchOutside(false) // 设置屏蔽对话框点击外部关闭
                dialog.setGravity(Gravity.BOTTOM) // 设置对话框在底部
                dialog.setWidth(1f) // 设置宽度为屏幕宽度
                dialog.setDimEnabled(false) // 设置无黑色透明背景
                return dialog
            }
    }
}
