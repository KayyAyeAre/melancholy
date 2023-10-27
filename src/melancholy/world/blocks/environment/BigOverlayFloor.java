package melancholy.world.blocks.environment;

import arc.graphics.g2d.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

// this is so stupid
public class BigOverlayFloor extends OverlayFloor {
    public BigOverlayFloor(String name) {
        super(name);
    }

    @Override
    public void drawBase(Tile tile) {
        float z = Draw.z();
        Draw.z(z + 0.1f);
        super.drawBase(tile);
        Draw.z(z);
    }
}
