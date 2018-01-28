package parkeerGarageMVCTest.Model;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class parkeerGarageSimulatorViewModel extends JFrame {
	private static final long serialVersionUID = 1L;
	private CarParkView carParkView;
	private int numberOfFloors;
	private int numberOfRows;
	private int numberOfPlaces;
	private int numberOfOpenSpots;
	private parkeerGarageCar[][][] cars;

	public parkeerGarageSimulatorViewModel(int numberOfFloors, int numberOfRows, int numberOfPlaces) {
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

	public class CarParkView extends JPanel {


		private static final long serialVersionUID = 1L;
		private Dimension size;
		private Image carParkImage;


		public CarParkView() {
			size = new Dimension(0, 0);
		}


		public Dimension getPreferredSize() {
			return new Dimension(800, 500);
		}

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

		private void updateView() {
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

		private void drawPlace(Graphics graphics, parkeerGarageLocation location, Color color) {
			graphics.setColor(color);
			graphics.fillRect(location.getFloor() * 260 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 75
					+ (location.getRow() % 2) * 20, 60 + location.getPlace() * 10, 20 - 1, 10 - 1); // TODO use dynamic
																									// size or constants
		}
	}

}

