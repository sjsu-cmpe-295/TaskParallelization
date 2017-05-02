package com.example;

import java.util.HashMap;

public class DataStructure {

	public Metrics humidityMetrics;

	public Metrics temperatueMetrics;

	public HashMap<String, Double> humidityDataPoints;

	public HashMap<String, Double> temperatureDataPoints;

	public DataStructure() {
		this.humidityDataPoints = new HashMap<>();
		this.temperatureDataPoints = new HashMap<>();
	}

	public class Metrics {
		private double average;
		private int count;
		private double maximum;
		private double minimum;

		public double getAverage() {
			return average;
		}

		public int getCount() {
			return count;
		}

		public double getMaximum() {
			return maximum;
		}

		public double getMinimum() {
			return minimum;
		}

		public void setAverage(double average) {
			this.average = average;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public void setMaximum(double maximum) {
			this.maximum = maximum;
		}

		public void setMinimum(double minimum) {
			this.minimum = minimum;
		}
	}

	

	public Metrics getTemperatueMetrics() {
		return temperatueMetrics;
	}

	public void setTemperatueMetrics(Metrics temperatueMetrics) {
		this.temperatueMetrics = temperatueMetrics;
	}

	public Metrics getHumidityMetrics() {
		return humidityMetrics;
	}

	public void setHumidityMetrics(Metrics humidityMetrics) {
		this.humidityMetrics = humidityMetrics;
	}

	
	public HashMap<String, Double> getTemperatureDataPoints() {
		return temperatureDataPoints;
	}

	public void setTemperatureDataPoints(
		HashMap<String, Double> humidityDataPoints) {
		this.temperatureDataPoints = humidityDataPoints;
	}

	public void addTemperatureDataPoint(String date, double value) {
		this.temperatureDataPoints.put(date, value);
	}

	public HashMap<String, Double> getHumidityDataPoints() {
		return humidityDataPoints;
	}

	public void setHumidityDataPoints(
		HashMap<String, Double> humidityDataPoints) {
		this.humidityDataPoints = humidityDataPoints;
	}

	public void addHumidityDataPoint(String date, double value) {
		this.humidityDataPoints.put(date, value);
	}

}
//
// "humidityMetrics": {
// "avg": 30,
// "count": 100,
// "max": 40,
// "min": 25
// },
// "humidityDataPoints": {
// "2016-3-3 10:45:30": 25,
// "2016-3-3 10:45:31": 25,
// "2016-3-3 10:45:32": 25,
// "2016-3-3 10:45:33": 25,
// "2016-3-3 10:45:34": 25
// },
// "temperatureMetrics": {
// "avg": 30,
// "count": 100,
// "max": 40,
// "min": 25
// },
// "temperatureDataPoints": {
// "2016-3-3 10:45:30": 25,
// "2016-3-3 10:45:31": 25,
// "2016-3-3 10:45:32": 25,
// "2016-3-3 10:45:33": 25,
// "2016-3-3 10:45:34": 25
// }
// }
