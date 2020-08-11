import java.io.PrintWriter;

/**
 * Data class that stores information for a single Refrigerated Packaged Cargo Item,
 * two constructors one for direct instantiation and the second for persistence
 * the constructor accepting an id is always called
 */
public class RefrigeratedPackagedCargo extends PackagedCargo{
	private final boolean isFrozen;

	public RefrigeratedPackagedCargo(String name, String destination, String pickupLocation,
									 int heightCM, int widthCM, int depthCM, int weightInGrams,
									 boolean isFrozen) {
		this(Cargo.getIdCounter(), name, destination,pickupLocation, heightCM, widthCM, depthCM,
				weightInGrams, isFrozen);
	}

	public RefrigeratedPackagedCargo(int id, String name, String destination, String pickupLocation,
									 int heightCM, int widthCM, int depthCM, int weightInGrams,
									 boolean isFrozen) {
		// the packagedCargo type will throw an exception
		super(id, name, destination, pickupLocation, heightCM, widthCM, depthCM, weightInGrams);
		this.isFrozen = isFrozen;
		super.setRate(0.25);
	}

	// prints information to console upon request
	@Override
	public void print() {
		super.print();
		String f = (isFrozen)? "Yes " : "No";
		System.out.printf("%-16s -> %s\n", "Frozen", f);
	}

	// writes a refrigerated packaged cargo object to defined file
	@Override
	public void writeToFile(PrintWriter pw) {
		String str =  "";
		str = "r\t";
		str += getId() + "\t";
		str += getCustomerName() + "\t";
		str += getDestination() + "\t";
		str += getPickupLocation() + "\t";
		str += getHeightCM() + "\t";
		str += getWidthCM() + "\t";
		str += getDepthCM() + "\t";
		str += getWeightInGrams() + "\t";
		str += getIsFrozen() + "\t";
		pw.write(str + "\n");
	}

	public boolean getIsFrozen(){
		return isFrozen;
	}
}