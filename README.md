# Android-BaseDialog
DialogFragment的基本封装

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)
[![JCenter](https://img.shields.io/badge/%20JCenter%20-1.0.0-5bc0de.svg?style=flat-square)](https://bintray.com/xuehuayous/maven/Android-BaseDialog/_latestVersion)
[![MinSdk](https://img.shields.io/badge/%20MinSdk%20-%2014%2B%20-f0ad4e.svg?style=flat-square)](https://android-arsenal.com/api?level=14)

## 引入

```
implementation 'com.kevin:basedialog:1.0.4'
```

## 使用

1. 继承 `BaseDialog`

    ```java
    public class MyDialog extends BaseDialog {
    }
    ```
    
2. 重写 `createView` 方法，传入布局

    ```java
    public class MyDialog extends BaseDialog {
    
        @Override
        public View createView(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup) {
            return null;
        }
    }
    ```

3. 重写 `onViewCreated` 方法，进行View操作

    ```java
    public class MyDialog extends BaseDialog {   
    
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }
    ```
    
4. 实例化

    ```java
    public class MyDialog extends BaseDialog {   
        private static MyDialog dialog;
    
        public static MyDialog getInstance() {
            if (dialog == null) {
                dialog = new MyDialog();
                dialog.alpha(0.8F) // 设置透明度
                .padding(8,0,8,0) // 设置边距
                .canceledBack(false) // 设置屏蔽返回键
                .canceledOnTouchOutside(false) // 设置屏蔽对话框点击外部关闭
                .width(1.0F) // 设置宽度为屏幕宽度
                .height(1.0F) // 设置高度占比，默认0为包裹内容
                .dimEnabled(false) // 设置无黑色透明背景
                .animations(android.R.style.Animation_InputMethod) // 设置动画
                .gravity(Gravity.BOTTOM); // 设置弹窗位置
            }
            return dialog;
        }
    }
    ```