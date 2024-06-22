package com.driver.repositories;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {

    //name,Hotel
    TreeMap<String, Hotel> hotelDB = new TreeMap<>();
    //aadhar, User
    HashMap<Integer, User> userDB = new HashMap<>();
    //bookingId, Booking
    HashMap<String, Booking> bookingDb = new HashMap<>();

    public boolean hotelExists(Hotel hotel) {
        String name = hotel.getHotelName();
        for(Map.Entry<String,Hotel> e: hotelDB.entrySet()){
            if(Objects.equals(e.getKey(), name)) return false;
        }
        return true;
    }

    public void addHotel(Hotel hotel) {
        hotelDB.put(hotel.getHotelName(),hotel);
    }

    public User addUser(User user) {
        userDB.put(user.getaadharCardNo(),user);
        return user;
    }

    public String getHotelWithMostFacilities() {
        int maxFacilities = 0;
        String hotel = null;
        for(Map.Entry<String,Hotel> e:hotelDB.entrySet()){
            int facilities = e.getValue().getFacilities().size();
            if(maxFacilities<facilities){
                maxFacilities = facilities;
                hotel = e.getKey();
            }
        }
        if(hotel==null) return "";
        return hotel;
    }

    public String roomsAvailable(Booking booking) {
        int roomsToBook = booking.getNoOfRooms();
        String hotelName = "";
        for(Map.Entry<String,Hotel> e:hotelDB.entrySet()){
            int noOfRoomsAvailable = e.getValue().getAvailableRooms();
            if(noOfRoomsAvailable>=roomsToBook){
                hotelName = e.getKey();
                break;
            }
        }
        if(hotelName.isEmpty()) return "";
        return hotelName;
    }

    public Hotel bookingRoom(String hotelNameAvailable, Booking booking) {
        //generate booking id
        UUID newUUID = UUID.randomUUID();
        booking.setBookingId(String.valueOf(newUUID));
        //saving Booking to DB
        bookingDb.put(String.valueOf(newUUID),booking);

        //updating hotel available rooms
        Hotel hotel = null;
        for(Map.Entry<String,Hotel> e:hotelDB.entrySet()){
            if(Objects.equals(e.getKey(), hotelNameAvailable)){
                hotel = e.getValue();
                Hotel updateHotel = hotel;
                updateHotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());
                hotelDB.put(hotelNameAvailable,updateHotel);
                break;
            }
        }
        return hotel;
    }

    public int getNoOfBookings(Integer aadharCard) {
        int noOfBookings = 0;
        for(Map.Entry<String,Booking> e:bookingDb.entrySet()){
            if(e.getValue().getBookingAadharCard()==aadharCard) noOfBookings++;
        }
        return noOfBookings;
    }

    public Hotel addFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelDB.get(hotelName);
        if (hotel != null) {
            List<Facility> existingFacilities = hotel.getFacilities();
            for (Facility newFacility : newFacilities) {
                if (!existingFacilities.contains(newFacility)) {
                    existingFacilities.add(newFacility);
                }
            }
            hotel.setFacilities(existingFacilities);
        }

        return hotel;
    }
}
