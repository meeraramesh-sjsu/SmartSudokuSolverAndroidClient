package sudoku.edu.sjsu.model;

public class SudokuUser {

    public SudokuUser(char[][] board, String gameToken, String emailId) {
        super();
        this.board = board;
        this.gameToken = gameToken;
        this.emailId = emailId;
    }
    public SudokuUser() {

    }
    public char[][] getBoard() {
        return board;
    }
    public void setBoard(char[][] board) {
        this.board = board;
    }
    public String getGameToken() {
        return gameToken;
    }
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    private char[][] board;
    String gameToken;
    String emailId;


}
