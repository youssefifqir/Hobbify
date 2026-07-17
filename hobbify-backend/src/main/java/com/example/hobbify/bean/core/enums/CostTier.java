package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: CostTier
 */
public enum CostTier implements BaseEnum {

    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayText;

    CostTier(String displayText) {
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
    public static CostTier fromDisplayText(String text) {
        if (text == null) return null;
        for (CostTier value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
