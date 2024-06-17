// import java.util.*;
// import java.util.concurrent.*;
// import javax.mail.*;
// import javax.mail.internet.*;

// public class NotificationManager {
//     private static final String FROM_EMAIL = "your-email@gmail.com";
//     private static final String EMAIL_PASSWORD = "your-password";
//     private static final String SMTP_HOST = "smtp.gmail.com";
//     private static final int SMTP_PORT = 587;
//     private static final String SMS_API_KEY = "your-api-key";
//     private static final String SMS_API_SECRET = "your-api-secret";
//     private static final String SMS_API_URL = "https://api.smsprovider.com/send";

//     private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

//     // Example method to send booking confirmation email
//     public static void sendBookingConfirmationEmail(String recipientEmail, String bookingDetails) {
//         Properties props = new Properties();
//         props.put("mail.smtp.auth", "true");
//         props.put("mail.smtp.starttls.enable", "true");
//         props.put("mail.smtp.host", SMTP_HOST);
//         props.put("mail.smtp.port", SMTP_PORT);

//         Session session = Session.getInstance(props, new Authenticator() {
//             protected PasswordAuthentication getPasswordAuthentication() {
//                 return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
//             }
//         });

//         try {
//             Message message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(FROM_EMAIL));
//             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
//             message.setSubject("Booking Confirmation");
//             message.setText("Dear User,\n\n" + bookingDetails + "\n\nThank you for booking with us!");

//             Transport.send(message);

//             System.out.println("Booking confirmation email sent to " + recipientEmail);
//         } catch (MessagingException e) {
//             throw new RuntimeException(e);
//         }
//     }

//     // Example method to send seat availability alert via SMS
//     public static void sendSeatAvailabilityAlertSMS(String phoneNumber, String message) {
//         // Hypothetical SMS API call
//         String apiUrl = SMS_API_URL + "?key=" + SMS_API_KEY + "&secret=" + SMS_API_SECRET + "&to=" + phoneNumber + "&text=" + message;

//         try {
//             // Execute HTTP request to send SMS (hypothetical implementation)
//             System.out.println("SMS sent to " + phoneNumber + ": " + message);
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }

//     // Example method to lock a seat and schedule its release
//     public static void lockSeatAndScheduleRelease(Seat seat) {
//         seat.lock(); // Lock the seat

//         // Schedule seat release after LOCK_DURATION seconds
//         ScheduledFuture<?> future = scheduler.schedule(() -> {
//             seat.unlock(); // Unlock the seat
//             System.out.println("Seat (" + (seat.getRow() + 1) + "," + (seat.getCol() + 1) + ") has been automatically unlocked.");
//         }, Seat.LOCK_DURATION, TimeUnit.SECONDS);

//         seat.setScheduledUnlockFuture(future); // Store the future for cancellation if necessary
//     }

//     public static void main(String[] args) {
//         // Simulated booking and seat locking
//         Seat seat = new Seat(1, 1); // Example seat
//         lockSeatAndScheduleRelease(seat);

//         // Simulated booking confirmation
//         String recipientEmail = "user@example.com";
//         String bookingDetails = "Booking details here...";
//         sendBookingConfirmationEmail(recipientEmail, bookingDetails);

//         // Simulated seat availability alert
//         String phoneNumber = "+1234567890";
//         String alertMessage = "Your selected seats are no longer available.";
//         sendSeatAvailabilityAlertSMS(phoneNumber, alertMessage);
//     }
// }

// // Seat class with lock functionality
// class Seat {
//     public static final int LOCK_DURATION = 10; // 10 seconds lock duration

//     private final int row;
//     private final int col;
//     private boolean booked;
//     private boolean locked;
//     private ScheduledFuture<?> scheduledUnlockFuture;

//     public Seat(int row, int col) {
//         this.row = row;
//         this.col = col;
//         this.booked = false;
//         this.locked = false;
//     }

//     public int getRow() {
//         return row;
//     }

//     public int getCol() {
//         return col;
//     }

//     public boolean isBooked() {
//         return booked;
//     }

//     public boolean isLocked() {
//         return locked;
//     }

//     public void book() {
//         booked = true;
//     }

//     public void lock() {
//         locked = true;
//     }

//     public void unlock() {
//         locked = false;
//         if (scheduledUnlockFuture != null && !scheduledUnlockFuture.isDone()) {
//             scheduledUnlockFuture.cancel(true); // Cancel the scheduled unlock task
//         }
//     }

//     public void setScheduledUnlockFuture(ScheduledFuture<?> future) {
//         this.scheduledUnlockFuture = future;
//     }
// }









import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailNotification {

    // Replace with your email credentials and SMTP server details
    private static final String FROM_EMAIL = "your-email@gmail.com";
    private static final String PASSWORD = "your-password";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    public static void sendBookingConfirmation(String recipientEmail, String bookingDetails) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Booking Confirmation");
            message.setText("Dear User,\n\n" + bookingDetails + "\n\nThank you for booking with us!");

            Transport.send(message);

            System.out.println("Booking confirmation email sent to " + recipientEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String recipientEmail = "user@example.com";
        String bookingDetails = "Booking details here...";

        sendBookingConfirmation(recipientEmail, bookingDetails);
    }
}

 public class SMSNotification {

    // Hypothetical SMS service credentials
    private static final String SMS_API_KEY = "your-api-key";
    private static final String SMS_API_SECRET = "your-api-secret";
    private static final String SMS_API_URL = "https://api.smsprovider.com/send";

    public static void sendSeatAvailabilityAlert(String phoneNumber, String message) {
        // Replace with actual implementation to send SMS
        // Example: Using a hypothetical SMS API
        String apiUrl = SMS_API_URL + "?key=" + SMS_API_KEY + "&secret=" + SMS_API_SECRET + "&to=" + phoneNumber + "&text=" + message;

        try {
            // Execute HTTP request to send SMS
            // Example: using HttpClient (Apache HttpClient) or HttpURLConnection
            System.out.println("SMS sent to " + phoneNumber + ": " + message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String phoneNumber = "+1234567890";
        String message = "Your selected seats are no longer available.";

        sendSeatAvailabilityAlert(phoneNumber, message);
    }
}