package parkeerGarageMVCTest;

import parkeerGarageMVCTest.Model.parkeerGarageSimulator;
import parkeerGarageMVCTest.View.histogram;
import parkeerGarageMVCTest.View.parkeerGarageView;

public class parkeerGarageTester {

	public static void main(String[] args) {

		//parkeerGarageView pgv = new parkeerGarageView();
		//pgv.gui();
		parkeerGarageSimulator simulator = new parkeerGarageSimulator();
		simulator.run();
		histogram hg = new histogram();
		hg.displayHistogram();
		
	}
}
