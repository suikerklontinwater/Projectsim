package parkeerGarageMVCTest.Model;

import java.util.Random;


public class parkeerGarageSimulator {

	private static final String AD_HOC = "1";
	private static final String PASS = "2";

	private parkeerGarageCarQueue entranceCarQueue;
	private parkeerGarageCarQueue entrancePassQueue;
	private parkeerGarageCarQueue paymentCarQueue;
	private parkeerGarageCarQueue exitCarQueue;
	private parkeerGarageSimulatorViewModel simulatorView;
	private int day = 3;
	private int hour = 0;
	private int minute = 0;
	private int tickPause = 5;
	int ticks = 0;
	int b = 0;
	int d = 0;
	static float PASScar = 0;
	static float ADHOCcar = 0;

	int weekDayArrivals = 20; // average number of arriving cars per hour
	int weekendArrivals = 20; // average number of arriving cars per hour
	int weekDayPassArrivals = 8; // average number of arriving cars per hour
	int weekendPassArrivals = 8; // average number of arriving cars per hour

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
		simulatorView = new parkeerGarageSimulatorViewModel(3, 6, 30);
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
		//countAllCars();
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
		if (minute == 30) {
			checkTime();
		}
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

	private void checkTime() {
		String dayString = null;
		switch (day) {
		case 0:
			dayString = "Monday";
			break;
		case 1:
			dayString = "Tuesday";
			break;
		case 2:
			dayString = "Wednesday";
			break;
		case 3:
			dayString = "Thursday";
			break;
		case 4:
			dayString = "Friday";
			break;
		case 5:
			dayString = "Saturday";
			break;
		case 6:
			dayString = "Sunday";
			break;
		}
		switch (hour) {
		case 0:
			switch (dayString) {
			case "Saturday":
			case "Sunday":
				setCars(10, -6, 30, -6);
				break;
			default:
				setCars(14, -1 , 42, -1);
			}
		case 3:
			weekDayArrivals = 20;
			weekendArrivals = 20;
			weekDayPassArrivals = 7;
			weekendPassArrivals = 7;
			setCars(40, 1, 120, 1);
			break;
		case 6:
			setCars(25, 1, 75, 1);
			break;
		case 9:
			switch (dayString) {
			case "Sunday":
				setCars(10, 2, 15 ,1);
				break;

			default:
				setCars(8, 1, 24, 1);
			}
			break;
		case 12:
			switch (dayString) {
			case "Sunday":
				setCars(10, 4 ,15, 2);
				break;

			default:
				setCars(12, 3, 36, 3);
			}
			break;
		case 15:
			switch (dayString) {
			case "Thursday":
				setCars(10, 2, 15, 1);
				break;

			case "Friday":
			case "Saturday":
				setCars(10, 3, 15, 2);
				break;

			default:
				setCars(20, 1, 60, 1);
			}
			break;
		case 18:
			switch (dayString) {
			case "Thursday":
				setCars(18, 2, 27, 1);
				break;
			case "Friday":
			case "Saturday":
				setCars(15, 3, 15, 1);
				break;
			default:
				setCars(15, -3 , 15, -1);
			}
		case 21:
			switch (dayString) {
			case "Thursday":
				setCars(10, -7, 30, -7);
				break;
			case "Friday":
			case "Saturday":
				break;
			default:
				setCars(10, -2, 15, -1);
			}
			break;
		}

		String time = (hour + ":30");
		if (hour < 10) {
			System.out.println(dayString + ": 0" + time);
		} else {
			System.out.println(dayString + ": " + time);
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
		while (queue.carsInQueue() > 0 && parkeerGarageSimulatorViewModel.getNumberOfOpenSpots() > 0 && i < enterSpeed) {
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
	
 public static float getADHOCcar() {
	 return ADHOCcar;
 }
 public static float getPASScar() {
	 return PASScar;
 }
	
	
	private void addArrivingCars(int numberOfCars, String type) {
		// Add the cars to the back of the queue.
		switch (type) {
		case AD_HOC:
			for (int i = 0; i < numberOfCars; i++) {
				entranceCarQueue.addCar(new parkeerGarageAdHocCar());
				ADHOCcar--;
			}
			break;
		case PASS:
			for (int i = 0; i < numberOfCars; i++) {
				entrancePassQueue.addCar(new parkeerGarageParkingPassCar());
				PASScar--;
			}
			break;
		}
	}

	private void carLeavesSpot(parkeerGarageCar car) {
		simulatorView.removeCarAt(car.getLocation());
		exitCarQueue.addCar(car);
	}

	private void setCars(int c, int weekDayArrival, int e, int weekDayPassArrival) {
		for (int a = 1; a < 180; a++) {
			b++;
			d++;
			if (b == c && (weekDayArrivals > 0 && weekendArrivals > 0)) {
				weekDayArrivals += weekDayArrival;
				weekendArrivals += weekDayArrival;
				b = 0;
			}
			if (d == e && (weekDayPassArrivals > 0 && weekendPassArrivals > 0)) {
				weekDayPassArrivals += weekDayPassArrival;
				weekendPassArrivals += weekDayPassArrival;
				d = 0;
			}
			tick();
		}
		b = 0;
		d= 0;
	}

}
