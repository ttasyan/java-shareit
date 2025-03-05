package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(long userId, NewBookingRequest newBookingRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(newBookingRequest.getItemId()).orElseThrow(() -> new NotFoundException("Item not found"));


        Booking booking = BookingMapper.fromBookingRequest(newBookingRequest, item, user);
        booking.setStatus(Booking.StatusType.WAITING);

        if (!item.isAvailable()) {
            throw new ValidationException("Item not available");
        }
        booking.setItem(item);
        booking.setBooker(user);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateBookingRequest(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (userId != booking.getItem().getOwner().getId()) {
            throw new ValidationException("User does not own item");
        }
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (approved) {
            booking.setStatus(Booking.StatusType.APPROVED);
        } else {
            booking.setStatus(Booking.StatusType.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findByBookerIdAndId(userId, bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking not found for userId: " + userId + " and bookingId: " + bookingId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingByUser(long userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings = switch (state.toUpperCase()) {
            case "CURRENT" -> bookingRepository.findCurrentBookings(userId);
            case "PAST" -> bookingRepository.findPastBookings(userId);
            case "FUTURE" -> bookingRepository.findFutureBookings(userId);
            case "WAITING" -> bookingRepository.findBookingByBookerIdAndStatus(userId, "WAITING");
            case "REJECTED" -> bookingRepository.findBookingByBookerIdAndStatus(userId, "REJECTED");
            default -> bookingRepository.findByBookerId(userId);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }


    @Override
    public List<BookingDto> getBookingsByOwner(long userId, String state) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Sort sort = Sort.by(Sort.Direction.ASC, "start");

        List<Booking> bookings = switch (state.toUpperCase()) {
            case "CURRENT" -> bookingRepository.findCurrentBookingsByOwner(userId);
            case "PAST" -> bookingRepository.findPastBookingsByOwner(userId);
            case "FUTURE" -> bookingRepository.findFutureBookingsByOwner(userId);
            case "WAITING" -> bookingRepository.findBookingByBookerIdAndStatus(userId, "WAITING");
            case "REJECTED" -> bookingRepository.findBookingByBookerIdAndStatus(userId, "REJECTED");
            default -> bookingRepository.findByItemId(userId, sort);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
