package com.example.movierate_backend.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.TriggeringPolicyBase;

public class LineCountTriggeringPolicy extends TriggeringPolicyBase<ILoggingEvent> {

    private int maxLines = 100;
    private int currentLines = 0;

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    public boolean isTriggeringEvent(java.io.File activeFile, ILoggingEvent event) {
        currentLines++;

        if (currentLines >= maxLines) {
            currentLines = 0;
            return true;
        }

        return false;
    }
}
