package com.indra.srcc.airsrcc.sensor.connect.messaging;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class MessageConsoleDecoder extends ByteToMessageDecoder {


	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf ByteBuf, List<Object> result)
			throws Exception {
		try {
			MessageConsole tmp = MessageConsole.decode(ByteBuf);
			if (tmp != null)
				result.add(tmp);
		} catch (Exception e) {
			log.warn(e.getMessage() + (e.getStackTrace().length > 0 ? "\n" + e.getStackTrace()[0] : ""));
		}
	}

}
