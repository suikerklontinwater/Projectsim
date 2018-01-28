package parkeerGarageMVCTest.Model;


import java.awt.Color;
import java.util.Random;

// Deze klasse geeft aan wanneer een auto geparkeerd staat op een locatie
public class parkeerGarageAdHocCar extends parkeerGarageCar {
	private static final Color meme = Color.red;

	// Auto word in een locatie gezet en blijft voor een random tijd op de locatie
	public parkeerGarageAdHocCar() {
		Random random = new Random();
		int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(true);
	}

	// De kleur wanneer een auto is geparkeerd op een locatie
	public Color getColor() {
		return meme;
	}
}
