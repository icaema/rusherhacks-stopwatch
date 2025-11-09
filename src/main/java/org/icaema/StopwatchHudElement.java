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
import java.util.List;

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

//        this.background.addSubSettings(backgroundColor);
        this.registerSettings(s_showAtZero);
    }

    @Override
    public void renderContent(RenderContext context, double mouseX, double mouseY) {
        if (this.stopwatchGraphic == null) return;
        double fontSize = getFontRenderer().getFontHeight();
        int iconSpace = (int) Math.ceil((fontSize*2)+2);

        if (!this.s_showAtZero.getValue() && stopwatchTime.getTicks() == 0) {
         if (mc.screen == RusherHackAPI.getThemeManager().getHudEditorScreen()) {
             this.getRenderer().drawGraphicRectangle(this.stopwatchGraphic, 0, 0, fontSize*2, fontSize*2);
             this.getFontRenderer().drawString(stopwatchTime.toTimeString(), iconSpace, 5, -1);
         }
         return;
        }

        this.getRenderer().drawGraphicRectangle(this.stopwatchGraphic, 0, 0, fontSize*2, fontSize*2);

        //RENDER ME TIME
        if (stopwatchTime.lapsLength() == 0){
            this.getFontRenderer().drawString(stopwatchTime.toTimeString(), iconSpace, 5, -1);
            return;
        }

        this.getFontRenderer().drawString(stopwatchTime.toTimeString(), iconSpace, 0, -1);

        List<String> lapStrings = stopwatchTime.getLapStrings();
        for (int i = 0; i < lapStrings.size(); i++) {
            this.getFontRenderer().drawString("[%2d]\t%s".formatted((lapStrings.size()-i), lapStrings.get(i)), iconSpace, (i+1)*fontSize, -1);
        }
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
        double fontSize = getFontRenderer().getFontHeight();
        int iconSpace = (int) Math.ceil((fontSize*2)+2);
        if (stopwatchTime.lapsLength() < 2) return (iconSpace*2.5) + getFontRenderer().getStringWidth(stopwatchTime.toTimeString());
        return (iconSpace * 2.5) + getFontRenderer().getStringWidth(stopwatchTime.getLapStrings().getFirst());
    }

    @Override
    public double getHeight() {
        double fontSize = getFontRenderer().getFontHeight();
        if (stopwatchTime.lapsLength() < 2) return fontSize * 2;
        return fontSize * (stopwatchTime.lapsLength() + 1);
    }

}
