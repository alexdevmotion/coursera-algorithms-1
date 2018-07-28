import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private Move lastMove;

    private class Move implements Comparable<Move> {
        private Move previous;
        private Board board;
        private int numMoves = 0;
        private int manhattan;

        public Move(Board board) {
            this.board = board;
            this.manhattan = board.manhattan();
        }

        public Move(Board board, Move previous) {
            this(board);
            this.previous = previous;
            this.numMoves = previous.numMoves + 1;
        }

        public int compareTo(Move move) {
            return (this.manhattan - move.manhattan) + (this.numMoves - move.numMoves);
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<Move> moves = new MinPQ<Move>();
        moves.insert(new Move(initial));

        MinPQ<Move> twinMoves = new MinPQ<Move>();
        twinMoves.insert(new Move(initial.twin()));

        while (true) {
            lastMove = expand(moves);
            if (lastMove != null || expand(twinMoves) != null) return;
        }
    }

    private Move expand(MinPQ<Move> moves) {
        if (moves.isEmpty()) return null;
        Move bestMove = moves.delMin();
        if (bestMove.board.isGoal()) return bestMove;
        for (Board neighbor : bestMove.board.neighbors()) {
            if (bestMove.previous == null || !bestMove.previous.board.equals(neighbor)) {
                moves.insert(new Move(neighbor, bestMove));
            }
        }
        return null;
    }

    public boolean isSolvable() {
        return (lastMove != null);
    }

    public int moves() {
        return isSolvable() ? lastMove.numMoves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Move curMove = lastMove;

        Stack<Board> moves = new Stack<Board>();
        while (curMove != null) {
            moves.push(curMove.board);
            curMove = curMove.previous;
        }

        return moves;
    }
}
