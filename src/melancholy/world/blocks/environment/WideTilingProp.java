package melancholy.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

// yes, these are basically just fumaroles from dusted lands as props
public class WideTilingProp extends Prop {
    public TextureRegion[][] tileRegions;

    public WideTilingProp(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        tileRegions = new TextureRegion[variants][4];
        for (int i = 0; i < variants; i++) {
            for (int j = 0; j < 4; j++) {
                tileRegions[i][j] = Core.atlas.find(name + (i + 1) + "-" + j);
            }
        }
    }

    @Override
    public void loadIcon() {
        fullIcon = uiIcon = Core.atlas.find(name + "1-0");
    }

    @Override
    public void drawBase(Tile tile) {
        Draw.z(layer);
        Mathf.rand.setSeed(tile.pos());
        Draw.rect(tileRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))][index(tile)], tile.worldx(), tile.worldy());
    }

    public int index(Tile tile) {
        int output = 0;
        // small chance to split, TODO only happens between odd and even numbers which might look a bit icky?
        boolean split = Mathf.randomSeed(Point2.pack((tile.x / 2 * 2), tile.y)) < 0.3f;
        if ((!split || tile.x % 2 == 1) && tile.nearby(0).block() == this) output |= 1;
        if ((!split || tile.x % 2 == 0) && tile.nearby(2).block() == this) output |= 2;
        return output;
    }
}
