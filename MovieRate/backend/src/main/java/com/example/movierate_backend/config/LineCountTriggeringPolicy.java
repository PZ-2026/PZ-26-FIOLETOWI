package com.example.movierate_backend.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.TriggeringPolicyBase;

/**
 * Polityka logowania odpowiedzialna za aktywację rotacji plików logów na podstawie zdefiniowanej liczby wprowadzonych wierszy w pliku.
 */
public class LineCountTriggeringPolicy extends TriggeringPolicyBase<ILoggingEvent> {

    private int maxLines = 100;
    private int currentLines = 0;
    
    /**
     * Domyślny bezparametrowy konstruktor aktywujący zachowanie reguły Logback'a.
     */
    public LineCountTriggeringPolicy() {
        super();
    }

    /**
     * Ustawia górny dopuszczalny limit linijek w pojedynczym pliku.
     * @param maxLines pułap, po którym następuje trigger rotacji pliku tekstowego
     */
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    /**
     * Podstawowa metoda bazująca z biblioteki ch.qos.logback, sprawdzająca na bieżąco, czy na rzucony nowy event zachodzi przelanie dopuszczalnego rozmiaru.
     * @param activeFile wskaźnik pod którym zachodzi logowanie systemu aktualnie podtrzymywanego w użyciu.
     * @param event rzucone i zmapowane zdarzenie wejścia do dodania na końcu pliku.
     * @return boolean oznaczający przekroczenie (true), by wymusić przejście w inny sektor (podział .log).
     */
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
