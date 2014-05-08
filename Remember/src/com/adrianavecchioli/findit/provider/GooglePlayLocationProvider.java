package com.adrianavecchioli.findit.provider;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class GooglePlayLocationProvider implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	LocationClient locationClient;
	LocationChangedListener changedListener;
	LocationRequest mLocationRequest;
	Location location;
	private Context context;

	public GooglePlayLocationProvider(Context context,
			LocationChangedListener locationChangedListener) {

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

		this.changedListener = locationChangedListener;
		this.context = context;

	}

	public void startLocationService() {
		locationClient = new LocationClient(context, this, this);
		locationClient.connect();
		if (location != null) {
			changedListener.onLocationReceived(location);
		}
	}

	public void stopLocationService() {
		locationClient.disconnect();

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationClient.requestLocationUpdates(mLocationRequest, this);

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		changedListener.onLocationReceived(location);

	}

}
