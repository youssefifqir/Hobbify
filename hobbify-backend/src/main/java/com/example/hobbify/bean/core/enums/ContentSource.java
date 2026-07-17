package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: ContentSource
 */
public enum ContentSource implements BaseEnum {

    MANUAL("Manual"),
    AI_ASSISTED("AI Assisted"),
    MIXED("Mixed");

    private final String displayText;

    ContentSource(String displayText) {
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
    public static ContentSource fromDisplayText(String text) {
        if (text == null) return null;
        for (ContentSource value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
