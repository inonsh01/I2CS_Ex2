
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Method;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } };
    private Map2D _m0, _m1, _m3_3;

    @BeforeEach
    public void setuo() {
        _m3_3 = new Map(_map_3_3);
        _m0 = new Map(1);
        _m1 = new Map(1);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int[500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3, 2);
        _m1.fill(p1, 1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }

    @Test
    void testEquals() {
        assertEquals(_m0, _m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }

    @Test
    void testGetMap() {
        _m0.init(_map_3_3);
        int[][] copy = _m0.getMap();
        assertEquals(_map_3_3.length, copy.length);
        assertEquals(_map_3_3[0].length, copy[0].length);
        copy[0][0] = 999;
        assertEquals(0, _m0.getPixel(0, 0));
    }

    @Test
    void testGetPixel() {
        _m0.init(_map_3_3);
        assertEquals(0, _m0.getPixel(0, 0));
        assertEquals(1, _m0.getPixel(1, 0));
        assertEquals(0, _m0.getPixel(2, 0));
        Pixel2D p = new Index2D(2, 1);
        assertEquals(1, _m0.getPixel(p));
    }

    @Test
    void testSetPixel() {
        _m0.init(_map_3_3);
        _m0.setPixel(0, 0, 5);
        assertEquals(5, _m0.getPixel(0, 0));
        Pixel2D p = new Index2D(1, 1);
        _m0.setPixel(p, 7);
        assertEquals(7, _m0.getPixel(p));
    }

    @Test
    void testIsInside() {
        _m0.init(_map_3_3);
        Pixel2D inside = new Index2D(1, 1);
        Pixel2D outside = new Index2D(5, 5);
        assertTrue(_m0.isInside(inside));
        assertFalse(_m0.isInside(outside));
        assertFalse(_m0.isInside(null));
    }

    @Test
    void testSameDimensions() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertTrue(_m0.sameDimensions(_m1));
        Map2D m2 = new Map(2, 4, 0);
        assertFalse(_m0.sameDimensions(m2));
        assertFalse(_m0.sameDimensions(null));
    }

    @Test
    void testAddMap2D() {
        _m0.init(3, 3, 1);
        _m1.init(3, 3, 2);
        _m0.addMap2D(_m1);
        assertEquals(3, _m0.getPixel(0, 0));
        assertEquals(3, _m0.getPixel(1, 1));
    }

    @Test
    void testMul() {
        _m0.init(3, 3, 2);
        _m0.mul(3.0);
        assertEquals(6, _m0.getPixel(0, 0));
        assertEquals(6, _m0.getPixel(1, 1));
    }

    @Test
    void testRescale() {
        _m0.init(2, 2, 5);
        _m0.rescale(2.0, 2.0);
        assertEquals(4, _m0.getWidth());
        assertEquals(4, _m0.getHeight());
        assertEquals(5, _m0.getPixel(0, 0));
    }

    @Test
    void testDrawCircle() {
        _m0.init(5, 5, 0);
        Pixel2D center = new Index2D(2, 2);
        _m0.drawCircle(center, 1.5, 9);
        assertEquals(9, _m0.getPixel(2, 2));
        assertEquals(9, _m0.getPixel(1, 2));
        assertEquals(9, _m0.getPixel(2, 1));
        assertEquals(0, _m0.getPixel(0, 0));
    }

    @Test
    void testDrawLine() {
        _m0.init(5, 5, 0);
        Pixel2D p1 = new Index2D(0, 0);
        Pixel2D p2 = new Index2D(2, 2);
        _m0.drawLine(p1, p2, 8);
        assertEquals(8, _m0.getPixel(0, 0));
        assertEquals(8, _m0.getPixel(1, 1));
        assertEquals(8, _m0.getPixel(2, 2));
    }

    @Test
    void testDrawRect() {
        _m0.init(5, 5, 0);
        Pixel2D p1 = new Index2D(1, 1);
        Pixel2D p2 = new Index2D(3, 3);
        _m0.drawRect(p1, p2, 7);
        assertEquals(7, _m0.getPixel(1, 1));
        assertEquals(7, _m0.getPixel(2, 2));
        assertEquals(7, _m0.getPixel(3, 3));
        assertEquals(0, _m0.getPixel(0, 0));
    }

    @Test
    void testShortestPath() {
        _m0.init(3, 3, 0);
        _m0.setPixel(1, 1, 1);
        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(2, 2);
        Pixel2D[] path = _m0.shortestPath(start, end, 1, false);
        assertNotNull(path);
        assertTrue(path.length >= 5);
        assertEquals(start, path[0]);
        assertEquals(end, path[path.length - 1]);
        
        Pixel2D blocked = new Index2D(0, 1);
        _m0.setPixel(blocked, 1);
        Pixel2D[] noPath = _m0.shortestPath(start, blocked, 1, false);
        assertNull(noPath);
    }

    @Test
    void testFill() {
        _m0.init(3, 3, 0);
        _m0.setPixel(1, 1, 1);
        Pixel2D start = new Index2D(0, 0);
        int filled = _m0.fill(start, 2, false);
        assertTrue(filled > 0);
        assertEquals(2, _m0.getPixel(0, 0));
        assertEquals(1, _m0.getPixel(1, 1));
    }

    @Test
    void testAllDistance() {
        _m0.init(3, 3, 0);
        _m0.setPixel(1, 1, 1);
        Pixel2D start = new Index2D(0, 0);
        Map2D result = _m0.allDistance(start, 1, false);
        
        assertNotNull(result);
        assertTrue(result.sameDimensions(_m0));
        assertEquals(0, result.getPixel(start));
        assertEquals(-1, result.getPixel(1, 1));
        assertTrue(result.getPixel(0, 1) > 0);
        assertTrue(result.getPixel(1, 0) > 0);
    }

    @Test
    void testGetNextPixel() throws Exception {
        Map map = new Map(3, 3, 0);
        Method method = Map.class.getDeclaredMethod("getNextPixel", Pixel2D.class, int.class, int.class, int.class,
                int.class, boolean.class);
        method.setAccessible(true);

        Pixel2D curr = new Index2D(1, 1);
        Pixel2D next = (Pixel2D) method.invoke(map, curr, 1, 0, 3, 3, false);
        assertEquals(2, next.getX());
        assertEquals(1, next.getY());

        Pixel2D boundary = (Pixel2D) method.invoke(map, curr, -2, 0, 3, 3, false);
        assertNull(boundary);

        Pixel2D cyclic = (Pixel2D) method.invoke(map, curr, -2, 0, 3, 3, true);
        assertEquals(2, cyclic.getX());
        assertEquals(1, cyclic.getY());
    }

    @Test
    void testReconstructPath() throws Exception {
        Map map = new Map(3, 3, 0);
        Method method = Map.class.getDeclaredMethod("reconstructPath", Pixel2D.class, Pixel2D.class, Pixel2D[][].class);
        method.setAccessible(true);

        Pixel2D start = new Index2D(0, 0);
        Pixel2D end = new Index2D(2, 2);
        Pixel2D[][] parent = new Pixel2D[3][3];
        parent[0][0] = start;
        parent[1][1] = new Index2D(0, 0);
        parent[2][2] = new Index2D(1, 1);

        Pixel2D[] path = (Pixel2D[]) method.invoke(map, start, end, parent);
        assertEquals(3, path.length);
        assertEquals(start, path[0]);
        assertEquals(end, path[2]);
    }

}