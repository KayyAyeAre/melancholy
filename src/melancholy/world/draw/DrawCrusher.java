package melancholy.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import melancholy.math.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// kinda weird to have a single drawer class for each of my crafters but whatever
// i have also just realized that this could be confused for the ore crusher, which it isnt
public class DrawCrusher extends DrawBlock {
    public TextureRegion[] regions, heatRegions;
    public float dst = 2.75f;
    public float start = 0.8f;
    public float spikePoint = 0.7f;
    public int chomps = 5;

    public Color colorFrom, colorTo;
    public float crystalDst = 7f, minDstMul = 0.4f, crystalWidth = 2f, minWidthMul = 0.2f, minCrystalSize = 1.6f, maxCrystalSize = 2.6f;
    public int crystalCount = 60;

    // funky
    public float particleDst = 8f, minParticleRadius = 0.4f, maxParticleRadius = 0.9f, particleSpread = 20f;
    public int particleCount = 40;

    public DrawCrusher(Color colorFrom, Color colorTo) {
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

    @Override
    public void draw(Building build) {
        float mod = (1f - start) / (chomps + (1f - spikePoint));
        float offset = (mod * (1f - spikePoint));
        float progresst = build.progress() < start ? Interp.pow2.apply(build.progress() / start) : Interp.pow2In.apply(Mathm.spike(Mathf.mod((build.progress() - start - offset), mod) / mod, spikePoint));

        rand.setSeed(build.id);

        // particles
        for (int i = 0; i < particleCount; i++) {
            float fin = Mathf.curve(build.progress(), rand.random(start + offset), start + offset);
            float angle = rand.range(particleSpread) + (rand.nextBoolean() ? 0f : 180f) + 90f;
            float dst = rand.random(particleDst) * (1f - fin);
            float radius = Interp.pow2Out.apply(Mathf.slope(fin)) * rand.random(minParticleRadius, maxParticleRadius);

            // weh
            Draw.color(colorFrom, colorTo, 1f - fin);
            Fill.circle(build.x + Angles.trnsx(angle, dst), build.y + Angles.trnsy(angle, dst), radius);
        }

        // crystals
        float progressp = build.progress() < start + offset ? build.progress() / (start + offset) : Mathf.round((1f - build.progress()) / (1f - (start + offset)), 1f / chomps) + (build.progress() - start);
        float maxDst = Mathf.dst(crystalWidth, crystalDst);
        for (int i = 0; i < crystalCount; i++) {
            float fin = Mathf.curve(progressp, rand.random(start + offset));
            float rot = rand.random(360f);
            float dst = rand.range(crystalDst) * (minDstMul + (1f - minDstMul) * fin);
            float width = rand.range(crystalWidth) * fin * (minWidthMul + (1f - minWidthMul) * (1f - Math.abs(dst) / crystalDst));
            float frac = Mathf.dst(width, dst) / maxDst;
            float radius = Interp.pow2Out.apply(fin) * rand.random(minCrystalSize, maxCrystalSize) * (1f - frac);

            Draw.color(colorFrom, colorTo, frac);
            Drawf.tri(build.x + width, build.y + dst, radius, radius, rot);
        }

        Draw.blend();
        Draw.color();

        // teeth
        for (int i = 0; i < 2; i++) {
            Draw.rect(regions[i], build.x + (progresst * dst * Mathf.sign(Mathf.booleans[i])), build.y);
        }
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        for (int i = 0; i < 2; i++) {
            Draw.rect(regions[i], plan.drawx(), plan.drawy());
        }
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return regions;
    }

    @Override
    public void load(Block block) {
        regions = new TextureRegion[]{
                Core.atlas.find(block.name + "-teeth-r"),
                Core.atlas.find(block.name + "-teeth-l")
        };

        // suffering and suffering and suffering and suffering and
        heatRegions = new TextureRegion[]{
                Core.atlas.find(block.name + "-heat-r"),
                Core.atlas.find(block.name + "-heat-l")
        };
    }
}
