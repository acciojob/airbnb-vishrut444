package com.driver.services;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repositories.HotelManagementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelManagementService {

    private final HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();


    public boolean addHotel(Hotel hotel) {
        //check if hotel e  exists or not
        if(hotel==null || hotel.getHotelName()==null || hotel.getHotelName().isEmpty()) return false;

        //check if hotel already exists
        if(hotelManagementRepository.hotelExists(hotel)) return false;

        //if hotel doesn't exist
        hotelManagementRepository.addHotel(hotel);
        return true;
    }

    public int addUser(User user) {
        //adding user to DB
        User user1 = hotelManagementRepository.addUser(user);
        return user1.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        String name = hotelManagementRepository.getHotelWithMostFacilities();
        if(name.isEmpty()) return "";
        return name;
    }

    public int bookRoom(Booking booking) {
        //if rooms are available
        String hotelNameAvailable = hotelManagementRepository.roomsAvailable(booking);
        if(hotelNameAvailable.isEmpty()) return 0;

        //booking
        Hotel hotel = hotelManagementRepository.bookingRoom(hotelNameAvailable,booking);

        int total_fare = hotel.getPricePerNight()*booking.getNoOfRooms();
        return total_fare;
    }


    public int getBookings(Integer aadharCard) {
        return hotelManagementRepository.getNoOfBookings(aadharCard);
    }

    public Hotel addFacilities(List<Facility> newFacilities, String hotelName) {
        return hotelManagementRepository.addFacilities(newFacilities,hotelName);
    }
}
