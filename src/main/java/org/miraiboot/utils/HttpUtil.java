package org.miraiboot.utils;

import org.miraiboot.annotation.HttpsProperties;
import org.miraiboot.constant.ConstantHttp;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpUtil {

	public static InputStream getInputStream_advanced(String urlString, HttpsProperties properties) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setConnectTimeout(properties.Timeout());
			connection.setRequestMethod(properties.RequestMethod());
			for(int i = 0; i < properties.RequestProperties().length; i += 2){
				connection.setRequestProperty(properties.RequestProperties()[i], properties.RequestProperties()[i + 1]);
			}
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