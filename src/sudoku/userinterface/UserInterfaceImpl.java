package sudoku.userinterface;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sudoku.constants.GameState;
import sudoku.problemdomain.Coordinates;
import sudoku.problemdomain.SudokuGame;

import java.awt.*;
import java.util.HashMap;

public class UserInterfaceImpl implements IUSerInterfaceContract.View,
        EventHandler<KeyEvent> {

    private final Stage stage;
    private final Group root;

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    private IUSerInterfaceContract.EventListener listener;

    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_Y = 576;

    private static Color WINDOW_BACKGROUND_COLOR = Color.BLACK;
    private static Color BOARD_FOREGROUND_COLOR = Color.WHITE;
    private static final String SUDOKU = "Sudoku";


    public UserInterfaceImpl (Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initialiseUserInterface();
    }

    private void initialiseUserInterface() {
        drawbackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridlines(root);
        stage.show();
    }

    private void drawGridlines(Group root) {
        int xAnhY = 114;
        int index = 0;
        while (index < 8) {
            int thickness;
            if (index == 2 || index == 5) {
                thickness = 3;
            } else {
                thickness = 2;
            }

            Rectangle verticalLine = getLine (
                    xAnhY + 64 * index,
                    BOARD_PADDING,
                    BOARD_X_Y,
                    thickness
            );

            Rectangle horizontalLine = getLine (
                    BOARD_PADDING,
                    xAnhY + 64 * index,
                    thickness,
                    BOARD_X_Y
            );

            root.getChildren().addAll(
                    verticalLine,
                    horizontalLine
            );

            index++;
        }
    }

    private void drawTextFields (Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;

        final int xAndYDelta = 64;

        // O(n^2) runtime Complex
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int x = xOrigin + xIndex * xAndYDelta;
                int y = yOrigin + yOrigin * xAndYDelta;

                SudokuTextField title = new SudokuTextField(xIndex, yIndex);

                styleSudokuTitle(title, x, y);

                title.setOnKeyPressed(this);

                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), title);

                root.getChildren().add(title);
            }
        }
    }

    private void styleSudokuTitle (SudokuTextField title, double x, double y) {
        Font numberFont = new Font(32);

        title.setFont(numberFont);
        title.setAlignment(Pos.CENTER);

        title.setLayoutX(x);
        title.setLayoutY(y);
        title.setPrefHeight(64);
        title.setPrefWidth(64);

        title.setBackground(Background.EMPTY);
    }


    private void drawSudokuBoard (Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);

        boardBackground.setWidth(BOARD_X_Y);
        boardBackground.setHeight(BOARD_X_Y);

        boardBackground.setFill(BOARD_BACKGROUND_COLOR);

        root.getChildren().addAll(boardBackground);
    }


    private void drawTitle (Group root) {
        Text title = new Text (235, 690, SUDOKU);

        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    private void drawBackground (Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }



    private Rectangle getLine (double x, double y, double height, double width) {
        Rectangle line = new Rectangle();

        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);

        line.setFill(Color.BLACK);
        return line;
    }


    @Override
    public void setListener (IUSerInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    private void updateSquare (int x, int y, int input) {
        SudokuTextField title = textFieldCoordinates.get(new Coordinates(x, y));

        String value = Integer.toString(input);

        if (value.equals("0")) value = "";
        title.textProperty().setValue(value);
    }

    @Override
    public void updateBoard (SudokuGame game) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                TextField title = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(
                        game.getCopyOfGridState()[xIndex][yIndex]
                );

                if (value.equals("0")) value = "";

                title.setText(value);

                if (game.getGameState() == GameState.NEW) {
                    if (value.equals("")) {
                        title.setStyle("-fx-opacity: 1;");
                        title.setDisable(false);
                    } else {
                        title.setStyle("-fx-opacity: 0.8;");
                        title.setDisable(true);
                    }
                }
            }
        }
    }

    @Override
    public void showDialog (String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError (String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }


    @Override
    public void handle (KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getText().matches("[0-9]")) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField) event.getSource()).setText("");
            }
        }
        event.consume();
    }

    private void handleInput (int value, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }
}
