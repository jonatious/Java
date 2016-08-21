/******************************************************************************
 *  Compilation:  javac TurtleSpike.java
 *  Execution:    java TurtleSpike
 *
 *  Data type for turtle graphics using standard draw.
 *
 ******************************************************************************/

import java.awt.Color;

public class TurtleSpike {
    private double x, y;     // turtle is at (x, y)
    private double angle;    // facing this many degrees counterclockwise from the x-axis

    // start at (x0, y0), facing a0 degrees counterclockwise from the x-axis
    public TurtleSpike(double x0, double y0, double a0) {
        x = x0;
        y = y0;
        angle = a0;
    }

    // rotate orientation delta degrees counterclockwise
    public void turnLeft(double delta) {
        angle += delta;
    }

    // move forward the given amount, with the pen down
    public void goForward(double step) {
        double oldx = x;
        double oldy = y;
        x += step * Math.cos(Math.toRadians(angle));
        y += step * Math.sin(Math.toRadians(angle));
        StdDraw.line(oldx, oldy, x, y);
    }

    // pause t milliseconds
    public void pause(int t) {
        StdDraw.show(t);
    }


    public void setPenColor(Color color) {
        StdDraw.setPenColor(color);
    }

    public void setPenRadius(double radius) {
        StdDraw.setPenRadius(radius);
    }

    public void setCanvasSize(int width, int height) {
        StdDraw.setCanvasSize(width, height);
    }

    public void setXscale(double min, double max) {
        StdDraw.setXscale(min, max);
    }

    public void setYscale(double min, double max) {
        StdDraw.setYscale(min, max);
    }


    // sample client for testing
    public static void main(String[] args) {
        double x0 = 0.01;
        double y0 = 0.99;
        double a0 = 60.0;
        //double step = Math.sqrt(3)/2;
        double step = 0.5;
        TurtleSpike turtleSpike = new TurtleSpike(x0, y0, 360-a0);
        turtleSpike.goForward(step);
        turtleSpike.turnLeft(130.0);
        turtleSpike.goForward(step);
        turtleSpike.turnLeft(120.0);
        turtleSpike.goForward(step);
        turtleSpike.turnLeft(120.0);
    }

}
