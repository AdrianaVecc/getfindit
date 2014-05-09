package com.adrianavecchioli.findit.request;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.service.SynchronizedItemsService;

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


	private List<String> getItemIDonServer() throws MalformedURLException,
			IOException, ProtocolException {
		URL urlObject = new URL(SynchronizedItemsService.UPLOAD_URL);
		HttpsURLConnection con = (HttpsURLConnection) urlObject
				.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer "
				+ SynchronizedItemsService.TOKEN);
		con.setRequestProperty("Content-Type", "application/json");

		con.setDoOutput(true);

		con.connect();

		OutputStreamWriter output = new OutputStreamWriter(
				con.getOutputStream());
		output.flush();
		output.close();
		return new ArrayList<String>();

	}

}
