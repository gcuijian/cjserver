package com.cjserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>处理文件、返回流、确认路径是否为空</p>
 * @ClassName FileManager
 * @Description 类工具类
 * @Author Cui Jian
 * @version
 * @Date 2019年1月17日 下午3:16:57
 */
public class FileManager {

	private String fileType;
	
	public String getFileType() {
		return fileType;
	}

	private String path;
	
	public FileManager(String path) {
		this.path = path;
	}
	
	public byte[] getByte() {
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		try {
			if(file.isDirectory()) {
				// 找到其中的默认配置的index.html等
				file = new File(path + File.separator + Start.DEFAULT_PAGE);
				if(!file.exists()) {
					return null;
				}
				fileType = "text/html";
				return getFileByte(file);
			}
			if(file.isFile()) {
				// 名称文件类型筛选决定返回类型
				String name = file.getName().toLowerCase();
				if(name.endsWith("html")) {
					fileType = "text/html";
				}
				if(name.endsWith(".png") ||
						name.endsWith(".jpg") ||
						name.endsWith(".jpge") ||
						name.endsWith(".gif")) {
					fileType = "image/png";
				}
				if(name.endsWith(".css")) {
					fileType = "text/css";
				}
				if(name.endsWith(".js")) {
					fileType = "text/plain";
				}
				// 直接将文件返回过去
				return getFileByte(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 转换成byte返回去
	private byte[] getFileByte(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[fis.available()];
		fis.read(data);
		fis.close();
		return data;
	}
	
}
