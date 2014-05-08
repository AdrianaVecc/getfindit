package com.adrianavecchioli.findit.provider;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


public class AndroidLocationProvider implements LocationListener {

	
	private LocationChangedListener changedListener;
	private LocationManager locationManager;
	
	public AndroidLocationProvider(Context context,LocationChangedListener locationChangedListener) {
		locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		this.changedListener=locationChangedListener;
	}
	

	public void startLocationService(){
		 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		 List<String> providers=locationManager.getAllProviders();
		 for(String provider :providers){
			 if(provider!=null){
				 locationManager.requestLocationUpdates(provider, 0, 0, this);	 
			 }
			 Log.i("PROVIDER", provider);
		 }
		 Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		 if(lastLocation!=null){
			 changedListener.onLocationReceived(lastLocation);
		 }
		
	}
	public void stopLocationService(){
		locationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		changedListener.onLocationReceived(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
	
}
