
class BookingNotificationService {

    
    public void sendBookingConfirmationEmail(String userEmail) {
        
        System.out.println("Booking confirmation email sent to: " + userEmail);
    }

    
    public void sendBookingConfirmationSMS(String userPhoneNumber) {
        
        System.out.println("Booking confirmation SMS sent to: " + userPhoneNumber);
    }
}


class SeatAvailabilityService {

    
    public void sendSeatAvailabilityAlert(String userEmail, String selectedSeats) {
        boolean seatsAvailable = checkSeatAvailability(selectedSeats);

        if (!seatsAvailable) {
           
            sendSeatAvailabilityNotification(userEmail);
        }
    }

    private boolean checkSeatAvailability(String selectedSeats) {
   
        return true; 
    }

    private void sendSeatAvailabilityNotification(String userEmail) {
        
        System.out.println("Seat availability alert sent to: " + userEmail);
    }
}

public class NotificationManager {
    public static void main(String[] args) {
        BookingNotificationService bookingService = new BookingNotificationService();
        SeatAvailabilityService seatService = new SeatAvailabilityService();

        
        bookingService.sendBookingConfirmationEmail("user@example.com");
        bookingService.sendBookingConfirmationSMS("+1234567890");

        
        seatService.sendSeatAvailabilityAlert("user@example.com", "A1, A2");
    }
}