package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: SpaceNeeded
 */
public enum SpaceNeeded implements BaseEnum {

    MINIMAL("Minimal"),
    MODERATE("Moderate"),
    DEDICATED("Dedicated Space");

    private final String displayText;

    SpaceNeeded(String displayText) {
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
    public static SpaceNeeded fromDisplayText(String text) {
        if (text == null) return null;
        for (SpaceNeeded value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
