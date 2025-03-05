package ru.practicum.shareit.booking;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewBookingRequest {
    private long itemId;

    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    private LocalDateTime end;

    @AssertTrue
    public boolean isStartEndNotEqual() {
        if (end == null || start == null) {
            return false;
        }

        return !start.isEqual(end);
    }

    @AssertTrue
    public boolean isEndAfterStart() {
        if (end == null || start == null) {
            return false;
        }

        return end.isAfter(start);
    }
}
