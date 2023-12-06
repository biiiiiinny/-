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

    public Board() {
        setTitle("KOBAB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1800, 1200);

        contents = new ArrayList<>();

        // 메인 패널을 BorderLayout으로 설정
        JPanel panel = new JPanel(new BorderLayout());

        // 게시글 작성을 위한 왼쪽 패널
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 10, 0)); // 상좌하우 

        // 게시글 목록을 위한 오른쪽 패널
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 1230));
        
        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));
        upPanel.setBorder(BorderFactory.createEmptyBorder(50, 320, 0, 0));
        
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
        upPanel.add(Box.createHorizontalStrut(60)); 
        
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
        upPanel.add(Box.createHorizontalStrut(60)); 

        	
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
        upPanel.add(sButton);
        upPanel.add(Box.createHorizontalStrut(60)); 
        
        JButton chatButton = new JButton("채팅");
        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChatDialog();
            }
        });

        upPanel.add(chatButton);
        upPanel.add(Box.createHorizontalStrut(60));
        
        titleField = new JTextField();
        titleField.setMaximumSize(new Dimension(450, 30));
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setMaximumSize(new Dimension(450, 800));
        // 오른쪽 패널에 룰렛 버튼 추가
        upPanel.add(rouletteButton);
        
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
        listScrollPane.setMaximumSize(new Dimension(800, 850));

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
        panel.add(upPanel, BorderLayout.NORTH);

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

    private void showStoreList() {
        // 가게 목록 정의
        String[] stores = {"멕시카나치킨 병천점", "수신반점 본점", "한솥도시락 병천한기대점", "신전떡복이 한기대점"
                           ,"왕천파닭 병천점", "김밥천국 한기대점", "거성한식식당 2호점", "마슬랜치킨 병천한기대점"
                           , ""};
        

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
        // 간단한 채팅 다이얼로그를 만들어 보여줍니다.
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

        JDialog chatDialog = new JDialog(this, "채팅", true);
        chatDialog.setSize(400, 300);
        chatDialog.setLocationRelativeTo(null);
        chatDialog.add(chatPanel);
        chatDialog.setVisible(true);
    }
}