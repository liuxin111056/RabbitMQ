package com.socket.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {

	public int port = 9898;
	public String hostAddress = "127.0.0.1";

	public static void main(String[] args) {
		SocketClient client = new SocketClient();
		client.start();
	}

	private void start() {
		BufferedReader inputReader = null;
		OutputStreamWriter output = null;
		Socket socket = null;
		try {
			socket = new Socket(hostAddress, port);
			inputReader = new BufferedReader(new InputStreamReader(System.in));
			output = new OutputStreamWriter(socket.getOutputStream());
			String inputContent;
			int count = 0;
			while (!(inputContent = inputReader.readLine()).equals("bye")) {
				output.write(inputContent);
				output.write("\n");
				output.flush();
				getServerMsg(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
				inputReader.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getServerMsg(final Socket socket) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					String serverMsg;
					while ((serverMsg = reader.readLine()) != null) {
						System.out.println("server say: " + serverMsg);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
