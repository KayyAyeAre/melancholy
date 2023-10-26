package melancholy.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import melancholy.graphics.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// its just an overgrown drawregion..
public class DrawVents extends DrawBlock {
    public TextureRegion ventRegion;
    public Color particleColor = MelancholyPal.cryoventGas;
    public float particleAlpha = 0.4f, particleLife = 280f, particleHeight = 0.03f, particleMinDst = 28f, particleMaxDst = 36f, particleRad = 2f, particleSpread = 20f;
    public int particleCount = 20;

    public static Tile intersected;

    @Override
    public void draw(Building build) {
        float z = Draw.z();
        Draw.z(Layer.block - 5f);
        Draw.rect(ventRegion, build.x, build.y);

        float base = (Time.time / particleLife);

        rand.setSeed(build.id);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < particleCount; j++) {
                float fin = (rand.random(1f) + base) % 1f;
                float fout = 1f - fin;
                float angle = rand.range(particleSpread) + (i * 90f);

                float dst = rand.random(particleMinDst, particleMaxDst) * Interp.pow2Out.apply(fin);

                float x = build.x + Angles.trnsx(angle, dst);
                float y = build.y + Angles.trnsy(angle, dst);

                intersected = null;
                World.raycastEachWorld(build.x, build.y, x, y, (rx, ry) -> {
                    Tile tile = Vars.world.tile(rx, ry);
                    if (tile.block() != Blocks.air && tile.build != build) {
                        intersected = tile;
                        return true;
                    }
                    return false;
                });

                // just straight up dont draw if the particle ends up hitting a block
                if (intersected == null) {
                    Draw.color(particleColor, particleAlpha * build.warmup() * fout);

                    // do i need to credit meep for this?
                    Fill.circle(
                            x + (x - Core.camera.position.x) * fin * particleHeight * Vars.renderer.getDisplayScale(),
                            y + (y - Core.camera.position.y) * fin * particleHeight * Vars.renderer.getDisplayScale(),
                            particleRad * fout * build.warmup()
                    );
                }
            }
        }

        Draw.z(z);
        Draw.color();
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        // draws over other plans and blocks, looks funky but i cant do anything about it
        Draw.rect(ventRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{};
    }

    @Override
    public void load(Block block) {
        ventRegion = Core.atlas.find(block.name + "-vents", "melancholy-vent-" + block.size);
        // big
        block.clipSize = Math.max(block.clipSize, ventRegion.width);
    }
}
