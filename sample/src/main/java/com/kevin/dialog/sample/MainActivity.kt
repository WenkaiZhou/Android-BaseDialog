package com.kevin.dialog.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.kevin.delegationadapter.DelegationAdapter
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        // ① 设置 LayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // ② 创建 DelegationAdapter 对象
        val delegationAdapter = DelegationAdapter()
        // ③ 向Adapter中注册委托Adapter
        delegationAdapter.addDelegate(TextAdapterDelegate(this))
        // ④ 设置Adapter
        recyclerView.adapter = delegationAdapter
        val companies: MutableList<String?> =
            ArrayList()
        companies.add("显示自定义弹窗")
        companies.add("显示CommonDialog")
        companies.add("显示CommonDialog自定义布局")
        // ⑤ 设置数据
        delegationAdapter.setDataItems(companies)
    }

    fun onItemClick(view: View, item: String, position: Int) {
        when (position) {
            0 -> {
                LogoutDialog.Builder(this)
                    .setOnLogoutListener { dialog, view ->
                        Toast.makeText(this@MainActivity, "点击了退出登录", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .build()
                    .show()

            }
            1 -> {
                CommonDialog.Builder(this)
                    .setTitle("您确定不去⛷吗？")
                    .setContent("其实🏂真的非常好玩，你不去吗？")
                    .setCanceledOnTouchOutside(false)
                    .setPositiveButton("确定") {
                        it.dismiss()
                    }
                    .setNegativeButton("取消") {
                        it.dismiss()
                    }
                    .build()
                    .show()
            }
            2 -> {
                CommonDialog.Builder(this)
                    .setLayoutRes(R.layout.layout_dialog_logout)
                    .setTitle("您确定不去⛷吗？")
                    .setContent("其实🏂真的非常好玩，你不去吗？")
                    .setCanceledOnTouchOutside(false)
                    .setGravity(Gravity.BOTTOM)
                    .setWidthRatio(1.0f)
                    .setBackgroundColor(Color.TRANSPARENT)
                    .onBindView { dialog, view ->
                        view.findViewById<ImageButton>(R.id.but_cancel).setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                    .build()
                    .show()
            }
            else -> {
            }
        }
    }
}