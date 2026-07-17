package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: ContentStatus
 */
public enum ContentStatus implements BaseEnum {

    DRAFT("Draft"),
    IN_REVIEW("In Review"),
    PUBLISHED("Published");

    private final String displayText;

    ContentStatus(String displayText) {
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
    public static ContentStatus fromDisplayText(String text) {
        if (text == null) return null;
        for (ContentStatus value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
