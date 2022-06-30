package liltrip.gencore.config;

import liltrip.gencore.utils.ConfigUtils;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;

@UtilityClass
public class CoreConfig {

    @Getter
    private final FileConfiguration lang = ConfigUtils.getConfigFile("CoreConfig.yml");
}
