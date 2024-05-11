package ai;

import game.Connect4;

/**
 * AIPlayer class represents an AI player for the Connect4 game.
 */
public class AIPlayer {
    private final int MAX_DEPTH = 4;
    private Connect4 connect4Game;

    public int chooseMove() {
        return findBestMove();
    }

    
    /**
     * Constructor for AIPlayer class.
     * @param game The Connect4 game instance.
     */
    public AIPlayer(Connect4 game) {
        this.connect4Game = game;
    }

    /**
     * Finds the best move for the AI player.
     * @return The column number of the best move.
     */
    public int findBestMove() {
        Move bestMove = minimax(0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        System.out.println("Best move found: Column " + bestMove.getCol() + " with score " + bestMove.getScore());
        return bestMove.getCol();
    }

    /**
     * Performs the minimax algorithm to find the best move.
     * @param depth The current depth of the search.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @param isMaximizing Indicates if it's the maximising player's turn.
     * @return The best move found.
     */
    private Move minimax(int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == MAX_DEPTH || connect4Game.isGameOver()) {
            return new Move(scoreBoard(), -1);
        }

        if (isMaximizing) {
            return max(depth, alpha, beta);
        } else {
            return min(depth, alpha, beta);
        }
    }

    /**
     * Performs the max part of the minimax algorithm.
     * @param depth The current depth of the search.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @return The best move found.
     */
    private Move max(int depth, int alpha, int beta) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;

        for (int col = 0; col < Connect4.COLUMNS; col++) {
            if (connect4Game.isValidMove(col)) {
                connect4Game.makeMove(col, Connect4.AI_PLAYER);
                int score = minimax(depth + 1, alpha, beta, false).getScore();
                connect4Game.undoMove(col);

                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }

                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        System.out.println("Maximizing at depth " + depth + ", best score: " + bestScore + ", best column: " + bestCol);
        return new Move(bestScore, bestCol);
    }

    /**
     * Performs the min part of the minimax algorithm.
     * @param depth The current depth of the search.
     * @param alpha The alpha value for alpha-beta pruning.
     * @param beta The beta value for alpha-beta pruning.
     * @return The best move found.
     */
    private Move min(int depth, int alpha, int beta) {
        int bestScore = Integer.MAX_VALUE;
        int bestCol = -1;

        for (int col = 0; col < Connect4.COLUMNS; col++) {
            if (connect4Game.isValidMove(col)) {
                connect4Game.makeMove(col, Connect4.HUMAN_PLAYER);
                int score = minimax(depth + 1, alpha, beta, true).getScore();
                connect4Game.undoMove(col);

                if (score < bestScore) {
                    bestScore = score;
                    bestCol = col;
                }

                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        System.out.println("Minimizing at depth " + depth + ", best score: " + bestScore + ", best column: " + bestCol);
        return new Move(bestScore, bestCol);
    }

    /**
     * Scores the current game board.
     * @return The score of the board.
     */
    private int scoreBoard() {
        int score = 0;

        // Check for AI player's pieces in the middle column
        for (int row = 0; row < Connect4.ROWS; row++) {
            if (connect4Game.getBoard()[row][Connect4.COLUMNS / 2] == Connect4.AI_PLAYER) {
                score += 3;
            }
        }

        // Evaluate lines in all directions
        for (int row = 0; row < Connect4.ROWS; row++) {
            for (int col = 0; col < Connect4.COLUMNS; col++) {
                score += evaluateLine(row, col, 1, 0); // Vertical
                score += evaluateLine(row, col, 0, 1); // Horizontal
                score += evaluateLine(row, col, 1, 1); // Diagonal up
                score += evaluateLine(row, col, 1, -1); // Diagonal down
            }
        }
        System.out.println("Score for board: " + score);
        return score;
    }

    /**
     * Evaluates a line of pieces on the game board.
     * @param row The starting row of the line.
     * @param col The starting column of the line.
     * @param deltaRow The change in row for each step.
     * @param deltaCol The change in column for each step.
     * @return The score of the line.
     */
    private int evaluateLine(int row, int col, int deltaRow, int deltaCol) {
        int aiCount = 0;
        int humanCount = 0;
        int emptyCount = 0;

        // Count the number of AI player's pieces, human player's pieces, and empty spaces in the line
        for (int i = 0; i < 4; i++) {
            int currentRow = row + i * deltaRow;
            int currentCol = col + i * deltaCol;
            if (currentRow >= 0 && currentRow < Connect4.ROWS && currentCol >= 0 && currentCol < Connect4.COLUMNS) {
                if (connect4Game.getBoard()[currentRow][currentCol] == Connect4.AI_PLAYER) {
                    aiCount++;
                } else if (connect4Game.getBoard()[currentRow][currentCol] == Connect4.HUMAN_PLAYER) {
                    humanCount++;
                } else {
                    emptyCount++;
                }
            }
        }

        // Assign scores based on the line configuration
        if (humanCount == 3 && emptyCount == 1) {
            return -500; // Opponent has 3 in a row
        } else if (aiCount == 3 && emptyCount == 1) {
            return 50; // AI has 3 in a row
        } else if (humanCount == 2 && emptyCount == 2) {
            return -50; // Opponent has 2 in a row
        } else {
            return aiCount * 5 - humanCount * 5; // Reduced impact
        }
    }

    /**
     * Move class represents a move in the Connect4 game.
     */
    private class Move {
        private int score;
        private int col;

        /**
         * Constructor for Move class.
         * @param score The score of the move.
         * @param col The column number of the move.
         */
        public Move(int score, int col) {
            this.score = score;
            this.col = col;
        }
        

        /**
         * Gets the score of the move.
         * @return The score.
         */
        public int getScore() {
            return this.score;
        }

        /**
         * Gets the column number of the move.
         * @return The column number.
         */
        public int getCol() {
            return this.col;
        }
    }
}