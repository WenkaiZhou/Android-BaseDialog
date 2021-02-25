# Android-BaseDialog
DialogFragment的封装

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)
[![JCenter](https://img.shields.io/badge/%20JCenter%20-1.1.0-5bc0de.svg?style=flat-square)](https://bintray.com/xuehuayous/maven/Android-BaseDialog/_latestVersion)
[![MinSdk](https://img.shields.io/badge/%20MinSdk%20-%2014%2B%20-f0ad4e.svg?style=flat-square)](https://android-arsenal.com/api?level=14)

## 引入

```
implementation 'com.kevin:basedialog:1.1.0'
```

## 说明

BaseDialog对Dialog进行了常用方法的封装，具体如下：

方法 | 说明 | 默认值
--- | --- | ----
setGravity(gravity: Int) | 设置设置对话框位置 | Gravity.CENTER
setCanceledOnTouchOutside(cancel: Boolean) | 设置是否触摸外部关闭 | true
setCanceledBack(cancel: Boolean) | 是否是否返回键关闭 | true
setWidth(width: Int) | 设置宽度 | 0
setHeight(height: Int) | 设置高度 | 0
setWidthRatio(widthRatio: Float) | 设置宽度占屏幕百分比 | 0.9
setHeightRatio(heightRatio: Float) | 设置高度占屏幕百分比 | 0 (包裹内容)
setOffsetY(offsetY: Float) | 设置Y方向偏移百分比 | 0
setPadding(left: Int, top: Int, right: Int, bottom: Int) | 设置边距 | 0, 0, 0, 0
setAnimations(animStyle: Int) | 设置动画 | 0 (无动画)
setDimEnabled(dimEnabled: Boolean) | 设置背景是否阴影 | true
setBackgroundColor(@ColorInt color: Int) | 设置对话框背景颜色 | Color.TRANSPARENT
setRadius(radius: Int) | 设置四周圆角弧度 | 0
setLeftTopRadius(radius: Int) | 设置左上圆角弧度 | 0
setRightTopRadius(radius: Int) | 设置右上圆角弧度 | 0
setLeftBottomRadius(radius: Int) | 设置左下圆角弧度 | 0
setRightBottomRadius(radius: Int) | 设置右下圆角弧度 | 0
setAlpha(alpha: Float) | 设置透明度 | 1.0
setX(x: Int) | 设置对话框在X方向偏移量 | 0
setY(y: Int) | 设置对话框在Y方向偏移量 | 0

## 使用

### 原始方式

1. 继承 `BaseDialog`, 复写 `createView` 方法，传入布局

    ```kotlin
    class LogoutDialog : BaseDialog() {

        override fun createView(context: Context?, inflater: LayoutInflater, container: ViewGroup?): View {
            return inflater.inflate(R.layout.layout_dialog_logout, container, false)
        }
    }
    ```
    
2. 使用`Builder`模式进行初始化

    ```kotlin
    class LogoutDialog : BaseDialog() {
 
        // ... ...
     
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
                    .setWidthRatio(1f) // 设置宽度为屏幕宽度
                    .setDimEnabled(false) // 设置无黑色透明背景
                    .setAnimations(android.R.style.Animation_InputMethod) // 设置动画
                dialog.builder = this
                return dialog
            }
        }
    }

    ```

3. 重写 `onViewCreated` 方法，进行View操作

    ```kotlin
    class LogoutDialog : BaseDialog() {
    
        private var builder: Builder? = null
     
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            this.retainInstance = true
            if(builder == null) {
                dismiss()
            }
        }
    
        // ... ...
    
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            // 设置按钮点击的监听
            view.findViewById<TextView>(R.id.logout_ok).setOnClickListener {
                builder!!.logoutListener?.invoke(this, view)
            }
        }
    }
    ```

4. 添加 `show` 方法，用于显示弹窗

    ```kotlin
    class LogoutDialog : BaseDialog() {
    
        // ... ...
    
        fun show(): LogoutDialog {
            super.show(builder!!.activity.supportFragmentManager, TAG)
            return this
        }
    
        // ... ...
    
        companion object {
            private const val TAG = "LogoutDialog"
        }
    }
    ```
    
5. 使用

    ```kotlin
    class MainActivity : AppCompatActivity() {
 
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
      
         LogoutDialog.Builder(this)
             .setOnLogoutListener { dialog, view ->
                 Toast.makeText(this@MainActivity, "点击了退出登录", Toast.LENGTH_SHORT).show()
                 dialog.dismiss()
             }
             .build()
             .show()
        }
     }
    ```

### dataBinding方式

1. 继承 `BaseDialog`, 复写 `createView` 方法

    ```kotlin
    class LogoutDialog : BaseDialog() {
 
        override fun createView(context: Context?, inflater: LayoutInflater, container: ViewGroup?): View {
            val binding = LogoutDialogBinding.inflate(LayoutInflater.from(getContext()), container, false)
            return binding.root
        }
    }
    ```
    
2. 使用`Builder`模式进行初始化，同原始方式
   
3. 在XML中设置点击的监听，在代码中增加处理方法

    ```xml
    <Button
        android:id="@+id/logout_ok"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/btn_bg_orange"
        android:onClick="@{view::onLogoutClicked}"
        android:text="退出登录"
        android:textColor="@color/c_e9ad44" />
    ```
    
    ```kotlin
    class LogoutDialog : BaseDialog() {
        // ... ...
     
        fun onLogoutClicked(view: View) {
            builder!!.logoutListener?.invoke(this, view)
        }
        
        // ... ...
    }
    ```

4. 添加 `show` 方法，用于显示弹窗，同原始方式

5. 使用，同原始方式


## License

```text
Copyright 2019 Kevin zhou

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
