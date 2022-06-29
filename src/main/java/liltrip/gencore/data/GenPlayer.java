package liltrip.gencore.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class GenPlayer {

    private UUID uuid;
    private long genSlots;
    private List<Location> generators;

    public GenPlayer(UUID uuid) {
        this.uuid = uuid;
        this.genSlots = genSlots;
        this.generators = generators;
    }

    public UUID getUser() { return this.uuid; }

    public void addGenSlots(long amount) { this.genSlots += amount; }
    public void removeGenSlots(long amount) { this.genSlots -= amount; }

    public void addGenerator(Location location) { this.generators.add(location); }
    public void removeGenerator(Location location) { this.generators.remove(location); }
}
