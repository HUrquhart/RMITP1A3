import java.io.PrintWriter;

/**
 * Data class that stores information for a single Packaged Cargo Item,
 * two constructors one for direct instantiation and the second for persistence
 * constructor accepting an id is always called
 */
public class PackagedCargo extends Cargo {

	private final int heightCM;
	private final int widthCM;
	private final int depthCM;
	private final int weightInGrams;

	public PackagedCargo(String name, String destination, String pickupLocation,
						 int heightCM, int widthCM, int depthCM, int weightInGrams) throws
			IllegalArgumentException {
		this(Cargo.getIdCounter(), name, destination, pickupLocation, heightCM, widthCM, depthCM, weightInGrams);
	}

	public PackagedCargo(int id, String name, String destination, String pickupLocation,
						 int heightCM, int widthCM, int depthCM, int weightInGrams) throws
			IllegalArgumentException {

		super(id, name,destination, pickupLocation);
		if(heightCM > 180 || widthCM > 180 || depthCM > 180){
			throw new IllegalArgumentException("No single Dimension can be greater then 180cm");
		}
		this.heightCM = heightCM;
		this.widthCM = widthCM;
		this.depthCM = depthCM;
		this.weightInGrams = weightInGrams;
		super.setRate(0.15);
	}

	@Override
	public double calculateCost() {
		// I calculate the ‘cubic weight’, which is the package height * width * depth
		// converted to meters * 250,
		double weightInKilograms = (double)weightInGrams/1000;
		double cubicWeight = (heightCM/100.0)*(widthCM/100.0)*(depthCM/100.0) * 250;
		if(cubicWeight > weightInKilograms){
			return cubicWeight * super.getRate();
		}else{
			return weightInKilograms * super.getRate();
		}
	}

	// getters
	public int getHeightCM(){
		return this.heightCM;
	}

	public int getWidthCM(){
		return this.widthCM;
	}

	public int getDepthCM(){
		return this.depthCM;
	}

	public int getWeightInGrams(){
		return this.weightInGrams;
	}

	// prints information to console upon request
	@Override
	public void print() {
		super.print();
		System.out.printf("%-16s -> %dcm\n", "Package Height", heightCM);
		System.out.printf("%-16s -> %dcm\n", "Package Width", widthCM);
		System.out.printf("%-16s -> %dcm\n", "Package Depth", heightCM);
		System.out.printf("%-16s -> %dg\n", "Package Weight", weightInGrams);
	}

	// writes a packaged cargo object to defined file
	@Override
	public void writeToFIle(PrintWriter pw) {
		String str =  "";
		str = "p\t";
		str += getId() + "\t";
		str += getCustomerName() + "\t";
		str += getDestination() + "\t";
		str += getPickupLocation() + "\t";
		str += getHeightCM() + "\t";
		str += getWidthCM() + "\t";
		str += getDepthCM() + "\t";
		str += getWeightInGrams() + "\t";
		pw.write(str + "\n");
	}
}