import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class CheckPwd {
  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hash = "$2a$10$9DkNwxgxHYQKeMz2HdK./.xtej88Q3pJDLcyQ6X/GuaH2xbQ3bN1a";
    System.out.println("password=" + encoder.matches("password", hash));
    System.out.println("admin123=" + encoder.matches("admin123", hash));
    System.out.println("123456=" + encoder.matches("123456", hash));
  }
}
