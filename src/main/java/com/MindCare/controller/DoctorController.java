package com.MindCare.controller;

import com.MindCare.dto.SessionBooking;
import com.MindCare.entity.Booking;
import com.MindCare.entity.Doctor;
import com.MindCare.entity.User;
import com.MindCare.repository.BookingRepository;
import com.MindCare.repository.DoctorRepository;
import com.MindCare.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private userRepo userRepository;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookSession(@RequestBody SessionBooking booking, Principal principal) {
        // Get authenticated user from JWT token
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Doctor doctor = doctorRepo.findById(booking.getDoctorId()).orElse(null);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        Booking newBooking = new Booking();
        newBooking.setDoctorId(doctor.getId());
        newBooking.setDoctorName(doctor.getName());
        newBooking.setUserEmail(user.getEmail());
        newBooking.setTimeSlot(booking.getTime());

        bookingRepo.save(newBooking);

        return ResponseEntity.ok("Session booked successfully with " + doctor.getName() + " at " + booking.getTime());
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getUserBookings(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        List<Booking> bookings = bookingRepo.findByUserEmail(email);
        return ResponseEntity.ok(bookings);
    }
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId, Principal principal) {
        String email = principal.getName();
        Booking booking = bookingRepo.findById(bookingId).orElse(null);

        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
        }

        if (!booking.getUserEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete this booking");
        }

        bookingRepo.deleteById(bookingId);
        return ResponseEntity.ok("Booking marked as done and deleted");
    }
}
