import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.Color;

/**
 * Intro2CS_2026A This class represents a Graphical User Interface (GUI) for
 * Map2D. The class has save and load functions, and a GUI draw function. You
 * should implement this class, it is recommender to use the StdDraw class, as
 * in: https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
 *
 *
 */
public class Ex2_GUI {

    /**
     * This function draws the map on the screen.
     * 
     * @param map
     */
    public static void drawMap(Map2D map) {

        // edge case
        if (map == null) {
            System.err.println("Error: Cannot draw a null map.");
            return;
        }

        // get dimensions
        int width = map.getWidth();
        int height = map.getHeight();

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();

        // set scales
        StdDraw.setXscale(-0.5, width - 0.5);
        StdDraw.setYscale(-0.5, height - 0.5);

        // loop over the map
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                // get the color
                int color = map.getPixel(i, j);

                // casting to Color object and set the pan color
                Color c = new Color(color);
                StdDraw.setPenColor(c);

                // draw a 1x1 square at (i, j)
                StdDraw.filledSquare(i, j, 0.5);
            }
        }

        // just for me, not really needed
        drawLines(width, height);

        // display the map
        StdDraw.show();
    }

    /**
     * This function load a map from file to Map2D array
     * 
     * @param mapFileName
     * @return Map2D Array
     */
    public static Map2D loadMap(String mapFileName) {
        Map2D ans = null;
        try {

            // scan mapFile
            Scanner scanner = new Scanner(new File(mapFileName));

            // get dimensions
            int width = scanner.nextInt();
            int height = scanner.nextInt();

            // create a new Map object
            ans = new Map(new int[width][height]);

            // loop all over the map like the file wrote (from the height to width)
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {

                    // set the current pixel
                    if (scanner.hasNextInt()) {
                        ans.setPixel(i, j, scanner.nextInt());
                    }
                }
            }
            scanner.close();
            System.out.println("File: " + mapFileName + " loaded successfully.");

        }
        catch (FileNotFoundException e) {
            // if file not found present a error message
            System.out.println("Error: " + e.getMessage());
        }
        return ans;
    }

    /**
     * This function save Map2D array to a file
     * 
     * @param map
     * @param mapFileName
     */
    public static void saveMap(Map2D map, String mapFileName) {
        if (map == null)
            return;

        try {
            // create new file and define writer
            File file = new File(mapFileName);
            PrintWriter writer = new PrintWriter(file);

            // get dimensions
            int width = map.getWidth();
            int height = map.getHeight();

            // write dimensinons on top of file ( sort of convention )
            writer.println(width + " " + height);

            // loop over map to write it in file
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    // write the current pixel in file
                    writer.print(map.getPixel(i, j));

                    // space between numbers
                    if (i < width - 1) {
                        writer.print(" ");
                    }
                }
                // \n
                writer.println();
            }

            // close file
            writer.close();

            System.out.println("File: " + mapFileName + " saved successfully.");

        }
        catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] a) {
        String mapFile = "map.txt";
        Map2D map = loadMap(mapFile);
        drawMap(map);
    }

    /// ///////////// Private functions ///////////////

    /**
     * This function draw grid lines to show pixel boundaries
     * 
     * @param width
     * @param height
     */
    private static void drawLines(int width, int height) {
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.setPenRadius(0.001);

        // vertical lines
        for (int i = 0; i <= width; i++) {
            StdDraw.line(i - 0.5, -0.5, i - 0.5, height - 0.5);
        }

        // horizontal lines
        for (int j = 0; j <= height; j++) {
            StdDraw.line(-0.5, j - 0.5, width - 0.5, j - 0.5);
        }

        // draw border around the entire map
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.005);
        StdDraw.rectangle((width - 1) / 2.0, (height - 1) / 2.0, width / 2.0, height / 2.0);
    }
}
