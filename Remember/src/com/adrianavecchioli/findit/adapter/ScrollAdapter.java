package com.adrianavecchioli.findit.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;

public class ScrollAdapter extends CardScrollAdapter {

	private List<Card> cards = new ArrayList<Card>();

	public ScrollAdapter(List<Card> cards) {
		this.cards = cards;
	}

	@Override
	public int getCount() {
		return cards.size();
	}

	@Override
	public Object getItem(int position) {
		return cards.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return cards.get(position).getView();
	}

	@Override
	public int getPosition(Object item) {
		return cards.indexOf(item);
	}
}