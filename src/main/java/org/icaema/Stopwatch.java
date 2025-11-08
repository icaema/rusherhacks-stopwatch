package org.icaema;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class Stopwatch extends Plugin {

    @Override
    public void onLoad() {

        this.getLogger().info("Icaema's Stopwatch Loaded!");

        final StopwatchModule stopwatchModule = new StopwatchModule();
        RusherHackAPI.getModuleManager().registerFeature(stopwatchModule);

        final StopwatchHudElement stopwatchHudElement = new StopwatchHudElement();
        RusherHackAPI.getHudManager().registerFeature(stopwatchHudElement);

        final StopwatchCommand stopwatchCommand = new StopwatchCommand();
        RusherHackAPI.getCommandManager().registerFeature(stopwatchCommand);
    }

    @Override
    public void onUnload() {
        this.getLogger().info("Icaema's Stopwatch unloaded!");
    }
}
