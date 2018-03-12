package com.pdl.app.wifi_app;

import java.util.Comparator;

public class WifiInfoSort  implements Comparator<MyWifiInfo> {


  //自定义的对象比较大小函数
@Override
public int compare(MyWifiInfo wifi1, MyWifiInfo wifi2) {
	return -(wifi1.getRssi()-wifi2.getRssi());
}

}
