public class Vector2D {
    private double x;
    private double y;
    private double mag;
    private double angle;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
        this.mag = Math.sqrt(x*x + y*y);
        this.angle = calculateAngle();
    }
    private double calculateAngle() {
        double ang = Math.toDegrees(Math.atan(y / x));
        if (y < 0 && x < 0) {
            ang += 180;
        } else if (y < 0 && x > 0) {
            ang += 270;
        } else if (y > 0 && x < 0) {
            ang += 90;
        }
        return ang;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getMagnitude() {
        return this.mag;
    }
    public double getAngle() {
        return angle;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void addX(double x) {this.x += x;}
    public void addY(double y) {this.y += y;}
    public Vector2D clone() {
        return new Vector2D(x, y);
    }
}
