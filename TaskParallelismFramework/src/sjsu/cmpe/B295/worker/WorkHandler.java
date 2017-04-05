package sjsu.cmpe.B295.worker;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class WorkHandler extends ChannelInboundHandlerAdapter {
	public static final ChannelGroup allConnected = new DefaultChannelGroup(
		GlobalEventExecutor.INSTANCE);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("In Channel Read for Worker");
		ByteBuf recvdMsg = (ByteBuf) msg;
		System.out
			.println("Received Bytes from Client =" + recvdMsg.readableBytes());
		byte[] recvdMsgBytes = new byte[recvdMsg.readableBytes()];
		recvdMsg.readBytes(recvdMsgBytes);

		IntBuffer source = ByteBuffer.wrap(recvdMsgBytes).asIntBuffer();
		IntBuffer destination = IntBuffer.allocate(recvdMsgBytes.length / 4);
		destination.put(source);
		int[] chunk = destination.array();

		System.out
			.println("Sum of this worker is : " + getSumOfChunkElements(chunk));
		// ByteBuf sendSumMsg = Unpooled.buffer(2);
		// sendSumMsg.writeInt(getSumOfChunkElements(chunk));
		// ctx.write(sendSumMsg);

	}

	public int getSumOfChunkElements(int[] chunk) {
		int chunkSum = 0;
		for (int i = 0; i < chunk.length; i++) {
			chunkSum += chunk[i];
		}
		return chunkSum;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
//		System.out.println(ctx.name() + " is active now from worker!");
//		int arr[] = new int[1];
//		arr[0]=1;
//		
//		ByteBuffer byteBuffer = ByteBuffer.allocate(arr.length * 4);        
//        IntBuffer intBuffer = byteBuffer.asIntBuffer();
//        intBuffer.put(arr);
//
//        byte[] arrBytes = byteBuffer.array();
//		System.out.println(arrBytes.length);
//
//		ByteBuf firstMessage = Unpooled.buffer(arrBytes.length);
//		firstMessage.writeBytes(arrBytes);
//		ctx.writeAndFlush(firstMessage);
	}

}
