import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int[][] blocks;
    private int manhattan = 0;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = new int[blocks.length][blocks.length];
        int expectedValue = 1;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                this.blocks[i][j] = blocks[i][j];
                int actualValue = blocks[i][j];
                manhattan += computeManhattanSumItem(actualValue, expectedValue, i, j);
                expectedValue++;
            }
        }
    }

    private Board(Board board, int i1, int j1, int i2, int j2) { // new board with exchange
        int[][] newBlocks = board.blocks;
        this.blocks = new int[newBlocks.length][newBlocks.length];
        this.manhattan = board.manhattan;

        for (int i = 0; i < newBlocks.length; i++) {
            System.arraycopy(newBlocks[i], 0, this.blocks[i], 0, newBlocks.length);
        }

        int i1j1ExpectedValue = i1 * newBlocks.length + j1 + 1;
        int i2j2ExpectedValue = i2 * newBlocks.length + j2 + 1;

        int i1j1BeforeManhattanSum = computeManhattanSumItem(this.blocks[i1][j1], i1j1ExpectedValue, i1, j1);
        int i2j2BeforeManhattanSum = computeManhattanSumItem(this.blocks[i2][j2], i2j2ExpectedValue, i2, j2);

        exch(this, i1, j1, i2, j2);

        int i1j1AfterManhattanSum = computeManhattanSumItem(this.blocks[i1][j1], i1j1ExpectedValue, i1, j1);
        int i2j2AfterManhattanSum = computeManhattanSumItem(this.blocks[i2][j2], i2j2ExpectedValue, i2, j2);

        this.manhattan = this.manhattan - i1j1BeforeManhattanSum - i2j2BeforeManhattanSum + i1j1AfterManhattanSum + i2j2AfterManhattanSum;
    }

    private int computeManhattanSumItem(int actualValue, int expectedValue, int i, int j) {
        int result = 0;
        if (expectedValue != actualValue && actualValue != 0) {
            int rowDifference = (actualValue - 1) / blocks.length;
            int colDifference = (actualValue - 1) % blocks.length;
            result = (Math.abs(rowDifference - i) + Math.abs(colDifference - j));
        }
        return result;
    }

    public int dimension() { // board dimension n
        return blocks.length;
    }

    public int hamming() { // number of blocks out of place
        int numBlocksWrongPosition = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                int expectedValue = i * blocks.length + j + 1;
                if (expectedValue != blocks[i][j] && blocks[i][j] != 0) numBlocksWrongPosition++;
            }
        }
        return numBlocksWrongPosition;
    }

    public int manhattan() { // sum of Manhattan distances between blocks and goal
        return manhattan;
    }

    public boolean isGoal() { // is this board the goal board?
        return manhattan == 0;
    }

    private void exch(Board b, int i1, int j1, int i2, int j2) {
        int aux = b.blocks[i1][j1];
        b.blocks[i1][j1] = b.blocks[i2][j2];
        b.blocks[i2][j2] = aux;
    }

    public Board twin() { // a board that is obtained by exchanging any pair of blocks
        int i1 = 0, j1 = 0, i2 = 0, j2 = 1;
        if (blocks[0][0] == 0) {
            i1 = 1;
            j1 = 1;
        }
        if (blocks[0][1] == 0) {
            i2 = 1;
            j2 = 0;
        }
        return new Board(this, i1, j1, i2, j2);
    }

    public boolean equals(Object y) { // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        if (dimension() != that.dimension()) return false;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }

    private int findZero() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] == 0) return i * blocks.length + j;
            }
        }
        return -1;
    }

    public Iterable<Board> neighbors() { // all neighboring boards
        int zeroIndex = findZero();
        int i = zeroIndex / blocks.length;
        int j = zeroIndex % blocks.length;

        List<Board> neighbors = new LinkedList<Board>();
        if (i > 0) { // add neighbor moving block from above
            neighbors.add(new Board(this, i, j, i - 1, j));
        }
        if (i < blocks.length - 1) {
            neighbors.add(new Board(this, i, j, i + 1, j));
        }
        if (j > 0) {
            neighbors.add(new Board(this, i, j, i, j - 1));
        }
        if (j < blocks.length - 1) {
            neighbors.add(new Board(this, i, j, i, j + 1));
        }
        return neighbors;
    }

    public String toString() { // string representation of this board (in the output format specified below)
        StringBuilder sb = new StringBuilder();
        sb.append(blocks.length);
        sb.append('\n');
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                sb.append(String.format("%2d", blocks[i][j]));
                if (j < blocks.length - 1) sb.append(' ');
            }
            if (i < blocks.length - 1) sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board b = new Board(tiles);
        StdOut.println(b);
        StdOut.println(b.twin().isGoal());
    }
}