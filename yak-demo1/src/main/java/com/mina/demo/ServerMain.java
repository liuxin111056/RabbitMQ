package com.mina.demo;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ServerMain {
	public static int port = 9898;
	public static void main(String[] args) {

		NioSocketAcceptor acceptor = new NioSocketAcceptor();

		try {	
			//设置handler
			acceptor.setHandler(new MyHandler());
			//设置过滤器
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
			//绑定端口号
			acceptor.bind(new InetSocketAddress(port));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
