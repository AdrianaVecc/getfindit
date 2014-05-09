package com.adrianavecchioli.findit;

import android.app.Activity;
import android.os.Bundle;

import com.adrianavecchioli.findit.util.RememberUtils;

public class BaseActivity extends Activity{
	
	
	public static final int OAUTH_CODE=1009;
	@Override
	protected void onResume() {
		super.onResume();
		if(!RememberUtils.isAuthentificated(this)){
			RememberUtils.startAuthentificationProcess(this, OAUTH_CODE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RememberUtils.startSynchronization(this);
	}

}
