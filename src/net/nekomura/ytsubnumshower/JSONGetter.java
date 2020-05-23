package net.nekomura.ytsubnumshower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONGetter {

	  public static JSONObject readJsonFromUrl(String urlStr) throws IOException, JSONException {
		  URL url = new URL(urlStr);   
		  URLConnection connection = url.openConnection();
		  HttpURLConnection httpConnection = (HttpURLConnection) connection;

		  InputStream inputStream;
		  int status = httpConnection.getResponseCode();
		  if (status != HttpURLConnection.HTTP_OK)
			  inputStream = httpConnection.getErrorStream();
		  else
			  inputStream = httpConnection.getInputStream();
		  
		  BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		  
		  StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = in.read()) != -1) {
				sb.append((char) cp);
			}
          
          in.close();
          httpConnection.disconnect();
          return new JSONObject(sb.toString());
	  }
}
