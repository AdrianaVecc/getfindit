package com.adrianavecchioli.findit.provider;

import android.location.Location;

public interface LocationChangedListener {
	void onLocationReceived(Location location);
}
