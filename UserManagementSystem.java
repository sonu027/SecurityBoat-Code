import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserManagementSystem {
    private Map<String, String> users;
    private Scanner scanner;

    public UserManagementSystem() {
        users = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        UserManagementSystem system = new UserManagementSystem();
        system.run();
    }

    public void run() {
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerUser() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        if (users.containsKey(email)) {
            System.out.println("Email already registered. Please try logging in.");
            return;
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        users.put(email, password + ":" + name);
        System.out.println("Registration successful. You can now log in.");
    }

    private void loginUser() {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        String userInfo = users.get(email);
        if (userInfo != null && userInfo.split(":")[0].equals(password)) {
            String name = userInfo.split(":")[1];
            System.out.println("Login successful. Welcome, " + name + "!");
            // Proceed to booking functionalities here
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }
}



//2 way

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Scanner;

// public class UserManagementSystem {
//     private static Map<String, User> users = new HashMap<>();
//     private static Scanner scanner = new Scanner(System.in);

//     public static void main(String[] args) {
//         while (true) {
//             System.out.println("\n1. Register");
//             System.out.println("2. Login");
//             System.out.println("3. Exit");

//             System.out.print("Enter your choice: ");
//             String choice = scanner.nextLine();

//             switch (choice) {
//                 case "1":
//                     register();
//                     break;
//                 case "2":
//                     login();
//                     break;
//                 case "3":
//                     System.out.println("Exiting...");
//                     return;
//                 default:
//                     System.out.println("Invalid choice. Please try again.");
//             }
//         }
//     }

//     private static void register() {
//         System.out.print("Enter your name: ");
//         String name = scanner.nextLine();

//         System.out.print("Enter your email: ");
//         String email = scanner.nextLine();

//         if (users.containsKey(email)) {
//             System.out.println("Email already registered. Please try logging in.");
//             return;
//         }

//         System.out.print("Enter your password: ");
//         String password = scanner.nextLine();

//         User user = new User(name, email, password);
//         users.put(email, user);

//         System.out.println("Registration successful. You can now log in.");
//     }

//     private static void login() {
//         System.out.print("Enter your email: ");
//         String email = scanner.nextLine();

//         System.out.print("Enter your password: ");
//         String password = scanner.nextLine();

//         User user = users.get(email);

//         if (user != null && user.getPassword().equals(password)) {
//             System.out.println("Login successful. Welcome, " + user.getName() + "!");
//             // Proceed to booking functionalities here
//         } else {
//             System.out.println("Invalid email or password. Please try again.");
//         }
//     }
// }

// class User {
//     private String name;
//     private String email;
//     private String password;

//     public User(String name, String email, String password) {
//         this.name = name;
//         this.email = email;
//         this.password = password;
//     }

//     public String getName() {
//         return name;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public String getPassword() {
//         return password;
//     }
// }