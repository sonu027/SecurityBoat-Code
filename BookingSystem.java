import java.util.*;

class Movie {
    private String title;
    private String genre;
    private String showtime;

    public Movie(String title, String genre, String showtime) {
        this.title = title;
        this.genre = genre;
        this.showtime = showtime;
    }

    public String getTitle() {
        return title;
    }

    public String getShowtime() {
        return showtime;
    }

    public String toString() {
        return title + " (" + genre + ") - Showtime: " + showtime;
    }
}

class Seat {
    private int row;
    private int col;
    private boolean booked;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        this.booked = false;
    }

    public boolean isBooked() {
        return booked;
    }

    public void book() {
        booked = true;
    }

    public String toString() {
        return (booked ? "[X]" : "[ ]");
    }
}

class BookingSystem {
    private List<Movie> movies;
    private Seat[][] seats;
    private static final int ROWS = 5;
    private static final int COLS = 5;

    public BookingSystem() {
        movies = new ArrayList<>();
        initializeMovies();
        initializeSeats();
    }

    private void initializeMovies() {
        movies.add(new Movie("The Shawshank Redemption", "Drama", "5:00 PM"));
        movies.add(new Movie("The Godfather", "Crime", "7:00 PM"));
        movies.add(new Movie("The Dark Knight", "Action", "9:00 PM"));
    }

    private void initializeSeats() {
        seats = new Seat[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                seats[i][j] = new Seat(i, j);
            }
        }
    }

    public void displayMovies() {
        System.out.println("Available Movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i));
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

    public boolean selectSeat(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLS && !seats[row][col].isBooked()) {
            seats[row][col].book();
            return true;
        }
        return false;
    }

    public void bookTickets(int movieIndex, List<int[]> selectedSeats) {
        Movie movie = movies.get(movieIndex);
        System.out.println("Booking Confirmation:");
        System.out.println("Movie: " + movie.getTitle());
        System.out.println("Showtime: " + movie.getShowtime());
        System.out.print("Seats: ");
        for (int[] seat : selectedSeats) {
            System.out.print("(" + (seat[0] + 1) + "," + (seat[1] + 1) + ") ");
        }
        System.out.println();
        System.out.println("Total Price: $" + (selectedSeats.size() * 10)); // Assuming each ticket costs $10
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingSystem bookingSystem = new BookingSystem();

        while (true) {
            System.out.println("\n1. Display Movies");
            System.out.println("2. Select Seats");
            System.out.println("3. Book Tickets");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    bookingSystem.displayMovies();
                    break;
                case 2:
                    bookingSystem.displaySeating();
                    break;
                case 3:
                    bookingSystem.displayMovies();
                    System.out.print("Select movie by number: ");
                    int movieIndex = Integer.parseInt(scanner.nextLine()) - 1;

                    List<int[]> selectedSeats = new ArrayList<>();
                    while (true) {
                        bookingSystem.displaySeating();
                        System.out.print("Enter seat row (or -1 to finish): ");
                        int row = Integer.parseInt(scanner.nextLine()) - 1;
                        if (row == -2) break; // Entering -1 ends the seat selection

                        System.out.print("Enter seat column: ");
                        int col = Integer.parseInt(scanner.nextLine()) - 1;

                        if (bookingSystem.selectSeat(row, col)) {
                            selectedSeats.add(new int[]{row, col});
                            System.out.println("Seat booked successfully.");
                        } else {
                            System.out.println("Seat already booked or invalid. Try again.");
                        }
                    }
                    bookingSystem.bookTickets(movieIndex, selectedSeats);
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