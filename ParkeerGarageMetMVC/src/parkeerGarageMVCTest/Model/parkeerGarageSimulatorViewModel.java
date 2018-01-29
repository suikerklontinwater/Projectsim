package parkeerGarageMVCTest.Model;


import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

import parkeerGarageMVCTest.View.parkeerGarageView;

public class parkeerGarageSimulatorViewModel extends JFrame {
	private static final long serialVersionUID = 1L;
	private parkeerGarageView.CarParkView carParkView;
	private static int numberOfFloors;
	private static int numberOfRows;
	private static int numberOfPlaces;
	private static int numberOfOpenSpots;
	private static parkeerGarageCar[][][] cars;
	private static float delCar = 0;

	public parkeerGarageSimulatorViewModel(int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		parkeerGarageSimulatorViewModel.numberOfFloors = numberOfFloors;
		parkeerGarageSimulatorViewModel.numberOfRows = numberOfRows;
		parkeerGarageSimulatorViewModel.numberOfPlaces = numberOfPlaces;
		parkeerGarageSimulatorViewModel.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
		cars = new parkeerGarageCar[numberOfFloors][numberOfRows][numberOfPlaces];

		carParkView = new parkeerGarageView.CarParkView();

		Container contentPane = getContentPane();
		contentPane.add(carParkView, BorderLayout.EAST);
		pack();
		setVisible(true);

		updateView();
	}

	public void updateView() {
		carParkView.updateView();
	}
	
	public static int getNumberOfFloors() {
		return numberOfFloors;
	}

	public static int getNumberOfRows() {
		return numberOfRows;
	}

	public static int getNumberOfPlaces() {
		return numberOfPlaces;
	}

	public static int getNumberOfOpenSpots() {
		return numberOfOpenSpots;
	}

	public static parkeerGarageCar getCarAt(parkeerGarageLocation location) {
		if (!locationIsValid(location)) {
			return null;
		}
		return cars[location.getFloor()][location.getRow()][location.getPlace()];
	}

	public boolean setCarAt(parkeerGarageLocation freeLocation, parkeerGarageCar car) {
		if (!locationIsValid(freeLocation)) {
			return false;
		}
		parkeerGarageCar oldCar = getCarAt(freeLocation);
		if (oldCar == null) {
			cars[freeLocation.getFloor()][freeLocation.getRow()][freeLocation.getPlace()] = car;
			car.setLocation(freeLocation);
			numberOfOpenSpots--;
			return true;
		}
		return false;
	}

	public parkeerGarageCar removeCarAt(parkeerGarageLocation location) {
		if (!locationIsValid(location)) {
			return null;
		}
		parkeerGarageCar car = getCarAt(location);
		if (car == null) {
			return null;
		}
		cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
		car.setLocation(null);
		delCar++;
		numberOfOpenSpots++;
		return car;
	}
	public static float getDelCar() {
		return delCar;
	}

	public parkeerGarageLocation getFirstFreeLocation() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					parkeerGarageLocation location = new parkeerGarageLocation(floor, row, place);
					if (getCarAt(location) == null) {
						return location;
					}
				}
			}
		}
		return null;
	}

	public parkeerGarageCar getFirstLeavingCar() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					parkeerGarageLocation location = new parkeerGarageLocation(floor, row, place);
					parkeerGarageCar car = getCarAt(location);
					if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
						return car;
					}
				}
			}
		}
		return null;
	}

	public void tick() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					parkeerGarageLocation location = new parkeerGarageLocation(floor, row, place);
					parkeerGarageCar car = getCarAt(location);
					if (car != null) {
						car.tick();
					}
				}
			}
		}
	}

	private static boolean locationIsValid(parkeerGarageLocation freeLocation) {
		int floor = freeLocation.getFloor();
		int row = freeLocation.getRow();
		int place = freeLocation.getPlace();
		if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0
				|| place > numberOfPlaces) {
			return false;
		}
		return true;
	}
}