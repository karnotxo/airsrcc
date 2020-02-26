package com.indra.srcc.airsrcc.sensor.connect.netty;

import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public abstract class ClientConnectionHandler extends ConnectionHandler {

	public ClientConnectionHandler(String id, ConnectionDescriptor descriptor) {
		super(id, descriptor);
	}

	@Override
	public final boolean isServer() {
		return false;
	}

	public void send(MessageConsole data) throws Exception {
		if (!sendingEnabled)
			throw new Exception("Operation not allowed");
		if (outputMessageLogger != null)
			outputMessageLogger.logMessage(data);
		log.trace("Sending " + super.getId() + "...");
		if (isOpen() && data != null) {
			final ChannelFuture f = sendMsg(data);
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					assert f == future;
					// ctx.close();
				}
			}); // (4)
		} else {
			log.trace("Channel " + super.getId() + " is not open for sending!");
			/*
			 * throw new Exception(
			 * "Operation not allowed while channel still closed");
			 */
		}
	}

	protected abstract ChannelFuture sendMsg(MessageConsole data);

}
