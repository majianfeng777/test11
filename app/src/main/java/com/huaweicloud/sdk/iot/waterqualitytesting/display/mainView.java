package com.huaweicloud.sdk.iot.waterqualitytesting.display;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.huaweicloud.sdk.iot.waterqualitytesting.R;
import com.huaweicloud.sdk.iot.waterqualitytesting.list.informationView;


public class mainView extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView icon_testing,icon_data,icon_info;
    private TextView test_testing,test_data,test_info;
    private LinearLayout btn_test,btn_data,btn_info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.hide();
        setContentView(R.layout.main_view);
        replaceFragment(new detectView());
        initView();
    }

    private void initView() {
        title=(TextView)findViewById(R.id.title_text);
        icon_testing=(ImageView) findViewById(R.id.image_test);
        icon_data=(ImageView) findViewById(R.id.image_data);
        icon_info=(ImageView) findViewById(R.id.image_info);
        test_testing=(TextView) findViewById(R.id.test_testing);
        test_data=(TextView) findViewById(R.id.test_data);
        test_info=(TextView) findViewById(R.id.test_info);
        btn_test=(LinearLayout)findViewById(R.id.btn_test);
        btn_data=(LinearLayout)findViewById(R.id.btn_data);
        btn_info=(LinearLayout)findViewById(R.id.btn_info);
        btn_test.setOnClickListener(this);
        btn_data.setOnClickListener(this);
        btn_info.setOnClickListener(this);
    }
    private void state(ImageView icon_check,int resouceid_check,ImageView icon_uncheck,int resouceid_uncheck,ImageView icon_uncheck1,int resouceid_uncheck1, TextView test_check,TextView test_uncheck,TextView test_uncheck1) {

        icon_check.setBackgroundResource(resouceid_check);
        icon_uncheck.setBackgroundResource(resouceid_uncheck);
        icon_uncheck1.setBackgroundResource(resouceid_uncheck1);
        test_check.setTextColor(getResources().getColor(R.color.blue));
        test_uncheck.setTextColor(getResources().getColor(R.color.gray_test));
        test_uncheck1.setTextColor(getResources().getColor(R.color.gray_test));
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_test:
                state(icon_testing,R.drawable.icon_test_check,icon_data,R.drawable.icon_data,icon_info,R.drawable.icon_info,test_testing,test_data,test_info);
                replaceFragment(new detectView());
                title.setText("实时检测");
                break;
            case R.id.btn_data:
                state(icon_data,R.drawable.icon_data_check,icon_testing,R.drawable.icon_test,icon_info,R.drawable.icon_info,test_data,test_testing,test_info);
                replaceFragment(new cardView());
                title.setText("水质数据");
                break;
            case R.id.btn_info:
                state(icon_info,R.drawable.icon_info_check,icon_data,R.drawable.icon_data,icon_testing,R.drawable.icon_test,test_info,test_data,test_testing);
                replaceFragment(new informationView());
                title.setText("个人中心");
                break;
        }
    }
}
