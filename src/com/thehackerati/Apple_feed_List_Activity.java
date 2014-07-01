package com.thehackerati;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonWriter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class Apple_feed_List_Activity extends Activity {
	ListView listViewUser ;
	Live_feed_Adpater live_feed_Adpater ;
	ArrayList<Custom_Object> arr_live_Feed = new ArrayList<Custom_Object>();
	ProgressDialog pbarDialog;
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	TextView tvtitle,tvfavorite ;

	DatabaseConnector databaseConnector ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apple_feed_list_activity);

		databaseConnector=new DatabaseConnector(Apple_feed_List_Activity.this);
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
		tvfavorite = (TextView)findViewById(R.id.tvfavorite);
		listViewUser = (ListView)findViewById(R.id.listViewUser);

		listViewUser.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				//With user name and its token it will pass to send message
				Custom_Object custom_Obj = (Custom_Object)adapter.getItemAtPosition(position);
				Utils.custom_Object = custom_Obj ;
				Intent sendMessageActivity = new Intent(getApplicationContext(),Apple_feed_List_details_activity.class);
				startActivity(sendMessageActivity);
				overridePendingTransition(R.anim.slide_in_right,  R.anim.slide_out_left);
			}
		});
		tvfavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.arr_live_Feed = arr_live_Feed ;
				Intent sendMessageActivity = new Intent(getApplicationContext(),Favorite_List_Activity.class);
				startActivity(sendMessageActivity);
				overridePendingTransition(R.anim.slide_in_right,  R.anim.slide_out_left);
			}
		});
		new DownloadList_Data().execute();

	}
	public class DownloadList_Data extends AsyncTask<Void, Void, Void>{
		String title  ;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String responseFromServer = Utils.getResponseText("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topgrossingapplications/sf=143441/limit=25/json");
			try {
				JSONObject jsonObject = new JSONObject(responseFromServer).getJSONObject("feed");
				title = jsonObject.getJSONObject("title").getString("label");
				JSONArray jsonFeedArray = jsonObject.getJSONArray("entry");
				if(jsonFeedArray != null && jsonFeedArray.length()>0){
					int size = jsonFeedArray.length();
					for(int i=0;i<size ;i++){
						JSONObject jsonObj_feed = jsonFeedArray.getJSONObject(i);
						Custom_Object custom_Object = new Custom_Object();
						custom_Object.name = jsonObj_feed.getJSONObject("im:name").getString("label");
						custom_Object.artist = jsonObj_feed.getJSONObject("im:artist").getString("label");
						custom_Object.link = jsonObj_feed.getJSONObject("link").getJSONObject("attributes").getString("href");
						JSONArray jsonArr_Image = jsonObj_feed.getJSONArray("im:image");
						if(jsonArr_Image != null && jsonArr_Image.length()>0){
							int image_size = jsonArr_Image.length();
							for(int j=0;j<image_size;j++){
								JSONObject jsonObj_Image = jsonArr_Image.getJSONObject(j);
								if(j==0){
									custom_Object.icon = jsonObj_Image.getString("label");
								}else if(j==2){
									custom_Object.icon_Details = jsonObj_Image.getString("label");
								}
							}
						}
						custom_Object.summary = jsonObj_feed.getJSONObject("summary").getString("label");
						custom_Object.price = jsonObj_feed.getJSONObject("im:price").getString("label");
						custom_Object.category = jsonObj_feed.getJSONObject("category").getJSONObject("attributes").getString("label");
						custom_Object.id = jsonObj_feed.getJSONObject("id").getJSONObject("attributes").getInt("im:id");
						custom_Object.releaseDate = jsonObj_feed.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label");

						arr_live_Feed.add(custom_Object);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Exception in json parser="+e);
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pbarDialog.show();
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			pbarDialog.dismiss();
			tvtitle.setText(title);
			live_feed_Adpater = new Live_feed_Adpater(Apple_feed_List_Activity.this, arr_live_Feed);
			listViewUser.setAdapter(live_feed_Adpater);
			super.onPostExecute(result);
		}
	}
	public class Live_feed_Adpater extends BaseAdapter{
		Activity activity ;
		ArrayList<Custom_Object> arr_live_Feed ;
		ViewHolder holder = null;
		LayoutInflater inflater ;
		public Live_feed_Adpater(Activity activity,ArrayList<Custom_Object> arr_live_Feed) {
			// TODO Auto-generated constructor stub
			inflater = activity.getLayoutInflater();
			this.activity = activity ;
			this.arr_live_Feed = arr_live_Feed ;
		}
		public void updateList(ArrayList<Custom_Object> arr_live_Feed){
			this.arr_live_Feed = arr_live_Feed ;
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arr_live_Feed.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			Custom_Object wallPage = arr_live_Feed .get(position);
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
			Custom_Object custom_Object = arr_live_Feed.get(position);
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
