package api.giybat.uz.util;

public class PhoneUtil {
    public static boolean isPhoneNumber(String phoneNumber) {
        String phoneRegex = "^998\\d{9}$";
        return phoneNumber.matches(phoneRegex);
    }
}
