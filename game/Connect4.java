package game;

public class Connect4 {
    public static final int ROWS = 6; 
    public static final int COLUMNS = 7;
    public static final int EMPTY = 0;
    public static final int HUMAN_PLAYER = 1;
    public static final int AI_PLAYER = 2;
    private int[][] board;
    private boolean gameOver;
    private int winner;
    private int currentPlayer;

    /**
     * Constructor for Connect4 class.
     * Initialises the board and game state.
     */
    public Connect4() {
        board = new int[ROWS][COLUMNS];
        gameOver = false;
        winner = EMPTY;
        currentPlayer = Connect4.HUMAN_PLAYER;
        initializeBoard();
    }

    /**
     * Initialises the board with empty cells.
     */
    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = EMPTY;
            }
        }
    }

    /**
     * Checks if a move is valid.
     * @param col The column to make the move.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidMove(int col) {
        return col >= 0 && col < COLUMNS && board[0][col] == EMPTY;
    }

    /**
     * Makes a move on the board.
     * @param col The column to make the move.
     * @param player The player making the move.
     * @return True if the move is successful, false otherwise.
     */
    
 // Add a logger or a simple boolean to toggle verbose output
    private boolean verbose = true;

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    private void log(String message) {
        if (verbose) {
            System.out.println(message);
        }
    }

    // Modify the method to use the log method instead of System.out.println
    public boolean makeMove(int col, int player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                // Replace direct calls with the log method
                log("Player " + player + " placed a piece in column " + col);
                
                boolean won = isWinningMove(row, col, player);
                if (won) {
                    gameOver = true;
                    winner = player;
                    log("Winning move by player " + player + " at column " + col + ", row " + row);
                } else if (isBoardFull()) {
                    gameOver = true;
                    winner = EMPTY;
                    log("The game is a draw.");
                }
                return true;
            }
        }
        return false;
    }

    
    /**
     * Undoes the last move made on the board.
     * @param col The column of the last move.
     */
    public void undoMove(int col) {
        for (int row = 0; row < ROWS; row++) {
            if (board[row][col] != EMPTY) {
                board[row][col] = EMPTY;
                break;
            }
        }
    }

    /**
     * Checks if the game is over.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        if (checkWinner() != 0) {
            return true;
        }

        for (int i = 0; i < COLUMNS; i++) {
            if (board[0][i] == EMPTY) {
                return false;
            }
        }

        return true;
    }

    public boolean dropDisc(int column, int player) {
        // Ensure column is within bounds
        if (column < 0 || column >= COLUMNS) {
            return false;
        }

        // Find the lowest empty spot in the column
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == EMPTY) {
                board[row][column] = player;
                return true;
            }
        }
        return false; // Column is full
    }
    
    /**
     * Checks for a winner on the board.
     * @return The player number of the winner, or 0 if there is no winner.
     */
    public int checkWinner() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int player = board[row][col];
                if (player == EMPTY) continue;

                if (col + 3 < COLUMNS &&
                    player == board[row][col + 1] &&
                    player == board[row][col + 2] &&
                    player == board[row][col + 3]) {
                    return player;
                }

                if (row + 3 < ROWS &&
                    player == board[row + 1][col] &&
                    player == board[row + 2][col] &&
                    player == board[row + 3][col]) {
                    return player;
                }

                if (row + 3 < ROWS && col + 3 < COLUMNS &&
                    player == board[row + 1][col + 1] &&
                    player == board[row + 2][col + 2] &&
                    player == board[row + 3][col + 3]) {
                    return player;
                }

                if (row + 3 < ROWS && col - 3 >= 0 &&
                    player == board[row + 1][col - 1] &&
                    player == board[row + 2][col - 2] &&
                    player == board[row + 3][col - 3]) {
                    return player;
                }
            }
        }
        return 0;
    }

 // Inside the Connect4 class
    public boolean checkForWin(int player) {
        // Check all rows for a horizontal win
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == player && 
                    board[row][col + 1] == player &&
                    board[row][col + 2] == player && 
                    board[row][col + 3] == player) {
                    return true;
                }
            }
        }

        // Check all columns for a vertical win
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if (board[row][col] == player && 
                    board[row + 1][col] == player &&
                    board[row + 2][col] == player && 
                    board[row + 3][col] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (bottom left to top right)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == player && 
                    board[row - 1][col + 1] == player &&
                    board[row - 2][col + 2] == player && 
                    board[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }

        // Check diagonal (top left to bottom right)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                if (board[row][col] == player && 
                    board[row + 1][col + 1] == player &&
                    board[row + 2][col + 2] == player && 
                    board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }

        return false; // No win found
    }

    
    /**
     * Gets the winner of the game.
     * @return The player number of the winner, or 0 if there is no winner.
     */
    public int getWinner() {
        return checkWinner();
    }

    /**
     * Gets a copy of the game board.
     * @return A copy of the game board.
     */
    public int[][] getBoard() {
        int[][] copy = new int[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, COLUMNS);
        }
        return copy;
    }

    /**
     * Checks if a move is a winning move.
     * @param row The row of the move.
     * @param col The column of the move.
     * @param player The player making the move.
     * @return True if the move is a winning move, false otherwise.
     */
    public boolean isWinningMove(int row, int col, int player) {
        for (int j = 0; j < COLUMNS - 3; j++) {
            if (board[row][j] == player && board[row][j + 1] == player &&
                board[row][j + 2] == player && board[row][j + 3] == player) {
                System.out.println("Horizontal win detected at row " + row + " starting at column " + j);
                return true;
            }
        }

        if (row <= ROWS - 4) {
            if (board[row][col] == player && board[row + 1][col] == player &&
                board[row + 2][col] == player && board[row + 3][col] == player) {
                return true;
            }
        }

        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLUMNS - 3; j++) {
                if (board[i][j] == player && board[i + 1][j + 1] == player &&
                    board[i + 2][j + 2] == player && board[i + 3][j + 3] == player) {
                    return true;
                }
            }
        }

        for (int i = 3; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS - 3; j++) {
                if (board[i][j] == player && board[i - 1][j + 1] == player &&
                    board[i - 2][j + 2] == player && board[i - 3][j + 3] == player) {
                    return true;
                }
            }
        }

        System.out.println("No win detected for player " + player + " at column " + col + ", row " + row);
        return false;
    }
    
    private void switchPlayer() {
        if (currentPlayer == Connect4.HUMAN_PLAYER) {
            currentPlayer = Connect4.AI_PLAYER;
        } else {
            currentPlayer = Connect4.HUMAN_PLAYER;
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if the board is full.
     * @return True if the board is full, false otherwise.
     */
    private boolean isBoardFull() {
        for (int col = 0; col < COLUMNS; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints the current state of the board.
     */
    public void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}