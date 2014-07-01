package com.thehackerati;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity{
	Handler handler ;
	Runnable runnable ;
	String regId ;
	final int SPLASH_DURATION = 2000 ;
	static SharedPreferences sharedPref ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		sharedPref = getSharedPreferences(getResources().getString(R.string.app_name), 0);
		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				Intent 	userListActivity = new Intent(getApplicationContext(), Apple_feed_List_Activity.class);
				startActivity(userListActivity);
				finish();
				overridePendingTransition(R.anim.slide_in_right,  R.anim.slide_out_left);
			}
		};
		//Splash screen will be wait for Two minite
		if(Utils.isNetwork(getApplicationContext())){
			handler.postDelayed(runnable, SPLASH_DURATION);
		}else{
			Utils.openToast(getApplicationContext(), getString(R.string.no_network_connection_toast));
			
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		handler.removeCallbacks(runnable);
		finish();
	}
}
