package org.miraiboot.utils;

import org.miraiboot.constant.ConstantHttp;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpUtil {

	public static InputStream getInputStream_https(String urlString) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("User-agent", ConstantHttp.HEADER_USER_AGENT);
			connection.connect();
			inputStream = connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	public static InputStream getInputStream(String urlString) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			inputStream = connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}
}