package com.adrianavecchioli.findit.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Map;

import android.content.Context;

import com.adrianavecchioli.findit.domain.RememberItem;
import com.adrianavecchioli.findit.request.UploadConst;
import com.adrianavecchioli.findit.service.SynchronizedItemsService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.Storage.Objects.Delete;
import com.google.api.services.storage.Storage.Objects.Insert;
import com.google.api.services.storage.Storage.Objects.List;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.StorageObject;



public class UploadFormatBuilder {


	public static void upload(Context context, RememberItem item)
			throws IOException, GeneralSecurityException {
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
	}

	public static Objects list(Context context) throws IOException,
			GeneralSecurityException {
		Storage storage = getStorage(context);
		List list = storage.objects().list(UploadConst.BUCKET_NAME);
		list.setOauthToken(SynchronizedItemsService.TOKEN);
		return list.execute();

	}

	public static void delete(Context context, RememberItem item)
			throws IOException, GeneralSecurityException {
		Storage storage = getStorage(context);
		Delete delete = storage.objects().delete(UploadConst.BUCKET_NAME, item.getId());
		delete.setOauthToken(SynchronizedItemsService.TOKEN);
		delete.execute();
	}

	private static class StorageRequestInitializer implements
			HttpRequestInitializer {
		@Override
		public void initialize(com.google.api.client.http.HttpRequest arg0)
				throws IOException {
		}
	}


	private static Storage getStorage(Context context)
			throws GeneralSecurityException, IOException {
		HttpTransport httpTransport = GoogleNetHttpTransport
				.newTrustedTransport();
		return new Storage.Builder(httpTransport, null,
				new StorageRequestInitializer()).setApplicationName("findit")
				.build();
	}

}
