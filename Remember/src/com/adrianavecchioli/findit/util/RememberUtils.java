package com.adrianavecchioli.findit.util;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adrianavecchioli.findit.AuthActivity;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.service.SynchronizedItemsService;

public class RememberUtils {

	
	private static String DATA_SEPARATOR=";";
	private static String DATA_NONE="";
	public static String EVERY_THING="everything";
	public static Location convertStringToLocation(String data){
		if(DATA_NONE.equalsIgnoreCase(data)){
			return null;
		}
		Location location=null;
		try{
			String items []=data.split(DATA_SEPARATOR);
			location=new Location(LocationManager.GPS_PROVIDER);
			double latitude=Double.parseDouble(items[0]);
			location.setLatitude(latitude);
			
			double longitude=Double.parseDouble(items[1]);
			location.setLongitude(longitude);
			
			double altitude=Double.parseDouble(items[2]);
			location.setAltitude(altitude);
			
			float bearing=Float.parseFloat(items[3]);
			location.setBearing(bearing);	
		}catch(Exception exception){
			exception.printStackTrace();
			location=null;
		}
		return location;
	}
	
	public static String getLocationAsString(Location location){
		if(location==null){
			return DATA_NONE;
		}
		StringBuilder builder=new StringBuilder();
		builder.append(location.getLatitude());
		builder.append(DATA_SEPARATOR);
		builder.append(location.getLongitude());
		builder.append(DATA_SEPARATOR);
		builder.append(location.getAltitude());
		builder.append(DATA_SEPARATOR);
		builder.append(location.getBearing());
		builder.append(DATA_SEPARATOR);
		return builder.toString();
	}
	

	public static Location getDefaultLocation(){
		Location location=new Location("Remember");
		location.setLatitude(48.860611);
		location.setLongitude(2.337644);
		return location;
		
	}
	public static Intent getGeoIntentFromLocation(Location location){
		Intent dir = new Intent(Intent.ACTION_VIEW);
    	dir.setData(Uri.parse("google.navigation:q=" + location.getLatitude() + ", " + location.getLongitude()));
		return dir;
	}
	public static Bitmap getBitmap(String filePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize=4;
		return BitmapFactory.decodeFile(filePath, options);
	}
	public static void launchGoogleMap(Context context,RememberItem item) {
		Location location = item.getLocation();
		Intent intent = RememberUtils.getGeoIntentFromLocation(location);
		context.startActivity(intent);
	}
	
	public static void logUserEmail(Context context){
		AccountManager accountManager = AccountManager.get(context);
		 Account[] accounts = accountManager.getAccounts();
		 for(Account account:accounts){
			 Log.i("EMAIL", account.name);
		 }

		// Pick an account from the list of returned accounts.
	}
	
	public static boolean isAuthentificated(Context context){
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		return !mPrefs.getString(PreferenceConstants.REFRESH_TOKEN, "").isEmpty();
		
	}
	
	public static void startAuthentificationProcess(Activity activity,int requestCode){
		Intent authSetupIntent  = new Intent(activity, AuthActivity.class);
		activity.startActivityForResult(authSetupIntent, requestCode);
		
	}

	public static void startSynchronization(Context context) {
		Intent synchronization  = new Intent(context, SynchronizedItemsService.class);
		context.startService(synchronization);
	}
	
	
	
	
	
	

}
