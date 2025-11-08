package org.icaema;

import org.apache.commons.lang3.ObjectUtils;
import org.lwjgl.glfw.GLFW;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.bind.key.GLFWKey;
import org.rusherhack.client.api.events.client.input.EventKeyboard;
import org.rusherhack.client.api.events.client.input.EventMouse;
import org.rusherhack.client.api.feature.command.ModuleCommand;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.client.api.setting.BindSetting;
import org.rusherhack.client.api.utils.ChatUtils;
import org.rusherhack.core.bind.key.NullKey;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;

public class StopwatchModule extends ToggleableModule {

    private StopwatchTime stopwatchTime = StopwatchTime.getInstance();

    private final BooleanSetting s_reset = new BooleanSetting("Reset", "Reset the stopwatch", true);
    private final BindSetting s_resetBind = new BindSetting("Reset Bind", NullKey.INSTANCE );
    private final BindSetting s_lapBind = new BindSetting("Lap Bind", NullKey.INSTANCE );
    private final BooleanSetting s_startOnJoin = new BooleanSetting("StartOnJoin", "Reset and start the timer when connecting to a server", true);

    public StopwatchModule() {
        super("Stopwatch", ModuleCategory.RENDER);

        this.s_reset.onChange(x -> {
            stopwatchTime.reset();
            this.s_reset.setValue(true);
        });

        this.registerSettings(
                s_reset,
                s_resetBind,
                s_lapBind,
                s_startOnJoin
        );
    }

    @Override
    public void onEnable() {
        StopwatchHudElement hudElement = (StopwatchHudElement) RusherHackAPI.getHudManager().getFeature("Stopwatch").orElseThrow(); // should never throw since same plugin
        if (!hudElement.isToggled()) {
            ChatUtils.print("Enabling Stopwatch HUD Element.");
            hudElement.setToggled(true);
        }
    }

    @Override
    public boolean isListening() { return true; }

    @Override
    public ModuleCommand createCommand() { return null; }

    @Subscribe
    private void EventKeyboard(EventKeyboard event) {
        resetBind: {
            if (!(this.s_resetBind.getValue() instanceof GLFWKey)) break resetBind;
            if (event.getKey() == ((GLFWKey) this.s_resetBind.getValue()).getKeyCode() && event.getAction() == GLFW.GLFW_PRESS) {
                this.stopwatchTime.reset();
            }
        }

        lapBind: {
            if (!(this.s_lapBind.getValue() instanceof GLFWKey)) break lapBind;
            if (event.getKey() == ((GLFWKey) this.s_lapBind.getValue()).getKeyCode() && event.getAction() == GLFW.GLFW_PRESS) {
                this.stopwatchTime.lap();
            }
        }
    }

    @Subscribe
    private void EventMouse(EventMouse.Key event) {
        resetBind: {
            if (!(this.s_resetBind.getValue() instanceof GLFWKey)) break resetBind;
            if ((((EventMouse.Key) event).getButton() == ((GLFWKey) this.s_resetBind.getValue()).getKeyCode()) && ((EventMouse.Key) event).getAction() == GLFW.GLFW_PRESS) {
                this.stopwatchTime.reset();
            }
        }
        lapBind: {
            if (!(this.s_lapBind.getValue() instanceof GLFWKey)) break lapBind;
            if ((((EventMouse.Key) event).getButton() == ((GLFWKey) this.s_lapBind.getValue()).getKeyCode()) && ((EventMouse.Key) event).getAction() == GLFW.GLFW_PRESS) {
                this.stopwatchTime.lap();
            }
        }
    }

}
