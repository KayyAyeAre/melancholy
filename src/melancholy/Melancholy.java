package melancholy;

import arc.*;
import melancholy.content.*;
import mindustry.game.*;
import mindustry.mod.*;

public class Melancholy extends Mod {
    public Melancholy() {
        Events.on(EventType.FileTreeInitEvent.class, e -> {
            MelancholySounds.load();
        });
    }

    @Override
    public void loadContent() {
        MelancholyLiquids.load();
        MelancholyItems.load();
        MelancholyUnitTypes.load();
        MelancholyBlocks.load();
    }
}
