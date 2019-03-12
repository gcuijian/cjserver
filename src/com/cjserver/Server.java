package com.cjserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>处理服务器应该做的那点事</p>
 * @ClassName Server
 * @Description 
 * @Author Cui Jian
 * @version
 * @Date 2019年1月17日 下午3:26:06
 */
public class Server {

	public void init() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(Start.PORT);
			int i = 0;
			while(true){
				Logger.log("listen", "Server is Listening!");
				Socket socket = server.accept();
				System.out.println("第"+ ++i +"个连接被创建。");
				Start.THREAD_POOL_EXECUTOR.execute(new ServerThread(socket));
				System.out.println("第"+ i +"个连接结束，被销毁。");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
