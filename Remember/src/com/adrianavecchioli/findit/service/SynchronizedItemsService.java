package com.adrianavecchioli.findit.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;

public class SynchronizedItemsService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		List<String> serverItemIds=new ArrayList<String>();
		List<RememberItem> items=SqlHelper.getInstance(this).findAllRememberItem();
		for(RememberItem item:items){
			if(!serverItemIds.contains(item)){
				uploadOnServer(item);
			}
		}
		List<String>deletedIds=SqlHelper.getInstance(this).findDeletedRememberItemId();
		for(String idDeleted:deletedIds){
			if(serverItemIds.contains(idDeleted)){
				deleteOnServer(idDeleted);
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	private void uploadOnServer(RememberItem item) {
		
	}

	private void deleteOnServer(String idDeleted) {
		
	}
}
