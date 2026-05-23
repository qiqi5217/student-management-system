public class DBTest {
    public static void main(String[] args) {
        try {
            // MySQL 8.0 推荐的驱动类名
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL 驱动加载成功！");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动类，请检查 jar 包是否正确导入。");
            e.printStackTrace();
        }
    }
}