package sjsu.cmpe.B295.sensorDataCollection;

import java.util.ArrayList;
import java.util.Random;

public class HumidityCollectorTask extends ParallelTask {

	private long from;
	private long to;
	private long duration;
	private ArrayList<Integer> data;
	private Random random;

	public void generateData(long duration) {
		random = new Random();
		data = new ArrayList<>();
		for (int i = 0; i < duration; i++) {
			data.add(20 + random.nextInt(20));
		}
	}

	public HumidityCollectorTask(long from, long to) {
		this.from = from;
		this.to = to;
		this.duration = to - from;
		generateData(duration);
	}

	public HumidityCollectorTask() {
		// TODO Auto-generated constructor stub
	}

	public HumidityCollectorTask(long duration) {
		this.duration = duration;
		generateData(duration);
	}

	@Override
	public void perform() {

		data.stream().forEach(datapoint -> {
			System.out.println("Humidity value:[" + datapoint + "]");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

}
