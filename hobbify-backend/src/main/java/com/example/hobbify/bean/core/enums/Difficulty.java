package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: Difficulty
 */
public enum Difficulty implements BaseEnum {

    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private final String displayText;

    Difficulty(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String getDisplayText() {
        return this.displayText;
    }

    /**
     * Find enum by display text (case-insensitive).
     *
     * @param text the display text to search for
     * @return the matching enum value, or null if not found
     */
    public static Difficulty fromDisplayText(String text) {
        if (text == null) return null;
        for (Difficulty value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
