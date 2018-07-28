import java.util.LinkedList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;


public class FastCollinearPoints {
    private final Point[] points;
    private final List<Segment> segments = new LinkedList<Segment>();
    private LineSegment[] result;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
        if (points == null || hasDuplicatesOrNulls(points)) {
            throw new IllegalArgumentException();
        }
        this.points = Arrays.copyOf(points, points.length);
        computeSegments();
        buildSegments();
    }

    private boolean hasDuplicatesOrNulls(Point[] pointsToCheck) {
        Point[] testPoints = Arrays.copyOf(pointsToCheck, pointsToCheck.length);
        for (int i = 0; i < testPoints.length; i++) {
            if (testPoints[i] == null) {
                return true;
            }
        }
        Arrays.sort(testPoints);
        for (int i = 1; i < testPoints.length; i++) {
            if (testPoints[i - 1].compareTo(testPoints[i]) == 0) {
                return true;
            }
        }
        return false;
    }

    private Segment getSegmentFromPoints(List<Point> pointsForSegment) {
        Collections.sort(pointsForSegment);
        return new Segment(pointsForSegment.get(0), pointsForSegment.get(pointsForSegment.size() - 1));
    }

    private void computeSegments() {
        for (Point p : points) {
            Comparator<Point> pComparator = p.slopeOrder();
            Point[] auxPoints = Arrays.copyOf(points, points.length);
            Arrays.sort(auxPoints, pComparator);

            List<Point> pointsForSegment = new LinkedList<Point>();
            pointsForSegment.add(auxPoints[0]);
            for (int i = 1; i < auxPoints.length; i++) {
                boolean isSlopeEqual = pComparator.compare(auxPoints[i-1], auxPoints[i]) == 0;
                if (isSlopeEqual) {
                    if (pointsForSegment.size() == 1) {
                        pointsForSegment.add(auxPoints[i-1]);
                    }
                    pointsForSegment.add(auxPoints[i]);
                } else {
                    if (pointsForSegment.size() >= 4) {
                        segments.add(getSegmentFromPoints(pointsForSegment));
                    }
                    pointsForSegment.clear();
                    pointsForSegment.add(p);
                }
            }
            if (pointsForSegment.size() >= 4) {
                segments.add(getSegmentFromPoints(pointsForSegment));
            }
            pointsForSegment.clear();
            pointsForSegment.add(p);
        }
    }

    public int numberOfSegments() { // the number of line segments
        return result.length;
    }

    private void buildSegments() { // the line segments
        Collections.sort(segments);
        Segment previousSegment = null;
        Iterator<Segment> segmentIterator = segments.iterator();
        while (segmentIterator.hasNext()) {
            Segment s = segmentIterator.next();
            if (previousSegment != null && s.compareTo(previousSegment) == 0) {
                segmentIterator.remove();
            }
            previousSegment = s;
        }

        int i = 0;
        result = new LineSegment[segments.size()];
        for (Segment s : segments) {
            result[i++] = new LineSegment(s.p1, s.p2);
        }
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(result, result.length);
    }

    private class Segment implements Comparable<Segment> {
        public Point p1;
        public Point p2;

        public Segment(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public int compareTo(Segment s) {
            int compare1 = this.p1.compareTo(s.p1);
            if (compare1 > 0) return 1;
            else if (compare1 < 0) return -1;
            else {
                int compare2 = this.p2.compareTo(s.p2);
                if (compare2 > 0) return 1;
                else if (compare2 < 0) return -1;
            }
            return 0;
        }
    }

}