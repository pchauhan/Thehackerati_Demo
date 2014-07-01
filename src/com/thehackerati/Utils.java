package com.thehackerati;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Utils {
	public static Custom_Object custom_Object ;
	public static ArrayList<Custom_Object> arr_live_Feed = new ArrayList<Custom_Object>(); 
	public static String getResponseText(String url){
		String		xmlString = null ;
		try {
			HttpGet httpget = new HttpGet(url);      
			System.out.println("URL hit="+url);
			HttpClient httpclient = new DefaultHttpClient();
			BasicHttpResponse httpResponse =   (BasicHttpResponse) httpclient.execute(httpget);
			HttpEntity r_entity = httpResponse.getEntity();
			xmlString = EntityUtils.toString(r_entity);
		}catch(Exception e){
			System.out.println("Exception="+e);
		}
		return xmlString ;
	}
	public static final boolean isNetwork(Context context) {
		boolean isFlag = false ;
		ConnectivityManager manager = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
		//For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();

		//For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		System.out.println(is3g + " net " + isWifi);
		if (!is3g && !isWifi) {
			isFlag = false ;
			//	Toast.makeText(context,"Please make sure your Network Connection is ON ",Toast.LENGTH_LONG).show();
		} else { 
			//" Your method what you want to do "
			isFlag = true ;
		} 
		return isFlag ;
	}

	public static void openToast(Context applicationContext, String string) {
		// TODO Auto-generated method stub
		Toast.makeText(applicationContext, string, 1000).show();
	}
}
