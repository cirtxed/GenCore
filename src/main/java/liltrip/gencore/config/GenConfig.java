package liltrip.gencore.config;

import liltrip.gencore.utils.ConfigUtils;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public class GenConfig {

    @Getter
    private final FileConfiguration generators = ConfigUtils.getConfigFile("generators.yml");

}
