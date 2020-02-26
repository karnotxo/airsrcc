package com.indra.srcc.airsrcc.sensor.connect.messaging;

import com.indra.srcc.airsrcc.sensor.connect.netty.ConnectionDescriptor;
import io.netty.channel.ChannelHandlerContext;

public interface IMessageConsoleListener {

	public abstract void messageReceived(ChannelHandlerContext ctx, MessageConsole paramI,
			ConnectionDescriptor connectionDescriptor) throws Exception;

	public abstract void connectionEstablished(ChannelHandlerContext ctx, ConnectionDescriptor connectionDescriptor)
			throws Exception;

	public abstract void connectionReleased(ChannelHandlerContext ctx, ConnectionDescriptor connectionDescriptor)
			throws Exception;

}