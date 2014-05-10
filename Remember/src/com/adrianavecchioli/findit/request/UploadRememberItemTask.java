package com.adrianavecchioli.findit.request;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;
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
			Log.i("REMEMBER", "UPLOADING ");
			UploadFormatBuilder.upload(context, this.rememberItem);
			Log.i("REMEMBER", "UPLOADING OK ");
		}catch(Exception exception){
			Log.i("REMEMBER", "UPLOADING FAIL "+exception.toString());
			exception.printStackTrace();
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
