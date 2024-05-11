package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import game.Connect4;
import ai.AIPlayer;

public class Connect4App extends Application {
    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private Connect4 game;
    private AIPlayer aiPlayer;
    private Circle[][] circles;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        initMainMenu(primaryStage);
    }

    // Initialise the main menu
    private void initMainMenu(Stage primaryStage) {
        VBox menuBox = new VBox(10);
        menuBox.setAlignment(Pos.CENTER);
        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 20px; -fx-background-color: #ffcc00; -fx-text-fill: #000;");
        playButton.setOnAction(e -> startGame(primaryStage));
        menuBox.getChildren().addAll(playButton);

        // Set background image
        Image backgroundImage = new Image("file:src/Image.png");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
        menuBox.setBackground(new Background(background));

        root = new BorderPane();
        root.setCenter(menuBox);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Start the game
    private void startGame(Stage primaryStage) {
        setupGameInterface();
        primaryStage.setScene(new Scene(root));
    }

    // Setup the game interface
    private void setupGameInterface() {
        game = new Connect4();
        aiPlayer = new AIPlayer(game);
        circles = new Circle[ROWS][COLUMNS];
        root.setTop(createMenu());

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); 
        
        // Create circles for the game board
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle circle = new Circle(TILE_SIZE / 2);
                circle.setFill(Color.WHITE);
                circle.setStroke(Color.BLACK);
                circles[row][col] = circle;
                gridPane.add(circle, col, row);
            }
        }

        GridPane buttonGrid = new GridPane();
        buttonGrid.setAlignment(Pos.CENTER); 

        // Create drop buttons for each column
        for (int col = 0; col < COLUMNS; col++) {
            final int column = col;
            Button dropButton = new Button("Drop");
            dropButton.setOnAction(e -> makeMove(column));
            buttonGrid.add(dropButton, col, 0);

            buttonGrid.setHgap(40);
        }

        // Set background color for the game board
        gridPane.setStyle("-fx-background-color: blue;");

        root.setCenter(gridPane);
        root.setBottom(buttonGrid);
    }

    // Create the menu bar
    private MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();

        // Create game menu
        Menu gameMenu = new Menu("Game");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> resetGame());
        MenuItem exitGame = new MenuItem("Exit");
        exitGame.setOnAction(e -> System.exit(0));
        gameMenu.getItems().addAll(newGame, exitGame);

        // Create help menu
        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("Rule");
        about.setOnAction(e -> showAbout());
        helpMenu.getItems().add(about);

        menuBar.getMenus().addAll(gameMenu, helpMenu);
        return menuBar;
    }

    // Reset the game
    private void resetGame() {
        game = new Connect4();
        aiPlayer = new AIPlayer(game);
        updateBoard();
    }

    // Show the about dialog
    private void showAbout() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About Connect4");
        aboutAlert.setHeaderText(null);
        aboutAlert.setContentText("The first player to form a horizontal, vertical, or diagonal line of four on the board.");
        aboutAlert.showAndWait();
    }

    // Make a move in the game
    private void makeMove(int col) {
        if (game.isValidMove(col)) {
            game.makeMove(col, Connect4.HUMAN_PLAYER);
            updateBoard();
            checkGameState();

            if (!game.isGameOver()) {
                int aiMove = aiPlayer.findBestMove();
                game.makeMove(aiMove, Connect4.AI_PLAYER);
                updateBoard();
                checkGameState();
            }
        }
    }

    // Update the game board
    private void updateBoard() {
        int[][] board = game.getBoard();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (board[row][col] == Connect4.HUMAN_PLAYER) {
                    circles[row][col].setFill(Color.RED);
                } else if (board[row][col] == Connect4.AI_PLAYER) {
                    circles[row][col].setFill(Color.YELLOW);
                } else {
                    circles[row][col].setFill(Color.WHITE);
                }
            }
        }
    }

    // Check the game state and show the result if the game is over
    private void checkGameState() {
        if (game.isGameOver()) {
            String message = "Game Over! ";
            message += (game.getWinner() == Connect4.HUMAN_PLAYER) ? "You win!" : (game.getWinner() == Connect4.AI_PLAYER) ? "AI wins!" : "It's a draw!";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}