package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CServer {
	public static final int HOST_PORT = 11234;
	public CServer() {
		ServerSocket serverSocket=null;
		try {
			serverSocket = new ServerSocket(CServer.HOST_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			Socket s=null;
			try {
				System.out.println("waiting for clients");
				s = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new CServerThread(s).start();
		}	
	}

}
