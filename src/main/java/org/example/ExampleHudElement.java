package org.example;

import org.rusherhack.client.api.feature.hud.ResizeableHudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.render.graphic.TextureGraphic;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.setting.BooleanSetting;
import org.rusherhack.core.utils.ColorUtils;

import java.awt.*;

import static javax.swing.text.html.CSS.Attribute.BACKGROUND_COLOR;

/**
 * Example rusherhack hud element
 * <p>
 * There are other hud element types than ResizeableHudElement, look at the other classes in the package
 *
 * @author John200410
 */
public class ExampleHudElement extends ResizeableHudElement {

    public static final int BACKGROUND_COLOR = ColorUtils.transparency(Color.BLACK.getRGB(), 0.5f);
    private TextureGraphic graphic = null;

    private final BooleanSetting background = new BooleanSetting("Background", true);
    private final ColorSetting backgroundColor = new ColorSetting("Color", new Color(BACKGROUND_COLOR, true));
	
	public ExampleHudElement() {
		super("ExampleHudElement");
		
		//try loading graphic
		try {
			this.graphic = new TextureGraphic("exampleplugin/graphics/rh_head.png", 235, 234);
		} catch (Throwable t) {
			this.getLogger().error("Failed to load graphic", t);
		}

        this.background.addSubSettings(backgroundColor);
        this.registerSettings(background);
    }
	
	@Override
	public void renderContent(RenderContext context, double mouseX, double mouseY) {
		//positions are relative to the top left corner of the hud element, so start drawing stuff from 0,0
		
		if (this.graphic != null) {
			this.getRenderer().drawGraphicRectangle(this.graphic, 0, 0, this.getWidth(), this.getHeight());
		}
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
		return 200;
	}
}
