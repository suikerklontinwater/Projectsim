
import java.util.Random;
import java.awt.*;

// Deze klasse geeft aan wanneer een auto geparkeerd staat op een locatie
public class AdHocCar extends Car {
	private static final Color COLOR = Color.red;

	// Auto word in een locatie gezet en blijft voor een random tijd op de locatie
	public AdHocCar() {
		Random random = new Random();
		int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
		this.setMinutesLeft(stayMinutes);
		this.setHasToPay(true);
	}

	// De kleur wanneer een auto is geparkeerd op een locatie
	public Color getColor() {
		return COLOR;
	}
}
