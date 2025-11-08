package org.icaema;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.feature.hud.HudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.graphic.TextureGraphic;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.event.stage.Stage;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.utils.ColorUtils;

import java.awt.*;

public class StopwatchHudElement extends HudElement {

    private static final int BACKGROUND_COLOR = ColorUtils.transparency(Color.BLACK.getRGB(), 0.5f);
    private final StopwatchTime stopwatchTime = StopwatchTime.getInstance();
    private TextureGraphic stopwatchGraphic = null;


    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final ColorSetting backgroundColor = new ColorSetting("Color", new Color(BACKGROUND_COLOR, true));
    private final BooleanSetting s_showAtZero = new BooleanSetting("ShowAtZero", "Show the HUD element when stopwatch is at zero.", false);

    private final StopwatchModule stopwatchModule = (StopwatchModule) RusherHackAPI.getModuleManager().getFeature("Stopwatch").orElseThrow(); // should never throw since the module is in the same plugin


    public StopwatchHudElement()
    {
        super("Stopwatch");

        try {
            this.stopwatchGraphic = new TextureGraphic("stopwatch/graphics/stopwatch.png", 64, 64);
        } catch (Throwable t) {
            this.getLogger().error("Failed to load stopwatch graphic", t);
        }

        this.background.addSubSettings(backgroundColor);
        this.registerSettings(background, s_showAtZero);
    }

    @Override
    public void renderContent(RenderContext context, double mouseX, double mouseY) {
        if (!this.s_showAtZero.getValue() && stopwatchTime.getTicks() == 0) return;

        double fontSize = getFontRenderer().getFontHeight();

        if (this.stopwatchGraphic != null) {
            this.getRenderer().drawGraphicRectangle(this.stopwatchGraphic, 0, 0, fontSize*2, fontSize*2);
        }

        int iconSpace = (int) Math.ceil((fontSize*2)+2);
        this.getFontRenderer().drawString(stopwatchTime.toTimeString(), iconSpace, 0, -1);
        this.getFontRenderer().drawString("1: --:--:--.--", iconSpace, fontSize, -1);
        this.getFontRenderer().drawString("2: --:--:--.--", iconSpace, fontSize*2, -1);
        this.getFontRenderer().drawString("3: --:--:--.--", iconSpace, fontSize*3, -1);
    }


    private boolean levelWasNullLastTick = true;

    @Subscribe(stage = Stage.PRE)
    private void onTick(EventUpdate eventUpdate) {
        if (mc.player == null || mc.level == null) {
            this.levelWasNullLastTick = true;
            return;
        }
        if (this.levelWasNullLastTick) { // was null, now its not
            this.levelWasNullLastTick = false;
            //this is basically on server change or world load, but not dim change
            if (((BooleanSetting)this.stopwatchModule.getSetting("StartOnJoin")).getValue()) {
                this.stopwatchModule.setToggled(true);
                stopwatchTime.reset();
            }
        }

        if (!this.stopwatchModule.isToggled()) return;
        stopwatchTime.tick();
    }


    private int getFillColor() {
        return this.backgroundColor.getValueRGB();
    }

    @Override
    public double getWidth() {
        return 200;
    }

    @Override
    public double getHeight() {
        return (getFontRenderer().getFontHeight() + 1) * 2;
    }

}
