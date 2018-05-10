package com.tiansu.eam.modules.sso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP操作类,张道祺
 */
public class HttpClientUtil {

	@SuppressWarnings("unused")
	private String url;
	private Map<String, Object> param;
	private URL u;
	private URLConnection uc;
	private BufferedReader br;
	private PrintWriter pw;
	private String charset;

	public HttpClientUtil(String url) {
		this.url = url;
		try {
			u = new URL(url);
			uc = u.openConnection();
		} catch (Exception e) {
			return;
		}
	}

	public String getLocation() {
		HttpURLConnection conn = (HttpURLConnection) uc;
		try {
			conn.setRequestMethod("GET");
		} catch (ProtocolException e) {
			return null;
		}
		conn.setInstanceFollowRedirects(false);
		return conn.getHeaderField("Location");
	}

	public String post() {
		uc.setDoOutput(true);
		uc.setDoInput(true);
		try {
			pw = new PrintWriter(uc.getOutputStream());
		} catch (Exception e) {
			return null;
		}
		String params = "";
		for (Entry<String, Object> entry : param.entrySet()) {
			params += "&" + entry.getKey() + "=" + entry.getValue();
		}
		pw.print(params.substring(1, params.length()));
		pw.flush();
		return start();
	}

	public String get() {
		try {
			uc.connect();
		} catch (Exception e) {
			return null;
		}
		return start();
	}

	public byte[] getByte() {
		try {
			InputStream instream = uc.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = instream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			instream.close();
			return outStream.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	private String start() {
		String result = "";
		try {
			if (charset == null)
				charset = "UTF-8";
			br = new BufferedReader(new InputStreamReader(uc.getInputStream(), charset));
			String line;
			while ((line = br.readLine()) != null) {
				result += line + "\r\n";
			}
			br.close();
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	public HttpClientUtil setParam(Map<String, Object> param) {
		this.param = param;
		return this;
	}

	public HttpClientUtil setCharset(String charset) {
		this.charset = charset;
		return this;
	}
}
