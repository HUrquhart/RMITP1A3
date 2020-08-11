import java.io.PrintWriter;

/**
 * Data class that stores information for a single Bulk Cargo Item,
 * two constructors one for direct instantiation and the second for persistence
 * constructor accepting an id is always called
 */
public class BulkCargo extends Cargo {
	private final int transportationDistanceKm;
	private final double cubicMeters;

	public BulkCargo(String customerName, String destination, String pickupLocation,
					 double cubicMeters, int transportationDistanceKm)
			throws IllegalArgumentException {
		this(Cargo.getIdCounter(), customerName, destination, pickupLocation, cubicMeters, transportationDistanceKm);
	}

	public BulkCargo(int id, String customerName, String destination, String pickupLocation,
					 double cubicMeters, int transportationDistanceKm)
			throws IllegalArgumentException {
		super(id, customerName, destination, pickupLocation);
		if(cubicMeters >= 6.00){
			throw new IllegalArgumentException("Volume cannot be greater then 6m^3");
		}
		this.cubicMeters = cubicMeters;
		this.transportationDistanceKm = transportationDistanceKm;
		super.setRate(.30);
	}

	/**
	 * Some cargo, such as wood chips, is ‘bulk cargo’. For this type, I need to record how many
	 * cubic meters there is (a double) and the number of kilometers it is going to be transported.
	 * The cost of a load of bulk cargo is
	 * the number of km * the number of cubic meters * the ‘bulk rate’ which is $0.30.
	 * @return total cost
	 */
	@Override
	public double calculateCost() {
		return cubicMeters*transportationDistanceKm*super.getRate();
	}

	// writes a bulk cargo object to defined file
	@Override
	public void writeToFile(PrintWriter pw) {
		String str =  "";
		str = "b\t";
		str += getId() + "\t";
		str += getCustomerName() + "\t";
		str += getDestination() + "\t";
		str += getPickupLocation() + "\t";
		str += getTransportationDistanceKm() + "\t";
		str += getVolume() + "\t";
		pw.write(str + "\n");
	}

	// writes information to console on request
	@Override
	public void print() {
		super.print();
		System.out.printf("%-16s -> %d\n", "Distance (km)", transportationDistanceKm);
		System.out.printf("%-16s -> %.2f\n", "Volume (m^3)", cubicMeters);
	}

	// getters
	public int getTransportationDistanceKm(){
		return transportationDistanceKm;
	}

	public double getVolume(){
		return cubicMeters;
	}
}