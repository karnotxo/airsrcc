package com.indra.srcc.airsrcc.sensor.connect.messaging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;

@Slf4j

public abstract class MessageConsole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8290844031728809316L;

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static boolean calculateCRC = false;
	public static boolean calculateHash = false;

	protected long timestamp;

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public abstract Object getIdKey();

	private SocketAddress localAddress;
	private SocketAddress remoteAddress;
	private SocketAddress multicastAddress;

	private Exception error = null;

	private static CRC32 crccalculator = new CRC32();
	private static StreamingXXHash32 hashCalculator = 
			XXHashFactory.fastestInstance().newStreamingHash32(0x9A44b21c);
	// used to initialize
	// the hash value,
	// use whatever value
	// you want,
	// but always the same

	private long crc = 0;

	private long hash = 0;

	public abstract int getSize();

	protected abstract MessageConsole internalDecode(final ByteBuf buffer) throws Exception;

	public static MessageConsole deserialize(final DataInputStream is) throws Exception {
		short cAT = (short) is.readUnsignedByte();

		int lEN = is.readUnsignedShort();

		byte[] buffer = new byte[lEN];
		buffer[0] = (byte) (cAT & 0xFF);
		buffer[1] = (byte) ((lEN & 0xff00) >> 8);
		buffer[2] = (byte) (lEN & 0xff);
		is.read(buffer, 3, lEN - 3);
		return fromBytes(buffer);

	}

	public void serialize(final DataOutputStream os) throws Exception {
		byte[] buffer = toBytes();
		os.write(buffer);
	}

	public byte[] toBytes() throws Exception {
		byte[] buffer = new byte[this.getSize()];
		ByteBuf bytebuffer = Unpooled.wrappedBuffer(buffer);
		bytebuffer.clear();
		encode(null, bytebuffer);
		return buffer;
	}

	public static MessageConsole fromBytes(byte[] buffer) throws Exception {
		return decode(Unpooled.wrappedBuffer(buffer));
	}

	public static MessageConsole decode(final ByteBuf buffer) throws Exception {
		MessageConsole message = null;
		// Wait until the length prefix is available.
		int idx = buffer.readerIndex();
		int readableBytes = buffer.readableBytes();
		int lEN = 0;
		short cAT = 0;
		try {

			if (readableBytes < 3) {
				return null;
			}
			buffer.markReaderIndex();
			cAT = buffer.readUnsignedByte();

			lEN = buffer.readUnsignedShort();

			if (readableBytes < lEN) {
				buffer.resetReaderIndex();
				return null;
			} else if (lEN == 0) {
				buffer.skipBytes(readableBytes - (buffer.readerIndex() - idx));
				return null;
			}
			log.trace("Readable bytes: " + readableBytes + ", idx=" + idx + ", LEN=" + lEN + ", CAT= " + cAT);

			byte[] tmp = null;
			if (lEN != 0 && cAT != 0) {
				// MSG ASTERIX

				if (calculateCRC || calculateHash || log.isDebugEnabled()) {
					if (buffer.hasArray())
						tmp = Arrays.copyOfRange(buffer.array(), idx, idx + lEN);
					else {
						tmp = new byte[lEN];
						buffer.getBytes(idx, tmp);
					}
				}

				if (log.isTraceEnabled()) {
					log.trace("CAT " + cAT + ", readableBytes={" + readableBytes + "}, LEN={" + lEN + "}");
					String hexResponseString = bytesToHex(tmp);
					log.trace("RECEIVED hex dump: " + hexResponseString);
				}

				if (cAT != 253 && cAT != 10 && cAT != 34 && cAT != 20 && cAT != 21 && cAT != 48) {
					log.info("Unsupported ASTERIX Category: " + cAT);
					buffer.skipBytes(lEN - 3);
					return null;
				}
				//message = new MessageAsterix(cAT, lEN); //TODO

				//message.internalDecode(buffer); //TODO
				//int size = 3;
				//for (int i = 0; i < ((MessageAsterix) message).getBodyElementCount(); i++) {
					// size += ((MessageAsterix) message).getBodyAt(i).getSize();
				//}
				//if (size < lEN) {
				//	if (log.isDebugEnabled()) {
				//		log.debug("Bad decoded ASTERIX: Readable bytes: " + readableBytes + ", idx=" + idx + ", LEN="
				//				+ lEN + ", CAT= " + cAT);
				//		String hexResponseString = toHex(buffer.nioBuffer());
				//		log.debug("Bad decoded ASTERIX: hex dump: " + hexResponseString);
				//	}
				//	log.warn("Bad size "+size+" decoded ASTERIX message!!!");
				//	message.error = new Exception("Wrong size specified: ("+lEN+">"+size+")");
				//}
				

			}
			
			if (message!= null) {
				if (lEN != buffer.readerIndex()-idx) {

					buffer.skipBytes(buffer.readableBytes());
					message = null;
					if (buffer.hasArray())
						tmp = Arrays.copyOfRange(buffer.array(), idx, idx + readableBytes);
					else {
						tmp = new byte[readableBytes];
						buffer.getBytes(idx, tmp);
					}
					log.warn("Error at " + (readableBytes - buffer.readableBytes()) + "\nMessage dump (" + idx + ", "
							+ tmp.length + "): \n" + bytesToHex(tmp));
				}

				if (calculateCRC) {
					crccalculator.reset();
					crccalculator.update(tmp);
					message.setCRC(crccalculator.getValue());
				}
				if (calculateHash) {
					hashCalculator.reset();
					hashCalculator.update(buffer.array(), 0, buffer.readerIndex());
					message.setHash(hashCalculator.getValue());
				}

				if (log.isTraceEnabled())
					log.trace(message.toString());
			}
		} catch (Exception ex) {
			buffer.skipBytes(buffer.readableBytes());
			if (message != null)
				message.setError(ex);
			log.warn("Error decoding console message (CAT:" + cAT + ", Length: " + lEN + ")", ex);
			byte[] tmp = null;
			if (buffer.hasArray())
				tmp = Arrays.copyOfRange(buffer.array(), idx, idx + readableBytes);
			else {
				tmp = new byte[readableBytes];
				buffer.getBytes(idx, tmp);
			}
			log.warn("Error at " + (readableBytes - buffer.readableBytes()) + "\nMessage dump (" + idx + ", "
					+ tmp.length + "): \n" + bytesToHex(tmp));

			if (message != null)
				log.trace("Message " + message.toString());
		}
		return message;
	}

	protected abstract Object internalEncode(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception;
	

	public Object encode(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {

		internalEncode(ctx, buffer);

		if (calculateCRC || calculateHash || log.isDebugEnabled()) {
			/*byte[] tmp;
			if (buffer.hasArray())
				tmp = Arrays.copyOf(buffer.array(), getSize());
			else {
				tmp = new byte[getSize()];
				buffer.getBytes(0, tmp);
			}*/
			if (calculateCRC) {
				crccalculator.reset();
				crccalculator.update(buffer.nioBuffer());
				crc = crccalculator.getValue();
			}
			if (calculateHash) {
				//hashCalculator.reset();
				//hashCalculator.update(tmp, 0, tmp.length);
				//hash = hashCalculator.getValue();
				hash = hash32(buffer);
			}
			if (log.isDebugEnabled()) {
				String hexResponseString = toHex(buffer.nioBuffer());
				log.trace(
						"ENCODING [message=" + this.getClass().getName() + ", size=" + buffer.readableBytes() + "]");
				log.trace("SENDING hex dump: " + hexResponseString);
			}
		}
		return buffer;
	}

	public long getCRC() {
		return crc;
	}

	protected void setCRC(long crc) {
		this.crc = crc;
	}

	public long getHash() {
		return hash;
	}

	protected void setHash(long value) {
		this.hash = value;
	}

	public Exception getError() {
		return error;
	}

	protected void setError(Exception error) {
		this.error = error;
	}

	public void setRemoteAddress(SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setLocalAddress(SocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	public void setMulticastAddress(SocketAddress address) {
		this.multicastAddress = address;
	}

	public SocketAddress getMulticastAddress() {
		return multicastAddress;
	}
	
	private static final int C1 = 0xcc9e2d51;
	private static final int C2 = 0x1b873593;

	public static int hash32(ByteBuf data) {
		return hash32(data, data.readerIndex(), data.readableBytes(), 0);
	}

	public static int hash32(ByteBuf data, final int offset, final int length) {
		return hash32(data, offset, length, 0);
	}

	public static int hash32(ByteBuf data, final int offset, final int length, final int seed) {
		//final ByteBuf ordered = data.order(ByteOrder.LITTLE_ENDIAN);

		int h = seed;

		final int len4 = length >>> 2;
				final int end4 = offset + (len4 << 2);

				for (int i = offset; i < end4; i += 4) {
					int k = data.getIntLE(i);

					k *= C1;
					k = k << 15 | k >>> 17;
					k *= C2;

					h ^= k;
					h = h << 13 | h >>> 19;
					h = h * 5 + 0xe6546b64;
				}

				int k = 0;
				switch (length & 3) {
				case 3:
					k = (data.getByte(end4 + 2) & 0xff) << 16;
				case 2:
					k |= (data.getByte(end4 + 1) & 0xff) << 8;
				case 1:
					k |= data.getByte(end4) & 0xff;

					k *= C1;
					k = (k << 15) | (k >>> 17);
					k *= C2;
					h ^= k;
				}

				h ^= length;
				h ^= h >>> 16;
					h *= 0x85ebca6b;
					h ^= h >>> 13;
			h *= 0xc2b2ae35;
			h ^= h >>> 16;

			return h;
	}
	
	public static String toHex(ByteBuffer bb) {
        StringBuilder sb = new StringBuilder("[ ");
        while (bb.hasRemaining()) {
            sb.append(String.format("%02X ", bb.get()));
        }//from  w w  w.  j  a va  2  s .  c  o  m
        sb.append("]");
        return sb.toString();
    }
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
