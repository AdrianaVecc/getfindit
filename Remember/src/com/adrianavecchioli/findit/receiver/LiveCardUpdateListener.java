package com.adrianavecchioli.findit.receiver;

import com.adrianavecchioli.findit.domain.RememberItem;

public interface LiveCardUpdateListener {

	public void onRememberItemAdded(RememberItem item);
}
