package com.huaweicloud.sdk.iot.waterqualitytesting.display;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.library.WaveLoadingView;
import com.huaweicloud.sdk.iot.waterqualitytesting.R;
import com.huaweicloud.sdk.iot.waterqualitytesting.util.httpConnect;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import im.dacer.androidcharts.LineView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class detectView extends Fragment implements View.OnClickListener {
    private final static String TAG = "MqttDemo";
    private final String tokenURL = "https://iam.cn-north-4.myhuaweicloud.com/v3/auth/tokens?nocatalog=true";
    private String IAMUserName = "hw93711744";
    private String IAMDoaminId = "hw93711744";
    private String IAMPassword = "xu123123";
    private String region = "cn-north-4";
    private String IOTDAEndpoint = "iotda.cn-north-4.myhuaweicloud.com";
    private String project_id = "5fba52c8b4ec2202e97f3df8";
    private String device_id = "5fba52c8b4ec2202e97f3df8_863866047136536";
    private String token;
    private Button btn_search;
    private Button PH, Conductivity, Turbidity;
    private Spinner deviceList;
    private Context mContext;
    private String ph, tur, con;  //要获取的设备影子值
    private WaveLoadingView mWaveLoadingView;
    private LineView chartView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ArrayList<ArrayList<Float>> attributeData = new ArrayList<>();
    private ArrayList<String> timeData = new ArrayList<>();
    private ArrayList<Float> data_ph=new ArrayList<>();
    private ArrayList<Float> data_tur=new ArrayList<>();
    private ArrayList<Float> data_con=new ArrayList<>();
    private httpConnect connect;
    boolean isConnect = false;
    boolean isPH = false, isTUR = false, isCON = false;
    private View view;
    private Calendar calendars;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detect_view, container, false);
        mContext = view.getContext();
        initView();
        initData();
        deviceListEvent();
        return view;
    }


    private void initData() {
        //执行一次先让数据库有值， 之后不用了
        for (int i=0;i<9;i++){
            timeData.add("0" + String.valueOf(i) + ":00");
        }
        //执行一次 之后不用了
        for (float i=0;i<9;i++){
            data_ph.add(i);
            data_con.add(i);
            data_tur.add(i);
        }
        //从数据库取数据赋初值
        convert(timeData,"time",true);
        convert(data_ph,"data_ph",false);
        convert(data_tur,"data_tur",false);
        convert(data_con,"data_con",false);

    }

    private void initView() {
        PH = (Button) view.findViewById(R.id.btn_PH);
        Conductivity = (Button) view.findViewById(R.id.btn_CON);
        Turbidity = (Button) view.findViewById(R.id.btn_TUR);
        btn_search = (Button) view.findViewById(R.id.btn_search);
        deviceList = (Spinner) view.findViewById(R.id.spineer_device);

        editor = mContext.getSharedPreferences("data", MODE_PRIVATE).edit();
        pref = mContext.getSharedPreferences("data", MODE_PRIVATE);

        chartView = (LineView) view.findViewById(R.id.chartView);


        mWaveLoadingView = (WaveLoadingView) view.findViewById(R.id.waveProgress);
        mWaveLoadingView.setShape(WaveLoadingView.Shape.CIRCLE);
        mWaveLoadingView.setWaveAmplitude(0.2f);
        mWaveLoadingView.setWaveVelocity(0.5f);
        mWaveLoadingView.setWaveBackgroundColor(getResources().getColor(R.color.p_blue));
        mWaveLoadingView.setBorderWidth(60);
        mWaveLoadingView.setBorderColor(getResources().getColor(R.color.p_white));
        mWaveLoadingView.setTextColor(Color.WHITE);
        mWaveLoadingView.setTextSize(100);
        mWaveLoadingView.setTextBold(true); //文字是否使用粗体
        mWaveLoadingView.setTextLocation(WaveLoadingView.Location.CENTER);
        mWaveLoadingView.setWaveColor(getResources().getColor(R.color.p_white));
        btn_search.setOnClickListener(this);
        PH.setOnClickListener(this);
        Conductivity.setOnClickListener(this);
        Turbidity.setOnClickListener(this);
        connect = new httpConnect();
        calendars = Calendar.getInstance();
        calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

    }

    private void deviceListEvent() {
        deviceList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] list = getResources().getStringArray(R.array.device);
                if (i == 0) {
//                    try {
//                        token=connect.POST(tokenURL,MediaType.parse("application/json"),postJSONRequest().toString());
//                        parseMessage(connect.GET("https://"+IOTDAEndpoint+"/v5/iot/"+project_id+"/devices/"+device_id+"/shadow",token));
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    getToken();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    //UI界面刷新显示数据
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (isPH){
                if (ph == null) {
                    ph = pref.getString("ph", "");
                }
                setWaveValue(ph, (int) Float.parseFloat(ph));
                Log.d("Handle: ",ph);
            }
            if (isTUR){
                if (tur == null) {
                    tur = pref.getString("tur", "");
                }
                setWaveValue(tur, ((int) (Float.parseFloat(tur))) / 10);
                Log.d("Handle: ",tur);
            }
            if (isCON){
                if (con == null) {
                    con = pref.getString("con", "");
                }
                setWaveValue(con, (int) Float.parseFloat(con));
                Log.d("Handle: ",con);
            }
            return false;
        }
    });

    private void setWaveValue(String waveText, int processRatio) {
        Log.d("processRatio", String.valueOf(processRatio));
        mWaveLoadingView.setText(waveText);
        mWaveLoadingView.setProcess(processRatio);  //波浪占整个控件的比例，取值范围是0~100
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                break;
            case R.id.btn_PH:
                if (ph == null) {
                    ph = pref.getString("ph", "");
                }
                state(PH, Conductivity, Turbidity, true, false, false, v);
                setWaveValue(ph, (int) Float.parseFloat(ph));
                break;
            case R.id.btn_TUR:
                if (tur == null) {
                    tur = pref.getString("tur", "");
                }
                state(Turbidity, Conductivity, PH, false, true, false, v);
                setWaveValue(tur, ((int) (Float.parseFloat(tur))) / 10);
                break;
            case R.id.btn_CON:
                if (con == null) {
                    con = pref.getString("con", "");
                }
                state(Conductivity, PH, Turbidity, false, false, true, v);
                setWaveValue(con, (int) Float.parseFloat(con));

                break;

        }
    }

    //选取状态变化
    private void state(Button check, Button uncheck1, Button uncheck2, boolean PH, boolean TUR, boolean CON, View v) {
        isPH = PH;
        isTUR = TUR;
        isCON = CON;
        check.setTextColor(getResources().getColor(R.color.blue));
        uncheck1.setTextColor(getResources().getColor(R.color.black));
        uncheck2.setTextColor(getResources().getColor(R.color.black));
        v.setFocusable(true);
        v.requestFocus();
        v.requestFocusFromTouch();

        //从editors取数据还有问题  怎么把String转List<String> ,,,或者改另一个数据库来存取

        //Arrays.asList(str .split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
        //timeData=pref.getString("time","")

        attributeData.clear();
        if (isPH) {
            convert(data_ph,"data_ph",false);
            setChartData(data_ph,ph);   //移位
            attributeData.add(data_ph);
        }
        if (isTUR) {
            convert(data_tur,"data_tur",false);
            setChartData(data_tur,tur);
            attributeData.add(data_tur);
        }
        if (isCON) {
            convert(data_con,"data_con",false);
            setChartData(data_con,con);
            attributeData.add(data_con);
        }
        convert(timeData,"time",true);
        setCharTimeData();
        chartView.setBottomTextList(timeData);  //图表X

        chartView.setFloatDataList(attributeData);//图表y

    }
    //从数据库取出String转List<Integer>
    private void convert(List list,String key,boolean isTime){
        String data=pref.getString(key,"");
        String data_1=data.replace("[","");
        String data_2=data_1.replace("]","");

        String[] list_data=data_2.split(",");

        if (isTime){
            for (int i=0;i<list_data.length;i++){
                list.set(i,list_data[i].trim());
            }
        }else
            for (int i=0;i<list_data.length;i++){
            list.set(i,Float.valueOf(list_data[i].trim()));
        }

//        Log.d("Data:::::::",data+"+"+list_data[0]);
    }
    private void setCharTimeData(){
        String hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(calendars.get(Calendar.MINUTE));
//        String second = String.valueOf(calendars.get(Calendar.SECOND));
        for (int i=0;i<8;i++){
            timeData.set(i,timeData.get(i+1));
        }
        timeData.set(8,hour+":"+min);
    }
    private void setChartData(List<Float> list,String value){

        for (int i=0;i<8;i++){
            list.set(i,list.get(i+1));
        }
        list.set(8,Float.parseFloat(value));

    }

    /**
     * 解析返回数据
     * @param message 传入string类型的数据
     */
    private void parseMessage(String message) {
        try {
            JSONObject jsonObjectF = new JSONObject(message);
            String data_services = jsonObjectF.getString("shadow");
            JSONArray jsonArray = new JSONArray(data_services);
            JSONObject jsonObjectS = jsonArray.getJSONObject(0);
            String data_properties = jsonObjectS.getString("reported");
            JSONObject jsonObjectT = new JSONObject(data_properties);
            String properties = jsonObjectT.getString("properties");
            JSONObject jsonObjectE = new JSONObject(properties);
            ph = jsonObjectE.getString("PH");
            tur = jsonObjectE.getString("turbidity");
            con = jsonObjectE.getString("conductivity");
            editor.putString("ph", ph);
            editor.putString("con", con);
            editor.putString("tur", tur);
            editor.putString("time",timeData.toString());
            editor.putString("data_ph",data_ph.toString());
            editor.putString("data_tur",data_tur.toString());
            editor.putString("data_con",data_con.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //获得返回数据
    private void getResponse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                while (client != null) {
                    try {
                        Thread.sleep(100);
                        Request request = new Request.Builder()
                                .url("https://" + IOTDAEndpoint + "/v5/iot/" + project_id + "/devices/" + device_id + "/shadow")
                                .addHeader("Content-Type", "application/json")
                                .addHeader("X-Auth-Token", token)
                                .get()
                                .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String message = response.body().string();
                                Log.d("Response", message);
                                parseMessage(message);
                                Message ms=new Message();
                                handler.sendMessage(ms);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    //获得JSON格式数据
    private JSONObject postJSONRequest() {
        JSONObject json_all = new JSONObject();
        JSONObject json_auth = new JSONObject();
        JSONObject json_identity = new JSONObject();
        JSONObject json_scope = new JSONObject();
        JSONObject json_password = new JSONObject();
        JSONObject json_user = new JSONObject();
        JSONObject json_domain = new JSONObject();
        JSONObject json_project = new JSONObject();
        JSONArray password = new JSONArray();
        password.put("password");
        try {
            json_all.put("auth", json_auth);
            json_auth.put("identity", json_identity);
            json_identity.put("methods", password);
            json_identity.put("password", json_password);
            json_password.put("user", json_user);
            json_user.put("name", IAMUserName);
            json_user.put("password", IAMPassword);
            json_user.put("domain", json_domain);
            json_domain.put("name", IAMDoaminId);

            json_auth.put("scope", json_scope);
            json_scope.put("project", json_project);
            json_project.put("name", region);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json_auth", json_auth.toString());
        return json_all;
    }

    //通过发送相应JSON格式POST请求到华为云获取token
    private void getToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), postJSONRequest().toString());
                final Request request = new Request.Builder()
                        .url("https://iam.cn-north-4.myhuaweicloud.com/v3/auth/tokens?nocatalog=true")
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .post(requestBody)
                        .build();
//                    Response response=client.newCall(request).execute();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        Headers headers = response.headers();

                        token = headers.get("X-Subject-Token");

                        getResponse();
                        Log.d("toke", response.body().string());

                    }
                });
            }
        }).start();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWaveLoadingView != null) {
            mWaveLoadingView.cancelLoading();
        }
    }


}
