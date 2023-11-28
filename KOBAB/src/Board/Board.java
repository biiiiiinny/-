package Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import Roulette.RandomRoulette;

public class Board extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private DefaultListModel<String> postListModel;
    private JList<String> postList;
    private List<String> contents;

    public Board() {
        setTitle("KOBAB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        contents = new ArrayList<>();

     // 메인 패널을 BorderLayout으로 설정
        JPanel panel = new JPanel(new BorderLayout());

        // 게시글 작성을 위한 왼쪽 패널
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 게시글 목록을 위한 오른쪽 패널
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 룰렛 기능 추가 버튼
        JButton rouletteButton = new JButton("룰렛 보기");
        rouletteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RandomRoulette roulette = new RandomRoulette();
                roulette.showRouletteDialog();
            }
        });
        
        titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(450, 30));
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setMaximumSize(new Dimension(450, 600));
        // 오른쪽 패널에 룰렛 버튼 추가
        rightPanel.add(rouletteButton);
        
        JButton addButton = new JButton("게시글 작성");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPost();
            }
        });

        postListModel = new DefaultListModel<>();      
        postList = new JList<>(postListModel);
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        postList.addListSelectionListener(e -> {
            int index = postList.getSelectedIndex();
            if (index >= 0) {
                String content = contents.get(index);
                JOptionPane.showMessageDialog(null, content, "게시글 내용", JOptionPane.PLAIN_MESSAGE);
            }
        });
        JScrollPane listScrollPane = new JScrollPane(postList);
        listScrollPane.setMaximumSize(new Dimension(900, 350));

        Font customFont = new Font("CookieRun Regular", Font.PLAIN, 14);

     // 왼쪽 패널에 컴포넌트 추가
        JLabel titleLabel = new JLabel("제목:");
        titleLabel.setFont(customFont);
        leftPanel.add(titleLabel);
        leftPanel.add(titleField);
        
        JLabel contentLabel = new JLabel("내용:");
        contentLabel.setFont(customFont);
        leftPanel.add(contentLabel);
        leftPanel.add(contentScrollPane);
        leftPanel.add(addButton);
        
     // 오른쪽 패널에 컴포넌트 추가
        JLabel postListLabel = new JLabel("게시글 목록:");
        postListLabel.setFont(customFont);
        rightPanel.add(postListLabel);
        rightPanel.add(listScrollPane);

        // 메인 패널에 왼쪽과 오른쪽 패널 추가
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        add(panel);
        setVisible(true);

        // 컴포넌트에 적용할 폰트 설정
        titleField.setFont(customFont);
        contentArea.setFont(customFont);
        postList.setFont(customFont);
        addButton.setFont(customFont);
    }

    private void addPost() {
        String title = titleField.getText();
        String content = contentArea.getText();
        if (!title.isEmpty() && !content.isEmpty()) {
            postListModel.addElement(title);
            contents.add(content);
            titleField.setText("");
            contentArea.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Board().setVisible(true);
            
        });
        
    }
}