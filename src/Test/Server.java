package Test;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	public static void start() throws Exception {
		ServerSocket a =new ServerSocket(7060);
	while(true) {
		Socket b=a.accept();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Scanner c=null;
				try {
					c = new Scanner(b.getInputStream());
					while(c.hasNext()) {
						System.out.println(c.next());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PrintStream c;
				try {
					c = new PrintStream(b.getOutputStream());
					c.println("HTTP/1.0 200 OK\r\n".getBytes());
					c.println("Content-Type:application/binary");
					c.println("\r\n");
					c.println("wewew".getBytes());
					c.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
				
	}
	public static void main(String[] args) {
		try {
			start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
