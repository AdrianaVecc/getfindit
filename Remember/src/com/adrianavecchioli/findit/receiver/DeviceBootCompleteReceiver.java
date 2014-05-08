package com.adrianavecchioli.findit.receiver;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.util.RememberUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class DeviceBootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intents) {
		RememberItem item=SqlHelper.getInstance(context).latestRememberItem();
		if(item!=null){
			RememberUtils.startLiveCardService(context, item);
		}
	}

}
