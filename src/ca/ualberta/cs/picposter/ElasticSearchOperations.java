package ca.ualberta.cs.picposter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.cs.picposter.model.PicPostModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ElasticSearchOperations {
	
	public static void searchPicPostModel(final String searchTerm) {
		Thread thread = new Thread() {
			
			@Override
			public void run() {
				Gson gson = new Gson();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/sarychuk/_search?q=" + searchTerm);
				
				try {
					HttpResponse response = client.execute(request);
					Log.w("ElasticSearch Response", response.getStatusLine().toString());
					
					response.getStatusLine().toString();
					HttpEntity entity = response.getEntity();

					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					String string = new String();
					while (output != null) {
						string.concat(output);
						//Log.w("ElasticSearch Entity", output);
						output = reader.readLine();
					}
					
					//PicPostModel object = gson.fromJson(string, PicPostModel.class);
					
					Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<PicPostModel>>(){}.getType();
					ElasticSearchSearchResponse<PicPostModel> esResponse = gson.fromJson(string, elasticSearchSearchResponseType);
					System.err.println(esResponse);
					for (ElasticSearchResponse<PicPostModel> r : esResponse.getHits()) {
						PicPostModel objects = r.getSource();
						System.err.println(recipe);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		thread.start();
		
		
	}
	
	public static void pushPicPostModel(final PicPostModel model) {
		Thread thread = new Thread() {
			
			@Override
			public void run() {
				Gson gson = new Gson();
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/sarychuk/");
				
				try {
					String jsonString = gson.toJson(model);
					request.setEntity(new StringEntity(jsonString));
					
					HttpResponse response = client.execute(request);
					Log.w("ElasticSearch Response", response.getStatusLine().toString());
					
					response.getStatusLine().toString();
					HttpEntity entity = response.getEntity();

					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						Log.w("ElasticSearch Entity", output);
						output = reader.readLine();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		};
		
		thread.start();
	}
}
