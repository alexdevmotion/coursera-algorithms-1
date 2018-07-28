import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONST = 1.96;

    private final int n;

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n, trials should be > 0");
        }
        double[] thresholds = new double[trials];
        this.n = n;

        for (int i = 0; i < trials; i++) {
            thresholds[i] = runTrial();
        }
        this.mean = StdStats.mean(thresholds);
        this.stddev = StdStats.stddev(thresholds);
        this.confidenceLo = this.mean - CONST * this.stddev / Math.sqrt(trials);
        this.confidenceHi = this.mean + CONST * this.stddev / Math.sqrt(trials);
    }

    private double runTrial() {
        Percolation percolation = new Percolation(n);
        for (int i = 0; ; i++) {
            int row, col;
            do {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
            } while (percolation.isOpen(row, col));
            percolation.open(row, col);

            if (i >= n && percolation.percolates()) {
                return (double) (i + 1) / (n * n);
            }
        }
    }

    public double mean() { // sample mean of percolation threshold
        return mean;
    }

    public double stddev() { // sample standard deviation of percolation threshold
        return stddev;
    }

    public double confidenceLo() { // low endpoint of 95% confidence interval
        return confidenceLo;
    }

    public double confidenceHi() { // high endpoint of 95% confidence interval
        return confidenceHi;
    }

    public static void main(String[] args) { // test client (described below)
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stddev = " + percolationStats.stddev());
        System.out.println("95% confidence = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}