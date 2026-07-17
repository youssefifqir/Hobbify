package com.example.hobbify.bean.core.enums;

import com.example.hobbify.common.bean.BaseEnum;

/**
 * Enum: AchievementType
 */
public enum AchievementType implements BaseEnum {

    ONBOARDING("Onboarding"),
    MILESTONE("Milestone"),
    STREAK("Streak"),
    EXPLORER("Explorer"),
    MASTERY("Mastery");

    private final String displayText;

    AchievementType(String displayText) {
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
    public static AchievementType fromDisplayText(String text) {
        if (text == null) return null;
        for (AchievementType value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
