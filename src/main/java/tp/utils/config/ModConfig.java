package tp.utils.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.minecraft.util.math.Vec3d;

@Config(name = "tp_utils")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    private String tpMethod = "/tp";
    @ConfigEntry.Gui.Tooltip
    private int tpThroughRange = 256;
    @ConfigEntry.Gui.Tooltip
    private int tpOnTopRange = 256;
    @ConfigEntry.Gui.Tooltip
    private int tpForwardRange = 100;
    @ConfigEntry.Gui.Tooltip
    private boolean allowCrawling = false;
    @ConfigEntry.Gui.Excluded
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


    public Vec3d getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Vec3d coordinates) {
        previousLocation = coordinates;
    }
}
