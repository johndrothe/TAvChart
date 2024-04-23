package org.rothe.john.working_hours.model;

import java.time.LocalTime;

public record Period(LocalTime start, LocalTime end) {
}
