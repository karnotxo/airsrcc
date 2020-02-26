package com.indra.srcc.airsrcc.sensor.connect.messaging;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext; 
import io.netty.handler.codec.MessageToByteEncoder;


public class MessageConsoleEncoder extends MessageToByteEncoder<MessageConsole> {


	@Override
	protected void encode(ChannelHandlerContext ctx, MessageConsole msg, ByteBuf buffer) throws Exception {
		msg.encode(ctx, buffer);
	}

}
