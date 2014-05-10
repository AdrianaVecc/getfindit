package com.adrianavecchioli.findit.request;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.util.UploadFormatBuilder;

public class GetRememberItemTask extends
		AsyncTask<String, Integer, List<String>> {

	private Context context;
	
	public GetRememberItemTask(Context context) {
		super();
		this.context = context;
	}
	@Override
	protected List<String> doInBackground(String... params) {
		try {
			return getItemIDonServer();
		} catch (Exception exception) {
			return null;
		}
	}
	@Override
	protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);
		if(result!=null){
			SqlHelper.getInstance(context).clearUploaded();
			SqlHelper.getInstance(context).saveUploaded(result);
		}
	}
	private List<String> getItemIDonServer() throws Exception {
		return  new ArrayList<String>(UploadFormatBuilder.list(context).keySet());

	}

}
