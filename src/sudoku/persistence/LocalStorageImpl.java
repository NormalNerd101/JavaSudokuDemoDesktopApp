package sudoku.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sudoku.problemdomain.IStorage;
import sudoku.problemdomain.SudokuGame;

public class LocalStorageImpl {

    private static file GAME_DATA = new file(
        System.getProperty("user.home"),
        "gamedata.txt"
    );
    
    @Override
    public void updateGameData(SudokuGame game) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(GAME_DATA);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream();
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new IOException("Unable to access Game Data.");
        } 
    }


    @Override
    public SudokuGame getGameData() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(GAME_DATA);
        ObjectInputStream   objectInputStream = new ObjectInputStream(fileInputStream);
        
        try {
            SudokuGame gameState = (SudokuGame) objectInputStream.readObject;
            objectInputStream.close();
            return gameState;
        } catch (ClassNotFoundException e) {
            throw new IOException("This method is not implemented yet.");
        }
    }

}
