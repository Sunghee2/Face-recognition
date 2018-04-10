package main;


import java.net.ServerSocket;
import java.net.Socket;


import org.opencv.core.Core;

import server.EchoServerHandler;

public class Main {
	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String[] dlink = {"http://172.30.1.60/image.jpg", "http://172.30.1.19/image.jpg"};
		
		try {
			ServerSocket server = new ServerSocket(1234);
			int id = 0;
			
			while(true) {
				Socket client = server.accept();
				System.out.println("클라이언트 접속 "+id);
				Thread clientThread = new Thread(new EchoServerHandler(client, id, dlink[id]));
				clientThread.start();
				id++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
}