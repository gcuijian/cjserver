package com.cjserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>处理每个请求</p>
 * @ClassName ServerThread
 * @Description 
 * @Author Cui Jian
 * @version
 * @Date 2019年1月17日 下午3:17:54
 */
public class ServerThread implements Runnable {

	private Socket socket;
	
	public ServerThread(Socket socket) {
		super();
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			InputStream is = socket.getInputStream();
			// 搞到访问的路径
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String url = br.readLine();
			if(url == null) {
				Logger.log("url", url+" is NULL!");
				br.close();
				is.close();
				socket.close();
				return;
			}
			Logger.log("url1", url+" is connected!");
			int head = url.indexOf("/");
			int end = url.lastIndexOf("HTTP/1.1");
			if(head == -1 || end == -1) {
				Logger.log("url", url+" don't have request state!");
				br.close();
				is.close();
				socket.close();
				return;
			}
			url = url.substring(url.indexOf("/") + 1, url.lastIndexOf("HTTP/1.1") - 1);
			// 对url过滤，同时打印参数
			url = formatUrl(url);
			Logger.log("url2", url+" is connected!");
			String path = Start.ROOT_PATH.replace("/", File.separator) + url.replace("/", File.separator);
			Logger.log("path", path+" is connected!");
			// 请求头打印
			String requestHeader = null;
			while("".equals((requestHeader = br.readLine())) &&
					requestHeader != null) {
				System.out.println(requestHeader);
				System.out.println("我在死循环哦！");
			}
			Logger.log("print", "Print header is complement!");
			// 响应
			OutputStream os = socket.getOutputStream();
			PrintStream ps = new PrintStream(os, true);
			// 响应对象
			FileManager fm = new FileManager(path);
			byte[] data = fm.getByte();
			if(data == null) {
				ps.println("HTTP/1.0 404 Not Found");
			} else {
				// 响应头
				String date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH).format(new Date());
				ps.println("HTTP/1.0 200 OK");
				ps.println("Server:CJ Server/1.0.0");
				ps.println("Content-Type:"+ fm.getFileType() +"; charset=utf-8");
				ps.println("date: " + date);
				ps.println("Content-Length:"+data.length);
				ps.println();
				// 响应数据
				ps.write(data);
			}
			// 关闭流
			ps.close();
			os.close();
			br.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 对url做一定的处理
	private String formatUrl(String url) throws UnsupportedEncodingException {
		// 有些字符自动转码了要转回来
		url = URLDecoder.decode(url, "UTF-8");
		Logger.log("encode", url + " is encode url!");
		// 过滤参数并打印
		int local = url.indexOf("?");
		if(local == -1) {
			return url;
		}
		String param = url.substring(local);
		System.out.println("参数有：" + param);
		return url.substring(0, local);
	}

}
