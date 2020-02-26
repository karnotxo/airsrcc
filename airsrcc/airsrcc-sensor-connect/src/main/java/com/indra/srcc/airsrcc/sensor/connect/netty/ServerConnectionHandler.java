package com.indra.srcc.airsrcc.sensor.connect.netty;

import java.net.SocketAddress;
import java.util.List;

import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public abstract class ServerConnectionHandler extends ConnectionHandler {

	public ServerConnectionHandler(String id, ConnectionDescriptor descriptor) {
		super(id, descriptor);
	}

	@Override
	public final boolean isServer() {
		return true;
	}

	public void send(MessageConsole data, SocketAddress clientAddress) throws Exception {
		if (!sendingEnabled)
			throw new Exception("Operation not allowed");
		if (outputMessageLogger != null)
			outputMessageLogger.logMessage(data);
		log.trace("Sending from channel " + super.getId() + " to client " + clientAddress + "...");
		if (isOpen() && data != null) {
			final ChannelFuture f = sendMsg(data, clientAddress);
			if (f != null) {
				f.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) {
						assert f == future;
						// ctx.close();
					}
				}); // (4)
				return;
			}
		}

		log.warn("Channel " + super.getId() + " is not open for sending!");
		/*
		 * throw new Exception(
		 * "Operation not allowed while channel still closed");
		 */

	}

	protected abstract ChannelFuture sendMsg(MessageConsole data, SocketAddress clientAddress);

	public abstract List<SocketAddress> getClients();
}
