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

    public String toString() {
        return (booked ? "[B]" : (locked ? "[L]" : "[A]")); // B for booked, L for locked, A for available
    }
}

class Screen {
    private final int id;
    private final Seat[][] seats;
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
}

public class ScreenConfiguration {
    private static final int MAX_SCREENS = 3;
    private static final int ROWS = 6;
    private static final int COLS = 10;
    private static final Screen[] screens = new Screen[MAX_SCREENS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize screens
        for (int i = 0; i < MAX_SCREENS; i++) {
            screens[i] = new Screen(i + 1, ROWS, COLS);
        }

        while (true) {
            System.out.println("\n1. Select Screen");
            System.out.println("2. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    selectScreen(scanner);
                    break;
                case 2:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void selectScreen(Scanner scanner) {
        System.out.println("\nSelect Screen:");
        for (int i = 0; i < MAX_SCREENS; i++) {
            System.out.println((i + 1) + ". Screen " + (i + 1));
        }
        System.out.print("Enter screen number: ");
        int screenNumber = Integer.parseInt(scanner.nextLine());

        if (screenNumber >= 1 && screenNumber <= MAX_SCREENS) {
            Screen selectedScreen = screens[screenNumber - 1];
            manageScreen(scanner, selectedScreen);
        } else {
            System.out.println("Invalid screen number. Please try again.");
        }
    }

    private static void manageScreen(Scanner scanner, Screen selectedScreen) {
        while (true) {
            System.out.println("\n1. Display Seating");
            System.out.println("2. Lock Seat");
            System.out.println("3. Book Seat");
            System.out.println("4. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    selectedScreen.displaySeating();
                    break;
                case 2:
                    selectedScreen.displaySeating();
                    System.out.print("Enter seat row to lock: ");
                    int lockRow = Integer.parseInt(scanner.nextLine()) - 1;
                    System.out.print("Enter seat column to lock: ");
                    int lockCol = Integer.parseInt(scanner.nextLine()) - 1;
                    if (selectedScreen.lockSeat(lockRow, lockCol)) {
                        System.out.println("Seat locked successfully.");
                    } else {
                        System.out.println("Failed to lock seat. It might be already booked or locked.");
                    }
                    break;
                case 3:
                    selectedScreen.displaySeating();
                    System.out.print("Enter seat row to book: ");
                    int bookRow = Integer.parseInt(scanner.nextLine()) - 1;
                    System.out.print("Enter seat column to book: ");
                    int bookCol = Integer.parseInt(scanner.nextLine()) - 1;
                    selectedScreen.bookSeat(bookRow, bookCol);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}