package sjsu.cmpe.B295.sensorDataCollection;

import java.util.ArrayList;
import java.util.Random;

public class TemperatureCollectorTask extends ParallelTask {
	private long from;
	private long to;
	private long duration;
	private ArrayList<Integer> data;
	private Random random;
	private static IParallelizable p;

	public void generateData(long duration) {
		data = new ArrayList<>();
		random = new Random();
		for (int i = 0; i < duration; i++) {
			data.add(40 + random.nextInt(20));
		}
	}

	public TemperatureCollectorTask(long from, long to) {
		this.from = from;
		this.to = to;
		this.duration = to - from;
		generateData(duration);
	}

	public TemperatureCollectorTask() {
		// TODO Auto-generated constructor stub
	}

	public TemperatureCollectorTask(long duration) {
		this.duration = duration;
		generateData(duration);
	}

	@Override
	public void perform() {
		System.out.println("In Temp Perform");
		data.stream().forEach(datapoint -> {
			System.out.println("Temp value:["+datapoint+"]");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
