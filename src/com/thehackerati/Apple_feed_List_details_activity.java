package com.thehackerati;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class Apple_feed_List_details_activity extends Activity{
	public TextView tvname  ,tvartist,tvprice,tvsummary ,tvreleasedate,tvaddtofavorite,tvshare;
	public ImageView imgthumb ;
	Custom_Object custom_Object ;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	DatabaseConnector databaseConnector ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apple_feed_list_details);
		
		databaseConnector=new DatabaseConnector(Apple_feed_List_details_activity.this);
		databaseConnector.open();
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(0))
		.build();
		custom_Object = Utils.custom_Object;
		tvname = (TextView)findViewById(R.id.tvname);
		tvaddtofavorite  = (TextView)findViewById(R.id.tvaddtofavorite);
		tvshare = (TextView)findViewById(R.id.tvshare);
		tvreleasedate = (TextView)findViewById(R.id.tvreleasedate);
		tvartist = (TextView)findViewById(R.id.tvartist);
		tvprice = (TextView)findViewById(R.id.tvprice);
		tvsummary = (TextView)findViewById(R.id.tvsummary);
		imgthumb = (ImageView)findViewById(R.id.imgthumb);
		
		tvname.setSelected(true);
		tvartist.setSelected(true);
		tvname.setText(custom_Object.name);
		tvartist.setText(custom_Object.artist);
		tvprice.setText(custom_Object.price);
		tvreleasedate .setText(custom_Object.releaseDate);
		imageLoader.displayImage(custom_Object.icon_Details, imgthumb , options);
		tvsummary.setText(custom_Object.summary);
		
		if(databaseConnector.isFavorite(custom_Object.id)){
			tvaddtofavorite.setText("Unfavorite");
			tvaddtofavorite.setTextColor(Color.parseColor(getString(R.color.themetextcolorhighlighted)));
		}else{
			tvaddtofavorite.setText("Add to favorite");
			tvaddtofavorite.setTextColor(Color.parseColor(getString(R.color.themebackgroundcolor)));
		}
		tvaddtofavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(databaseConnector.isFavorite(custom_Object.id)){
					databaseConnector.deleteentry_Id(custom_Object.id);
					tvaddtofavorite.setText("Add to favorite");
					tvaddtofavorite.setTextColor(Color.parseColor(getString(R.color.themebackgroundcolor)));
				}else{
					databaseConnector.insertentry_Id(custom_Object.id);
					tvaddtofavorite.setText("Unfavorite");
					tvaddtofavorite.setTextColor(Color.parseColor(getString(R.color.themetextcolorhighlighted)));
					
				}
			}
		});
		tvshare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    String subject ="Hey I am using " +custom_Object.name +" - "+custom_Object.artist;
			    String text =  custom_Object.summary +"\n You can download from \n"+custom_Object.link ;
			    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			    sharingIntent.setType("text/plain");
			    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
			    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
			    startActivity(Intent.createChooser(sharingIntent, "Share"));
			}
		});
		tvsummary.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				copyToClipboard(getApplicationContext(), tvsummary.getText().toString().trim());
				Utils.openToast(getApplicationContext(),"Copied Successfully...");
				return false;
			}
		});

	}
	public void copyToClipboard(Context context, String text) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
					.getSystemService(context.CLIPBOARD_SERVICE);
			clipboard.setText(text);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
					.getSystemService(context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData
					.newPlainText("Copied Successfully...", text);
			clipboard.setPrimaryClip(clip);
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		super.onBackPressed();
	}
}
