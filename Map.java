
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

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

		// if pixel is out of bounds
		if (x < 0 || x >= w || y < 0 || y >= h)
			ans = false;

		return ans;
	}

	@Override
	public boolean sameDimensions(Map2D p) {
		// edge case
		if (p == null)
			return false;

		return (this.getWidth() == p.getWidth() && this.getHeight() == p.getHeight());
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

		// set steps for the longer distance
		double steps = Math.max(Math.abs(dx), Math.abs(dy));

		// set increament for each step x \ y
		double incX = dx / steps;
		double incY = dy / steps;

		// set current x & y
		double currX = x1;
		double currY = y1;

		for (int i = 0; i <= steps; i++) {
			// round current x and y
			int roundedCurrX = (int) Math.round(currX);
			int roundedCurrY = (int) Math.round(currY);

			// paint with given color the current pixel
			this.setPixel(roundedCurrX, roundedCurrY, color);

			// set current x and y to the next step
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
	 * 
	 * @param xy     coordinate of starting point
	 * @param new_v  new value (color) to set the right pixels
	 * @param cyclic boolean value for map is cyclic or not
	 * 
	 * @return the number of cells updated on the map (including the source)
	 */
	public int fill(Pixel2D xy, int new_v, boolean cyclic) {
		int ans = 0;

		// get details
		int width = getWidth();
		int height = getHeight();
		int oldColor = getPixel(xy.getX(), xy.getY());

		// if the color is already the new color, no need to fill
		if (oldColor == new_v)
			return ans;

		Queue<Pixel2D> q = new LinkedList<>();

		// for tracking visited pixels
		boolean[][] visited = new boolean[width][height];

		// add first pixel to queue and to visited list
		q.add(xy);
		visited[xy.getX()][xy.getY()] = true;

		// right, left, down, up
		int[] dx = { 1, -1, 0, 0 };
		int[] dy = { 0, 0, 1, -1 };

		while (!q.isEmpty()) {
			// return and remove the head of the queue
			Pixel2D curr = q.poll();

			// paint with color the current pixel and increment count
			setPixel(curr.getX(), curr.getY(), new_v);
			ans++;

			// check the four near neighbors
			for (int i = 0; i < 4; i++) {
				Pixel2D next = getNextPixel(curr, dx[i], dy[i], width, height, cyclic);

				if (next != null) {
					int nx = next.getX();
					int ny = next.getY();

					// check if the neighbor has the old color and hasn't been visited
					if (!visited[nx][ny] && getPixel(nx, ny) == oldColor) {

						// add pixel to queue and turn visited to true
						visited[nx][ny] = true;
						q.add(next);
					}
				}
			}
		}

		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of
	 * BFS, see: https://en.wikipedia.org/wiki/Breadth-first_search
	 * 
	 * @param p1       the source (starting) point
	 * @param p2       the target (destination) point
	 * @param obsColor the color representing obstacles
	 * @param cyclic   boolean value for map is cyclic or not
	 * 
	 * @return the shortest path as an array of points from source to target
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
		Pixel2D[] ans = null; // the result.

		int width = getWidth();
		int height = getHeight();

		// check if start or end are valid (not obstacles)
		if (getPixel(p1.getX(), p1.getY()) == obsColor || getPixel(p2.getX(), p2.getY()) == obsColor) {
			return null;
		}

		// Special case: start is same as end
		if (p1.equals(p2)) {
			return new Pixel2D[] { p1 };
		}

		Queue<Pixel2D> q = new LinkedList<>();
		q.add(p1);

		// parent[x][y] stores the pixel we came from to reach (x, y)
		Pixel2D[][] parent = new Pixel2D[width][height];

		// mark start as visited
		parent[p1.getX()][p1.getY()] = p1;

		int[] dx = { 1, -1, 0, 0 };
		int[] dy = { 0, 0, 1, -1 };

		while (!q.isEmpty()) {
			Pixel2D curr = q.poll();

			// if we reached the target, stop and reconstruct the path
			if (curr.equals(p2)) {
				ans = reconstructPath(p1, p2, parent);
				break;
			}

			for (int i = 0; i < 4; i++) {
				Pixel2D next = getNextPixel(curr, dx[i], dy[i], width, height, cyclic);

				if (next != null) {
					int nx = next.getX();
					int ny = next.getY();

					// check if the neighbor is not an obstacle and hasn't been visited
					if (parent[nx][ny] == null && getPixel(nx, ny) != obsColor) {
						parent[nx][ny] = curr;
						q.add(next);
					}
				}
			}
		}

		return ans;
	}

	@Override
	/**
	 * Computes a new map with the shortest path distance from the start point to
	 * each entry in this map. Uses BFS algorithm to compute distances.
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 * 
	 * @param start    the source (starting) point
	 * @param obsColor the color representing obstacles
	 * @param cyclic   boolean value for map is cyclic or not
	 *
	 * @return a new map with all the shortest path distances from the starting
	 *         point to each entry in this map.
	 */
	public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
		Map2D ans = null; // the result.

		// get map dimensions
		int width = getWidth();
		int height = getHeight();

		// create result map with same dimensions, initialize with -1
		ans = new Map(width, height, -1);

		// check if start point is an obstacle
		if (getPixel(start.getX(), start.getY()) == obsColor) {
			return ans;
		}

		// BFS queue and visited tracking
		Queue<Pixel2D> q = new LinkedList<>();
		boolean[][] visited = new boolean[width][height];

		// start point has distance 0
		q.add(start);
		ans.setPixel(start, 0);
		visited[start.getX()][start.getY()] = true;

		// directions: right, left, down, up
		int[] dx = { 1, -1, 0, 0 };
		int[] dy = { 0, 0, 1, -1 };

		while (!q.isEmpty()) {
			// get current pixel and its distance
			Pixel2D curr = q.poll();
			int currDistance = ans.getPixel(curr);

			// check all four neighbors
			for (int i = 0; i < 4; i++) {
				Pixel2D next = getNextPixel(curr, dx[i], dy[i], width, height, cyclic);

				if (next != null) {
					int nx = next.getX();
					int ny = next.getY();

					// if neighbor is not visited and not an obstacle
					if (!visited[nx][ny] && getPixel(nx, ny) != obsColor) {
						// set distance and mark as visited
						ans.setPixel(nx, ny, currDistance + 1);
						visited[nx][ny] = true;
						q.add(next);
					}
				}
			}
		}

		return ans;
	}
	////////////////////// Private Methods ///////////////////////

	/**
	 * Computes the next neighbor pixel based on the given direction (dx, dy). If
	 * the map is cyclic, it wraps around the boundaries using modulo. If not
	 * cyclic, it returns null if the neighbor is outside the map boundaries. *
	 * 
	 * @param curr   The current pixel.
	 * @param dx     The change in x direction.
	 * @param dy     The change in y direction.
	 * @param width  The map width.
	 * @param height The map height.
	 * @param cyclic Whether the map is cyclic.
	 * @return A new Pixel2D representing the neighbor, or null if out of bounds.
	 */
	private Pixel2D getNextPixel(Pixel2D curr, int dx, int dy, int width, int height, boolean cyclic) {

		// get the next pixel
		int nx = curr.getX() + dx;
		int ny = curr.getY() + dy;

		if (cyclic) {
			nx = (nx + width) % width;
			ny = (ny + height) % height;
		}
		else {
			// boundary check
			if (nx < 0 || nx >= width || ny < 0 || ny >= height) {
				return null;
			}
		}
		return new Index2D(nx, ny);
	}

	/**
	 * Reconstructs the path from p2 back to p1 using the parent map.
	 * 
	 * @param p1     The starting pixel.
	 * @param p2     The ending pixel.
	 * @param parent The parent map.
	 * 
	 * @return An array of Pixel2D representing the path from start to end.
	 */
	private Pixel2D[] reconstructPath(Pixel2D p1, Pixel2D p2, Pixel2D[][] parent) {
		LinkedList<Pixel2D> pathList = new LinkedList<>();
		Pixel2D curr = p2;

		// backtrack from end to start
		while (curr != null && !curr.equals(p1)) {

			// add to the front to keep start-to-end order
			pathList.addFirst(curr);

			// update curr to be his parent
			curr = parent[curr.getX()][curr.getY()];
		}

		// add the start pixel
		pathList.addFirst(p1);

		// convert the list to a fixed-size array
		return pathList.toArray(new Pixel2D[0]);
	}
}
