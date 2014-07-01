package com.thehackerati;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
public class Favorite_List_Activity extends Activity{
	ListView listViewFav ;
	MyFav_Adapter myFav_Adapter ;
	ArrayList<Custom_Object> arr_Myfav_Feed = new ArrayList<Custom_Object>();
	ProgressDialog pbarDialog;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	TextView tvtitle,tvfavorite ;
	boolean isAsynchCalled = true ;
	DatabaseConnector databaseConnector ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apple_feed_list_activity);
		databaseConnector=new DatabaseConnector(Favorite_List_Activity.this);
		databaseConnector.open();
		
		pbarDialog = new ProgressDialog(this);
		pbarDialog.setMessage(getResources().getString(R.string.pleasewait));
		pbarDialog.setCancelable(false);
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(0))
		.build();
		tvtitle = (TextView)findViewById(R.id.tvtitle);
		tvtitle.setText("My Favorite List");
		tvfavorite = (TextView)findViewById(R.id.tvfavorite);
		tvfavorite.setVisibility(View.GONE);
		listViewFav = (ListView)findViewById(R.id.listViewUser);
		listViewFav.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				Custom_Object custom_Obj = (Custom_Object)adapter.getItemAtPosition(position);
				Utils.custom_Object = custom_Obj ;
				Intent sendMessageActivity = new Intent(getApplicationContext(),Apple_feed_List_details_activity.class);
				startActivity(sendMessageActivity);
				overridePendingTransition(R.anim.slide_in_right,  R.anim.slide_out_left);
			}
		});

		myFav_Adapter = new MyFav_Adapter(Favorite_List_Activity.this, arr_Myfav_Feed);
		listViewFav.setAdapter(myFav_Adapter);

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//arr_Myfav_Feed = Utils.arr_live_Feed ;
		arr_Myfav_Feed.clear();
		HashMap<Integer, Integer> hashMapMyFavorite = databaseConnector.getMyFavorite();
		int size = Utils.arr_live_Feed.size()  ;
		for(int i=0;i<size;i++){
			Custom_Object custom_Object = Utils.arr_live_Feed.get(i);
			if(hashMapMyFavorite.containsKey(custom_Object.id)){
				arr_Myfav_Feed.add(custom_Object);
			}
		}
		myFav_Adapter.updateList(arr_Myfav_Feed);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	public class MyFav_Adapter extends BaseAdapter{
		Activity activity ;
		ArrayList<Custom_Object> arr_MyFav ;
		ViewHolder holder = null;
		LayoutInflater inflater ;
		public MyFav_Adapter(Activity activity,ArrayList<Custom_Object> arr_MyFav) {
			// TODO Auto-generated constructor stub
			inflater = activity.getLayoutInflater();
			this.activity = activity ;
			this.arr_MyFav = arr_MyFav ;
		}
		public void updateList(ArrayList<Custom_Object> arr_MyFav){
			this.arr_MyFav = arr_MyFav ;
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arr_MyFav.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			Custom_Object wallPage = arr_MyFav .get(position);
			return wallPage;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.apple_list_item, null);
				holder.tvname = (TextView)convertView.findViewById(R.id.tvname);
				holder.tvartist = (TextView)convertView.findViewById(R.id.tvartist);
				holder.tvprice = (TextView)convertView.findViewById(R.id.tvprice);
				holder.imgthumb = (ImageView)convertView.findViewById(R.id.imgthumb);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			Custom_Object custom_Object = arr_MyFav.get(position);
			holder.tvname.setText(custom_Object.name);
			holder.tvartist.setText(custom_Object.artist);
			holder.tvprice.setText(custom_Object.price);
			imageLoader.displayImage(custom_Object.icon, holder.imgthumb , options);
			return convertView;
		}
		public class  ViewHolder{
			public TextView tvname  ,tvartist,tvprice ;
			public ImageView imgthumb ;
		}
	}
}
