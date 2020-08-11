import java.io.PrintWriter;

/**
 * Data class that stores information for a single Cargo Item,
 * this class must be extended as it is abstract
 * only one constructor is required as the second constructor is always called from
 * sub classes
 */
public abstract class Cargo {
	private final String customerName;
	private final String destination;
	private final String pickupLocation;

	private final int id;
	private double rate;
	private static int idCounter = 100;

	public Cargo(int id, String customerName, String destination, String pickupLocation) {
		this.id = id;
		Cargo.idCounter++;
		this.customerName = customerName;
		this.destination = destination;
		this.pickupLocation = pickupLocation;
	}

	@Override
	public String toString() {
		return String.format("%3d | %-20s | %-20s | %-20s",
				id, customerName, pickupLocation, destination);
	}

	public void print() {
		System.out.printf("%-16s -> %-40d\n", "Cargo Identifier", id);
		System.out.printf("%-16s -> %-40s\n", "Customer Name", customerName);
		System.out.printf("%-16s -> %-40s\n", "Destination", destination);
		System.out.printf("%-16s -> %-40s\n", "Pickup Location", pickupLocation);
		System.out.printf("%-16s -> $%-40.2f\n", "Transport Cost", calculateCost());
	}

	// allows us to deduce the total cost of the cargos transportation from instance variables
	public abstract double calculateCost();

	// getters
	public int getId() {
		return this.id;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public String getDestination() {
		return this.destination;
	}

	public String getPickupLocation() {
		return this.pickupLocation;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	// static methods
	public static int getIdCounter() {
		return idCounter;
	}

	// make each object responsible for writing to file
	public abstract void writeToFile(PrintWriter pw);
}