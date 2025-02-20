package sudoku;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

import sudoku.userinterface.*;
import sudoku.userinterface.logic.*;
import sudoku.buildlogic.*;

public class SudokuApplication extends Application {
    private IUSerInterfaceContract.View uiImpl;

    @Override
    public void start(Stage primaryStage) throws Exception {
        uiImpl  = new UserInterfaceImpl(primaryStage);
        SudokuBuildLogic.build(uiImpl);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
