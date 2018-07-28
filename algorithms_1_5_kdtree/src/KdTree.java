import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size = 0;

    public KdTree() { // construct an empty set of points
    }

    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    public int size() { // number of points in the set
        return size;
    }

    public void insert(Point2D p) { // add the p to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        if (root == null) {
            root = new Node(p, false);
            size = 1;
            return;
        }
        if (insert(root, p)) size++;
    }

    private boolean insert(Node n, Point2D p) {
        int cmpX = Point2D.X_ORDER.compare(p, n.p);
        int cmpY = Point2D.Y_ORDER.compare(p, n.p);
        if (n.horiz) {
            if (cmpY < 0) {
                if (n.left == null) {
                    n.left = new Node(p, false, n);
                    return true;
                }
                return insert(n.left, p);
            }
            if (cmpY > 0 || (cmpY == 0 && cmpX != 0)) {
                if (n.right == null) {
                    n.right = new Node(p, false, n);
                    return true;
                }
                return insert(n.right, p);
            }
            return false; // the node already exists
        } else {
            if (cmpX < 0) {
                if (n.left == null) {
                    n.left = new Node(p, true, n);
                    return true;
                }
                return insert(n.left, p);
            }
            if (cmpX > 0 || (cmpX == 0 && cmpY != 0)) {
                if (n.right == null) {
                    n.right = new Node(p, true, n);
                    return true;
                }
                return insert(n.right, p);
            }
            return false;
        }
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return false;
        return contains(root, p);
    }

    private boolean contains(Node n, Point2D p) {
        if (n == null) return false;
        if (n.p.equals(p)) return true;
        int cmp;
        if (n.horiz) cmp = Point2D.Y_ORDER.compare(n.p, p);
        else cmp = Point2D.X_ORDER.compare(n.p, p);
        if (cmp > 0) return contains(n.left, p);
        else return contains(n.right, p);
    }

    public void draw() { // draw all points to standard draw
        if (root != null) draw(root);
    }

    private void draw(Node n) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();

        // draw the line
        double xmin, ymin, xmax, ymax;
        if (n.horiz) {
            StdDraw.setPenColor(StdDraw.BLUE);
            ymin = n.p.y();
            ymax = n.p.y();
            xmin = n.rect.xmin();
            xmax = n.rect.xmax();
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            xmin = n.p.x();
            xmax = n.p.x();
            ymin = n.rect.ymin();
            ymax = n.rect.ymax();
        }
        StdDraw.line(xmin, ymin, xmax, ymax);
        if (n.left != null) draw(n.left);
        if (n.right != null) draw(n.right);
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> pointsInRange = new LinkedList<Point2D>();
        if (root != null) range(root, rect, pointsInRange);
        return pointsInRange;
    }

    private void range(Node n, RectHV rect, List<Point2D> points) {
        if (rect.contains(n.p)) points.add(n.p);
        if (n.left != null && rect.intersects(n.left.rect)) range(n.left, rect, points);
        if (n.right != null && rect.intersects(n.right.rect)) range(n.right, rect, points);
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        return nearest(root, root.p, p);
    }

    private Point2D nearest(Node n, Point2D nearestSoFar, Point2D p) {
        if (n.p.distanceSquaredTo(p) < nearestSoFar.distanceSquaredTo(p)) nearestSoFar = n.p;
        if (n.left != null) nearestSoFar = nearest(n.left, nearestSoFar, p);
        if (n.right != null) nearestSoFar = nearest(n.right, nearestSoFar, p);
        return nearestSoFar;
    }

    private class Node {
        Point2D p;
        Node left, right;
        RectHV rect;
        boolean horiz;

        public Node(Point2D p, boolean horiz) {
            this.p = p;
            this.horiz = horiz;
            this.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        }

        public Node(Point2D p, boolean horiz, Node previous) {
            this(p, horiz);
            if (previous.horiz) {
                int cmpY = Point2D.Y_ORDER.compare(p, previous.p);
                if (cmpY < 0) {
                    this.rect = new RectHV(previous.rect.xmin(), previous.rect.ymin(), previous.rect.xmax(), previous.p.y());
                } else {
                    this.rect = new RectHV(previous.rect.xmin(), previous.p.y(), previous.rect.xmax(), previous.rect.ymax());
                }
            } else {
                int cmpX = Point2D.X_ORDER.compare(p, previous.p);
                if (cmpX < 0) {
                    this.rect = new RectHV(previous.rect.xmin(), previous.rect.ymin(), previous.p.x(), previous.rect.ymax());
                } else {
                    this.rect = new RectHV(previous.p.x(), previous.rect.ymin(), previous.rect.xmax(), previous.rect.ymax());
                }
            }
        }
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(0.1, 0.1));
        kdTree.insert(new Point2D(0.1, 0.1));
        kdTree.insert(new Point2D(0.2, 0.1));

        System.out.println(kdTree.contains(new Point2D(0.1, 0.1)));
        System.out.println(kdTree.contains(new Point2D(0.2, 0.1)));
        System.out.println(kdTree.contains(new Point2D(0.3, 0.1)));

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdTree.draw();
        StdDraw.show();
    }
}