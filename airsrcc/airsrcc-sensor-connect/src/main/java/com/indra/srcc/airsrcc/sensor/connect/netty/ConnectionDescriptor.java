package com.indra.srcc.airsrcc.sensor.connect.netty;

import java.net.InetSocketAddress;


public class ConnectionDescriptor {
	protected final int id;
	protected final String description;
	protected final ConnectionProtocol protocol;
	protected final ConnectionType type;
	protected final InetSocketAddress localAddress;
	protected final InetSocketAddress remoteAddress;
	protected final boolean keepAlive;
	protected final boolean reconnect;
	protected final boolean server;
	protected final boolean ssl;
	protected final long reconnectDelay;

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isReconnect() {
		return reconnect;
	}

	public boolean isSsl() {
		return ssl;
	}

	public long getReconnectDelay() {
		return reconnectDelay;
	}

	public transient boolean used;


	public ConnectionDescriptor(int id, String description, ConnectionProtocol protocol, ConnectionType type,
			InetSocketAddress localAddress, InetSocketAddress remoteAddress, boolean server, boolean keepAlive,
			final boolean reconnect, final long reconnectDelay, final boolean ssl) {
		super();
		this.id = id;
		this.description = description;
		this.protocol = protocol;
		this.type = type;
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
		this.server = server;
		this.keepAlive = keepAlive;
		this.reconnect = reconnect;
		this.reconnectDelay = reconnectDelay;
		this.ssl = ssl;

		this.used = false;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(this.getClass().getSimpleName()).append(": ");

		result.append("(").append(id).append(") ").append("[").append(description).append("]");
		result.append(" ").append(protocol).append(", ").append(type).append(", server = ").append(server)
				.append(", ssl = ").append(ssl).append(", keepalive = ").append(keepAlive);
		result.append(" {local = ").append(localAddress).append(", remote = ").append(remoteAddress).append("}");
		return result.toString();
	}


	public int getId() {
		return id;
	}


	public String getDescription() {
		return description;
	}


	public ConnectionProtocol getProtocol() {
		return protocol;
	}


	public ConnectionType getType() {
		return type;
	}


	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}


	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}


	public boolean isKeepAlive() {
		return keepAlive;
	}


	public boolean isServer() {
		return server;
	}

	public ConnectionDescriptor invert() {
		ConnectionDescriptor result;
		final int id = -this.id;
		final String description = "(INVERSE) " + this.description;
		final ConnectionProtocol protocol = this.protocol;
		final ConnectionType type = (this.type != ConnectionType.TX_RX
				? (this.type == ConnectionType.TX ? ConnectionType.RX : ConnectionType.TX) : ConnectionType.TX_RX);
		final InetSocketAddress localAddress = (protocol == ConnectionProtocol.UDP_MULTICAST ? this.localAddress
				: this.remoteAddress);
		final InetSocketAddress remoteAddress = (protocol == ConnectionProtocol.UDP_MULTICAST ? this.remoteAddress
				: this.localAddress);
		final boolean keepAlive = this.keepAlive;
		final boolean server = (protocol == ConnectionProtocol.TCP ? !this.server : false);
		final boolean ssl = this.ssl;
		result = new ConnectionDescriptor(id, description, protocol, type, localAddress, remoteAddress, server,
				keepAlive, this.reconnect, this.reconnectDelay, ssl);
		return result;

	}
}
