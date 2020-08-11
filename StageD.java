/*
 * Main class and application logic can be found in this class for the application
 * this class separates out logic from data
 */
import java.io.*;
import java.util.*;

public class StageD {
	private Cargo[] cargo;
	private int cargoCounter;
	private final Scanner scanner;

	public static void main(String[] args) {

		// we can get the users

		StageD s = new StageD(4);

		// we need a way to get out of the loop
		int option = 0;
		while (option != -1) {
			s.showMenu();
			option = s.getMenuInput();
			System.out.println();
		}

		// write current instance to file we are closing
		try {
			s.writeToFile();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// scanner get closed here by default
	}

	public StageD(int initialSize) {
		scanner = new Scanner(System.in);
		try {
			Scanner reader = new Scanner(new FileReader("StageD.txt"));
			this.cargo = readFile(reader, initialSize);
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("No File Found");
			this.cargo = new Cargo[initialSize];
			this.cargoCounter = 0;
		}
	}

	// writes only pertinent information to file so that we can reconstruct everything on load
	private void writeToFile() throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter("StageD.txt"));
		for (int i = 0; i < cargoCounter; i++) {
			cargo[i].writeToFIle(pw);
		}
		pw.flush();
		pw.close();
	}

	// reads contents from file specified and then saves it to the heap in the correct format
	private Cargo[] readFile(Scanner scanner, int size) {
		Cargo[] contents = new Cargo[size];
		while (scanner.hasNext()) {
			// read each line from the file
			// depending on the first item in array assign each item to a different field
			String line = scanner.nextLine();
			String[] fields = line.split("\t");

			// get the common fields
			int i = Integer.parseInt(fields[1]);
			String n = fields[2];    // name as a string
			String dst = fields[3]; // destination
			String p = fields[4];    // pickup location as a string

			switch (fields[0].toLowerCase()) {
				case "b":
					int distance = Integer.parseInt(fields[5]);
					double volume = Double.parseDouble(fields[6]);
					// attempt to create a new item, note this should not error out
					contents[cargoCounter] =
							new BulkCargo(i, n, dst, p, volume, distance);
					break;
				case "p":
					int h = Integer.parseInt(fields[5]); // height in cm
					int w = Integer.parseInt(fields[6]); // width in cm
					int d = Integer.parseInt(fields[7]); // depth in cm
					int grams = Integer.parseInt(fields[8]); // weight in grams
					contents[cargoCounter] =
							new PackagedCargo(i, n, dst, p, h, w, d, grams);
					break;
				case "r":
					h = Integer.parseInt(fields[5]); // height in cm
					w = Integer.parseInt(fields[6]); // width in cm
					d = Integer.parseInt(fields[7]); // depth in cm
					grams = Integer.parseInt(fields[8]); // weight in grams
					boolean f = Boolean.parseBoolean(fields[9]); // is it frozen?
					contents[cargoCounter] =
							new RefrigeratedPackagedCargo(i, n, dst, p, h, w, d, grams, f);
					break;
				default:
					System.err.println("There was an error, too many rows in file");
			}
			cargoCounter++;
		}
		return contents;
	}

	// show the menu to the user when called
	public void showMenu() {
		System.out.println("================== MENU ==================");
		System.out.printf("%-36s -> %2d\n", "Add new cargo", 1);
		System.out.printf("%-36s -> %2d\n", "Remove cargo", 2);
		System.out.printf("%-36s -> %2d\n", "Display all cargo", 3);
		System.out.printf("%-36s -> %2d\n", "Lookup cargo", 4);
		System.out.printf("%-36s -> %2d\n", "Quit", -1);
		System.out.println();
	}

	// when called return the selection to the caller
	public int getMenuInput() {
		System.out.print("Select menu option > ");
		int opt = Integer.parseInt(scanner.nextLine());
		switch (opt) {
			case -1:
				// exit the program
				return -1;
			case 1:
				try {
					addNewCargo();
				} catch (CargoException | IllegalArgumentException c) {
					System.err.println(c.getMessage());
				}
				return 1;
			case 2:
				removeCargoItem();
				return 2;
			case 3:
				if (!displayAllCargo()) {
					System.err.println("There are not cargo items to show");
				}
				return 3;
			case 4:
				lookupCargo();
				return 4;
			default:
				System.err.println("Not a valid menu option");
				return 0;
		}

	}

	// attempt to create a new Cargo object
	public void addNewCargo() throws CargoException {
		if (cargoCounter == cargo.length) {
			throw new CargoException("Cargo area full, remove items first");
		}
		String name 		= promptForField("Customer Name");
		String pickup 		= promptForField("Pickup Location");
		String destination 	= promptForField("Destination");
		int cType 			= getCargoType();

		// intellij indicates that this switch could be replaced by an enhanced switch statement
		switch (cType) {
			case 1:
				BulkCargo c = createNewBulkCargo(name, pickup, destination);
				cargoCreationAlert(c);
				cargo[cargoCounter++] = c;
				break;
			case 2:
				PackagedCargo p = createNewPackagedCargo(name, pickup, destination);
				cargoCreationAlert(p);
				cargo[cargoCounter++] = p;
				break;
			case 3:
				RefrigeratedPackagedCargo r =
						createNewRefrigeratedPackagedCargo(name, pickup, destination);
				cargoCreationAlert(r);
				cargo[cargoCounter++] = r;
				break;
			default:
				System.err.println("Invalid cargo type");
				break;
		}
	}

	// helper function to inform the user that the item was created ...
	private void cargoCreationAlert(Cargo c) {
		System.out.printf("Item %d added at the cost of $ %.2f", c.getId(), c.calculateCost());
	}

	// ensure that the user enters a Specified field
	private String promptForField(String fieldName) {
		String str;
		while (true) {
			System.out.print("Please enter " + fieldName + " > ");
			str = scanner.nextLine();
			if (str.isBlank()) {
				System.err.println(fieldName + " must not be empty");
			} else {
				break;
			}
		}
		return str;
	}

	// to reduce repeated code we return an array containing ints needed by two helper fns
	// [height, width, depth, weight]
	private int[] packagedCargoPrompt(){
		int[] fields = new int[4];
		System.out.print("How high (cm) is the item ? > ");
		fields[0]= Integer.parseInt(scanner.nextLine());
		System.out.print("How wide (cm) is the item ? > ");
		fields[1] = Integer.parseInt(scanner.nextLine());
		System.out.print("How deep (cm) is the item ? > ");
		fields[2] = Integer.parseInt(scanner.nextLine());
		System.out.print("What is the weight in grams ? > ");
		fields[3] = Integer.parseInt(scanner.nextLine());
		return fields;
	}

	// helper fn returns a new RefrigeratedPackagedCargo Item
	private RefrigeratedPackagedCargo createNewRefrigeratedPackagedCargo
	(String n, String p, String dest) {
		int[] i = packagedCargoPrompt();
		System.out.print("Is the cargo Frozen (Y / N) ?> ");
		boolean f = scanner.nextLine().trim().toLowerCase().startsWith("y");
		return new RefrigeratedPackagedCargo(n, dest, p, i[0], i[1], i[2], i[3], f);
	}

	// helper fn returns a new PackagedCargo Item
	private PackagedCargo createNewPackagedCargo(String n, String p, String dest) {
		int[] i = packagedCargoPrompt();
		return new PackagedCargo(n, dest, p, i[0], i[1], i[2], i[3]);
	}

	// helper fn returns a new BulkCargo item
	private BulkCargo createNewBulkCargo(String name, String pickup, String destination) {
		int dst;
		System.out.print("What is the volume (m^3) ? > ");
		double mtrs = Double.parseDouble(scanner.nextLine());
		System.out.print("How far is this cargo being transported (km) ? > ");
		dst = Integer.parseInt(scanner.nextLine());
		return new BulkCargo(name, destination, pickup, mtrs, dst);
	}

	// this function allows us to remove a single item from the array of Cargo objects
	public void removeCargoItem() {
		if(cargoCounter > 0) {
			// get the users desired object to remove
			displayAllCargo();
			System.out.print("Which cargo item(id) would you like to remove? > ");
			int itemIndex = lookupCargoIndex(Integer.parseInt(scanner.nextLine()));
			if (itemIndex == -1) {
				System.err.println("Could not find cargo item to remove");
			} else {
				cargo[itemIndex] = null;
				// rearrange array if cargo item is not last item in array
				// if the item was the last in the array then we do nothing
				if (itemIndex != cargoCounter - 1) {
					Cargo[] temp = new Cargo[cargo.length];
					for (int i = 0; i < cargoCounter; i++) {
						if (i < itemIndex) {
							temp[i] = cargo[i];
						} else if (i + 1 < temp.length) {
							temp[i] = cargo[i + 1];
						}
					}
					cargo = temp;
				}
				cargoCounter--;
			}
		}else{
			System.err.println("No items exist. Cannot remove anything!");
		}
	}

	// this fn displays all items in the cargo array
	// using their provided print methods if possible
	public boolean displayAllCargo() {
		if (cargoCounter == 0) {
			return false;
		}
		System.out.printf("%3s | %-20s | %-20s | %-20s\n",
				"ID", "Customer Name", "Pickup Location", "Destination");
		for (int i = 0; i < cargoCounter; i++) {
			System.out.println(cargo[i].toString());
		}
		return true;
	}

	// Attempts to find a cargo item by id
	public void lookupCargo(){
		if(cargoCounter < 1){
			System.err.println("Cargo must be added to enable this feature");
		}else{
			System.out.print("Which cargo item(id) would you like to display > ");
			int itemIndex = lookupCargoIndex(Integer.parseInt(scanner.nextLine()));
			if(itemIndex < 0){
				System.err.println("Cargo Item does not exist");
			}else{
				cargo[itemIndex].print();
				cargo[itemIndex].calculateCost();
			}
		}
	}

	// finds the index of a specified cargo item otherwise returning -1
	private int lookupCargoIndex(int itemId) {
		int itemIndex = -1;
		for (int i = 0; i < cargoCounter; i++) {
			if (cargo[i].getId() == itemId) {
				itemIndex = i;
				break;
			}
		}
		return itemIndex;
	}

	// helper fn allowing us to prompt the user for the desired cargo type
	// loops until a valid cargo type is provided
	private int getCargoType() {
		System.out.printf("%-36s -> %2d\n", "Bulk Cargo", 1);
		System.out.printf("%-36s -> %2d\n", "Packaged Cargo", 2);
		System.out.printf("%-36s -> %2d\n", "Refrigerated Packaged Cargo", 3);

		String type = "";
		System.out.print("What type of cargo is this? > ");
		while (type.isBlank()) {
			type = scanner.nextLine();
			// this switch is written this way to allow for natural fall through behaviour
			switch (Integer.parseInt(type)) {
				case 1:
				case 2:
				case 3:
					break;
				default:
					System.out.print("Enter a valid cargo type! > ");
					type = "";
					break;
			}
		}
		return Integer.parseInt(type);
	}
}