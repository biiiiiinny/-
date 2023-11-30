package Main;

import LoginEx.LoginForm;
import LoginEx.LoginSuccessListener;
import Board.Board;

public class MainClass implements LoginSuccessListener {

    private Board board;
    private LoginForm loginForm; // LoginForm에 대한 참조를 추가

    public MainClass() {
        loginForm = new LoginForm(); // LoginForm의 참조를 저장
        loginForm.setLoginSuccessListener(this);
        board = new Board();
        board.setVisible(false); // 처음에는 Board를 숨깁니다.
    }

    @Override
    public void onLoginSuccess(String userId) {
        loginForm.setVisible(false); // 로그인 성공 시 LoginForm을 숨깁니다.
        board.setVisible(true); // 로그인 성공 시 Board를 보여줍니다.
    }

    public static void main(String[] args) {
        new MainClass();
    }        
}