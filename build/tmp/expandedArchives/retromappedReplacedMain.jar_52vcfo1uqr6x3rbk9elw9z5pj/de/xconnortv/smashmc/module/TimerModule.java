package de.xconnortv.smashmc.module;

import de.xconnortv.smashmc.SmashMc;
import net.labymod.ingamegui.moduletypes.ResizeableModule;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class TimerModule extends ResizeableModule {
    private static final short size = 20;

    private int time;

    public static final ResourceLocation logo = new ResourceLocation("smashmc/textures/logo.png");
    public TimerModule() {
        super(size,size,size,size,size,size, true);
        time = 0;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(logo);
    }

    @Override
    public void drawModule(double x, double y, double rightX, double width, double height, double mouseX, double mouseY) {
        super.drawModule(x, y, rightX, width, height, mouseX, mouseY);

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        Minecraft.func_71410_x().func_110434_K().func_110577_a(logo);
        draw.drawTexture(x,y,256,256,size,size);
        draw.drawString("§7"+time, x + size - 6,y + size - 8);

    }

    @Override
    public void loadSettings() {

    }

    @Override
    public boolean isShown() {
        return SmashMc.isRound();
    }

    @Override
    public String getSettingName() {
        return "SmashMc";
    }

    @Override
    public String getDescription() {
        return "Timer Addon for SmashMc";
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @Override
    public String getControlName() {
        return "SmashMc";
    }
}
