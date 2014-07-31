package es.jshcd.lectorrss.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class NewsDownloader extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {

		String FeedPath = params[0];

		InputStream is = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(FeedPath);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("NewsDownloader", "Error in http connection " + e.toString());
		}

		// publishProgress("progress");
		return responseToString(is);
	}
	
	private String responseToString(InputStream is) {
		String result = "";
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8); // again, spanishfag here
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		return result;
	}

	protected void onProgressUpdate(String... progress) {
		StringBuilder str = new StringBuilder();
		for (int i = 1; i < progress.length; i++) {
			str.append(progress[i] + " ");
		}
	}
}
