import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;
import java.util.List;

public class PointSET {
    private final SET<Point2D> set = new SET<Point2D>();

    public PointSET() { // construct an empty set of points
    }

    public boolean isEmpty() { // is the set empty?
        return set.isEmpty();
    }

    public int size() { // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) { // add the p to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        set.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain p p?
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw() { // draw all points to standard draw
        for (Point2D point : set) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> result = new LinkedList<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point)) result.add(point);
        }
        return result;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to p p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        double minDist = Double.POSITIVE_INFINITY;
        Point2D result = null;
        for (Point2D point : set) {
            double newDist = point.distanceSquaredTo(p);
            if (newDist < minDist) {
                minDist = newDist;
                result = point;
            }
        }
        return result;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)

    }
}