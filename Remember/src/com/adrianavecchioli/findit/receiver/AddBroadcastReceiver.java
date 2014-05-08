package com.adrianavecchioli.findit.receiver;

import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.service.LiveCardService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;



public class AddBroadcastReceiver extends BroadcastReceiver {

	public static final String ACTION="new_remember_item_added";
	LiveCardUpdateListener liveCardUpdateListener;
	public AddBroadcastReceiver(LiveCardUpdateListener liveCardUpdateListener){
		this.liveCardUpdateListener=liveCardUpdateListener;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		RememberItem item=intent.getParcelableExtra(LiveCardService.KEY_REMEMBER_ITEM);
		liveCardUpdateListener.onRememberItemAdded(item);
	}
	public static IntentFilter getIntentFilter() {
		return new IntentFilter(ACTION);
	}

}
