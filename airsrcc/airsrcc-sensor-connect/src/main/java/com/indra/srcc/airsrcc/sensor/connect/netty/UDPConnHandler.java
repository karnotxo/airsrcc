package com.indra.srcc.airsrcc.sensor.connect.netty;

import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleDecoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleEncoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class UDPConnHandler extends ClientConnectionHandler implements IConnectionHandler {

	protected UDPConnHandler(String id, ConnectionDescriptor descriptor) {
		super(id, descriptor);
	}

	@Override
	protected void configureBootstrap() {
		bootstrap = new Bootstrap();
		((Bootstrap) bootstrap).group(getWorkerGroup());
		((Bootstrap) bootstrap).channelFactory(() -> new NioDatagramChannel(InternetProtocolFamily.IPv4));
		
		((Bootstrap) bootstrap).option(ChannelOption.SO_BROADCAST, false);
		((Bootstrap) bootstrap).option(ChannelOption.SO_REUSEADDR, true);

		bootstrap.handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				if (sendingEnabled) {
					pipeline.addLast(new MessageToMessageEncoder<ByteBuf>() {
						protected void encode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf msg,
								List<Object> lista) {
							lista.add(new DatagramPacket(msg.retain(), descriptor.remoteAddress));
						}

					});
					pipeline.addLast(new MessageConsoleEncoder());
				}
				if (receivingEnabled) {
					pipeline.addLast(new MessageToMessageDecoder<DatagramPacket>() {
						protected void decode(ChannelHandlerContext paramChannelHandlerContext, DatagramPacket msg,
								List<Object> lista) {
							lista.add(msg.content());
							msg.retain();
						}

					});
					// pipeline.addLast(new AsterixFrameLegthDecoder());
					pipeline.addLast(new MessageConsoleDecoder());
					pipeline.addLast(new MessageConsoleHandler() {

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, MessageConsole msg) throws Exception {
							msg.setRemoteAddress(ctx.channel().remoteAddress());
							msg.setLocalAddress(ctx.channel().localAddress());
							fireMessageReceived(ctx, msg);
						}

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							log.debug("Channel activated");
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							log.debug("Channel deactivated");
						}

					});
				}
			}
		});
		// Server
		ch = bootstrap.bind(descriptor.localAddress).syncUninterruptibly().channel();
		// client
		// ch = bootstrap.bind().syncUninterruptibly().channel();
	}

	@Override
	public void channelsClose() {

		log.trace("Closing chanels: " + descriptor.remoteAddress.getHostString() + ':'
				+ descriptor.remoteAddress.getPort());
		if (ch != null && ch.isActive()) {
			ch.close().addListener(new GenericFutureListener<ChannelFuture>() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					ch = null;
				}
			});
		}
	}

	@Override
	protected ChannelFuture sendMsg(MessageConsole data) {
		log.trace("Started sending in thread " + Thread.currentThread().getId() + " at " + System.currentTimeMillis()
				+ "ms for handler " + this.getId());
		ChannelFuture result = ch.writeAndFlush(data);
		log.trace("Finished sending in thread " + Thread.currentThread().getId() + " at "
				+ System.currentTimeMillis() + "ms for handler " + this.getId());
		return result;
	}

	@Override
	protected void unjoin() {
		// TODO Auto-generated method stub

	}

}
