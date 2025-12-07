package model;

import java.time.LocalDateTime;

/**
 * Interface defining common behavior for all bank entities
 * Demonstrates Interface concept in OOP
 */
public interface BankEntity {
    String getId();
    LocalDateTime getCreatedAt();
    void validate() throws IllegalArgumentException;
    String getDisplayInfo();
}