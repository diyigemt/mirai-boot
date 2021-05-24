package net.diyigemt.miraiboot.utils;

import net.diyigemt.miraiboot.entity.HttpProperties;
import net.diyigemt.miraiboot.utils.builder.FileMessageBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * <p>HTTP请求工具类</p>
 *
 * @author Haythem, diyigemt
 * @since 1.0.0
 */

public class HttpUtil {

	/**
	 * <h2>带高级设置的HTTP请求</h2>
	 * <p>通过实例化注解或添加注解来增加HOST等设置</p>
	 *
	 * @param urlString  目标URL
	 * @param properties 注解实例化结果
	 * @return 目标文件输入流
	 */
	public static InputStream getInputStream_advanced(String urlString, HttpProperties properties) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setConnectTimeout(properties.getTimeout());
			connection.setRequestMethod(properties.getRequestMethod());
			Map<String, String> map = properties.getRequestProperties();
			Set set = map.entrySet();
			Iterator it=set.iterator();
			while(it.hasNext()) {
				Map.Entry entry=(Map.Entry)it.next();
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				connection.setRequestProperty(key, value);
			}

			connection.connect();
			inputStream = connection.getInputStream();
			URL su = connection.getURL();
			FileMessageBuilder.FileName = getFileName(su.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * <h2>使用默认配置的HTTP请求</h2>
	 * <p>此为裸连请求，不适合用来请求外网资源</p>
	 *
	 * @param urlString 目标URL
	 * @return 目标文件输入流
	 */
	public static InputStream getInputStream(String urlString) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			inputStream = connection.getInputStream();
			URL su = connection.getURL();
			FileMessageBuilder.FileName = getFileName(su.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	private static String getFileName(String file) {
		StringTokenizer st = new StringTokenizer(file, "/");
		while (st.hasMoreTokens()) {
			file = st.nextToken();
		}
		return file;
	}

}