package io.github.sjouwer.tputils.config;

import io.github.sjouwer.tputils.TpUtils;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import net.minecraft.util.math.Vec3d;

@SuppressWarnings("FieldMayBeFinal")
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

    public String getTpMethod() {
        if (tpMethod.startsWith("/")) {
            return tpMethod.substring(1);
        }
        return tpMethod;
    }

    public void setTpMethod(String command) {
        this.tpMethod = command;
        TpUtils.saveConfig();
    }

    public int getTpThroughRange() {
        return tpThroughRange;
    }

    public int getTpOnTopRange() {
        return tpOnTopRange;
    }

    public int getTpForwardRange() {
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
