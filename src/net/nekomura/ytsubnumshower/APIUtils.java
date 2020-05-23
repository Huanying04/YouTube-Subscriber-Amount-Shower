package net.nekomura.ytsubnumshower;

import java.io.IOException;

import org.json.JSONObject;

public class APIUtils {
	public static String getSubscriberAmount(String appid, String channelID) throws IOException {
		String apiurl = "https://www.googleapis.com/youtube/v3/channels?part=statistics,snippet&id=" + channelID + "&key=" + appid;
		String amount = new String();
		JSONObject infoJSON = JSONGetter.readJsonFromUrl(apiurl);
		if(infoJSON.has("items")) {
			amount = JSONGetter.readJsonFromUrl(apiurl).getJSONArray("items").getJSONObject(0).getJSONObject("statistics").getString("subscriberCount");
		}else if(infoJSON.has("error")) {
			amount = "-1";
		}else if(infoJSON.has("pageInfo") && infoJSON.getJSONObject("pageInfo").getInt("totalResults") == 0) {
			amount = "-2";
		}else {
			amount = "-3";
		}
		return amount;
	}
	
	public static int getSubscriberAmountinInt(String appid, String channelID) throws IOException{
		return Integer.valueOf(getSubscriberAmount(appid, channelID));
	}
	
	public static String getAvatarURL() throws IOException{
		String apiurl = "https://www.googleapis.com/youtube/v3/channels?part=statistics,snippet&id=" + ConfigUtils.channelID + "&key=" + ConfigUtils.appid;
		String url = new String();
		JSONObject infoJSON = JSONGetter.readJsonFromUrl(apiurl);
		if(infoJSON.has("items")) 
			url = JSONGetter.readJsonFromUrl(apiurl).getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");
		return url;
	}
	
	public static String getChannelName() throws IOException{
		String apiurl = "https://www.googleapis.com/youtube/v3/channels?part=statistics,snippet&id=" + ConfigUtils.channelID + "&key=" + ConfigUtils.appid;
		String name = new String();
		JSONObject infoJSON = JSONGetter.readJsonFromUrl(apiurl);
		if(infoJSON.has("items")) 
			name = JSONGetter.readJsonFromUrl(apiurl).getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getString("title");
		return name;
	}
	
	public static String userIDtoChannelID(String userID) throws IOException{
		String apiurl = "https://www.googleapis.com/youtube/v3/channels?part=id&forUsername=" + userID + "&key=" + ConfigUtils.appid;
		String channelID = new String();
		JSONObject infoJSON = JSONGetter.readJsonFromUrl(apiurl);
		if(infoJSON.has("items")) {
			channelID = JSONGetter.readJsonFromUrl(apiurl).getJSONArray("items").getJSONObject(0).getString("id");
		}else if(infoJSON.has("error")) {
			channelID = "error";
		}else if(infoJSON.has("pageInfo") && infoJSON.getJSONObject("pageInfo").getInt("totalResults") == 0) {
			channelID = "noresult";
		}else {
			channelID = "unknown";
		}
		return channelID;
	}
}