package org.icaema;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.command.Command;
import org.rusherhack.core.command.annotations.CommandExecutor;

public class StopwatchCommand extends Command
{

    private final StopwatchTime stopwatchTime = StopwatchTime.getInstance();

    private final StopwatchModule stopwatchModule = (StopwatchModule) RusherHackAPI.getModuleManager().getFeature("Stopwatch").orElseThrow(); // should never throw since the module is in the same plugin
    private final StopwatchHudElement stopwatchHudElement = (StopwatchHudElement) RusherHackAPI.getHudManager().getFeature("Stopwatch").orElseThrow();


    public StopwatchCommand() {
        super("stopwatch", "Stopwatch control commands");
        this.addAliases("sw");
    }

    @CommandExecutor(subCommand = "start")
    private String swstart() {
        if (!stopwatchHudElement.isToggled()) return "Hud element is not active, can't start!";
        stopwatchTime.reset();
        return "Reset stopwatch!";
    }

    @CommandExecutor(subCommand = "stop")
    private String swstop() {
        stopwatchModule.setToggled(false);
        return "Stopped stopwatch!";
    }

    @CommandExecutor(subCommand = "lap")
    private String swlap() {
        stopwatchTime.lap();
        return "Lapped stopwatch!";
    }

    @CommandExecutor(subCommand = "reset")
    private String swreset() {
        if (!stopwatchHudElement.isToggled()) return "Hud element is not active, can't reset!";
        stopwatchTime.reset();
        return "Reset stopwatch!";
    }

}