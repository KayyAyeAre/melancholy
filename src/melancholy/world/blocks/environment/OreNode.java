package melancholy.world.blocks.environment;

import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.*;
import mindustry.graphics.*;
import mindustry.world.*;

// big
// also incredibly painful to sprite
public class OreNode extends Block {
    public int nodeSize = 4;

    public OreNode(String name) {
        super(name);
        solid = true;
        clipSize = 90f;
    }

    @Override
    public void drawBase(Tile tile) {
        if (checkAdjacent(tile)) {
            // would draw over crushers which looks icky and i dont like
            Draw.z(Layer.block - 0.1f);
            Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], tile.worldx() + (nodeSize - 1f) * (Vars.tilesize / 2f), tile.worldy() + (nodeSize - 1f) * (Vars.tilesize / 2f));
            Draw.z(Layer.block);
        }
    }

    @Override
    public void drawShadow(Tile tile) {
        if (checkAdjacent(tile)) {
            Draw.color(0f, 0f, 0f, BlockRenderer.shadowColor.a);
            Draw.rect(variantShadowRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantShadowRegions.length - 1))], tile.worldx() + (nodeSize - 1f) * (Vars.tilesize / 2f), tile.worldy() + (nodeSize - 1f) * (Vars.tilesize / 2f));
            Draw.color();
        }
    }

    public boolean checkAdjacent(Tile tile) {
        for (int x = 0; x < nodeSize; x++) {
            for (int y = 0; y < nodeSize; y++) {
                Tile other = Vars.world.tile(tile.x + x, tile.y + y);
                if (other == null || other.block() != this) {
                    return false;
                }
            }
        }

        return true;
    }
}
