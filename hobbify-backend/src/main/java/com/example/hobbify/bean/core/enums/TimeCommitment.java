package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: TimeCommitment
 */
public enum TimeCommitment implements BaseEnum {

    LIGHT("Light (a few minutes a day)"),
    MODERATE("Moderate (a few hours a week)"),
    INTENSIVE("Intensive (daily practice)");

    private final String displayText;

    TimeCommitment(String displayText) {
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
    public static TimeCommitment fromDisplayText(String text) {
        if (text == null) return null;
        for (TimeCommitment value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
