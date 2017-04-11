package sjsu.cmpe.B295.s2da;

import java.util.Arrays;

import sjsu.cmpe.B295.common.MessageProto.Message;
import sjsu.cmpe.B295.common.MessageProto.Message.Builder;
import sjsu.cmpe.B295.common.MessageProto.Message.MessageType;

public class InputSplitter implements IMessageHandler {
	private IMessageHandler successor = null;
	int[][] chunks;
	int totalChunks = 0;
	int[] array;

	public InputSplitter() {

	}

	public InputSplitter(int[] arr) {
		int totalArraysN = availableWorkers();
		this.array = new int[arr.length];
		this.array = Arrays.copyOf(arr, arr.length);

		this.chunks = new int[totalArraysN][];
		int sizeOfNminus1arrays = arr.length / totalArraysN;
		int sizeOfNthArray = arr.length
			- ((totalArraysN - 1) * sizeOfNminus1arrays);
		for (int i = 0; i < totalArraysN - 1; i++) {
			this.chunks[i] = new int[sizeOfNminus1arrays];
		}
		this.chunks[this.chunks.length - 1] = new int[sizeOfNthArray];
	}

	public int[][] getChunks() {
		createChunks();
		return this.chunks;
	}

	public void createChunks() {
		int k = 0;
		for (int i = 0; i < this.chunks.length; i++) {
			for (int j = 0; j < this.chunks[i].length; j++) {
				this.chunks[i][j] = this.array[k];
				k++;
			}
		}
	}

	public int availableWorkers() {
		return 3;
	}

	@Override
	public void handleMessage(Message msg) {
		Message receivedMsg = (Message) msg;
		System.out.println("****Received Message by Splitter****");
		System.out.println(receivedMsg.toString());
		System.out.println("****Received Message Ends Here****");
		if (msg.getMessageType().equals(MessageType.SPLIT)) {

			Builder builder = Message.newBuilder()
				.setMessageType(Message.MessageType.MAP)
				.setMessageStr("Map Input");
			Message msgForMapper = builder.build();

			successor.handleMessage(msgForMapper);

		} else {
			if (successor != null) {
				successor.handleMessage(receivedMsg);
			}
		}

	}

	@Override
	public void setNextHandler(IMessageHandler nextHandler) {
		// TODO Auto-generated method stub
		this.successor = nextHandler;
	}

}
