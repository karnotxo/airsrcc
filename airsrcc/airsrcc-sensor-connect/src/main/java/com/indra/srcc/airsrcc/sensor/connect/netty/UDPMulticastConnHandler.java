package com.indra.srcc.airsrcc.sensor.connect.netty;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;

import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleDecoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleEncoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class UDPMulticastConnHandler extends ClientConnectionHandler implements IConnectionHandler {


	private Bootstrap bootstrap;
	private NetworkInterface ni;;

	protected UDPMulticastConnHandler(String id, ConnectionDescriptor descriptor) {
		super(id, descriptor);
	}

	@Override
	public void configureBootstrap() {
		try {
			ni = NetworkInterface.getByInetAddress(descriptor.localAddress.getAddress());
			bootstrap = new Bootstrap();
			bootstrap.group(
					getWorkerGroup());/* channel(NioDatagramChannel.class) */
			// .option(ChannelOption.SO_KEEPALIVE, descriptor.keepAlive);
			bootstrap.channelFactory(() -> new NioDatagramChannel(InternetProtocolFamily.IPv4));
			
			if (receivingEnabled)
				bootstrap.localAddress(PlatformDependent.isWindows()
						? new InetSocketAddress(descriptor.localAddress.getAddress(),
								descriptor.remoteAddress.getPort())
						: new InetSocketAddress(descriptor.remoteAddress.getPort()));
			// if (sendingEnabled)
			bootstrap.option(ChannelOption.SO_BROADCAST, true);
			// if (sendingEnabled)
			bootstrap.option(ChannelOption.IP_MULTICAST_IF, ni);
			// if (sendingEnabled)
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			/*
			 * if (sendingEnabled)
			 * bootstrap.option(ChannelOption.IP_MULTICAST_LOOP_DISABLED,
			 * false);
			 */
			bootstrap.option(ChannelOption.SO_RCVBUF, 2048);
			bootstrap.option(ChannelOption.IP_MULTICAST_TTL, 32);
			log.debug("am i logged on as root: " + PlatformDependent.maybeSuperUser());
			bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {

				@Override
				protected void initChannel(NioDatagramChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					if (sendingEnabled) {
						pipeline.addLast(new MessageToMessageEncoder<ByteBuf>() {
							/**
							 * {@inheritDoc}
							 */
							@Override
							protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
								out.add(new DatagramPacket(msg.retain(), descriptor.remoteAddress));
							}

							/**
							 * {@inheritDoc}
							 */
							@Override
							public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
								log.warn("UDP encoding error " + UDPMulticastConnHandler.super.getId());
							}

						});
						pipeline.addLast(new MessageConsoleEncoder());
					}
					if (receivingEnabled) {
						pipeline.addLast(new MessageToMessageDecoder<DatagramPacket>() {
							@Override
							protected void decode(ChannelHandlerContext paramChannelHandlerContext, DatagramPacket msg,
									List<Object> lista) {
								lista.add(msg.content().retain());
							}

						});
						// pipeline.addLast(new AsterixFrameLegthDecoder());
						pipeline.addLast(new MessageConsoleDecoder());
						pipeline.addLast(new MessageConsoleHandler() {

							@Override
							protected void channelRead0(ChannelHandlerContext ctx, MessageConsole msg)
									throws Exception {
								msg.setMulticastAddress(ctx.channel().remoteAddress());
								msg.setLocalAddress(ctx.channel().localAddress());
								fireMessageReceived(ctx, msg);
							}

							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								log.debug("Channel " + UDPMulticastConnHandler.super.getId() + " activated");
							}

							@Override
							public void channelInactive(ChannelHandlerContext ctx) throws Exception {
								log.debug("Channel " + UDPMulticastConnHandler.super.getId() + " deactivated");
							}

						});
					}
				}
			});
			// Multicast
			if (descriptor.remoteAddress.getAddress().isMulticastAddress()) {
				if (receivingEnabled)
					ch = bootstrap.bind(descriptor.remoteAddress.getPort())
							/*
							 * ch = (PlatformDependent.isWindows() ?
							 * bootstrap.bind(
							 * descriptor.localAddress.getAddress(),
							 * descriptor.remoteAddress.getPort()) : bootstrap
							 * .bind(descriptor.remoteAddress.getPort()))
							 */
							.syncUninterruptibly().channel();

				else
					ch = bootstrap.bind(descriptor.localAddress.getAddress(), 0).syncUninterruptibly().channel();
				log.debug("Channel " + UDPMulticastConnHandler.super.getId() + " opened");
				if (receivingEnabled) {
					ChannelFuture future = ((DatagramChannel) ch).joinGroup(descriptor.remoteAddress, ni);
					log.debug("Result of Join: " + future.toString());
				}
			} else {
				log.warn("No multicast address: " + descriptor.remoteAddress.getAddress());
				ch = bootstrap.bind(descriptor.localAddress).syncUninterruptibly().channel(); // TIENE
																								// SENTIDO????
			}
		} catch (SocketException e) {
			log.warn(e.getMessage(), e);
		}
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
		return ch.writeAndFlush(data);
	}

	@Override
	protected void unjoin() {

		log.trace(
				"unjoining: " + descriptor.remoteAddress.getHostString() + ':' + descriptor.remoteAddress.getPort());
		if (receivingEnabled) {
			ChannelFuture future = ((DatagramChannel) ch).leaveGroup(descriptor.remoteAddress, ni);
			log.debug("Result of Leave: " + future.toString());
		}
	}

}
