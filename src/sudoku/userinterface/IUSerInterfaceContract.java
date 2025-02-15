package sudoku.userinterface;

import sudoku.problemdomain.SudokuGame;

public interface IUSerInterfaceContract {
    interface EventListener {
        void onSudokuInput(int x, int y, int input);
        void onDialogClick();
    }

    interface View {
        void setListener (IUSerInterfaceContract.EventListener listener);
        void updateSquare (int x, int y, int input);
        void updateBoard (SudokuGame game);
        void showDialog (String Message);
        void showError (String Message);
    }
}
