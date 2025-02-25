package api.giybat.uz.util;

public class EmailUtil {
    public static Boolean isEmail(String email) {
        String emailRegex="^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return email.matches(emailRegex);
    }
}
