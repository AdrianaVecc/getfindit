package com.adrianavecchioli.findit;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.service.LocationServiceConnector;
import com.adrianavecchioli.findit.util.RememberUtils;
import com.google.android.glass.app.Card;
import com.google.android.glass.app.Card.ImageLayout;
import com.google.android.glass.media.CameraManager;

public class Remember extends Activity implements Callback{
	
	private static final int TAKE_PICTURE_REQUEST_CODE = 1;
	private LocationServiceConnector locationServiceConnector=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Card cardView = new Card(this);
		cardView.setImageLayout(Card.ImageLayout.FULL);
		cardView.addImage(R.drawable.taptotakeapicture);
		View failView = cardView.getView();
		setContentView(failView);
		locationServiceConnector=new LocationServiceConnector();
		locationServiceConnector.bind(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(locationServiceConnector!=null){
			locationServiceConnector.unbind(this);
		}
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
			takePicture();
			return true;
		} else {
			return false;
		}
	}

	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
			String picturePath = data
					.getStringExtra(CameraManager.EXTRA_THUMBNAIL_FILE_PATH);
			processPictureWhenReady(picturePath);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void processPictureWhenReady(final String picturePath) {
		final File pictureFile = new File(picturePath);

		if (pictureFile.exists()) {
			processFile(pictureFile);
		} else {

			final File parentDirectory = pictureFile.getParentFile();
			FileObserver observer = new FileObserver(parentDirectory.getPath()) {
				private boolean isFileWritten;

				@Override
				public void onEvent(int event, String path) {
					if (!isFileWritten) {
						File affectedFile = new File(parentDirectory, path);
						isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile
								.equals(pictureFile));

						if (isFileWritten) {
							stopWatching();
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									processPictureWhenReady(picturePath);
								}
							});
						}
					}
				}
			};
			observer.startWatching();
		}
	}

	private void processFile(File pictureFile) {
		RememberItem item=new RememberItem();
		
		ArrayList<String> voiceResults = getIntent().getExtras()
				.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
		String tag = voiceResults.get(0);
		item.setTag(tag);
		
		String imagePath=pictureFile.getAbsolutePath();
		item.setImagePath(imagePath);
		
		
		Location location=locationServiceConnector.getCurrentLocation();
		item.setLocation(location);
		
		long addedDate=System.currentTimeMillis();
		item.setAddedDate(addedDate);
		
		SqlHelper.getInstance(this).saveRememberItem(item);
		showSucessCard(item);
		
	}
	private void showSucessCard(RememberItem item){
		Card card = new Card(this);
		card.setText(R.string.object_saved);
		card.setImageLayout(ImageLayout.LEFT);
		card.addImage(BitmapFactory.decodeFile(item.getImagePath()));
		card.setFootnote(item.getTag());
		setContentView(card.getView());
		Handler handler=new Handler(this);
		Message message=Message.obtain();
		message.obj=item;
		handler.sendMessageDelayed(message, 3000);
	}
	@Override
	public boolean handleMessage(Message msg) {
		if(msg!=null && msg.obj instanceof RememberItem){
			RememberUtils.sendAddRememberItemBroadcast(this, (RememberItem)msg.obj);
		}
		finish();
		return false;
	}
	

}
