package parkeerGarageMVCTest.Model;


import java.util.Random;

import parkeerGarageMVCTest.View.parkeerGarageAbonnementen;
import parkeerGarageMVCTest.View.parkeerGarageSimulatorView;


public class parkeerGarageSimulator {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";

	private parkeerGarageCarQueue entranceCarQueue;
	private parkeerGarageCarQueue entrancePassQueue;
	private parkeerGarageCarQueue paymentCarQueue;
	private parkeerGarageCarQueue exitCarQueue;
	private parkeerGarageSimulatorView simulatorView;
	@SuppressWarnings("unused")
	private parkeerGarageAbonnementen abonnementen;

	private int day = 0;
	private int hour = 0;
	private int minute = 0;

	private int tickPause = 100;

	int weekDayArrivals = 100; // average number of arriving cars per hour
	int weekendArrivals = 200; // average number of arriving cars per hour
	int weekDayPassArrivals = 50; // average number of arriving cars per hour
	int weekendPassArrivals = 50; // average number of arriving cars per hour

	int enterSpeed = 30; // number of cars that can enter per minute
	//int enterSpeed = 10; 
	int paymentSpeed = 70; // number of cars that can pay per minute
	int exitSpeed = 5; // number of cars that can leave per minute

	public parkeerGarageSimulator() {
		entranceCarQueue = new parkeerGarageCarQueue();
		entrancePassQueue = new parkeerGarageCarQueue();
		paymentCarQueue = new parkeerGarageCarQueue();
		exitCarQueue = new parkeerGarageCarQueue();
		simulatorView = new parkeerGarageSimulatorView(2, 4, 40);
		abonnementen = new parkeerGarageAbonnementen(1, 4, 45);
	}

	public void run() {
		for (int i = 0; i < 10000; i++) {
			tick();
		}
	}

	private void tick() {
		advanceTime();
		handleExit();
		updateViews();
		// Pause.
		try {
			Thread.sleep(tickPause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handleEntrance();
	}

	private void advanceTime() {
		// Advance the time by one minute.
		minute++;
		while (minute > 59) {
			minute -= 60;
			hour++;
		}
		while (hour > 23) {
			hour -= 24;
			day++;
		}
		while (day > 6) {
			day -= 7;
		}

	}

	private void handleEntrance() {
		carsArriving();
		carsEntering(entrancePassQueue);
		carsEntering(entranceCarQueue);
	}

	private void handleExit() {
		carsReadyToLeave();
		carsPaying();
		carsLeaving();
	}

	private void updateViews() {
		simulatorView.tick();
		// Update the car park view.
		simulatorView.updateView();
	}

	private void carsArriving() {
		int numberOfCars = getNumberOfCars(weekDayArrivals, weekendArrivals);
		addArrivingCars(numberOfCars, AD_HOC);
		numberOfCars = getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
		addArrivingCars(numberOfCars, PASS);
	}

	private void carsEntering(parkeerGarageCarQueue queue) {
		int i = 0;
		// Remove car from the front of the queue and assign to a parking space.
		while (queue.carsInQueue() > 0 && simulatorView.getNumberOfOpenSpots() > 0 && i < enterSpeed) {
			parkeerGarageCar car = queue.removeCar();
			parkeerGarageLocation freeLocation = simulatorView.getFirstFreeLocation();
			simulatorView.setCarAt(freeLocation, car);
			i++;
		}
	}

	private void carsReadyToLeave() {
		// Add leaving cars to the payment queue.
		parkeerGarageCar car = simulatorView.getFirstLeavingCar();
		while (car != null) {
			if (car.getHasToPay()) {
				car.setIsPaying(true);
				paymentCarQueue.addCar(car);
			} else {
				carLeavesSpot(car);
			}
			car = simulatorView.getFirstLeavingCar();
		}
	}

	private void carsPaying() {
		// Let cars pay.
		int i = 0;
		while (paymentCarQueue.carsInQueue() > 0 && i < paymentSpeed) {
			parkeerGarageCar car = paymentCarQueue.removeCar();
			// TODO Handle payment.
			carLeavesSpot(car);
			i++;
		}
	}

	private void carsLeaving() {
		// Let cars leave.
		int i = 0;
		while (exitCarQueue.carsInQueue() > 0 && i < exitSpeed) {
			exitCarQueue.removeCar();
			i++;
		}
	}

	private int getNumberOfCars(int weekDay, int weekend) {
		Random random = new Random();

		// Get the average number of cars that arrive per hour.
		int averageNumberOfCarsPerHour = day < 5 ? weekDay : weekend;

		// Calculate the number of cars that arrive this minute.
		double standardDeviation = averageNumberOfCarsPerHour * 0.3;
		double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
		return (int) Math.round(numberOfCarsPerHour / 60);
	}

	private void addArrivingCars(int numberOfCars, String type) {
		// Add the cars to the back of the queue.
		switch (type) {
		case AD_HOC:
			for (int i = 0; i < numberOfCars; i++) {
				entranceCarQueue.addCar(new parkeerGarageAdHocCar());
			}
			break;
		case PASS:
			for (int i = 0; i < numberOfCars; i++) {
				entrancePassQueue.addCar(new parkeerGarageParkingPassCar());
			}
			break;
		}
	}

	private void carLeavesSpot(parkeerGarageCar car) {
		simulatorView.removeCarAt(car.getLocation());
		exitCarQueue.addCar(car);
	}

}
/*
		Maandag		Dinsdag		Woensdag	Donderdag	Vrijdag		Zaterdag	Zondag
00:00	--			--			--			--			--			--			--
01:00	--			--			--			--			--			--			--
02:00	--			--			--			--			--			--			--
03:00	--			--			--			--			--			--			--
04:00	--			--			--			--			--			--			--
05:00	--			--			--			--			--			--			--
06:00	--			--			--			--			--			--			--
07:00	--			--			--			--			--			--			--
08:00	--			--			--			--			--			--			--
09:00	--			--			--			--			--			--			--
10:00	--			--			--			--			--			--			--
11:00	--			--			--			--			--			--			--
12:00	--			--			--			--			--			--			--
13:00	--			--			--			--			--			--			500
14:00	--			--			--			--			--			--			500
15:00	--			--			--			--			--			--			500
16:00	--			--			--			--			--			--			500
17:00	--			--			--			--			--			--			500
18:00	--			--			--			450			200			250			500
19:00	--			--			--			400			300			350			--
20:00	--			--			--			350			500			500			--
21:00	--			--			--			250			500			500			--
22:00	--			--			--			--			500			500			--
23:00	--			--			--			--			500			500			--

*/