package com.adrianavecchioli.findit.request;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.service.SynchronizedItemsService;

public class DeleteRememberItemTask extends
		AsyncTask<String, Integer, Integer> {

	private static final int SUCCESS = 1;
	private static final int FAILURE= 0;
	
	private String rememberItemId=null;
	private Context context;
	
	public DeleteRememberItemTask(Context context,String itemId){
		this.rememberItemId=itemId;
		this.context=context;
	}
	@Override
	protected Integer doInBackground(String... params) {
		try{
			delete(this.rememberItemId);
		}catch(Exception exception){
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result!=null && result==SUCCESS){
			SqlHelper.getInstance(context).deleteListOfRememberItemToBeDeleted(rememberItemId);
		}
	}
	private void delete(String item) throws MalformedURLException,
			IOException, ProtocolException {
		URL urlObject = new URL(SynchronizedItemsService.UPLOAD_URL);
		HttpsURLConnection con = (HttpsURLConnection) urlObject
				.openConnection();
		con.setRequestMethod("DELETE");
		con.setRequestProperty("Authorization", "Bearer "
				+ SynchronizedItemsService.TOKEN);
		con.setRequestProperty("Content-Type", "application/json");

		con.setDoOutput(true);

		con.connect();

		OutputStreamWriter output = new OutputStreamWriter(
				con.getOutputStream());
		output.write(item);
		output.flush();
		output.close();
	}

}
