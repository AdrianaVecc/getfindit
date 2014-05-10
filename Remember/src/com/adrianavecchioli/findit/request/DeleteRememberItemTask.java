package com.adrianavecchioli.findit.request;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.adrianavecchioli.findit.db.SqlHelper;
import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.util.UploadFormatBuilder;

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
			UploadFormatBuilder.delete(context,this.rememberItemId);
		}catch(Exception exception){
			exception.printStackTrace();
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
		else{
			Toast.makeText(context, "DELETION FAILURE", Toast.LENGTH_SHORT).show();
		}
	}

}
