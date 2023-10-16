package melancholy.world.blocks.environment;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.util.noise.*;
import melancholy.content.*;
import melancholy.graphics.*;
import mindustry.content.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

// straight copied from my old mod's rift floors, but those were copied from sh1p's frostscape
// so uhh, credit to sh1p?
public class GlowFloor extends Floor {
    public TextureRegion[] glowRegions;
    public Color glowColor = MelancholyPal.lushGlow;

    public GlowFloor(String name, int variants) {
        super(name, variants);
    }

    @Override
    public void load() {
        super.load();

        glowRegions = new TextureRegion[variants];

        for (int i = 0; i < variants; i++) {
            glowRegions[i] = Core.atlas.find(name + "-glow" + (i + 1));
        }
    }

    @Override
    public boolean updateRender(Tile tile) {
        return true;
    }

    @Override
    public void renderUpdate(UpdateRenderState tile) {
        float otime = Time.time / 100f;
        float offset = Simplex.noise2d(0, 9, 0.2f, 0.6f, tile.tile.x + otime * 0.6f, tile.tile.y + otime * -0.4f) +
                Simplex.noise2d(0, 9, 0.2f, 0.6f, tile.tile.x + (otime * 1.1f) * 0.3f, tile.tile.y + (otime * 1.1f) * 0.1f) * 180f;

        if ((tile.data += Time.delta) >= 180f + offset) {
            if (tile.tile.block() == Blocks.air) MelancholyFx.floorGlow.at(tile.tile.worldx(), tile.tile.worldy(), 0f, glowColor, tile.tile);
            tile.data = offset;
        }
    }
}
