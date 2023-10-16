package melancholy.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import melancholy.math.*;
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
    public float particleDst = 7f, minDstMul = 0.4f, particleWidth = 2f, minWidthMul = 0.2f, minParticleSize = 1.6f, maxParticleSize = 2.6f;
    public int particleCount = 60;

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
        float progressp = build.progress() < start + offset ? build.progress() / (start + offset) : Mathf.round((1f - build.progress()) / (1f - (start + offset)), 1f / chomps) + (build.progress() - start);
        float maxDst = Mathf.dst(particleWidth, particleDst);
        for (int i = 0; i < particleCount; i++) {
            float fin = Mathf.curve(progressp, rand.random(start + offset));
            float rot = rand.random(360f);
            float dst = rand.range(particleDst) * (minDstMul + (1f - minDstMul) * fin);
            float width = rand.range(particleWidth) * fin * (minWidthMul + (1f - minWidthMul) * (1f - Math.abs(dst) / particleDst));
            float frac = Mathf.dst(width, dst) / maxDst;
            float radius = Interp.pow2Out.apply(fin) * rand.random(minParticleSize, maxParticleSize) * (1f - frac);

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
