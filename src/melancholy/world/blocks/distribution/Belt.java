package melancholy.world.blocks.distribution;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.units.*;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.*;

// yep
public class Belt extends Duct {
    public static final boolean[][] blendTypes = {
            {true, false, true, false},
            {true, true, false, false},
            {true, false, true, true},
            {true, true, true, true},
            {true, true, false, true}
    };

    public TextureRegion[] cornerRegions, inRegions, outRegions, topRegions;

    public Belt(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        // kinda sucks but whatever
        inRegions = new TextureRegion[2];
        outRegions = new TextureRegion[2];
        topRegions = new TextureRegion[2];
        cornerRegions = new TextureRegion[4];

        for (int i = 0; i < 2; i++) {
            inRegions[i] = Core.atlas.find(name + "-in" + (i + 1), "melancholy-pulse-belt-in" + (i + 1));
            outRegions[i] = Core.atlas.find(name + "-out" + (i + 1), "melancholy-pulse-belt-out" + (i + 1));
            topRegions[i] = Core.atlas.find(name + "-top" + (i + 1), "melancholy-pulse-belt-top" + (i + 1));
        }

        for (int i = 0; i < 4; i++) {
            cornerRegions[i] = Core.atlas.find(name + "-corner" + i, "melancholy-pulse-belt-corner" + i);
        }
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, inRegions[0], outRegions[0]};
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        int[] bits = getTiling(plan, list);

        if(bits == null) return;

        // shrimple
        drawAt(plan.drawx(), plan.drawy(), bits[0], plan.rotation, bits[2] == -1, SliceMode.none);
    }

    protected void drawAt(float x, float y, int bits, int rotation, boolean flipped, SliceMode slice) {
        Draw.z(Layer.blockUnder);
        Draw.rect(region, x, y);

        for (int i = 1; i < 4; i++) {
            if (blendTypes[bits][(i % 2 == 0 || !flipped ? i : i + 2) % 4]) {
                Draw.z(Layer.blockUnder);
                if (slice != SliceMode.bottom) Draw.rect(inRegions[(i + rotation) % 4 == 0 || (i + rotation) % 4 == 3 ? 1 : 0], x, y, (i * 90f - 180f) + (rotation * 90f));
            } else {
                Draw.z(Layer.blockUnder + 0.2f);
                Draw.rect(topRegions[(i + rotation) % 4 > 1 ? 1 : 0], x, y, (i * 90f - 180f) + (rotation * 90f));
            }
        }

        Draw.z(Layer.blockUnder);
        if (slice != SliceMode.top) Draw.rect(outRegions[rotation == 0 || rotation == 3 ? 0 : 1], x, y, rotation * 90f);

        // yes im running the exact same loop twice, what are you gonna do about it
        for (int i = 0; i < 4; i++) {
            int r = Mathf.mod(i - rotation, 4);
            // corners
            if (blendTypes[bits][(r % 2 == 0 || !flipped ? r : r + 2) % 4] && blendTypes[bits][((r + 1) % 2 == 0 || !flipped ? (r + 1) : (r + 3)) % 4]) {
                Draw.rect(cornerRegions[i], x, y);
            }
        }
    }

    public class BeltBuild extends DuctBuild {
        @Override
        public void draw() {
            int r = this.rotation;

            for (int i = 0; i < 4; i++) {
                if ((blending & (1 << i)) != 0) {
                    int dir = r - i;
                    Belt.this.drawAt(x + Geometry.d4x(dir) * Vars.tilesize, y + Geometry.d4y(dir) * Vars.tilesize, 0, r, false, i != 0 ? SliceMode.bottom : SliceMode.top);
                }
            }

            //draw item
            if (current != null) {
                Draw.z(Layer.blockUnder + 0.1f);
                Tmp.v1.set(Geometry.d4x(recDir) * Vars.tilesize / 2f, Geometry.d4y(recDir) * Vars.tilesize / 2f)
                        .lerp(Geometry.d4x(r) * Vars.tilesize / 2f, Geometry.d4y(r) * Vars.tilesize / 2f,
                                Mathf.clamp((progress + 1f) / 2f));

                Draw.rect(current.fullIcon, x + Tmp.v1.x, y + Tmp.v1.y, Vars.itemSize, Vars.itemSize);
            }

            Belt.this.drawAt(x, y, blendbits, r, yscl == -1, SliceMode.none);
            Draw.reset();
        }
    }
}
