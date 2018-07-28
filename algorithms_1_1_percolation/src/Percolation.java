import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    private boolean[][] grid;
    private int openSites = 0;

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException("n should be > 0");
        }
        grid = new boolean[n][n];
        weightedQuickUnionUF = new WeightedQuickUnionUF(n*n + 2);
        initTopBottom();
    }

    public void open(int row, int col) { // open site (row, col) if it is not open already
        int n = grid.length;
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("row & col should be between [1," + n);
        }
        doOpen(row-1, col-1);
    }

    private void initTopBottom() {
        int n = grid.length;
        int top = n*n;
        int bottom = n*n + 1;

        for (int i = 0; i < n; i++) {
            weightedQuickUnionUF.union(top, i);
            weightedQuickUnionUF.union(bottom, n * (n-1) + i);
        }
    }

    private void doOpen(int i, int j) {
        if (!grid[i][j]) {
            grid[i][j] = true;
            openSites++;

            int n = grid.length;
            if (i > 0 && grid[i-1][j]) { // cell above is open
                weightedQuickUnionUF.union((i-1) * n + j, i * n + j);
            }
            if (j > 0 && grid[i][j-1]) { // cell to the left is open
                weightedQuickUnionUF.union(i * n + j-1, i * n + j);
            }
            if (i < n-1 && grid[i+1][j]) { // cell below is open
                weightedQuickUnionUF.union((i+1) * n + j, i * n + j);
            }
            if (j < n-1 && grid[i][j+1]) { // cell to the right is open
                weightedQuickUnionUF.union(i * n + j+1, i * n + j);
            }
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        int n = grid.length;
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("row & col should be between [1," + n);
        }
        return grid[row-1][col-1];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        int n = grid.length;
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("row & col should be between [1," + n);
        }
        return grid[row-1][col-1] && weightedQuickUnionUF.connected(n*n, (row-1) * n + col-1);
    }

    public int numberOfOpenSites() { // number of open sites
        return openSites;
    }

    public boolean percolates() { // does the system percolate?
        int n = grid.length;
        if (n == 1 && openSites == 0) {
            return false;
        }
        return weightedQuickUnionUF.connected(n*n, n*n+1);
    }

    public static void main(String[] args) { // test client (optional)
        Percolation percolation = new Percolation(3);
        percolation.open(1, 3);
        percolation.open(2, 3);
        percolation.open(3, 3);
        percolation.open(3, 1);
        percolation.open(2, 1);

        PercolationVisualizer.draw(percolation, 3);
    }

}