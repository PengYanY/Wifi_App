package com.pdl.app.wifi_app;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WifiManager mWifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openWifi(this.getApplicationContext());

    }

    // 打开WIFI
    public void openWifi(Context context) {
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (mWifiManager!=null&&!mWifiManager.isWifiEnabled()) {//wifi是否打开的判断
            MyDialog();
            Toast.makeText(MainActivity.this,"请稍等....",Toast.LENGTH_LONG).show();

        }else if (mWifiManager!=null&&mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            Toast.makeText(context,"亲，Wifi正在开启，不用再开了", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(context,"亲，Wifi已经开启,不用再开了", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
            startActivity(intent);
        }
    }



    //打开wifi的对话框
    private void MyDialog(){
        Dialog dialog=new AlertDialog.Builder(MainActivity.this).setMessage("是否打开WIFI？")
                .setPositiveButton("是",new  DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialog,int whichButton){
                 mWifiManager.setWifiEnabled(true);
                 Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                 startActivity(intent);
            }
        }).setNegativeButton("否",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int whichButton){
                MainActivity.this.finish();
            }
        }).create();
        dialog.show();
    }
}
