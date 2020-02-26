package com.indra.srcc.airsrcc.sensor.connect.netty;

import com.indra.srcc.airsrcc.sensor.connect.messaging.IMessageConsoleListener;

import java.net.SocketException;

public interface IConnectionHandler {

	public ConnectionDescriptor getDescriptor();

	public void open() throws SocketException;

	public void close();

	// public void finish();

	// public void send(MessageConsole data) throws Exception;

	public boolean isOpen();

	public void addMessageListener(IMessageConsoleListener listener);

	public void removeMessageListener(IMessageConsoleListener listener);

	public void removeAllMessageListeners();

	// boolean isConnected();

	boolean isActive();

	boolean isServer();

}
