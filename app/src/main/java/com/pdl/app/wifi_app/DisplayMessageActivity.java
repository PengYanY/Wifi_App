package com.pdl.app.wifi_app;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayMessageActivity extends AppCompatActivity {
    private EditText editText1=null;
    private EditText editText2=null;
    private TextView infoText=null;
    private String WIFIJSONINFO="";  //wifi的json数据
    private List<MyWifiInfo> wifiInfoList=new ArrayList<MyWifiInfo>();
    private WifiManager wifiManager;
    private Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        infoText =(TextView) this.findViewById(R.id.info);
        editText1=(EditText)findViewById(R.id.edittext1);//两个编辑文本框
        editText2=(EditText)findViewById(R.id.edittext2);
        infoText.setTextIsSelectable(true);//设置文本可以复制
        setTimerTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();//销毁时取消定时
    }

    //获取wifi信息函数
    private void getInfo(){
        WIFIJSONINFO="";
        wifiInfoList.clear();
        ArrayList<ScanResult> list; //存放周围wifi热点对象的列表
        wifiManager =(WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();//扫描
        list = (ArrayList<ScanResult>) wifiManager.getScanResults();
        JSONArray jsonArray = new JSONArray();//json对象数组
        for (ScanResult result:list) {

            String wifiname=result.SSID;//名字
            if("NCHU_Wireless".equals(wifiname)){continue;}
            String wifimac=result.BSSID;//mac地址
            int  wifirssi=result.level;//信号强度
            long stamp=(System.currentTimeMillis());

            SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
            String time = format.format(stamp);
            infoText.setText(time+"\n");
            MyWifiInfo wifiInfo=new MyWifiInfo(wifiname,wifirssi,wifimac);
            wifiInfoList.add(wifiInfo);
            try {
                    JSONObject json = new JSONObject();
                    json.put("ssid", wifiname);
                    json.put("pointid", editText1.getText());
                    json.put("tag", editText2.getText());
                    json.put("mac", wifimac);
                    json.put("rssi",wifirssi);
                    json.put("stamp", stamp);
                    jsonArray.put(json);
                }
            catch(Exception e){}


            }
        JSONObject json = new JSONObject();
        try{
            json.put("data",jsonArray);
            WIFIJSONINFO=json.toString();//把获取的json数据存起来
        }
         catch(Exception e){
         }

    }

    //在页面显示获取的各wifi的信息
    private void displayWifiinfo(){
        Collections.sort(wifiInfoList,new WifiInfoSort());
        String Stringwifi="";
        Stringwifi+="pointid:   "+editText1.getText()+"\n";
        Stringwifi+="tag:   "+editText2.getText()+"\n\n\n";
        for (MyWifiInfo wifiInfo:wifiInfoList) {
            Stringwifi+=("SSID:  "+wifiInfo.getSsid()+"\n");
            Stringwifi+=("RSSI:  "+wifiInfo.getRssi()+"\n");
            Stringwifi+=("MAC:  "+wifiInfo.getMac()+"\n");
            Stringwifi+=("\n\n");
        }

        infoText.append(Stringwifi);
        infoText.append(WIFIJSONINFO);
    }


   //发送按钮触发函数
    public void sendInfo(View view) {
        Intent intent = new Intent(this, SendMessageActivity.class);
        intent.putExtra("wifiinfo",WIFIJSONINFO);
        startActivity(intent);
    }

    //定时器任务
    private void setTimerTask() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 1000, 200/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }


   //handler对象处理
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    getInfo();
                    displayWifiinfo();
                    break;
                default:
                    break;
            }
        }
    };


    //infotext="{\"data\":"+jsonArray.toString()+"}";
    //json.put("rssi", URLEncoder.encode("" + wifirssi, "UTF-8"));
    //json.put("frequency", fre);
    //json.put("capabilities", cap);
    //int fre=result.frequency;
    //String cap=result.capabilities;

}




