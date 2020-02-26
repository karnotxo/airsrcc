package com.indra.srcc.airsrcc.sensor.connect.netty;

import com.indra.srcc.airsrcc.sensor.connect.messaging.IMessageLogger;
import com.indra.srcc.airsrcc.sensor.connect.messaging.IMessageConsoleListener;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public abstract class ConnectionHandler implements IConnectionHandler {

	private static boolean onExit = false;

	private final String id;

	protected boolean closing;

	private static EventLoopGroup bossGroup;
	private static EventLoopGroup workerGroup;

	protected final ConnectionDescriptor descriptor;
	protected final boolean sendingEnabled;
	protected final boolean receivingEnabled;

	protected IMessageLogger outputMessageLogger;

	protected IMessageLogger inputMessageLogger;

	protected Channel ch;
	@SuppressWarnings("rawtypes")
	protected AbstractBootstrap bootstrap;

	protected final List<IMessageConsoleListener> messageListeners;

	public ConnectionHandler(String id, ConnectionDescriptor descriptor) {
		super();
		this.id = id;
		this.descriptor = descriptor;
		messageListeners = new ArrayList<IMessageConsoleListener>();
		sendingEnabled = (descriptor.type == ConnectionType.TX | descriptor.type == ConnectionType.TX_RX);
		receivingEnabled = (descriptor.type == ConnectionType.RX | descriptor.type == ConnectionType.TX_RX);
	}

	protected EventLoopGroup getBossGroup() {
		if (bossGroup == null && !onExit)
			bossGroup = new NioEventLoopGroup();
		return bossGroup;
	}

	protected EventLoopGroup getWorkerGroup() {
		if (workerGroup == null && !onExit)
			workerGroup = new NioEventLoopGroup();
		return workerGroup;
	}

	public String getId() {
		return id;
	}

	@Override
	public ConnectionDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public final void close() {
		closing = true;
		unjoin();

		/*
		 * if (ch != null && ch.isOpen()) { try { ch.close();
		 * ch.closeFuture().sync(); } catch (InterruptedException e) {
		 * logger.warn(e.getMessage(), e); } }
		 */

		channelsClose();
		ch = null;
		log.debug("Channel " + id + " closed");
	}

	public static void finish() {

		if (!onExit) {
			onExit = true;
			// TODO: si se hace, deja el socket pillado en el cliente (!!!)
			/*
			 * if (bossGroup != null && !bossGroup.isTerminated()) {
			 * bossGroup.shutdownGracefully().addListener( new
			 * GenericFutureListener() {
			 * 
			 * @Override public void operationComplete(Future future) throws
			 * Exception { bossGroup = null; } }); } if (workerGroup!= null &&
			 * !workerGroup.isTerminated()) {
			 * workerGroup.shutdownGracefully().addListener( new
			 * GenericFutureListener() {
			 * 
			 * @Override public void operationComplete(Future future) throws
			 * Exception { workerGroup = null; } }); }
			 */
		}
	}

	protected abstract void channelsClose();

	@Override
	public void open() throws ConnectException {
		closing = false;
		configureBootstrap();
	}

	protected abstract void configureBootstrap() throws ConnectException;

	protected abstract void unjoin();

	@Override
	public boolean isOpen() {
		boolean open = (ch != null) && ch.isOpen();
		return open;
	}

	@Override
	public boolean isActive() {
		return (ch != null && ch.isActive());
	}

	protected void fireMessageReceived(ChannelHandlerContext paramChannelHandlerContext, MessageConsole paramI)
			throws Exception {

		if (inputMessageLogger != null)
			inputMessageLogger.logMessage(paramI);
		log.trace("Receiving " + id + "...");
		for (IMessageConsoleListener listener : messageListeners) {
			listener.messageReceived(paramChannelHandlerContext, (MessageConsole) paramI, getDescriptor());
		}

	}

	protected void fireConnectionEstablished(ChannelHandlerContext ctx) throws Exception {
		for (IMessageConsoleListener listener : messageListeners) {
			listener.connectionEstablished(ctx, getDescriptor());
		}
	}

	protected void fireConnectionReleased(ChannelHandlerContext ctx) throws Exception {
		for (IMessageConsoleListener listener : messageListeners) {
			listener.connectionReleased(ctx, getDescriptor());
		}
	}

	public void addMessageListener(IMessageConsoleListener listener) {
		messageListeners.add(listener);
	}

	public void removeMessageListener(IMessageConsoleListener listener) {
		messageListeners.remove(listener);
	}

	public void removeAllMessageListeners() {
		messageListeners.clear();
	}

	@Override
	public abstract boolean isServer();

	public IMessageLogger getInputMessageLogger() {
		return inputMessageLogger;
	}

	public void setInputMessageLogger(IMessageLogger inputMessageLogger) {
		this.inputMessageLogger = inputMessageLogger;
	}

	public IMessageLogger getOutputMessageLogger() {
		return outputMessageLogger;
	}

	public void setOutputMessageLogger(IMessageLogger outputMessageLogger) {
		this.outputMessageLogger = outputMessageLogger;
	}
}
