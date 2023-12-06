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
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.net.URLEncoder;
import java.net.URL;
import java.awt.Desktop;

public class Board extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private DefaultListModel<String> postListModel;
    private JList<String> postList;
    private List<String> contents;
    private static final int BUTTON_FONT_SIZE = 23; // 버튼 폰트 크기 상수

    
    private static final double BUTTON_WIDTH_RATIO = 0.5;  // 폭 비율
    private static final double BUTTON_HEIGHT_RATIO = 1.8; // 높이 비율

    private List<List<String>> comments; // 댓글 목록을 저장하는 리스트

    public Board() {
        setTitle("KOBAB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1800, 1200);

        contents = new ArrayList<>();

        comments = new ArrayList<>();
        
        // 메인 패널을 BorderLayout으로 설정
        JPanel panel = new JPanel(new BorderLayout());
        
     // 각 버튼에 사용할 폰트 생성
        Font buttonFont = new Font("Noto Sans KR", Font.PLAIN, 16);  // 원하는 폰트 설정으로 변경

        // 게시글 작성을 위한 왼쪽 패널
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(180, 50, 0, 0)); // 상좌하우 

        // 게시글 목록을 위한 오른쪽 패널
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(180, 0, 0, 1150));
        
        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));

        upPanel.setBorder(BorderFactory.createEmptyBorder(40, 500, 0, 0));

        
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
        upPanel.add(storeButton);
        upPanel.add(Box.createHorizontalStrut(30)); 
        
     // 가게 보기 버튼에 액션 리스너 추가
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStoreList();
            }
        });
        
     // 카테고리 버튼
        JButton menuButton = new JButton("카테고리 보기");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCategoryDialog();
            }
        });
        upPanel.add(menuButton);
        upPanel.add(Box.createHorizontalStrut(30)); 

        	
     // 학식당 메뉴 버튼
        JButton sButton = new JButton("오늘 학식 보기");
        sButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 이미지 파일의 경로를 적절히 변경하세요.
                String imagePath = "1.jpeg";
                showImage(imagePath);
            }
        });
        upPanel.add(sButton);
        upPanel.add(Box.createHorizontalStrut(30)); 
        
        JButton chatButton = new JButton("채팅");
        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChatDialog();
            }
        });

        upPanel.add(chatButton);
        upPanel.add(Box.createHorizontalStrut(30));
        
        titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(450, 30));
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setMaximumSize(new Dimension(450, 600));
        // 오른쪽 패널에 룰렛 버튼 추가
        upPanel.add(rouletteButton);
        
        JButton addButton = new JButton("게시글 작성");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPost();
            }
        });

        upPanel.add(Box.createHorizontalStrut(50));
     // 종료 버튼
        JButton exitButton = new JButton("로그아웃");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "프로그램을 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0); // 프로그램 종료
                }
            }
        });

        // upPanel에 종료 버튼 추가
        upPanel.add(exitButton);

     // 회원 정보 확인하기 버튼
        JButton userInfoButton = new JButton("회원 정보");
        userInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberName = "권진영";
                // 회원 정보를 나타내는 창을 생성하여 이름 표
                JOptionPane.showMessageDialog(null, "회원 이름: " + memberName, "회원 정보", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // upPanel에 회원 정보 확인하기 버튼 추가
        upPanel.add(userInfoButton);

        
        postListModel = new DefaultListModel<>();      
        postList = new JList<>(postListModel);
        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        postList.addListSelectionListener(e -> {
            int index = postList.getSelectedIndex();
            if (index >= 0) {
                String content = contents.get(index);
                //JOptionPane.showMessageDialog(null, content, "게시글 내용", JOptionPane.PLAIN_MESSAGE);
            }
        });
        JScrollPane listScrollPane = new JScrollPane(postList);
        listScrollPane.setMaximumSize(new Dimension(800, 650));

        Font customFont = new Font("Noto Sans KR", Font.PLAIN, 16);

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
        panel.add(upPanel, BorderLayout.NORTH);

        add(panel);
        setVisible(true);

        setLocationRelativeTo(null);
        
        // 컴포넌트에 적용할 폰트 설정
        titleField.setFont(customFont);
        contentArea.setFont(customFont);
        postList.setFont(customFont);
        addButton.setFont(customFont);
        

        postList.addListSelectionListener(e -> {
            int index = postList.getSelectedIndex();
            if (index >= 0) {
                String content = contents.get(index);
                showPostDetail(content, comments.get(index)); // 게시글 상세 정보 및 댓글 표시
            }
        });
        
        Dimension buttonSize = new Dimension((int)(100 * BUTTON_WIDTH_RATIO), (int)(30 * BUTTON_HEIGHT_RATIO));
        storeButton.setPreferredSize(buttonSize);
        storeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(30 * BUTTON_HEIGHT_RATIO)));

        menuButton.setPreferredSize(buttonSize);
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(30 * BUTTON_HEIGHT_RATIO)));

        sButton.setPreferredSize(buttonSize);
        sButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(30 * BUTTON_HEIGHT_RATIO)));

        chatButton.setPreferredSize(buttonSize);
        chatButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(30 * BUTTON_HEIGHT_RATIO)));

        rouletteButton.setPreferredSize(buttonSize);
        rouletteButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)(30 * BUTTON_HEIGHT_RATIO)));

    }

    private void showPostDetail(String content, List<String> postComments) {
        JTextArea contentTextArea = new JTextArea(content);
        contentTextArea.setEditable(false);

        JTextArea commentsTextArea = new JTextArea();
        commentsTextArea.setEditable(false);

        // 댓글 목록 추가
        for (String comment : postComments) {
            commentsTextArea.append(comment + "\n");
        }

        JScrollPane commentsScrollPane = new JScrollPane(commentsTextArea);

        // 댓글 달기 버튼
        JButton commentButton = new JButton("댓글 달기");
        commentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newComment = JOptionPane.showInputDialog("댓글을 입력하세요:");
                if (newComment != null && !newComment.isEmpty()) {
                    postComments.add(newComment);
                    commentsTextArea.append(newComment + "\n");
                }
            }
        });

        // 상세 정보 패널
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(new JScrollPane(contentTextArea), BorderLayout.CENTER);
        detailPanel.add(commentsScrollPane, BorderLayout.SOUTH);

        // 댓글 달기 버튼 추가
        detailPanel.add(commentButton, BorderLayout.SOUTH);

        // 상세 정보 다이얼로그
        JDialog detailDialog = new JDialog(this, "게시글 상세 정보", true);
        detailDialog.setSize(400, 300);
        detailDialog.setLocationRelativeTo(null);
        detailDialog.add(detailPanel);
        detailDialog.setVisible(true);
    }
    
    private void addPost() {
        String title = titleField.getText();
        String content = contentArea.getText();
        if (!title.isEmpty() && !content.isEmpty()) {
            postListModel.addElement(title);
            contents.add(content);
            comments.add(new ArrayList<>()); // 새로운 게시글에 대한 댓글 목록 초기화
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

    private void showStoreList() {
        // 가게 목록 정의
        String[] stores = {"멕시카나치킨 병천점", "수신반점 본점", "한솥도시락 병천한기대점", "신전떡복이 한기대점"
                           ,"왕천파닭 병천점", "김밥천국 한기대점", "거성한식식당 2호점", "마슬랜치킨 병천한기대점"
                           , "카페 요깃", "심야술집"};
        

        // 각 가게에 대한 버튼 생성 및 리스너 추가
        JPanel storePanel = new JPanel();
        storePanel.setLayout(new BoxLayout(storePanel, BoxLayout.Y_AXIS));
        for (String store : stores) {
            JButton storeButton = new JButton(store);
            storeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMapForStore(store);
                }
            });
            storePanel.add(storeButton);
        }

        // 가게 목록을 표시하는 다이얼로그 생성 및 표시
        JDialog dialog = new JDialog(this, "가게 목록", true);
        dialog.add(new JScrollPane(storePanel));
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showMapForStore(String storeName) {
        // 네이버 지도 API를 사용하여 지도를 표시하는 로직
        try {
            String encodedStoreName = URLEncoder.encode(storeName, "UTF-8");
            Desktop.getDesktop().browse(new URL("https://map.naver.com/v5/search/" + encodedStoreName).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showImageForCategory(String category) {
        if ("한식".equals(category)) {
            showStoreListDialog();
        } else {
            // 각 카테고리에 해당하는 이미지 경로를 설정
            String imagePath = getImagePathForCategory(category);

            if (imagePath != null) {
                showImage(imagePath);
            } else {
                JOptionPane.showMessageDialog(null, "해당 카테고리에 대한 이미지가 없습니다.", "이미지 없음", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void showStoreListDialog() {
        // 가게 목록을 표시하는 다이얼로그 생성
        JDialog storeListDialog = new JDialog(this, "한식 가게 목록", true);
        
        // 가게 목록 정의
        String[] stores = {"한솥도시락 병천한기대점", "신전떡복이 한기대점",
                          "김밥천국 한기대점", "거성한식식당 2호점"};
        
        // 각 가게에 대한 버튼 생성 및 리스너 추가
        JPanel storePanel = new JPanel();
        storePanel.setLayout(new BoxLayout(storePanel, BoxLayout.Y_AXIS));
        for (String store : stores) {
            JButton storeButton = new JButton(store);
            storeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMapForStore(store);
                }
            });
            storePanel.add(storeButton);
        }

        // 다이얼로그에 가게 목록을 표시하는 패널 추가
        storeListDialog.add(new JScrollPane(storePanel));
        storeListDialog.setSize(300, 400);
        storeListDialog.setLocationRelativeTo(null);
        storeListDialog.setVisible(true);
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
    
    private String[] getStoresFromAPI() {
        String clientId = "ij1uyxo960"; // 네이버 개발자 센터에서 발급받은 클라이언트 ID
        String clientSecret = "Jfx8Tgv7beNUkkY83yiZAv7H3I9Rotwbjrv4TWqI"; // 네이버 개발자 센터에서 발급받은 클라이언트 시크릿
        try {
            String apiURL = "https://openapi.naver.com/v1/search/local.json?query=한기대음식점"; // 검색어 '한기대 음식점'으로 지역 검색
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            // JSON 파싱
            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
            JSONArray items = (JSONArray) jsonResponse.get("items");
            String[] stores = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                String title = (String) item.get("title");
                stores[i] = title.replaceAll("<[^>]*>", ""); // HTML 태그 제거
            }

            return stores;

        } catch (Exception e) {
            e.printStackTrace();
            return new String[] {}; // 에러 발생 시 빈 배열 반환
        }
    }
    
    private void openChatDialog() {
        // 유저 선택 창
        String[] users = {"서정빈", "정예원", "윤성빈", "안예준"};  // 예시 유저 목록
        JComboBox<String> userComboBox = new JComboBox<>(users);
        
        JPanel userSelectionPanel = new JPanel();
        userSelectionPanel.add(new JLabel("대화 상대 선택:"));
        userSelectionPanel.add(userComboBox);

        int userSelectionResult = JOptionPane.showConfirmDialog(
                this,
                userSelectionPanel,
                "채팅 상대 선택",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (userSelectionResult == JOptionPane.OK_OPTION) {
            // OK 버튼이 클릭되었을 때 선택된 유저를 가져옴
            String selectedUser = (String) userComboBox.getSelectedItem();

            // 채팅 창
            JTextArea chatArea = new JTextArea();
            chatArea.setEditable(false);

            JTextField messageField = new JTextField();
            messageField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = messageField.getText();
                    chatArea.append("나: " + message + "\n");
                    messageField.setText("");
                }
            });

            JPanel chatPanel = new JPanel(new BorderLayout());
            chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
            chatPanel.add(messageField, BorderLayout.SOUTH);

            JDialog chatDialog = new JDialog(this, "채팅 (" + selectedUser + ")", true);
            chatDialog.setSize(400, 300);
            chatDialog.setLocationRelativeTo(null);
            chatDialog.add(chatPanel);
            chatDialog.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Board().setVisible(true));
    }
}