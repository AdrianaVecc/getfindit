package com.adrianavecchioli.findit.service;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.adrianavecchioli.findit.LiveCardMenu;
import com.adrianavecchioli.findit.R;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.receiver.AddBroadcastReceiver;
import com.adrianavecchioli.findit.receiver.LiveCardUpdateListener;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

public class LiveCardService extends Service implements LiveCardUpdateListener{
	private LiveCard mLiveCard;
	public static final String LIVE_CARD_TAG="livecardTag";
	public static final String KEY_REMEMBER_ITEM="remember_item_key";
	
	private AddBroadcastReceiver addBroadcastReceiver;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		RememberItem item=intent.getExtras().getParcelable(KEY_REMEMBER_ITEM);
		mLiveCard = new LiveCard(this,LIVE_CARD_TAG);
		updateLiveCardWithRememberItem(mLiveCard,item);
		mLiveCard.publish(PublishMode.REVEAL);
		addBroadcastReceiver=new AddBroadcastReceiver(this);
		registerReceiver(addBroadcastReceiver, AddBroadcastReceiver.getIntentFilter());
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		return new LocalBinder<Service>(this);
	}
	private void updateLiveCardWithRememberItem(LiveCard mLiveCard,RememberItem item) {
        mLiveCard.setViews(createLiveCardRemoteView(item));
        mLiveCard.setAction(createIntentHandler(item));
	}
	
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        unregisterReceiver(addBroadcastReceiver);
        super.onDestroy();
    }
    

	private PendingIntent createIntentHandler(RememberItem item) {
		Intent menuIntent = new Intent(this, LiveCardMenu.class);
        menuIntent.putExtra(KEY_REMEMBER_ITEM, item);
        menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_CLEAR_TASK);
       return  PendingIntent.getActivity(
                this, 0, menuIntent, 0);
	}
	private RemoteViews createLiveCardRemoteView(RememberItem item2) {
		RemoteViews mLiveCardView = new RemoteViews(getPackageName(),
            R.layout.remote_card_view_file);
        mLiveCardView.setTextViewText(R.id.tag_text,item2.getTag());
        return mLiveCardView;
	}
	@Override
	public void onRememberItemAdded(RememberItem item) {
		updateLiveCardWithRememberItem(mLiveCard,item);
		mLiveCard.publish(PublishMode.REVEAL);
	}
}
