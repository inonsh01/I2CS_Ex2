
public class Index2D implements Pixel2D {
    private int x;
    private int y;

    public Index2D(int w, int h) {
        this.x = w;
        this.y = h;
    }

    public Index2D(Pixel2D other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {

        return this.y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        // if p2 is null throw an error
        if (p2 == null)
            throw new RuntimeException("p2 is null");

        // calculate distance by algorithm -> sqrt((x2-x1)^2 + (y2-y1)^2
        double dx = this.x - p2.getX();
        double dy = this.y - p2.getY();

        return Math.sqrt((dx * dx) + (dy * dy));
    }

    @Override
    public String toString() {
        String ans = null;
        ans = "(" + this.x + "," + this.y + ")";
        return ans;
    }

    @Override
    public boolean equals(Object p) {
        boolean ans = true;

        // if the given object is not of type of Pixel2D
        if (!(p instanceof Pixel2D))
            return false;

        // casting Object to Pixel2D
        Pixel2D other = (Pixel2D) p;

        // ans is the result of comperation between x's and y's
        ans = (this.x == other.getX() && this.y == other.getY());

        return ans;
    }
}
