package io.github.sjouwer.tputils.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import net.minecraft.util.math.Vec3d;

@Config(name = "tp_utils")
public class ModConfig implements ConfigData {
    @Tooltip
    private String tpMethod = "/tp";
    @Tooltip
    private int tpThroughRange = 256;
    @Tooltip
    private int tpOnTopRange = 256;
    @Tooltip
    private int tpForwardRange = 100;
    @Tooltip
    private boolean allowCrawling = false;
    @Tooltip
    private boolean allowLava = false;
    @Tooltip
    private boolean setBedrockLimit = true;
    @Excluded
    private Vec3d previousLocation;

    public String tpMethod() {
        return tpMethod;
    }

    public int tpThroughRange() {
        return tpThroughRange;
    }

    public int tpOnTopRange() {
        return tpOnTopRange;
    }

    public int tpForwardRange() {
        return tpForwardRange;
    }

    public boolean isCrawlingAllowed() {
        return allowCrawling;
    }

    public boolean isLavaAllowed() {
        return allowLava;
    }

    public boolean isBedrockLimitSet() {
        return setBedrockLimit;
    }

    public Vec3d getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Vec3d coordinates) {
        previousLocation = coordinates;
    }
}
