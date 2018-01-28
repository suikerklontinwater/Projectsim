package parkeerGarageMVCTest.Model;

import java.util.Random;

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
	// private parkeerGarageAbonnementen abonnementen;

	private int day = 0;
	private int hour = 2;
	private int minute = 0;
	private int tickPause = 100;
	int ticks = 0;

	int weekDayArrivals = 20; // average number of arriving cars per hour
	int weekendArrivals = 40; // average number of arriving cars per hour
	int weekDayPassArrivals = 00; // average number of arriving cars per hour
	int weekendPassArrivals = 50; // average number of arriving cars per hour

	int enterSpeed = 30; // number of cars that can enter per minute
	// int enterSpeed = 10;
	int paymentSpeed = 70; // number of cars that can pay per minute
	int exitSpeed = 5; // number of cars that can leave per minute

	public parkeerGarageSimulator() {
		entranceCarQueue = new parkeerGarageCarQueue();
		entrancePassQueue = new parkeerGarageCarQueue();
		paymentCarQueue = new parkeerGarageCarQueue();
		exitCarQueue = new parkeerGarageCarQueue();
		// simulatorView = new parkeerGarageSimulatorView(2, 4, 40);
		simulatorView = new parkeerGarageSimulatorView(3, 6, 30);
		// abonnementen = new parkeerGarageAbonnementen(1, 4, 45);
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
		ticks++;
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
			checkTime();
		}
		while (hour > 22) {
			hour -= 24;
			day++;
		}
		while (day > 6) {
			day -= 7;
		}

	}

	private void checkTime() {
		int b = 0;
		String dayString = null;
		switch (day) {
		case 0: dayString = "Monday";
				break;
		case 1: dayString = "Tuesday";
				break;
		case 2: dayString = "Wednesday";
				break;
		case 3: dayString = "Thursday";
				break;
		case 4: dayString = "Friday";
				break;
		case 5: dayString = "Saturday";
				break;
		case 6: dayString = "Sunday";
				break;
		}
		switch (hour){
		case 0:
			weekDayArrivals = 20;
			break;
		case 3:
			for (int a = 0; a <178 ; a++) {
				b++;
				if(b == 20) {
					weekDayArrivals += 1;
					b = 0;
				}
				tick();
			}
			if (b != 0) {
				b = 0;
			}
			 break;
		case 6:
			for (int a = 0; a <179 ; a++) {
				b++;
				if(b == 15) {
					weekDayArrivals += 1;
					b= 0;
				}
				tick();
			}
			break;
		case 9:
			weekDayArrivals = 40;
			break;
		case 12:
			weekDayArrivals = 130;
			break;
		case 15:
			weekDayArrivals = 110;
			break;
		case 18:
			weekDayArrivals = 70;
			break;
		case 21:
			weekDayArrivals = 40;
			break;
		}
		String time = (hour + ":00");
		if (hour < 10) {
			System.out.println(dayString +": 0" + time + ": " + weekDayArrivals);
		}
		else {
		System.out.println(dayString + ": " + time+ ": " + weekDayArrivals);
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
