package com.huaweicloud.sdk.iot.waterqualitytesting.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.huaweicloud.sdk.iot.waterqualitytesting.R;

import java.util.List;

public class listViewInfor_adapter extends ArrayAdapter<listViewInfor_item> {
    private int resourceId;
    public listViewInfor_adapter(@NonNull Context context, int resource, List<listViewInfor_item> list) {
        super(context, resource,list);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        listViewInfor_item item=getItem(position);
        View view=null;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.text_infor=(TextView) view.findViewById(R.id.text_infor);
            viewHolder.text_infornum=(TextView)view.findViewById(R.id.text_infornum);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        if (item.getText_infor()==null||item.getText_infornum()==null){
            viewHolder.text_infor.setText("");
            viewHolder.text_infornum.setText("");
        }

        viewHolder.text_infor.setText(item.getText_infor());
        viewHolder.text_infornum.setText(item.getText_infornum());
        return view;
    }

    public class ViewHolder {
        TextView text_infor,text_infornum;
    }
}
