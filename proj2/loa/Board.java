/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Amit Bhat
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
        _winnerKnown = false;
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        for (int r = 0; r < contents.length; r += 1) {
            for (int c = 0; c < contents[0].length; c += 1) {
                Square sq = sq(c,  r);
                _board[sq.index()] = contents[r][c];
            }
        }
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;

    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        System.arraycopy(board._board, 0, _board, 0, board._board.length);
        _turn = board._turn;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        if (_board[move.getTo().index()] == _turn.opposite()) {
            move = move.captureMove();
        }
        set(move.getTo(), _turn, _turn.opposite());
        set(move.getFrom(), Piece.EMP);
        _moves.add(move);
        _subsetsInitialized = false;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move retracted = _moves.remove(_moves.size() - 1);
        if (retracted.isCapture()) {
            set(retracted.getTo(), _turn);
        } else {
            set(retracted.getTo(), Piece.EMP);
        }
        set(retracted.getFrom(), _turn.opposite(), _turn.opposite());
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        return from.distance(to) == numPiecesInLine(from, to)
                && !(blocked(from, to));
    }


    /** Return the total number of pieces in the line containing
     *  FROM and TO. */
    private int numPiecesInLine(Square from, Square to) {
        int forwardDir = from.direction(to);
        int backwardsDir = to.direction(from);
        int steps = 1; int numPieces = 1;
        Square sq = from.moveDest(forwardDir, steps);
        while (sq != null) {
            if (_board[sq.index()] != Piece.EMP) {
                numPieces += 1;
            }
            steps += 1;
            sq = from.moveDest(forwardDir, steps);
        }
        steps = 1;
        sq = from.moveDest(backwardsDir, steps);
        while (sq != null) {
            if (_board[sq.index()] != Piece.EMP) {
                numPieces += 1;
            }
            steps += 1;
            sq = from.moveDest(backwardsDir, steps);
        }

        return numPieces;
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        int forwardDir = from.direction(to);
        int steps = 1;
        int firstOppPiece = BOARD_SIZE;
        Square sq = from.moveDest(forwardDir, steps);
        while (sq != null) {
            Piece curr = _board[sq.index()];
            if (curr == _turn.opposite()
                    && firstOppPiece == BOARD_SIZE) {
                firstOppPiece = steps;
            }
            steps += 1;
            sq = from.moveDest(forwardDir, steps);
        }

        int moveDist = from.distance(to);
        return firstOppPiece < moveDist || _board[to.index()] == _turn;
    }



    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        for (int i = 0; i < ALL_SQUARES.length; i += 1) {
            for (int j = 0; j < ALL_SQUARES.length; j += 1) {
                Square from = ALL_SQUARES[i];
                Square to = ALL_SQUARES[j];
                if (_board[from.index()] != Piece.EMP) {
                    if (isLegal(from, to)) {
                        legalMoves.add(Move.mv(from, to));
                    }
                }
            }
        }
        return legalMoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (!_subsetsInitialized) {
                computeRegions();
            }
            if (piecesContiguous(WP)
                    && piecesContiguous(BP)) {
                _winner = _turn;
            } else if (piecesContiguous(WP)) {
                _winner = WP;
            } else if (piecesContiguous(BP)) {
                _winner = BP;
            } else if (_moves.size() > _moveLimit * 2) {
                _winner = EMP;
            }
            if (_winner != null) {
                _winnerKnown = true;
            }
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }



    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (_board[sq.index()] == EMP
                || _board[sq.index()] != p
                || visited[sq.row()][sq.col()]) {
            return 0;
        } else {
            int numSquares = 1;
            visited[sq.row()][sq.col()] = true;
            for (int i = 0; i < BOARD_SIZE; i += 1) {
                Square nextSq = sq.moveDest(i, 1);
                if (nextSq != null) {
                    numSquares += numContig(nextSq, visited, p);
                }
            }
            return numSquares;
        }
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < visited.length; i += 1) {
            System.arraycopy(INITIAL_ROW_CONTIG, 0,
                    visited[i], 0, visited[i].length);
        }

        for (int r = 0; r < BOARD_SIZE; r += 1) {
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                Square sq = sq(c, r);
                int numWhite = numContig(sq, visited, WP);
                int numBlack = numContig(sq, visited, BP);
                if (numWhite != 0) {
                    _whiteRegionSizes.add(numWhite);
                }
                if (numBlack != 0) {
                    _blackRegionSizes.add(numWhite);
                }
            }
        }

        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }


    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Utility array to create the initial visited array at the beginning
     *  of computeRegions. */
    private static final boolean[] INITIAL_ROW_CONTIG =
            { false, false, false, false, false, false, false, false };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
