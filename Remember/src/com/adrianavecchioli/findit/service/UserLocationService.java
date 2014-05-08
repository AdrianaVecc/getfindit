package com.adrianavecchioli.findit.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import com.adrianavecchioli.findit.provider.AndroidLocationProvider;
import com.adrianavecchioli.findit.provider.LocationChangedListener;
import com.adrianavecchioli.findit.util.RememberUtils;

public class UserLocationService extends Service implements LocationChangedListener {

	private Location location;
	private AndroidLocationProvider androidLocationProvider;
	
	@Override
	public void onCreate() {
		super.onCreate();
		androidLocationProvider=new AndroidLocationProvider(this, this);
		androidLocationProvider.startLocationService();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		androidLocationProvider.stopLocationService();
		
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		  return new LocalBinder<Service>(this);
	}
	
	
	public Location getCurrentLocation(){
		if(location==null){
			location=getLastLocation(this);
		}
		if(location==null){
			location=RememberUtils.getDefaultLocation();
		}
		return location;
		
	}

	@Override
	public void onLocationReceived(Location location) {
		this.location=location;
		
	}
	
	
	 public static Location getLastLocation(Context context) {
	      LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	      Criteria criteria = new Criteria();
	      criteria.setAccuracy(Criteria.NO_REQUIREMENT);
	      List<String> providers = manager.getProviders(criteria, true);
	      List<Location> locations = new ArrayList<Location>();
	      for (String provider : providers) {
	           Location location = manager.getLastKnownLocation(provider);
	           if (location != null) {
	               locations.add(location);
	           }
	      }
	      Collections.sort(locations, new Comparator<Location>() {
	          @Override
	          public int compare(Location location, Location location2) {
	              return (int) (location.getAccuracy() - location2.getAccuracy());
	          }
	      });
	      if (locations.size() > 0) {
	          return locations.get(0);
	      }
	      return null;
	 }
	

}
