package model;

import java.net.Socket;

public class CServerThread extends Thread{
	private	Socket s =null;
	public CServerThread() {
		
	}
	public CServerThread(Socket s) {
		this.s = s;
	}
	@Override
	public void run() {
		System.out.println("a CServerThread is running");
	}
}
