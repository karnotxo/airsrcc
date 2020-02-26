package com.indra.srcc.airsrcc.sensor.connect.netty;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsole;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleDecoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleEncoder;
import com.indra.srcc.airsrcc.sensor.connect.messaging.MessageConsoleHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class TCPConnHandler extends ClientConnectionHandler implements IConnectionHandler {


	public static final long RECONNECT_DELAY = 2000L;

	private SslContext sslCtx;

	private boolean autoReconnect;
	private long reconnectDelay;

	protected TCPConnHandler(String id, ConnectionDescriptor descriptor) {
		super(id, descriptor);
		this.autoReconnect = descriptor.reconnect;
		if (descriptor.reconnectDelay == -1L)
			reconnectDelay = RECONNECT_DELAY;
		else
			reconnectDelay = descriptor.reconnectDelay;
		try {
			if (descriptor.ssl) {
				sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
				//sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
			} else {
				sslCtx = null;
			}
		} catch (SSLException e) {
			sslCtx = null;
			log.warn(e.getMessage(), e);
		}
	}

	@Override
	protected void configureBootstrap() {

		bootstrap = new Bootstrap();
		((Bootstrap) bootstrap).group(getWorkerGroup()).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, descriptor.keepAlive);
		((Bootstrap) bootstrap).option(ChannelOption.TCP_NODELAY, true);
		((Bootstrap) bootstrap).option(ChannelOption.SO_REUSEADDR, true);

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				if (sslCtx != null) {
					pipeline.addLast(
							sslCtx.newHandler(ch.alloc(), descriptor.remoteAddress.getAddress().getHostAddress(),
									descriptor.remoteAddress.getPort()));
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
							fireConnectionEstablished(ctx);
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							fireConnectionReleased(ctx);
						}

						@Override
						public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
							if (closing)
								return;
							if (autoReconnect) {
								log.trace("Sleeping for: " + reconnectDelay + "ms");
								if (ch != null)
									ch.close();
								final EventLoop loop = ctx.channel().eventLoop();
								loop.schedule(new Runnable() {
									@Override
									public void run() {
										if (closing)
											return;
										log.trace("Reconnecting to: " + descriptor.remoteAddress.getHostString()
												+ ':' + descriptor.remoteAddress.getPort());
										configureBootstrap();
									}
								}, reconnectDelay, TimeUnit.MILLISECONDS);
							}
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
							log.warn(cause.getMessage(), cause);
							channelsClose();
						}

					});
				}
			}
		});
		// client
		ch = ((Bootstrap) bootstrap).connect(descriptor.remoteAddress, descriptor.localAddress).syncUninterruptibly()
				.channel();
		// ch.channel().closeFuture().sync();
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
		if (log.isTraceEnabled())
			log.trace("Started sending in thread " + Thread.currentThread().getId() + " at "
					+ System.currentTimeMillis() + "ms for handler " + this.getId());
		ChannelFuture result = ch.writeAndFlush(data);
		if (log.isTraceEnabled())
			log.trace("Finished sending in thread " + Thread.currentThread().getId() + " at "
					+ System.currentTimeMillis() + "ms for handler " + this.getId());
		return result;
	}

	@Override
	protected void unjoin() {
		// TODO Auto-generated method stub

	}

}
