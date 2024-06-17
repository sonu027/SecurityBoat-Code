import java.util.*;
import java.util.concurrent.*;

class Seat {
    private final int row;
    private final int col;
    private boolean booked;
    private boolean locked;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        this.booked = false;
        this.locked = false;
    }

    public boolean isBooked() {
        return booked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void book() {
        booked = true;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    @Override
    public String toString() {
        return (booked ? "[B]" : (locked ? "[L]" : "[A]")); // B for booked, L for locked, A for available
    }
}

class Screen {
    private final int id;
    private Seat[][] seats;
    private final int rows;
    private final int cols;
    private final Map<Integer, ScheduledFuture<?>> lockedSeats;
    private final ScheduledExecutorService scheduler;
    private static final int LOCK_DURATION = 10; // lock duration in seconds

    public Screen(int id, int rows, int cols) {
        this.id = id;
        this.rows = rows;
        this.cols = cols;
        seats = new Seat[rows][cols];
        lockedSeats = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
        initializeSeats();
    }

    private void initializeSeats() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                seats[i][j] = new Seat(i, j);
            }
        }
    }

    public void displaySeating() {
        System.out.println("Seating Chart for Screen " + id + ":");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean lockSeat(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols && !seats[row][col].isBooked() && !seats[row][col].isLocked()) {
            seats[row][col].lock();
            int seatId = row * cols + col;
            ScheduledFuture<?> future = scheduler.schedule(() -> unlockSeat(row, col), LOCK_DURATION, TimeUnit.SECONDS);
            lockedSeats.put(seatId, future);
            return true;
        }
        return false;
    }

    public void unlockSeat(int row, int col) {
        int seatId = row * cols + col;
        seats[row][col].unlock();
        lockedSeats.remove(seatId);
        System.out.println("Seat (" + (row + 1) + "," + (col + 1) + ") has been unlocked.");
    }

    public void bookSeat(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols && !seats[row][col].isBooked() && seats[row][col].isLocked()) {
            seats[row][col].book();
            int seatId = row * cols + col;
            if (lockedSeats.containsKey(seatId)) {
                lockedSeats.get(seatId).cancel(true);
                lockedSeats.remove(seatId);
            }
            System.out.println("Seat (" + (row + 1) + "," + (col + 1) + ") has been booked.");
        }
    }

    public void resetSeats() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                seats[i][j].unlock();
                seats[i][j] = new Seat(i, j);
            }
        }
    }

    public void configureSeats(int rows, int cols) {
        resetSeats();
        seats = new Seat[rows][cols];
        initializeSeats();
        System.out.println("Screen " + id + " has been reconfigured with " + rows + " rows and " + cols + " columns.");
    }

    public int getId() {
        return id;
    }
}

class AdminPanel {
    private static final List<Screen> screens = new ArrayList<>();
    private static final Map<String, String> bookings = new HashMap<>();
    private static final Map<String, Double> foodMenu = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Screen Management");
            System.out.println("2. Booking Management");
            System.out.println("3. Food Menu Management");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    screenManagement(scanner);
                    break;
                case 2:
                    bookingManagement(scanner);
                    break;
                case 3:
                    foodMenuManagement(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void screenManagement(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Add Screen");
            System.out.println("2. Configure Screen");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter screen id: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter number of rows: ");
                    int rows = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter number of columns: ");
                    int cols = Integer.parseInt(scanner.nextLine());
                    screens.add(new Screen(id, rows, cols));
                    System.out.println("Screen added successfully.");
                    break;
                case 2:
                    System.out.print("Enter screen id to configure: ");
                    int screenId = Integer.parseInt(scanner.nextLine());
                    Optional<Screen> screenOpt = screens.stream().filter(s -> s.getId() == screenId).findFirst();
                    if (screenOpt.isPresent()) {
                        System.out.print("Enter new number of rows: ");
                        rows = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new number of columns: ");
                        cols = Integer.parseInt(scanner.nextLine());
                        screenOpt.get().configureSeats(rows, cols);
                    } else {
                        System.out.println("Screen not found.");
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void bookingManagement(Scanner scanner) {
        while (true) {
            System.out.println("\n1. View Bookings");
            System.out.println("2. Manage Bookings");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Current Bookings:");
                    for (Map.Entry<String, String> booking : bookings.entrySet()) {
                        System.out.println("Booking ID: " + booking.getKey() + ", Details: " + booking.getValue());
                    }
                    break;
                case 2:
                    System.out.print("Enter booking ID to manage: ");
                    String bookingId = scanner.nextLine();
                    if (bookings.containsKey(bookingId)) {
                        System.out.println("Booking Details: " + bookings.get(bookingId));
                        System.out.println("1. Cancel Booking");
                        System.out.println("2. Back");

                        System.out.print("Enter your choice: ");
                        int manageChoice = Integer.parseInt(scanner.nextLine());

                        switch (manageChoice) {
                            case 1:
                                bookings.remove(bookingId);
                                System.out.println("Booking cancelled.");
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    } else {
                        System.out.println("Booking ID not found.");
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void foodMenuManagement(Scanner scanner) {
        while (true) {
            System.out.println("\n1. View Food Menu");
            System.out.println("2. Update Food Menu");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Food Menu:");
                    for (Map.Entry<String, Double> item : foodMenu.entrySet()) {
                        System.out.println("Item: " + item.getKey() + ", Price: $" + item.getValue());
                    }
                    break;
                case 2:
                    System.out.print("Enter food item name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter item price: ");
                    double itemPrice = Double.parseDouble(scanner.nextLine());
                    foodMenu.put(itemName, itemPrice);
                    System.out.println("Food menu updated successfully.");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}