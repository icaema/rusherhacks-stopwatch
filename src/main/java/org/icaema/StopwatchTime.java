package org.icaema;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StopwatchTime {
    // Object for keeping track of the time for the stopwatch
    // oh fun, it's a singleton too for good measure
    private static StopwatchTime instance;

    private int ticks = 0;
    private ArrayList<Integer> laps = new ArrayList<>();

    private StopwatchTime() {
    }

    private String convertTicksToTimeString(int t) {
        long milliseconds = t * 50L;
        Duration duration = Duration.ofMillis(milliseconds);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

    static {
        instance = new StopwatchTime();
    }

    public static StopwatchTime getInstance() {
        return instance;
    }

    public void tick(){
        ticks++;
    }

    public void reset() {
        ticks = 0;
        laps.clear();
    }

    public int getTicks() {
        return ticks;
    }

    public void lap() {
        laps.add(ticks);
    }

    public String toTimeString() {
        return convertTicksToTimeString(ticks);
    }

    public List<String> getLapStrings() {
        return laps.stream().map(this::convertTicksToTimeString).toList();
    }
}
