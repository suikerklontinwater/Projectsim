package parkeerGarageMVCTest.View;

import java.util.*;

import parkeerGarageMVCTest.Model.parkeerGarageSimulator;


public class histogram extends parkeerGarageView {
	int wda;
	public void displayHistogram() {
	//	System.out.println("Enter 5 number in a same line (eg. 5 2 4 7 9.)");
		Scanner in = new Scanner(System.in);
		wda = 0;
		if(wda ==0) {
			parkeerGarageSimulator sim = new parkeerGarageSimulator();
			
		}
		int i, n, j;
		for(i=1; i<=5; i++) {
			n = in.nextInt();
			System.out.printf("\n%2d ", n);
			for(j=1; j<=n; j++) {
				System.out.print("*");
			}
		}
		
		
	}
}
