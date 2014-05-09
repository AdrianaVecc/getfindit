package com.adrianavecchioli.findit.request;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.service.SynchronizedItemsService;
import com.adrianavecchioli.findit.util.UploadFormatBuilder;

public class UploadRememberItemTask extends
		AsyncTask<String, Integer, Integer> {

	private static final int SUCCESS = 1;
	private static final int FAILURE= 0;
	
	private RememberItem rememberItem=null;
	private Context context;
	
	
	public UploadRememberItemTask(RememberItem rememberItem, Context context) {
		super();
		this.rememberItem = rememberItem;
		this.context = context;
	}
	@Override
	protected Integer doInBackground(String... params) {
		try{
			UploadFormatBuilder.upload(context, this.rememberItem);
		}catch(Exception exception){
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result!=null && result==SUCCESS){
			SqlHelper.getInstance(context).saveUploaded(rememberItem.getId());
		}
	}

}
