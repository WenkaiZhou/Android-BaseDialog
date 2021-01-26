/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.kevin.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

/**
 * NoLeakDialog
 *
 * @author zhouwenkai@baidu.com, Created on 2021-01-26 19:32:00
 *         Major Function：<b>Fix the leak memory bug.</b>
 *         <p/>
 *         Note: If you modify this class please fill in the following content as a record.
 * @author mender，Modified Date Modify Content:
 */

internal class NoLeakDialog (context: Context, themeResId: Int) : Dialog(context, themeResId) {
    private val listenerHandler = ListenersHandler(this)

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
        if (listener != null) {
            val message = Message.obtain(listenerHandler, CANCEL)
            listenerHandler.setOnCancelListener(listener)
            setCancelMessage(message)
            return
        }
        listenerHandler.setOnCancelListener(null)
        setCancelMessage(null)
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        if (listener != null) {
            listenerHandler.setOnDismissListener(listener)
            setDismissMessage(Message.obtain(listenerHandler, DISMISS))
            return
        }
        listenerHandler.setOnDismissListener(null)
        setDismissMessage(null)
    }

    private class ListenersHandler(dialog: Dialog) : Handler() {
        private val dialog: WeakReference<DialogInterface> = WeakReference(dialog)
        private var onCancelListener: WeakReference<DialogInterface.OnCancelListener>? = null
        private var onDismissListener: WeakReference<DialogInterface.OnDismissListener>? = null

        override fun handleMessage(message: Message) {
            when (message.what) {
                DISMISS -> {
                    onDismissListener?.get()?.onDismiss(dialog.get())
                }
                CANCEL -> {
                    onCancelListener?.get()?.onCancel(dialog.get())
                }
            }
        }

        fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
            if (listener == null) {
                onCancelListener = null
                return
            }
            onCancelListener = WeakReference(listener)
        }

        fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
            if (listener == null) {
                onDismissListener = null
                return
            }
            onDismissListener = WeakReference(listener)
        }
    }

    companion object {
        private const val DISMISS = 0x57
        private const val CANCEL = 0x58
    }
}