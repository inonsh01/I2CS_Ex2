import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Index2D
 */
public class Index2DTest {

    private Index2D p1, p2, p3;

    @BeforeEach
    public void setUp() {
        p1 = new Index2D(3, 4);
        p2 = new Index2D(0, 0);
        p3 = new Index2D(-2, 5);
    }

    @Test
    void testConstructorWithInts() {
        assertEquals(3, p1.getX());
        assertEquals(4, p1.getY());
        assertEquals(0, p2.getX());
    }

    @Test
    void testConstructorWithPixel2D() {
        Index2D copy = new Index2D(p1);
        assertEquals(3, copy.getX());
        assertEquals(4, copy.getY());
    }

    @Test
    void testGetX() {
        assertEquals(3, p1.getX());
        assertEquals(0, p2.getX());
        assertEquals(-2, p3.getX());
    }

    @Test
    void testGetY() {
        assertEquals(4, p1.getY());
        assertEquals(0, p2.getY());
        assertEquals(5, p3.getY());
    }

    @Test
    void testDistance2D() {
        assertEquals(5.0, p1.distance2D(p2), 0.001);
        assertEquals(0.0, p1.distance2D(p1), 0.001);
        double expected = Math.sqrt(26);
        assertEquals(expected, p1.distance2D(p3), 0.001);
    }

    @Test
    void testDistance2DWithNull() {
        assertThrows(RuntimeException.class, () -> p1.distance2D(null));
    }

    @Test
    void testToString() {
        assertEquals("(3,4)", p1.toString());
        assertEquals("(0,0)", p2.toString());
        assertEquals("(-2,5)", p3.toString());
    }

    @Test
    void testEquals() {
        Index2D p1Copy = new Index2D(3, 4);
        Index2D different = new Index2D(3, 5);

        assertTrue(p1.equals(p1Copy));
        assertTrue(p1.equals(p1));

        assertFalse(p1.equals(different));
        assertFalse(p1.equals(null));
    }
}