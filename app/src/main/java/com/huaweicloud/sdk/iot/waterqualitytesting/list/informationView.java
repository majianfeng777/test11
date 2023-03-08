package com.huaweicloud.sdk.iot.waterqualitytesting.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huaweicloud.sdk.iot.waterqualitytesting.R;

import java.util.ArrayList;
import java.util.List;

public class informationView extends Fragment {
    private List<listViewInfor_item> list=new ArrayList<>();
    private String IAMUserName = "hw93711744";
    private String IAMDoaminId = "hw93711744";
    private String IAMPassword = "xu123123";
    private String project_id = "iotbysj";
    private String device_id = "iot";
    private String region = "cn-north-4";
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.listview_info,container,false);
//        Intent intent=getIntent();
//        admin=intent.getStringExtra("isAdmin");
//        account=intent.getStringExtra("account");
//        password=intent.getStringExtra("password");
        initData();
        init();
        return view;
    }

    private void initData() {
        listViewInfor_item item_a=new listViewInfor_item("IAMUserName:",IAMUserName);
        list.add(item_a);
        listViewInfor_item item_b=new listViewInfor_item("IAMDoaminId:",IAMDoaminId);
        list.add(item_b);
        listViewInfor_item item_c=new listViewInfor_item("IAMPassword:",IAMPassword);
        list.add(item_c);
        listViewInfor_item item_d=new listViewInfor_item("project_id:",project_id);
        list.add(item_d);
        listViewInfor_item item_e=new listViewInfor_item("device_id:",device_id);
        list.add(item_e);
        listViewInfor_item item_f=new listViewInfor_item("region:",region);
        list.add(item_f);

    }

    private void init() {
        ListView listView=view.findViewById(R.id.listview_infor);
        listViewInfor_adapter listViewInfor_adapter=new listViewInfor_adapter(view.getContext(),R.layout.list_item_infor,list);
        listView.setAdapter(listViewInfor_adapter);
    }

}
