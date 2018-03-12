package com.pdl.app.wifi_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 发送模块
 */
public class SendMessageActivity extends AppCompatActivity {
    private TextView textview=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        textview= (TextView) findViewById(R.id.textview1);
        Intent intent = getIntent();
        final String message = intent.getStringExtra("wifiinfo");//获取从上一个页面得到的数据
        final TextView textView1=textview;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String  str=httpUrlConnPost(message);
                textView1.post(new Runnable() {
                    @Override
                    public void run() {
                        textView1.setText(str);
                    }
                });
            }
        }).start();
   }


    //发送从上一个activity获取的json数据
    public static String httpUrlConnPost(String ParamStr){
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL("http://108.61.163.82/data");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setUseCaches(false);
            //urlConnection.setFollowRedirects(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(3000);//超时
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();
            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(ParamStr);//把json字符串写入缓冲区中
            bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();//使用完关闭
            if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str1 = null;
                StringBuffer buffer = new StringBuffer();
                while((str1 = br.readLine())!=null){
                    buffer.append(str1);
                }
                in.close();
                br.close();
                //JSONObject rjson = new JSONObject(buffer.toString());
                return buffer.toString();
            }
        } catch (Exception e) {
            return e.toString();

        }finally{
            urlConnection.disconnect();
        }
        return "";
    }


//            OutputStream out = urlConnection.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(out);//缓冲字节流包装字节流
//            byte[] bytes = jsonstr.getBytes("UTF-8");//把字符串转化为字节数组
//            bos.write(bytes);//把这个字节数组的数据写入缓冲区中
//            bos.flush();//刷新缓冲区，发送数据
//            out.close();
//            bos.close();


/*    public static String writeToFile(String message,String filepath){

        File file =new File(filepath);
        FileOutputStream fs=null;
        try {
             fs= new FileOutputStream(file);
              byte[] buffer=new byte[1024];
              buffer=message.getBytes();
              fs.write(buffer,0,buffer.length);
              fs.flush();
              fs.close();
        }
        catch(Exception e){
           return e.toString();
        }
        return "OK";}*/

}




