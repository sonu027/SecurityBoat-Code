import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class SecureAuthenticationAndEncryption {
    private static Map<String, String> userCredentials = new HashMap<>();
    private static final String AES = "AES";

    public static void main(String[] args) {
        // Simulate user database
        userCredentials.put("user1", "password1");
        userCredentials.put("user2", "password2");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (authenticateUser(username, password)) {
            System.out.println("Authentication successful.");
            
            // Encrypt sensitive data (password)
            try {
                String encryptedPassword = encrypt(password);
                System.out.println("Encrypted password: " + encryptedPassword);
            } catch (Exception e) {
                System.err.println("Error while encrypting: " + e.getMessage());
            }

        } else {
            System.out.println("Authentication failed. Invalid username or password.");
        }
    }

    private static boolean authenticateUser(String username, String password) {
        // Check if the username exists and the password matches
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private static String encrypt(String data) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private static String decrypt(String encryptedData) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

        return new String(decryptedData);
    }
}