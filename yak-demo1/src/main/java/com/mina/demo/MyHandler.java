package com.mina.demo;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MyHandler implements IoHandler {

    public void exceptionCaught(IoSession arg0, Throwable arg1)
            throws Exception {
        System.out.println("exception");
    }

    public void inputClosed(IoSession arg0) throws Exception {
        System.out.println("inputClosed");
    }

    public void messageReceived(IoSession arg0, Object arg1) throws Exception {
        String msg = (String) arg1;
        System.out.println("messageReceived server: " + msg);
        arg0.write(msg);
    }

    public void messageSent(IoSession arg0, Object arg1) throws Exception {
        System.out.println("messageSent");
    }

    public void sessionClosed(IoSession arg0) throws Exception {
        System.out.println("sessionClosed "+arg0.hashCode());
    }

    public void sessionCreated(IoSession arg0) throws Exception {
        System.out.println("sessionCreated "+arg0.hashCode());

    }

    public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
        System.out.println("sessionIdle "+arg0.hashCode()+" , "+arg1);
    }

    public void sessionOpened(IoSession arg0) throws Exception {
        System.out.println("sessionOpened "+arg0.hashCode());
    }

}