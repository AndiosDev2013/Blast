package com.example.blast.http;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpApi {
	private static final String LOG = HttpApi.class.getSimpleName();
	private static CookieStore cookieStore = null;
	
	/*
	 * Get Request
	 */
	public static class ResponseModel {
		public int status = -1;
		public String response = "";
	}
	
	public static ResponseModel sendGetRequest(String url) {
		ResponseModel result = new ResponseModel();
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			
			result.status = response.getStatusLine().getStatusCode();
			String body = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			
			Log.v(LOG, "###### STATUS: " + result.status);
			Log.v(LOG, "###### BODY: " + result.status);
			
			if (result.status == 200) {
				Log.v(LOG, "###### RESPONSE: " + body);
				result.response = body.trim();
				return result;
			
			} else {
				switch (result.status) {
				case 400:
					result.response = body.trim();
					//resultStr = "Bad Request";
					break;
				case 401:
					result.response = body.trim();
					//resultStr = "Access denied";
					break;
				case 403:
					result.response = body.trim();
					//resultStr = "Request is forbidden";
					break;
				case 404:
					result.response = body.trim();
					//resultStr = "Url not found";
					break;
				case 405:
					result.response = body.trim();
					//resultStr = "Method Not Allowed";
					break;
				case 500:
					result.response = body.trim();
					//resultStr = "Internal Server Error";
					break;
				}
			}
			
		} catch (Exception e) {
			result.status = -1;
			result.response = e.getMessage();
			if (e instanceof java.net.SocketTimeoutException) {
				result.response = "It has occured SocketTimeoutException";
			}
			
			Log.v(LOG, "####### Send GET request error: " + result.response);
		}
		
		return result;
	}
	
	
	/*
	 * Post Request
	 */
	public static ResponseModel sendPostRequest(String url, String data) {
		ResponseModel result = new ResponseModel();
		
		try
		{
			StringEntity se = new StringEntity(data);
			
			/* Sets the HTTP post request */
			HttpPost request = new HttpPost(url);
			request.getParams().setParameter("http.protocol.expect-continue", false);
			request.getParams().setParameter("http.connection.timeout", 20000);
			request.getParams().setParameter("http.socket.timeout", 20000);
			request.setHeader("Content-Type", "application/json");
			request.setEntity(se);
			
			DefaultHttpClient client = new DefaultHttpClient();
			
			/* Gets the HTTP response*/
			HttpResponse response = client.execute(request);
			
			result.status = response.getStatusLine().getStatusCode();
			String body = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			
			Log.v(LOG, "###### STATUS: " + result.status);
			Log.v(LOG, "###### BODY: " + body);
			
			/* if the status code is 200, response successfully */
			if (result.status == 200) {
				Log.v(LOG, "###### RESPONSE SUCCESS #####");
				result.response = body.trim();
				
				return result;
				
			} else {
				switch (result.status) {
				case 400:
					result.response = body.trim();
					break;
				case 401:
					result.response = body.trim();
					//resultStr = "Access denied";
					break;
				case 403:
					result.response = body.trim();
					//resultStr = "Request is forbidden";
					break;
				case 404:
					result.response = body.trim();
					//resultStr = "Url not found";
					break;
				case 405:
					result.response = body.trim();
					//resultStr = "Method Not Allowed";
					break;
				case 406:
					result.response = body.trim();
					//resultStr = "Not Acceptable Response";
					break;
				case 500:
					result.response = body.trim();
					break;
				}
			}
		
		} catch (Exception e) {
			result.status = -1;
			result.response = e.getMessage();
			if (e instanceof java.net.SocketTimeoutException) {
				result.response = "It has occured SocketTimeoutException";
			}
			
			Log.v(LOG, "####### Send POST request error: " + result.response);
		}
		
		return result;
	}
	
	
	public static ResponseModel sendPostRequest(String serverUrl, HttpParams params) {
		ResponseModel result = new ResponseModel();
		
		try {
			HttpPost httpRequest = new HttpPost(serverUrl);
			httpRequest.getParams().setParameter("http.protocol.expect-continue", false);
			httpRequest.getParams().setParameter("http.connection.timeout", ServerConfig.CONNECTION_TIMEOUT * 1000);
			httpRequest.getParams().setParameter("http.socket.timeout", ServerConfig.CONNECTION_TIMEOUT * 1000);
			httpRequest.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "TVAppGuideAndroid");

			/** Makes an HTTP request request */
			if (params != null) {
				httpRequest.setEntity(new UrlEncodedFormEntity(params.getParams(), HTTP.UTF_8));
			}

			/** Create an HTTP client */
			DefaultHttpClient httpClient = new DefaultHttpClient();

			/** Set Cookie information */
			if (cookieStore != null) {
				httpClient.setCookieStore(cookieStore);
			}

			/** Gets the HTTP response response */
			HttpResponse httpresponse = httpClient.execute(httpRequest);

			result.status = httpresponse.getStatusLine().getStatusCode();
			String body = EntityUtils.toString(httpresponse.getEntity(), HTTP.UTF_8);

			/** If the status code 200 response successfully */
			Log.v(LOG, "" + result.status);
			Log.v(LOG, "" + body);
			if (result.status == 200) {
				/** Remove the response string */
				String strResponse = body.trim();
				cookieStore = httpClient.getCookieStore();
				
				return result;
				
			} else {
				switch (result.status) {
				case 400:
					result.response = body.trim();
					//resultStr = "Bad Request.";
					break;
				case 401:
					result.response = body.trim();
					//resultStr = "Access denied.";
					break;
				case 403:
					result.response = body.trim();
					//resultStr = "Request is forbidden.";
					break;
				case 404:
					result.response = body.trim();
					//resultStr = "Url not found.";
					break;
				case 405:
					result.response = body.trim();
					//resultStr = "Method Not Allowed.";
					break;
				case 500:
					result.response = body.trim();
					//resultStr = "Internal Server Error.";
					break;
				}
			}
		} catch (Exception e) {
			result.status = -1;
			result.response = e.getMessage();
			if (e instanceof java.net.SocketTimeoutException) {
				result.response = "It has occured SoketTimeoutException.";
			}
			Log.v(LOG, "send request error");
		}

		return result;
	}
}
