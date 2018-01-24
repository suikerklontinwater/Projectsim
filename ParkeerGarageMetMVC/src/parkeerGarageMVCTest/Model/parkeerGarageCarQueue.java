package parkeerGarageMVCTest.Model;


import java.util.LinkedList;
import java.util.Queue;

public class parkeerGarageCarQueue {
	private Queue<parkeerGarageCar> queue = new LinkedList<>();

	public boolean addCar(parkeerGarageCar car) {
		return queue.add(car);
	}

	public parkeerGarageCar removeCar() {
		return queue.poll();
	}

	public int carsInQueue() {
		return queue.size();
	}
}
