package parkeerGarageMVCTest.Model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class parkeerGarageViewModel extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CarParkView carParkView;
	private int numberOfFloors;
	private int numberOfRows;
	private int numberOfPlaces;
	private int numberOfOpenSpots;
	private parkeerGarageCar[][][] cars;

	public parkeerGarageViewModel(int numberOfFloors, int numberOfRows, int numberOfPlaces) {
		this.numberOfFloors = numberOfFloors;
		this.numberOfRows = numberOfRows;
		this.numberOfPlaces = numberOfPlaces;
		this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
		cars = new parkeerGarageCar[numberOfFloors][numberOfRows][numberOfPlaces];

		carParkView = new CarParkView();

		Container contentPane = getContentPane();
		contentPane.add(carParkView, BorderLayout.EAST);
		pack();
		setVisible(true);

		updateView();
	}

	public void updateView() {
		carParkView.updateView();
	}
	
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public int getNumberOfPlaces() {
		return numberOfPlaces;
	}

	public int getNumberOfOpenSpots() {
		return numberOfOpenSpots;
	}

	public parkeerGarageCar getCarAt(parkeerGarageLocation location) {
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
		numberOfOpenSpots++;
		return car;
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

	private boolean locationIsValid(parkeerGarageLocation freeLocation) {
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
