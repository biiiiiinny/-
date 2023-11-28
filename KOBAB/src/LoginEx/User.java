package LoginEx;

class User {
    private String id;
    private String pw;
    private String name;
    private String nickName;
    private String gender;

    public User(String id, String pw, String name, String nickName, String gender) {
        setId(id);
        setPw(pw);
        setName(name);
        setNickName(nickName);
        setGender(gender);
    }
    public User(String id) {
        setId(id);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof User)) {
            return false;
        }
        User temp = (User)o;

        return id.equals(temp.getId());
    }

    @Override
    public String toString() {
        String info = "아이디 : " + id + "\n";
        info += "비밀번호 : " + pw + "\n";
        info += "이름 : " + name + "\n";
        info += "닉네임 : " + nickName + "\n";
        info += "성별 : " + gender + "\n";
        return info;
    }
}