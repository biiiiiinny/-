package Main;

import LoginEx.LoginForm;
import LoginEx.LoginSuccessListener;
import Board.Board;

public class MainClass implements LoginSuccessListener {

    private Board board;

    public MainClass() {
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginSuccessListener(this);
        board = new Board();
        board.setVisible(false); // 처음에는 Board를 숨깁니다.
    }

    @Override
    public void onLoginSuccess(String userId) {
        board.setVisible(true); // 로그인 성공 시 Board를 보여줍니다.
    }

    public static void main(String[] args) {
        new MainClass();
    }
}