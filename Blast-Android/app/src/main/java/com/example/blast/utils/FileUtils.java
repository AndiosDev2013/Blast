package com.example.blast.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Environment;

public class FileUtils {

	public static void downloadFile(String dwnload_file_path){

		try {
			URL url = new URL(dwnload_file_path);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			//connect
			urlConnection.connect();

			//set the path where we want to save the file           
			String SDCardRoot= Environment.getExternalStorageDirectory().toString() ; 
			//create a new file, to save the downloaded file 
			File file = new File(SDCardRoot,"mydownloadmovie.mp4");

			FileOutputStream fileOutput = new FileOutputStream(file);

			//Stream used for reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();

			//this is the total size of the file which we are downloading
			int totalSize = urlConnection.getContentLength();


			//create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			int downloadedSize = 0;

			while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
			}
			//close the output stream when complete //
			fileOutput.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}       
	}
}
