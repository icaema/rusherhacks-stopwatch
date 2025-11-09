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

        return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, millis/10);
    }

    private String convertTicksToDiffString(int t) {
        long milliseconds = t * 50L;
        Duration duration = Duration.ofMillis(milliseconds);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        StringBuilder tempDiffStringBuilder = new StringBuilder();

        if (hours > 0) tempDiffStringBuilder.append(hours).append("h");
        if (minutes > 0) tempDiffStringBuilder.append(minutes).append("m");
        if (seconds > 0 || millis > 0 || tempDiffStringBuilder.isEmpty()) {
            if (millis > 0) {
                tempDiffStringBuilder.append(String.format("%d.%02ds", seconds, millis/10));
            } else {
                tempDiffStringBuilder.append(seconds).append("s");
            }
        }

        return tempDiffStringBuilder.toString();
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
        laps.addFirst(ticks);
    }

    public String toTimeString() {
        return convertTicksToTimeString(ticks);
    }

    public int lapsLength() {
        return laps.size();
    }

    public List<String> getLapStrings() {
        if (laps.isEmpty()) return new ArrayList<>();

        List<String> lapStrings = new ArrayList<>();

        for  (int i = 0; i < laps.size(); i++) {
            String tempLapString = convertTicksToTimeString(laps.get(i));
            if (i==laps.size()-1) {
                lapStrings.add(tempLapString);
                continue;
            }
            tempLapString += "\t+" + convertTicksToDiffString(laps.get(i)-laps.get(i+1));
            lapStrings.add(tempLapString);
        }
        return lapStrings;
    }
}
