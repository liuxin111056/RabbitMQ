package com.socket.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	public static void main(String[] args) {
		SocketServer server = new SocketServer();
		server.startServer();
	}

	private void startServer() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9898);
			while (true) {
				socket = serverSocket.accept();
				System.out.println(socket.hashCode() + " is connect");
				connect(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void connect(final Socket socket) {
		new Thread(new Runnable() {

			public void run() {
				BufferedReader reader = null;
				OutputStreamWriter writer = null;

				try {
					reader = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					writer = new OutputStreamWriter(socket.getOutputStream());
					String msg;
					while ((msg = reader.readLine()) != null) {
						System.out.println(socket.hashCode()+"say: "+msg);
						writer.write(msg + "\n");
						writer.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						writer.close();
						reader.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
