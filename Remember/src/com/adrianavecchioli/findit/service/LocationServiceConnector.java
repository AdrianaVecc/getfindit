package com.adrianavecchioli.findit.service;



import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;

public class LocationServiceConnector implements ServiceConnection {

        private UserLocationService service;

        public void bind(Context context) {
                Intent intent = new Intent(context, UserLocationService.class);
                context.bindService(intent, this, Context.BIND_AUTO_CREATE);
        }

        
        public void unbind(Context context) {
                context.unbindService(this);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
                @SuppressWarnings("unchecked")
                LocalBinder<UserLocationService> localBinder = (LocalBinder<UserLocationService>) binder;
                service = localBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        public Location getCurrentLocation(){
        	return service.getCurrentLocation();
        }

}
