package com.cjserver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	public static void log(String info, String name) {
		StringBuilder log = new StringBuilder();
		log.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		log.append("\t");
		log.append(info);
		log.append("\t");
		log.append(name);
		System.out.println(log.toString());
	}
	
}
