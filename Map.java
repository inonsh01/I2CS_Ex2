
import java.io.Serializable;

/**
 * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix
 * or maze over integers. This is the main class needed to be implemented.
 *
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D, Serializable {

	// edit this class below

	private int[][] _map;

	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 * 
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
		init(w, h, v);
	}

	/**
	 * Constructs a square map (size*size).
	 * 
	 * @param size
	 */
	public Map(int size) {
		this(size, size, 0);
	}

	/**
	 * Constructs a map from a given 2D array.
	 * 
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

	@Override
	public void init(int w, int h, int v) {
		// edge cases
		if (w <= 0 || h <= 0)
			throw new RuntimeException("Map2D init error: wrong dimensions");

		// create new map
		this._map = new int[w][h];

		// insert v to every 'cell'
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				this._map[i][j] = v;
			}
		}
	}

	@Override
	public void init(int[][] arr) {
		// edge cases
		if (arr == null || arr.length == 0 || arr[0] == null)
			throw new RuntimeException("Map2D init error: null or empty array");

		// create new map -> width = array length and height = one of the elements in
		// array length
		this._map = new int[arr.length][arr[0].length];

		// deep copy array to map
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				this._map[i][j] = arr[i][j];
			}
		}
	}

	@Override
	public int[][] getMap() {

		// edge cases
		if (this._map == null)
			return null;

		// get length
		int w = this.getWidth();
		int h = this.getHeight();

		// create new 2D array
		int[][] ans = new int[w][h];

		// deep copy map
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				ans[i][j] = this._map[i][j];
			}
		}

		return ans;
	}

	@Override
	public int getWidth() {
		int ans = -1;

		if (_map == null || _map.length == 0)
			ans = 0;
		else
			ans = _map.length;

		return ans;
	}

	@Override
	public int getHeight() {
		int ans = -1;

		if (_map == null || _map.length == 0 || _map[0] == null)
			ans = 0;
		else
			ans = _map[0].length;
		return ans;
	}

	@Override
	public int getPixel(int x, int y) {
		int ans = -1;

		// edge case
		if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
			throw new RuntimeException("Map2D getPixel error: out of bounds");

		// get pixel
		ans = _map[x][y];

		return ans;
	}

	@Override
	public int getPixel(Pixel2D p) {
		int ans = -1;

		// get coordinates
		int x = p.getX();
		int y = p.getY();

		// use first getPixel
		ans = this.getPixel(x, y);

		return ans;
	}

	@Override
	public void setPixel(int x, int y, int v) {
		// if coordinates in map set pixel
		if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight())
			this._map[x][y] = v;
	}

	@Override
	public void setPixel(Pixel2D p, int v) {
		// edge case
		if (p == null)
			throw new RuntimeException("Map2D setPixel error: null pixel");

		// get coordinates
		int x = p.getX();
		int y = p.getY();

		// use first setPixel
		this.setPixel(x, y, v);
	}

	@Override
	public boolean isInside(Pixel2D p) {
		boolean ans = true;

		if (p == null)
			return false;

		// get coordinates
		int x = p.getX();
		int y = p.getY();
		int w = this.getWidth();
		int h = this.getHeight();

		if (x < 0 || x >= w || y < 0 || y >= h)
			ans = false;

		return ans;
	}

	@Override
	public boolean sameDimensions(Map2D p) {
		// edge case
		if (p == null)
			return false;

		boolean ans = false;

		if (this.getWidth() == p.getWidth() && this.getHeight() == p.getHeight())
			ans = true;

		return ans;
	}

	@Override
	public void addMap2D(Map2D p) {
		// edge case
		if (p == null || !this.sameDimensions(p))
			return;

		int p1, p2;

		// loop all over the map
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {

				// get pixels
				p1 = this._map[i][j];
				p2 = p.getPixel(i, j);

				// set the pixel to the sum of them
				this.setPixel(i, j, p1 + p2);
			}
		}
	}

	@Override
	public void mul(double scalar) {

		int p1, mulResult;

		// loop all over the map
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {

				// get pixel
				p1 = this._map[i][j];

				// scale and round to the nearest integer
				mulResult = (int) Math.round(p1 * scalar);

				// set the pixel
				this.setPixel(i, j, mulResult);
			}
		}
	}

	@Override
	public void rescale(double sx, double sy) {
		int oldX, oldY, p;
		int width = (int) Math.round(this.getWidth() * sx);
		int height = (int) Math.round(this.getHeight() * sy);

		int[][] newMap = new int[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				// get x and y relative to the old map
				oldX = (int) (i / sx);
				oldY = (int) (j / sy);

				// check if coordinates within bounds (for rounding errors)
				if (oldX >= getWidth())
					oldX = getWidth() - 1;
				if (oldY >= getHeight())
					oldY = getHeight() - 1;

				// get pixel in the old map
				p = this.getPixel(oldX, oldY);

				// set to new map;
				newMap[i][j] = p;
			}
		}

		// point to the new map
		this._map = newMap;
	}

	@Override
	public void drawCircle(Pixel2D center, double rad, int color) {
		int centerX = center.getX();
		int centerY = center.getY();
		double radSquared = rad * rad;

		// loop all over the map
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {

				// distance from center
				double dx = i - centerX;
				double dy = j - centerY;
				double distanceSquared = (dx * dx) + (dy * dy);

				// if pixel is in circle, paint it in color
				if (distanceSquared <= radSquared) {
					this.setPixel(i, j, color);
				}
			}
		}
	}

	@Override
	public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
		// edge cases
		if (p1 == null || p2 == null)
			return;

		// if its the same pixel just paint it
		if (p1.equals(p2)) {
			this.setPixel(p1, color);
			return;
		}

		// get variables
		int x1 = p1.getX();
		int y1 = p1.getY();
		int x2 = p2.getX();
		int y2 = p2.getY();

		// calculate horizontal and vertical distance between two points
		double dx = x2 - x1;
		double dy = y2 - y1;

		// the bigger of them
		double steps = Math.max(Math.abs(dx), Math.abs(dy));

		// increment for each step
		double incX = dx / steps;
		double incY = dy / steps;

		double currX = x1;
		double currY = y1;

		for (int i = 0; i <= steps; i++) {
			this.setPixel((int) Math.round(currX), (int) Math.round(currY), color);

			currX += incX;
			currY += incY;
		}
	}

	@Override
	public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
		// edge cases
		if (p1 == null || p2 == null)
			return;

		// find the borders (for paint from top left to the bottom rights)
		int minX = Math.min(p1.getX(), p2.getX());
		int maxX = Math.max(p1.getX(), p2.getX());
		int minY = Math.min(p1.getY(), p2.getY());
		int maxY = Math.max(p1.getY(), p2.getY());

		// loop all over the rectangle
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				this.setPixel(i, j, color);
			}
		}
	}

	@Override
	public boolean equals(Object ob) {
		boolean ans = false;

		// if the given object is not of type of Map2D
		if (!(ob instanceof Map2D))
			return false;

		// casting Object to Map2D
		Map2D other = (Map2D) ob;

		// check dimensions first
		if (this.sameDimensions(other)) {

			// loop all over the map, if one cell in map isn't equals to ob they are not
			// equals
			for (int i = 0; i < this.getWidth(); i++) {
				for (int j = 0; j < this.getHeight(); j++) {
					if (this.getPixel(i, j) != other.getPixel(i, j))
						return false;
				}
			}
			ans = true;
		}

		return ans;
	}

	@Override
	/**
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v, boolean cyclic) {
		int ans = -1;

		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of
	 * BFS, see: https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
		Pixel2D[] ans = null; // the result.

		return ans;
	}

	@Override
	public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
		Map2D ans = null; // the result.

		return ans;
	}
	////////////////////// Private Methods ///////////////////////

}
