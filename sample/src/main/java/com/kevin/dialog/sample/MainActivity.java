package com.kevin.dialog.sample;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.kevin.delegationadapter.AdapterDelegate;
import com.kevin.delegationadapter.DelegationAdapter;
import com.kevin.delegationadapter.extras.ClickableAdapterDelegate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // ① 设置 LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // ② 创建 DelegationAdapter 对象
        DelegationAdapter delegationAdapter = new DelegationAdapter();
        // ③ 向Adapter中注册委托Adapter
        delegationAdapter.addDelegate(new TextAdapterDelegate(this));
        // ④ 设置Adapter
        recyclerView.setAdapter(delegationAdapter);

        List<String> companies = new ArrayList<>();
        companies.add("显示自定义弹窗");
        // ⑤ 设置数据
        delegationAdapter.setDataItems(companies);

    }

    public void onItemClick(@NotNull View view, @NotNull String item, int position) {
        switch (position) {
            case 0:
                LogoutDialog.getInstance().show(MainActivity.this, new LogoutDialog.OnLogoutListener() {
                    @Override
                    public void onLogout(@NotNull View view, @NotNull Dialog dialog) {
                        Toast.makeText(MainActivity.this, "点击了退出", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }
}
