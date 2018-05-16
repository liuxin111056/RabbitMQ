package com.mina.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class SocketClient {
	public int port = 9898;
	public String hostAddress = "127.0.0.1";

	public static void main(String[] args) throws IOException {

		NioSocketConnector connector = new NioSocketConnector();
		connector.setHandler(new MyClientHandler());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
		ConnectFuture future = connector.connect(new InetSocketAddress("127.0.0.1", 9898));
		future.awaitUninterruptibly();//µÈ´ýÁ¬½Ó
		IoSession session = future.getSession();
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String inputContent;
		while (!(inputContent = inputReader.readLine()).equals("bye")) {
			session.write(inputContent);
		}

	}
}
