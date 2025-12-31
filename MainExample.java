import java.awt.Color;
import java.util.Scanner;

public class MainExample {
    private static Scanner scanner = new Scanner(System.in);
    private static Map mainMap;

    public static void main() {
        System.out.println("=== Map2D Interactive Demo ===");

        // Initialize main map
        mainMap = new Map(15, 15, Color.WHITE.getRGB());
        addSampleObstacles();

        while (true) {
            showMenu();
            int choice = getChoice();

            switch (choice) {
            case 1:
                testInit();
                break;
            case 2:
                testGetSetPixel();
                break;
            case 3:
                testDimensions();
                break;
            case 4:
                testDrawCircle();
                break;
            case 5:
                testDrawLine();
                break;
            case 6:
                testDrawRect();
                break;
            case 7:
                testShortestPath();
                break;
            case 8:
                testAllDistance();
                break;
            case 9:
                testFill();
                break;
            case 10:
                testMapOperations();
                break;
            case 11:
                displayMap();
                break;
            case 12:
                testSaveLoad();
                break;
            case 0:
                System.out.println("Goodbye!");
                return;
            default:
                System.out.println("Invalid choice!");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void showMenu() {
        System.out.println("\n=== Choose a function to test ===");
        System.out.println("1.  init() - Initialize map");
        System.out.println("2.  getPixel/setPixel() - Get/Set pixel values");
        System.out.println("3.  Dimensions - getWidth/getHeight/isInside/sameDimensions");
        System.out.println("4.  drawCircle() - Draw a circle");
        System.out.println("5.  drawLine() - Draw a line");
        System.out.println("6.  drawRect() - Draw a rectangle");
        System.out.println("7.  shortestPath() - Find shortest path");
        System.out.println("8.  allDistance() - Calculate all distances");
        System.out.println("9.  fill() - Flood fill");
        System.out.println("10. Map Operations - add/multiply/rescale");
        System.out.println("11. Display current map");
        System.out.println("12. Save/Load map");
        System.out.println("0.  Exit");
        System.out.print("Enter choice: ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addSampleObstacles() {
        // Add L-shaped obstacle
        for (int i = 5; i < 10; i++) {
            mainMap.setPixel(i, 7, Color.BLACK.getRGB());
        }
        for (int j = 7; j < 12; j++) {
            mainMap.setPixel(9, j, Color.BLACK.getRGB());
        }
    }

    private static void testInit() {
        System.out.println("\n=== Testing init() ===");
        System.out.println("1. Create new 10x10 map with value 5");
        System.out.println("2. Create map from 2D array");
        System.out.print("Choose: ");
        int choice = getChoice();

        if (choice == 1) {
            Map newMap = new Map(10, 10, 5);
            System.out.println("Created 10x10 map, pixel[0][0] = " + newMap.getPixel(0, 0));
            System.out.println("Width: " + newMap.getWidth() + ", Height: " + newMap.getHeight());
        }
        else if (choice == 2) {
            int[][] arr = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
            Map newMap = new Map(arr);
            System.out.println("Created map from array:");
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    System.out.print(newMap.getPixel(i, j) + " ");
                }
                System.out.println();
            }
        }
    }

    private static void testGetSetPixel() {
        System.out.println("\n=== Testing getPixel/setPixel ===");
        System.out.print("Enter x coordinate (0-14): ");
        int x = getChoice();
        System.out.print("Enter y coordinate (0-14): ");
        int y = getChoice();

        if (x >= 0 && x < 15 && y >= 0 && y < 15) {
            System.out.println("Current pixel[" + x + "][" + y + "] = " + mainMap.getPixel(x, y));
            System.out.print("Enter new value: ");
            int value = getChoice();
            mainMap.setPixel(x, y, value);
            System.out.println("Set pixel[" + x + "][" + y + "] = " + value);

            Pixel2D p = new Index2D(x, y);
            System.out.println("Using Pixel2D: " + mainMap.getPixel(p));
        }
        else {
            System.out.println("Invalid coordinates!");
        }
    }

    private static void testDimensions() {
        System.out.println("\n=== Testing Dimensions ===");
        System.out.println("Width: " + mainMap.getWidth());
        System.out.println("Height: " + mainMap.getHeight());

        Pixel2D inside = new Index2D(5, 5);
        Pixel2D outside = new Index2D(20, 20);
        System.out.println("Point (5,5) is inside: " + mainMap.isInside(inside));
        System.out.println("Point (20,20) is inside: " + mainMap.isInside(outside));

        Map other = new Map(15, 15, 0);
        Map different = new Map(10, 10, 0);
        System.out.println("Same dimensions with 15x15 map: " + mainMap.sameDimensions(other));
        System.out.println("Same dimensions with 10x10 map: " + mainMap.sameDimensions(different));
    }

    private static void testDrawCircle() {
        System.out.println("\n=== Testing drawCircle ===");
        System.out.print("Enter center x (0-14): ");
        int x = getChoice();
        System.out.print("Enter center y (0-14): ");
        int y = getChoice();
        System.out.print("Enter radius: ");
        double radius = Double.parseDouble(scanner.nextLine());

        Pixel2D center = new Index2D(x, y);
        mainMap.drawCircle(center, radius, Color.YELLOW.getRGB());
        System.out.println("Drew circle at (" + x + "," + y + ") with radius " + radius);
    }

    private static void testDrawLine() {
        System.out.println("\n=== Testing drawLine ===");
        System.out.print("Enter start x: ");
        int x1 = getChoice();
        System.out.print("Enter start y: ");
        int y1 = getChoice();
        System.out.print("Enter end x: ");
        int x2 = getChoice();
        System.out.print("Enter end y: ");
        int y2 = getChoice();

        Pixel2D p1 = new Index2D(x1, y1);
        Pixel2D p2 = new Index2D(x2, y2);
        mainMap.drawLine(p1, p2, Color.CYAN.getRGB());
        System.out.println("Drew line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
    }

    private static void testDrawRect() {
        System.out.println("\n=== Testing drawRect ===");
        System.out.print("Enter corner1 x: ");
        int x1 = getChoice();
        System.out.print("Enter corner1 y: ");
        int y1 = getChoice();
        System.out.print("Enter corner2 x: ");
        int x2 = getChoice();
        System.out.print("Enter corner2 y: ");
        int y2 = getChoice();

        Pixel2D p1 = new Index2D(x1, y1);
        Pixel2D p2 = new Index2D(x2, y2);
        mainMap.drawRect(p1, p2, Color.MAGENTA.getRGB());
        System.out.println("Drew rectangle from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
    }

    private static void testShortestPath() {
        System.out.println("\n=== Testing shortestPath ===");
        System.out.print("Enter start x: ");
        int x1 = getChoice();
        System.out.print("Enter start y: ");
        int y1 = getChoice();
        System.out.print("Enter end x: ");
        int x2 = getChoice();
        System.out.print("Enter end y: ");
        int y2 = getChoice();

        Pixel2D start = new Index2D(x1, y1);
        Pixel2D end = new Index2D(x2, y2);

        Pixel2D[] path = mainMap.shortestPath(start, end, Color.BLACK.getRGB(), false);
        if (path != null) {
            System.out.println("Path found! Length: " + path.length);
            for (Pixel2D p : path) {
                mainMap.setPixel(p, Color.RED.getRGB());
            }
            mainMap.setPixel(start, Color.GREEN.getRGB());
            mainMap.setPixel(end, Color.BLUE.getRGB());
        }
        else {
            System.out.println("No path found!");
        }
    }

    private static void testAllDistance() {
        System.out.println("\n=== Testing allDistance ===");
        System.out.print("Enter start x: ");
        int x = getChoice();
        System.out.print("Enter start y: ");
        int y = getChoice();

        Pixel2D start = new Index2D(x, y);
        Map2D distMap = mainMap.allDistance(start, Color.BLACK.getRGB(), false);

        if (distMap != null) {
            System.out.println("Distance map created!");
            System.out.println("Start distance: " + distMap.getPixel(start));
            System.out.println("\nDistance values (5x5 section):");
            for (int j = 0; j < Math.min(5, distMap.getHeight()); j++) {
                for (int i = 0; i < Math.min(5, distMap.getWidth()); i++) {
                    System.out.printf("%3d ", distMap.getPixel(i, j));
                }
                System.out.println();
            }
        }
    }

    private static void testFill() {
        System.out.println("\n=== Testing fill ===");
        System.out.print("Enter start x: ");
        int x = getChoice();
        System.out.print("Enter start y: ");
        int y = getChoice();
        System.out.print("Enter new color value: ");
        int color = getChoice();

        Pixel2D start = new Index2D(x, y);
        int filled = mainMap.fill(start, color, false);
        System.out.println("Filled " + filled + " pixels with color " + color);
    }

    private static void testMapOperations() {
        System.out.println("\n=== Testing Map Operations ===");
        System.out.println("1. Add another map");
        System.out.println("2. Multiply by scalar");
        System.out.println("3. Rescale map");
        System.out.print("Choose: ");
        int choice = getChoice();

        switch (choice) {
        case 1:
            Map other = new Map(15, 15, 10);
            mainMap.addMap2D(other);
            System.out.println("Added map with value 10 to all pixels");
            break;
        case 2:
            System.out.print("Enter scalar: ");
            double scalar = Double.parseDouble(scanner.nextLine());
            mainMap.mul(scalar);
            System.out.println("Multiplied all pixels by " + scalar);
            break;
        case 3:
            System.out.print("Enter x scale factor: ");
            double sx = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter y scale factor: ");
            double sy = Double.parseDouble(scanner.nextLine());
            mainMap.rescale(sx, sy);
            System.out.println("Rescaled map by (" + sx + ", " + sy + ")");
            System.out.println("New dimensions: " + mainMap.getWidth() + "x" + mainMap.getHeight());
            break;
        }
    }

    private static void displayMap() {
        System.out.println("\n=== Displaying Map ===");
        System.out.println("Map size: " + mainMap.getWidth() + "x" + mainMap.getHeight());
        Ex2_GUI.drawMap(mainMap);
    }
    
    private static void testSaveLoad() {
        System.out.println("\n=== Testing Save/Load Map ===");
        System.out.println("1. Save current map");
        System.out.println("2. Load map from file");
        System.out.print("Choose: ");
        int choice = getChoice();
        
        switch (choice) {
        case 1:
            System.out.print("Enter filename to save (e.g., mymap.txt): ");
            String saveFile = scanner.nextLine();
            Ex2_GUI.saveMap(mainMap, saveFile);
            break;
        case 2:
            System.out.print("Enter filename to load (e.g., mymap.txt): ");
            String loadFile = scanner.nextLine();
            Map2D loadedMap = Ex2_GUI.loadMap(loadFile);
            if (loadedMap != null) {
                mainMap = (Map) loadedMap;
                System.out.println("Map loaded successfully!");
                System.out.println("New map size: " + mainMap.getWidth() + "x" + mainMap.getHeight());
            }
            break;
        default:
            System.out.println("Invalid choice!");
        }
    }
}