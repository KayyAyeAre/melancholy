package melancholy.world.draw;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// modified version of the absorb thingy from dusted lands
public class DrawAccumulation extends DrawBlock {
    public Color colorFrom, colorTo;

    public float circleAlpha = 0.9f;
    public float circleRadius = 2f;
    public float circleScl = 12f, circleMag = 2f, circleRotationSpeed = 0.2f;
    public int circleCount = 3;

    public float particleAlpha = 0.7f;
    public float particleDst = 6f, minParticleRadius = 0.5f, maxParticleRadius = 1.5f;
    public int particleCount = 40;

    public DrawAccumulation(Color colorFrom, Color colorTo) {
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
    }

    @Override
    public void draw(Building build) {
        rand.setSeed(build.id);
        Draw.blend(Blending.additive);
        Draw.color(colorFrom, colorTo, build.progress());
        Draw.alpha(particleAlpha);

        // particles
        for (int i = 0; i < particleCount; i++) {
            float fin = Mathf.curve(build.progress(), rand.nextFloat());
            float angle = rand.random(360f);
            float dst = rand.random(particleDst) * (1f - fin);
            float radius = Interp.pow2Out.apply(fin) * rand.random(minParticleRadius, maxParticleRadius);

            Fill.circle(build.x + Angles.trnsx(angle, dst), build.y + Angles.trnsy(angle, dst), radius);
        }

        // center thingy
        for (int i = 0; i < circleCount; i++) {
            float rotation = Time.time * circleRotationSpeed;
            float length = Mathf.absin(circleScl + rand.random(0.4f, 1.2f), circleMag);

            Draw.alpha(circleAlpha * build.warmup());
            Fill.circle(build.x + Angles.trnsx(rotation, length), build.y + Angles.trnsy(rotation, length), build.progress() * circleRadius);
        }

        Draw.color();
        Draw.blend();
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{};
    }
}
