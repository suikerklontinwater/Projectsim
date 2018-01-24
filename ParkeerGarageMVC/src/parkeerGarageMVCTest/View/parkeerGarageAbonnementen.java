package parkeerGarageMVCTest.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import parkeerGarageMVCTest.Model.parkeerGarageCar;
import parkeerGarageMVCTest.Model.parkeerGarageLocation;

public class parkeerGarageAbonnementen extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CarParkView carParkViewAbbo;
	private int numberOfFloorsAbbo;
	private int numberOfRowsAbbo;
	private int numberOfPlacesAbbo;
	private int numberOfOpenSpotsAbbo;
	private parkeerGarageCar[][][] carsAbbo;

	public parkeerGarageAbonnementen(int numberOfFloorsAbbo, int numberOfRowsAbbo, int numberOfPlacesAbbo) {
		this.numberOfFloorsAbbo = numberOfFloorsAbbo;
		this.numberOfRowsAbbo = numberOfRowsAbbo;
		this.numberOfPlacesAbbo = numberOfPlacesAbbo;
		this.numberOfOpenSpotsAbbo = numberOfFloorsAbbo * numberOfRowsAbbo * numberOfPlacesAbbo;
		carsAbbo = new parkeerGarageCar[numberOfFloorsAbbo][numberOfRowsAbbo][numberOfPlacesAbbo];

		carParkViewAbbo = new CarParkView();

		Container contentPane = getContentPane();
		contentPane.add(carParkViewAbbo, BorderLayout.SOUTH);
		pack();
		setVisible(true);

		updateView();
	}

	public void updateView() {
		carParkViewAbbo.updateView();
	}

	public int getNumberOfFloors() {
		return numberOfFloorsAbbo;
	}

	public int getNumberOfRows() {
		return numberOfRowsAbbo;
	}

	public int getNumberOfPlaces() {
		return numberOfPlacesAbbo;
	}

	public int getNumberOfOpenSpots() {
		return numberOfOpenSpotsAbbo;
	}

	public parkeerGarageCar getCarAt(parkeerGarageLocation location) {
		if (!locationIsValid(location)) {
			return null;
		}
		return carsAbbo[location.getFloor()][location.getRow()][location.getPlace()];
	}

	public boolean setCarAt(parkeerGarageLocation freeLocation, parkeerGarageCar car) {
		if (!locationIsValid(freeLocation)) {
			return false;
		}
		parkeerGarageCar oldCar = getCarAt(freeLocation);
		if (oldCar == null) {
			carsAbbo[freeLocation.getFloor()][freeLocation.getRow()][freeLocation.getPlace()] = car;
			car.setLocation(freeLocation);
			numberOfOpenSpotsAbbo--;
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
		carsAbbo[location.getFloor()][location.getRow()][location.getPlace()] = null;
		car.setLocation(null);
		numberOfOpenSpotsAbbo++;
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
		if (floor < 0 || floor >= numberOfFloorsAbbo || row < 0 || row > numberOfRowsAbbo || place < 0
				|| place > numberOfPlacesAbbo) {
			return false;
		}
		return true;
	}

	private class CarParkView extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Dimension size;
		private Image carParkImage;

		/**
		 * Constructor for objects of class CarPark
		 */
		public CarParkView() {
			size = new Dimension(0, 0);
		}

		/**
		 * Overridden. Tell the GUI manager how big we would like to be.
		 */
		public Dimension getPreferredSize() {
			return new Dimension(270, 550);
		}

		/**
		 * Overriden. The car park view component needs to be redisplayed. Copy the
		 * internal image to screen.
		 */
		public void paintComponent(Graphics g) {
			if (carParkImage == null) {
				return;
			}

			Dimension currentSize = getSize();
			if (size.equals(currentSize)) {
				g.drawImage(carParkImage, 0, 0, null);
			} else {
				// Rescale the previous image.
				g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
			}
		}

		public void updateView() {
			// Create a new car park image if the size has changed.
			if (!size.equals(getSize())) {
				size = getSize();
				carParkImage = createImage(size.width, size.height);
			}
			Graphics graphics = carParkImage.getGraphics();
			for (int floor = 0; floor < getNumberOfFloors(); floor++) {
				for (int row = 0; row < getNumberOfRows(); row++) {
					for (int place = 0; place < getNumberOfPlaces(); place++) {
						parkeerGarageLocation location = new parkeerGarageLocation(floor, row, place);
						parkeerGarageCar car = getCarAt(location);
						Color color = car == null ? Color.white : car.getColor();
						drawPlace(graphics, location, color);
					}
				}
			}
			repaint();
		}

		/**
		 * Paint a place on this car park view in a given color.
		 */
		private void drawPlace(Graphics graphics, parkeerGarageLocation location, Color color) {
			graphics.setColor(color);
			graphics.fillRect(location.getFloor() * 260 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 75
					+ (location.getRow() % 2) * 20, 60 + location.getPlace() * 10, 20 - 1, 10 - 1); // TODO use dynamic
																									// size or constants
		}
	}

}
