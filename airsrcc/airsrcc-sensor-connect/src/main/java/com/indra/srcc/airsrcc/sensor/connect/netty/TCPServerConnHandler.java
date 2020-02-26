package com.indra.srcc.airsrcc.sensor.connect.netty;

import java.net.SocketAddress;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;

import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleDecoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleEncoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class TCPServerConnHandler extends ServerConnectionHandler implements IConnectionHandler {

	private SslContext sslCtx;

	private final Map<SocketAddress, ChannelHandlerContext> clients;

	protected TCPServerConnHandler(String id, ConnectionDescriptor descriptor) {
		super(id, descriptor);
		clients = new HashMap<SocketAddress, ChannelHandlerContext>();
		try {
			if (descriptor.ssl) {
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				//sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
				sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			} else {
				sslCtx = null;
			}
		} catch (SSLException e) {
			sslCtx = null;
			log.warn(e.getMessage(), e);
		} catch (CertificateException e) {
			sslCtx = null;
			log.warn(e.getMessage(), e);
		}
	}

	@Override
	protected void configureBootstrap() {
		bootstrap = new ServerBootstrap();
		((ServerBootstrap) bootstrap).group(getBossGroup(), getWorkerGroup()).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100).childOption(ChannelOption.SO_KEEPALIVE, descriptor.keepAlive);
		((ServerBootstrap) bootstrap).childOption(ChannelOption.SO_REUSEADDR, true);

		if (log.isDebugEnabled())
			((ServerBootstrap) bootstrap).handler(new LoggingHandler(LogLevel.INFO));
		((ServerBootstrap) bootstrap).childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				if (sslCtx != null) {
					pipeline.addLast(sslCtx.newHandler(ch.alloc()));
				}
				if (log.isTraceEnabled())
					pipeline.addLast(new LoggingHandler(LogLevel.INFO));
				if (sendingEnabled)
					pipeline.addLast(new MessageConsoleEncoder());
				if (receivingEnabled) {
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
							clients.put(ctx.channel().remoteAddress(), ctx);
							fireConnectionEstablished(ctx);
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							clients.remove(ctx.channel().remoteAddress());
							ctx.close();
							fireConnectionReleased(ctx);
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

		for (ChannelHandlerContext ctx : clients.values()) {
			if (ctx.channel().isActive()) {
				ctx.channel().close();
			}
		}

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
	protected ChannelFuture sendMsg(MessageConsole data, SocketAddress clientAddress) {
		ChannelHandlerContext ctx = clients.get(clientAddress);
		if (ctx != null && ctx.channel().isActive())
			return ctx.writeAndFlush(data);
		else
			return null;
	}

	@Override
	protected void unjoin() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SocketAddress> getClients() {
		List<SocketAddress> result = new ArrayList<SocketAddress>();
		result.addAll(clients.keySet());
		return result;
	}

}
