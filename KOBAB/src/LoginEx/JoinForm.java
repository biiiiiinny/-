package LoginEx;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinForm extends JDialog {
    private LoginForm owner;
    private UserDataSet users;

    
    private JLabel lblTitle;
    private JLabel lblId;
    private JLabel lblPw;
    private JLabel lblRe;
    private JLabel lblName;
    private JLabel lblNickName;
    private JRadioButton rbtnMale;
    private JRadioButton rbtnFemale;
    private JTextField tfId;
    private JPasswordField tfPw;
    private JPasswordField tfRe;
    private JTextField tfName;
    private JTextField tfNickName;
    private JButton btnJoin;
    private JButton btnCancel;

    public JoinForm(LoginForm owner) {
        super(owner, "회원가입", true);
        this.owner = owner;
        users = owner.getUsers();
        
        init();
        setDisplay();
        addListeners();
        showFrame();
    }
    private void init() {
        // 크기 고정
        int tfSize = 10;
        Dimension lblSize = new Dimension(80, 35);
        Dimension btnSize = new Dimension(100 ,25);


        lblTitle = new JLabel("- 정보를 입력하세요");
        lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        lblId = new JLabel("아이디", JLabel.LEFT);
        lblId.setPreferredSize(lblSize);
        lblPw = new JLabel("비밀번호", JLabel.LEFT);
        lblPw.setPreferredSize(lblSize);
        lblRe = new JLabel("비밀번호확인", JLabel.LEFT);
        lblRe.setPreferredSize(lblSize);
        lblName = new JLabel("이름", JLabel.LEFT);
        lblName.setPreferredSize(lblSize);
        lblNickName = new JLabel("닉네임", JLabel.LEFT);
        lblNickName.setPreferredSize(lblSize);

        tfId = new JTextField(tfSize);
        tfPw = new JPasswordField(tfSize);
        tfRe = new JPasswordField(tfSize);
        tfName = new JTextField(tfSize);
        tfNickName = new JTextField(tfSize);

        rbtnMale = new JRadioButton("남성", true);
        rbtnFemale = new JRadioButton("여성");
        ButtonGroup group = new ButtonGroup();
        group.add(rbtnMale);
        group.add(rbtnFemale);

        btnJoin = new JButton("회원가입");
        btnJoin.setPreferredSize(btnSize);
        btnCancel = new JButton("취소");
        btnCancel.setPreferredSize(btnSize);

    }
    private void setDisplay() {
        // FlowLayout 왼쪽 정렬
        FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

        // pnlMain(pnlMNorth / pnlMCenter / pnlMSouth)
        JPanel pnlMain = new JPanel(new BorderLayout());

        // pnlMNorth(lblTitle)
        JPanel pnlMNorth = new JPanel(flowLeft);
        pnlMNorth.add(lblTitle);

        // pnlMCenter(pnlId / pnlPw / pnlRe / pnlName / pnlNickName)
        JPanel pnlMCenter = new JPanel(new GridLayout(0, 1));
        JPanel pnlId = new JPanel(flowLeft);
        pnlId.add(lblId);
        pnlId.add(tfId);

        JPanel pnlPw = new JPanel(flowLeft);
        pnlPw.add(lblPw);
        pnlPw.add(tfPw);

        JPanel pnlRe = new JPanel(flowLeft);
        pnlRe.add(lblRe);
        pnlRe.add(tfRe);

        JPanel pnlName = new JPanel(flowLeft);
        pnlName.add(lblName);
        pnlName.add(tfName);

        JPanel pnlNickName = new JPanel(flowLeft);
        pnlNickName.add(lblNickName);
        pnlNickName.add(tfNickName);

        pnlMCenter.add(pnlId);
        pnlMCenter.add(pnlPw);
        pnlMCenter.add(pnlRe);
        pnlMCenter.add(pnlName);
        pnlMCenter.add(pnlNickName);

        // pnlMSouth(rbtnMale / rbtnFemale)
        JPanel pnlMSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlMSouth.add(rbtnMale);
        pnlMSouth.add(rbtnFemale);
        pnlMSouth.setBorder(new TitledBorder("성별"));

        // pnlMain
        pnlMain.add(pnlMNorth, BorderLayout.NORTH);
        pnlMain.add(pnlMCenter, BorderLayout.CENTER);
        pnlMain.add(pnlMSouth, BorderLayout.SOUTH);

        // pnlSouth(btnJoin / btnCancel)
        JPanel pnlSouth = new JPanel();
        pnlSouth.add(btnJoin);
        pnlSouth.add(btnCancel);

        // 화면 테두리의 간격을 주기 위해 설정 (insets 사용 가능)
        pnlMain.setBorder(new EmptyBorder(0, 20, 0, 20));
        pnlSouth.setBorder(new EmptyBorder(0, 0, 10, 0));

        add(pnlMain, BorderLayout.NORTH);
        add(pnlSouth, BorderLayout.SOUTH);
    }
    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                owner.setVisible(true);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                owner.setVisible(true);
            }
        });
        btnJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // 정보 하나라도 비어있으면
                if(isBlank()) {
                    JOptionPane.showMessageDialog(
                            JoinForm.this,
                            "모든 정보를 입력해주세요."
                    );
                    // 모두 입력했을 때
                } else {
                    // Id 중복일 때
                    if(users.isIdOverlap(tfId.getText())) {
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "이미 존재하는 Id입니다."
                        );
                        tfId.requestFocus();

                        // Pw와 Re가 일치하지 않았을 때
                    } else if(!String.valueOf(tfPw.getPassword()).equals(String.valueOf(tfRe.getPassword()))) {
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "비밀번호와 비밀번호확인이 일치하지 않습니다."
                        );
                        tfPw.requestFocus();
                    } else {
                        users.addUsers(new User(
                                tfId.getText(),
                                String.valueOf(tfPw.getPassword()),
                                tfName.getText(),
                                tfNickName.getText(),
                                getGender())
                        );
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "회원가입을 완료했습니다!"
                        );
                        dispose();
                        owner.setVisible(true);
                    }
                }
            }
        });
    }
    public boolean isBlank() {
        boolean result = false;
        if(tfId.getText().isEmpty()) {
            tfId.requestFocus();
            return true;
        }
        if(String.valueOf(tfPw.getPassword()).isEmpty()) {
            tfPw.requestFocus();
            return true;
        }
        if(String.valueOf(tfRe.getPassword()).isEmpty()) {
            tfRe.requestFocus();
            return true;
        }
        if(tfName.getText().isEmpty()) {
            tfName.requestFocus();
            return true;
        }
        if(tfNickName.getText().isEmpty()) {
            tfNickName.requestFocus();
            return true;
        }
        return result;
    }
    
    public String getGender() {
        if(rbtnMale.isSelected()) {
            return rbtnMale.getText();
        }
        return rbtnFemale.getText();
    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}