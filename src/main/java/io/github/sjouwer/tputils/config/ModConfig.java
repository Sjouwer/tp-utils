package io.github.sjouwer.tputils.config;

import io.github.sjouwer.tputils.TpUtils;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("FieldMayBeFinal")
@Config(name = TpUtils.NAMESPACE)
public class ModConfig implements ConfigData {
    @Tooltip
    private String singleplayerTpMethod = "/tp";
    @Tooltip
    private String serverTpMethod = "/tppos";
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
    private Vec3 previousLocation;

    public String getTpMethod(boolean isInSingleplayer) {
        String tpMethod = isInSingleplayer ? singleplayerTpMethod : serverTpMethod;

        if (tpMethod.startsWith("/")) {
            return tpMethod.substring(1);
        }
        return tpMethod;
    }

    public void setSingleplayerTpMethod(String command) {
        this.singleplayerTpMethod = command;
        TpUtils.saveConfig();
    }

    public void setServerTpMethod(String command) {
        this.serverTpMethod = command;
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

    public Vec3 getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Vec3 coordinates) {
        previousLocation = coordinates;
    }
}
