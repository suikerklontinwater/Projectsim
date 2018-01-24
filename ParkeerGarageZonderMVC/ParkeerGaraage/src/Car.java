
import java.awt.*;

public abstract class Car {
	// De locatie van de auto in de garage
	private Location location;
	// Hoeveel minuten de auto nog in de garage mag staan
	private int minutesLeft;
	// True als de eigenaar van de auto aan het betalen is
	private boolean isPaying;
	// True als de auto betaald is
	private boolean hasToPay;

	/**
	 * Constructor for objects of class Car
	 */
	public Car() {

	}

	// @return Geeft de locatie weer van de auto
	public Location getLocation() {
		return location;
	}

	// @param Verandert de locatie van de auto
	public void setLocation(Location location) {
		this.location = location;
	}

	// @return Geeft weer hoeveel minuten de auto nog in de garage mag staan
	public int getMinutesLeft() {
		return minutesLeft;
	}

	// @param Verandert het aantal minuten van de auto
	public void setMinutesLeft(int minutesLeft) {
		this.minutesLeft = minutesLeft;
	}

	// @return Geeft weer of de eigenaar van de auto aan het betalen is of niet
	public boolean getIsPaying() {
		return isPaying;
	}

	// @param De waarde van isPaying wordt verandert in true of false
	public void setIsPaying(boolean isPaying) {
		this.isPaying = isPaying;
	}

	// @return Geeft weer of de eigenaar moet betalen van de auto
	public boolean getHasToPay() {
		return hasToPay;
	}

	// @param verandert de waartde van hasToPay in true of false
	public void setHasToPay(boolean hasToPay) {
		this.hasToPay = hasToPay;
	}

	// @param Zorgt ervoor dat de tijd van het parkeren word verlaagd
	public void tick() {
		minutesLeft--;
	}

	public abstract Color getColor();
}