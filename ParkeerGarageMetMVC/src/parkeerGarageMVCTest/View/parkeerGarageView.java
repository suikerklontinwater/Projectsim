package parkeerGarageMVCTest.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import parkeerGarageMVCTest.Model.parkeerGarageCar;
import parkeerGarageMVCTest.Model.parkeerGarageLocation;
import parkeerGarageMVCTest.Model.parkeerGarageSimulator;
import parkeerGarageMVCTest.Model.parkeerGarageSimulatorViewModel;

public class parkeerGarageView {
	@SuppressWarnings("serial")
	public static class CarParkView extends JPanel {

		private Dimension size;
		private Image carParkImage;

		public CarParkView() {
			size = new Dimension(0, 0);
		}

		public Dimension getPreferredSize() {
			return new Dimension(850, 600);
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

		public void updateView() {
			// Create a new car park image if the size has changed.
			if (!size.equals(getSize())) {
				size = getSize();
				carParkImage = createImage(size.width, size.height);
			}
			Graphics graphics = carParkImage.getGraphics();
			for (int floor = 0; floor < parkeerGarageSimulatorViewModel.getNumberOfFloors(); floor++) {
				for (int row = 0; row < parkeerGarageSimulatorViewModel.getNumberOfRows(); row++) {
					for (int place = 0; place < parkeerGarageSimulatorViewModel.getNumberOfPlaces(); place++) {
						parkeerGarageLocation location = new parkeerGarageLocation(floor, row, place);
						parkeerGarageCar car = parkeerGarageSimulatorViewModel.getCarAt(location);
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
																									// size or constants'
			graphics.setColor(Color.black);
			graphics.fillArc(80, 400, 125, 125, 0, 360);
			//graphics.setColor(Color.red);
			//graphics.fillArc(80, 400, 125, 125, 0, Math.round((parkeerGarageSimulatorViewModel.count / 540) * 360));
			graphics.setColor(Color.red);
			graphics.fillArc(80, 400, 125, 125, 0, Math.round((parkeerGarageSimulator.getADHOCcar() / 540) * 360));
			graphics.setColor(Color.blue);
			graphics.fillArc(80, 400, 125, 125, Math.round((parkeerGarageSimulator.getADHOCcar() / 540) * 360), Math.round((parkeerGarageSimulator.getPASScar() / 540) * 360));
		}
	}

}