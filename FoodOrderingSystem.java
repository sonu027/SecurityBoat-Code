import java.util.*;

public class FoodOrderingSystem {
    private Map<String, Double> menu;
    private Map<String, Integer> cart;

    public FoodOrderingSystem() {
        menu = new HashMap<>();
        cart = new HashMap<>();
        initializeMenu();
    }

    private void initializeMenu() {
        menu.put("Pizza", 8.99);
        menu.put("Burger", 5.49);
        menu.put("Pasta", 7.99);
        menu.put("Salad", 4.99);
        menu.put("Coffee", 2.99);
        menu.put("Juice", 3.49);
    }

    public void displayMenu() {
        System.out.println("Menu:");
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue());
        }
    }

    public void addToCart(String item, int quantity) {
        if (menu.containsKey(item)) {
            cart.put(item, cart.getOrDefault(item, 0) + quantity);
            System.out.printf("Added %d x %s to cart.%n", quantity, item);
        } else {
            System.out.printf("Item %s not found in menu.%n", item);
        }
    }

    public void reviewOrder() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("Your Order:");
        double total = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            double price = menu.get(entry.getKey()) * entry.getValue();
            System.out.printf("%d x %s - $%.2f%n", entry.getValue(), entry.getKey(), price);
            total += price;
        }
        System.out.printf("Total: $%.2f%n", total);
    }

    public void adjustQuantity(String item, int quantity) {
        if (cart.containsKey(item)) {
            if (quantity > 0) {
                cart.put(item, quantity);
                System.out.printf("Updated %s quantity to %d.%n", item, quantity);
            } else {
                cart.remove(item);
                System.out.printf("Removed %s from cart.%n", item);
            }
        } else {
            System.out.printf("Item %s not found in cart.%n", item);
        }
    }

    public void proceedToPayment() {
        reviewOrder();
        if (!cart.isEmpty()) {
            System.out.println("Proceeding to payment...");
            // Payment logic would go here
            System.out.println("Payment successful. Thank you for your order!");
            cart.clear();
        }
    }

    public static void main(String[] args) {
        FoodOrderingSystem system = new FoodOrderingSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Display Menu");
            System.out.println("2. Add to Cart");
            System.out.println("3. Review Order");
            System.out.println("4. Adjust Quantity");
            System.out.println("5. Proceed to Payment");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    system.displayMenu();
                    break;
                case "2":
                    System.out.print("Enter item name: ");
                    String item = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    system.addToCart(item, quantity);
                    break;
                case "3":
                    system.reviewOrder();
                    break;
                case "4":
                    System.out.print("Enter item name: ");
                    String itemToAdjust = scanner.nextLine();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = Integer.parseInt(scanner.nextLine());
                    system.adjustQuantity(itemToAdjust, newQuantity);
                    break;
                case "5":
                    system.proceedToPayment();
                    break;
                case "6":
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}