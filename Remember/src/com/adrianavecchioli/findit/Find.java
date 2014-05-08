package com.adrianavecchioli.findit;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.adrianavecchioli.findit.adapter.ScrollAdapter;
import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.service.LiveCardService;
import com.adrianavecchioli.findit.util.RememberUtils;
import com.google.android.glass.app.Card;
import com.google.android.glass.app.Card.ImageLayout;
import com.google.android.glass.widget.CardScrollView;

public class Find extends Activity implements Callback {

	private RememberItem itemSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RememberItem item = getIntent().getParcelableExtra(
				LiveCardService.KEY_REMEMBER_ITEM);
		String tag = null;
		if (item == null) {
			ArrayList<String> voiceResults = getIntent().getExtras()
					.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
			if (voiceResults != null && !voiceResults.isEmpty()) {
				tag = voiceResults.get(0);
				item = SqlHelper.getInstance(this).findRememberItem(tag);
			}
		}
		if (item != null) {
			List<RememberItem> items = new ArrayList<RememberItem>();
			items.add(item);
			displayRememberItems(items);
			this.itemSelected = item;
		} else {
			if (RememberUtils.EVERY_THING.equalsIgnoreCase(tag)) {
				List<RememberItem> items = SqlHelper.getInstance(this)
						.findAllRememberItem();
				displayRememberItems(items);
			} else {
				displayFailureView();
			}
		}
	}
	private void displayFailureView() {
		Card fail = new Card(this);
		fail.setText(R.string.storefailhead);
		fail.setFootnote(R.string.storefailfoot);
		fail.setImageLayout(Card.ImageLayout.FULL);
		fail.addImage(R.drawable.storefailbackground);
		View failView = fail.getView();
		setContentView(failView);
	}

	private Card createCardOfRememberItem(RememberItem item) {
		Card card = new Card(this);
		card.setText(item.getTag());
		card.setFootnote(String.format("%tc", item.getAddedDate()));
		card.setImageLayout(ImageLayout.FULL);
		card.addImage(BitmapFactory.decodeFile(item.getImagePath()));
		return card;
	}


	private void displayRememberItems(final List<RememberItem> items) {
		List<Card> mCards = new ArrayList<Card>();
		for (RememberItem item : items) {
			Card card = createCardOfRememberItem(item);
			mCards.add(card);
		}
		CardScrollView mCardScrollView = new CardScrollView(this);
		final ScrollAdapter adapter = new ScrollAdapter(mCards);
		mCardScrollView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Card card = (Card) adapter.getItem(arg2);
				itemSelected = SqlHelper.getInstance(getApplicationContext())
						.findRememberItem(card.getText().toString());
				openOptionsMenu();

			}
		});

		mCardScrollView.setAdapter(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
	}

	private void showSucessDeleteCard(RememberItem item) {
		Card card = new Card(this);
		card.setText(R.string.object_delete);
		card.setImageLayout(ImageLayout.FULL);
		card.addImage(R.drawable.finditlogobg);
		setContentView(card.getView());
		Handler handler = new Handler(this);
		handler.sendEmptyMessageDelayed(0, 3000);
	}

	@Override
	public boolean handleMessage(Message msg) {
		finish();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.saveditemmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuitem) {
		if (itemSelected == null) {
			return super.onOptionsItemSelected(menuitem);
		}
		switch (menuitem.getItemId()) {
		case R.id.menu_getdirections:
			RememberUtils.launchGoogleMap(this,itemSelected);
			return true;
		case R.id.menu_delete:
			boolean result = SqlHelper.getInstance(getApplication())
					.deleteRememberItem(itemSelected);
			if (result) {
				showSucessDeleteCard(itemSelected);
			}

			return true;
		default:
			return super.onOptionsItemSelected(menuitem);
		}
	}
}
