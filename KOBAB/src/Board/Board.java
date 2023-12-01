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
        JButton rouletteButton = new JButton("메뉴 추천 룰렛");
        rouletteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RandomRoulette roulette = new RandomRoulette();
                roulette.showRouletteDialog();
            }
        });
        
     // 가게 버튼
        JButton storeButton = new JButton("가게 보기");
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        rightPanel.add(storeButton);
        
     // 카테고리 버튼
        JButton menuButton = new JButton("카테고리 보기");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCategoryDialog();
            }
        });
        rightPanel.add(menuButton);

        
     // 학식당 메뉴 버튼
        JButton sButton = new JButton("오늘 학식 보기");
        sButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 이미지 파일의 경로를 적절히 변경하세요.
                String imagePath = "a.jpeg";
                showImage(imagePath);
            }
        });
        rightPanel.add(sButton);
        
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

        setLocationRelativeTo(null);
        
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
    
    private void showCategoryDialog() {
        // 새 다이얼로그 생성
        JDialog categoryDialog = new JDialog(this, "카테고리 선택", true);
        categoryDialog.setSize(300, 200);

        // 각 카테고리를 위한 버튼 생성
        String[] categories = {"한식", "일식", "중식", "도시락", "기타"};
        for (String category : categories) {
            JButton categoryButton = new JButton(category);
            categoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 카테고리 버튼 클릭 시 이미지 창 표시
                    showImageForCategory(category);
                    categoryDialog.dispose(); // 다이얼로그 닫기
                }
            });
            categoryDialog.add(categoryButton);
        }

        // 다이얼로그의 레이아웃 설정
        categoryDialog.setLayout(new GridLayout(categories.length, 1));
        
        // 다디얼로그 위치 화면 중앙으로 설정
        categoryDialog.setLocationRelativeTo(null);
        
        // 다이얼로그 표시
        categoryDialog.setVisible(true);
    }

    private void showImageForCategory(String category) {
        // 각 카테고리에 해당하는 이미지 경로를 설정
        String imagePath = getImagePathForCategory(category);

        if (imagePath != null) {
            showImage(imagePath);
        } else {
            JOptionPane.showMessageDialog(null, "해당 카테고리에 대한 이미지가 없습니다.", "이미지 없음", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String getImagePathForCategory(String category) {
    	// 예시: 이미지 파일이 "category_이름.jpg" 형식으로 저장되어 있다고 가정
        String imageName = "category_" + category.toLowerCase() + ".jpeg";
        
        // 실제 파일 경로를 가져오는 로직으로 변경해야 함
        // 여기서는 현재 디렉토리에 이미지 파일이 있다고 가정
        File imageFile = new File(imageName);

        if (imageFile.exists()) {
            return imageFile.getAbsolutePath();
        } else {
            // 파일이 없을 경우 경고 메시지 출력
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Board().setVisible(true);
            
        });
        
    }
    
    private void showImage(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        JLabel label = new JLabel(icon);

        // JDialog 생성
        JDialog dialog = new JDialog(this, "학식 메뉴", true); // 모달로 설정

        // 이미지 표시를 위한 패널 추가
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        
        // 이미지 크기에 맞게 다이얼로그 크기 설정
        dialog.setSize(icon.getIconWidth() + 20, icon.getIconHeight() + 60);
        
        dialog.add(panel);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}