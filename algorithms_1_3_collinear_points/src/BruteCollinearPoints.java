import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;


public class BruteCollinearPoints {
    private final Point[] points;
    private final List<Segment> segments = new ArrayList<Segment>();
    private LineSegment[] result;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
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
            if (testPoints[i-1].compareTo(testPoints[i]) == 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean areCollinear(Point[] possiblyCollinearPoints) {
        Comparator<Point> pointSlopeComparator = possiblyCollinearPoints[0].slopeOrder();
        return pointSlopeComparator.compare(possiblyCollinearPoints[1], possiblyCollinearPoints[2]) == 0
                && pointSlopeComparator.compare(possiblyCollinearPoints[2], possiblyCollinearPoints[3]) == 0;
    }

    private Segment getSegmentFromFourPoints(Point[] pointsForSegment) {
        Arrays.sort(pointsForSegment);
        return new Segment(pointsForSegment[0], pointsForSegment[3]);
    }

    private void combinationUtil(Point[] segmentPoints, int start, int end, int index) {
        if (index == 4) {
            if (areCollinear(segmentPoints)) {
                segments.add(getSegmentFromFourPoints(segmentPoints));
            }
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= 4 - index; i++) {
            segmentPoints[index] = points[i];
            combinationUtil(segmentPoints, i + 1, end, index + 1);
        }
    }

    private void computeSegments() {
        Point[] segmentPoints = new Point[4];
        combinationUtil(segmentPoints, 0, points.length - 1, 0);
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
