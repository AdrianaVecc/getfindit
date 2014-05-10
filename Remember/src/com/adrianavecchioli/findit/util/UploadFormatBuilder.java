package com.adrianavecchioli.findit.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.request.UploadConst;
import com.adrianavecchioli.findit.service.SynchronizedItemsService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.Storage.Objects.Delete;
import com.google.api.services.storage.Storage.Objects.Insert;
import com.google.api.services.storage.Storage.Objects.List;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.StorageObject;



public class UploadFormatBuilder {


	private static final String APPLICATION_NAME_PROPERTY = "findit";
	private static final String ACCOUNT_ID_PROPERTY = "175257221130@developer.gserviceaccount.com";
	private static final String PRIVATE_KEY_PATH_PROPERTY = "client_secret_175257221130.apps.googleusercontent.com.json";
	
	
	


			

	public static void upload(Context context, RememberItem item)
			throws Exception {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				new File(item.getImagePath())));
		
		InputStreamContent mediaContent = new InputStreamContent(
				"application/octet-stream", inputStream);
		StorageObject objectMetadata = new StorageObject();
		objectMetadata.setName(item.getId());
		Map<String, String> maps = RememberUtils.convertToMaps(item);
		objectMetadata.setMetadata(maps);
		Storage storage = getStorage(context);
		 Insert insertObject = storage.objects().insert(
				UploadConst.BUCKET_NAME, objectMetadata, mediaContent);
		if (mediaContent.getLength() > 0
				&& mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
			insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
		}
		insertObject.setOauthToken(SynchronizedItemsService.TOKEN);
		insertObject.execute();
		Log.i("REMEMBER", "UPLOADED"+item.getTag());
	}

	public static Objects list(Context context) throws Exception {
		Storage storage = getStorage(context);
		List list = storage.objects().list(UploadConst.BUCKET_NAME);
		list.setOauthToken(SynchronizedItemsService.TOKEN);
		Log.i("REMEMBER", "GETLIT");
		return list.execute();
		

	}

	public static void delete(Context context, String item)
			throws Exception {
		Storage storage = getStorage(context);
		Delete delete = storage.objects().delete(UploadConst.BUCKET_NAME, item);
		delete.setOauthToken(SynchronizedItemsService.TOKEN);
		delete.execute();
		
		Log.i("REMEMBER", "DELETED"+item);
	}





	private static Storage getStorage(Context context) throws Exception {
		HttpTransport httpTransport =AndroidHttp.newCompatibleTransport();
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		Storage	storage = new Storage.Builder(httpTransport, JSON_FACTORY,
					new HttpRequestInitializer() {
						
						@Override
						public void initialize(HttpRequest arg0) throws IOException {
							HttpHeaders headers=new HttpHeaders();
							
						}
					}).setApplicationName(APPLICATION_NAME_PROPERTY)
					.build();
		return storage;
	}


}
