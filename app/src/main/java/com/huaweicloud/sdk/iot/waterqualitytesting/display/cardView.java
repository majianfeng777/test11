package com.huaweicloud.sdk.iot.waterqualitytesting.display;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.huaweicloud.sdk.iot.waterqualitytesting.R;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class cardView extends Fragment {
    private View view;
    private Context mContext;
    private SharedPreferences pref;
    private String ph,con,tur;
    private String[] Title={"PH：","TUR：","CON："};
    private String[] Data={"","",""};
    private String[] Context={"当前PH良好","当前TUR良好","当前CON良好"};
//    private int[] Images={R.drawable.welcome,R.drawable.barchar,R.drawable.sport,R.drawable.calendar};
//
//    private int[] Content={R.drawable.car1,R.drawable.car2,R.drawable.car3,R.drawable.car1};

    //将数据封装成数据源
    List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.display_view, container, false);
        mContext = view.getContext();
        pref = mContext.getSharedPreferences("data", MODE_PRIVATE);
        initData();

        ListView listview=(ListView) view.findViewById(R.id.list_card);
       
//        cardView.setCardBackgroundColor();
        listview.setAdapter(new MyAdapter());
        return view;
    }

    private void initData() {
        ph = pref.getString("ph", "0.0");
        tur = pref.getString("tur", "0.0");
        con = pref.getString("con", "0.0");
        Data[0]=ph;
        Data[1]=tur;
        Data[2]=con;
        if (Float.parseFloat(ph)>=6.5&&8.5<=Float.parseFloat(ph)){
            Context[0]="当前PH良好";
        }
        if (Float.parseFloat(ph)<6.5){
            Context[0]="当前PH呈酸性";
        }
        if (Float.parseFloat(ph)>8.5){
            Context[0]="当前PH呈碱性";
        }

        //将数据封装成数据源
        for(int i=0;i<Title.length;i++){
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("title",Title[i]);
            map.put("data", Data[i]);
            map.put("context",Context[i]);
//            map.put("img",Images[i]);
//            map.put("content",Content[i]);
            list.add(map);
        }
    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder mHolder;
            CardView cardView = null;
            if(convertView==null){
                view= LayoutInflater.from(mContext).inflate(R.layout.list_item_card,null);
                mHolder=new ViewHolder();
                cardView=(CardView)view.findViewById(R.id.cardView);
                
                mHolder.card_title=(TextView)view.findViewById(R.id.cardTitle);
                mHolder.card_data=(TextView)view.findViewById(R.id.cardData);
                mHolder.card_context=(TextView)view.findViewById(R.id.cardcontext);
                view.setTag(mHolder);  //将ViewHolder存储在View中
            }else {
                view=convertView;
                mHolder=(ViewHolder)view.getTag();  //重新获得ViewHolder
            }
            cardView.setRadius(15);
            cardView.setCardElevation(8);//设置阴影部分大小
            cardView.setPadding(15,15,15,15);
            mHolder.card_title.setText(list.get(position).get("title").toString());
            mHolder.card_data.setText(list.get(position).get("data").toString());
            mHolder.card_context.setText(list.get(position).get("context").toString());
            return view;
        }

    }

    class ViewHolder{
        TextView card_title;
        TextView card_data;
        TextView card_context;
    }
}