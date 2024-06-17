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

class BookingSystem {
    private final Seat[][] seats;
    private final Map<Integer, ScheduledFuture<?>> lockedSeats;
    private final ScheduledExecutorService scheduler;
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private static final int LOCK_DURATION = 10; // lock duration in seconds

    public BookingSystem() {
        seats = new Seat[ROWS][COLS];
        lockedSeats = new HashMap<>();
        scheduler = Executors.newScheduledThreadPool(1);
        initializeSeats();
    }

    private void initializeSeats() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                seats[i][j] = new Seat(i, j);
            }
        }
    }

    public void displaySeating() {
        System.out.println("Seating Chart:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(seats[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean lockSeat(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS && !seats[row][col].isBooked() && !seats[row][col].isLocked()) {
            seats[row][col].lock();
            int seatId = row * COLS + col;
            ScheduledFuture<?> future = scheduler.schedule(() -> unlockSeat(row, col), LOCK_DURATION, TimeUnit.SECONDS);
            lockedSeats.put(seatId, future);
            return true;
        }
        return false;
    }

    public void unlockSeat(int row, int col) {
        int seatId = row * COLS + col;
        seats[row][col].unlock();
        lockedSeats.remove(seatId);
        System.out.println("Seat (" + (row + 1) + "," + (col + 1) + ") has been unlocked.");
    }

    public void bookSeat(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS && !seats[row][col].isBooked() && seats[row][col].isLocked()) {
            seats[row][col].book();
            int seatId = row * COLS + col;
            if (lockedSeats.containsKey(seatId)) {
                lockedSeats.get(seatId).cancel(true);
                lockedSeats.remove(seatId);
            }
            System.out.println("Seat (" + (row + 1) + "," + (col + 1) + ") has been booked.");
        }
    }
}

public class SeatManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingSystem bookingSystem = new BookingSystem();

        while (true) {
            System.out.println("\n1. Display Seating");
            System.out.println("2. Lock Seat");
            System.out.println("3. Book Seat");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    bookingSystem.displaySeating();
                    break;
                case 2:
                    bookingSystem.displaySeating();
                    System.out.print("Enter seat row to lock: ");
                    int lockRow = Integer.parseInt(scanner.nextLine()) - 1;
                    System.out.print("Enter seat column to lock: ");
                    int lockCol = Integer.parseInt(scanner.nextLine()) - 1;
                    if (bookingSystem.lockSeat(lockRow, lockCol)) {
                        System.out.println("Seat locked successfully.");
                    } else {
                        System.out.println("Failed to lock seat. It might be already booked or locked.");
                    }
                    break;
                case 3:
                    bookingSystem.displaySeating();
                    System.out.print("Enter seat row to book: ");
                    int bookRow = Integer.parseInt(scanner.nextLine()) - 1;
                    System.out.print("Enter seat column to book: ");
                    int bookCol = Integer.parseInt(scanner.nextLine()) - 1;
                    bookingSystem.bookSeat(bookRow, bookCol);
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
}