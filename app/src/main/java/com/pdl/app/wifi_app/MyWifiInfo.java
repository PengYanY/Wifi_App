package com.pdl.app.wifi_app;

public class MyWifiInfo {

	private String ssid;
	private int rssi;
	private String mac;
	
	public MyWifiInfo(){}
	
	public MyWifiInfo(String ssid, int rssi, String mac) {
		super();
		this.ssid = ssid;
		this.rssi = rssi;
		this.mac = mac;
	}
	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
