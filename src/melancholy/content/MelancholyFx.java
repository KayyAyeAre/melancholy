package melancholy.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import melancholy.graphics.*;
import melancholy.math.*;
import melancholy.world.blocks.environment.*;
import melancholy.world.draw.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.production.GenericCrafter.*;
import mindustry.world.draw.*;

public class MelancholyFx {
    public static final Rand rand = new Rand();

    public static Effect
    craftHeat = new Effect(10f, e -> {
        e.lifetime = e.rotation;
        Draw.color(e.color, e.fout() * e.color.a);
        Draw.blend(Blending.additive);

        // very hacky but itll have to do since crafters dont send effect data
        Building build = Vars.world.buildWorld(e.x, e.y);

        if (build != null) Draw.rect(Core.atlas.find(build.block().name + "-heat"), e.x, e.y, build.rotdeg());

        Draw.blend();
    }),

    // the above but for crushers, of which there is only one...
    crusherCraftHeat = new Effect(10f, e -> {
        e.lifetime = e.rotation;
        Draw.color(e.color, e.fout() * e.color.a);
        Draw.z(Layer.block + 0.01f);
        Draw.blend(Blending.additive);

        Building build = Vars.world.buildWorld(e.x, e.y);

        // oh my fucking god i want to die
        if (build != null && build.block() instanceof GenericCrafter crafter && crafter.drawer instanceof DrawMulti multi) {
            DrawCrusher draw = (DrawCrusher) Structs.find(multi.drawers, d -> d instanceof DrawCrusher);
            if (draw != null) {
                float mod = (1f - draw.start) / (draw.chomps + (1f - draw.spikePoint));
                float offset = (mod * (1f - draw.spikePoint));
                float progresst = build.progress() < draw.start ? Interp.pow2.apply(build.progress() / draw.start) : Interp.pow2In.apply(Mathm.spike(Mathf.mod((build.progress() - draw.start - offset), mod) / mod, draw.spikePoint));
                for (int i = 0; i < 2; i++) {
                    boolean side = Mathf.booleans[i];
                    Draw.rect(draw.heatRegions[i], build.x + (progresst * draw.dst * Mathf.sign(side)), build.y);
                }
            }
        }

        // whoopsies!
        Draw.blend();
    }),

    floorGlow = new Effect(180f, e -> {
        if (!(e.data instanceof Tile tile) || !(tile.floor() instanceof GlowFloor floor)) return;
        int variant = Mathf.randomSeed(tile.pos(), 0, Math.max(0, floor.variants - 1));

        Draw.color(e.color, e.fslope() * e.fslope());
        Draw.rect(floor.glowRegions[variant], tile.worldx(), tile.worldy());

        Drawf.light(e.x, e.y, 12f, e.color, 0.7f * e.fslope() * e.fslope());
    }).layer(Layer.floor + 0.01f),

    oreCrusherSmoke = new Effect(30f, e -> {
        Draw.color(Pal.lightOrange, Color.gray, e.fin());
        Angles.randLenVectors(e.id, 12, e.finpow() * 8f, e.rotation + 180f, 10f, (x, y) -> {
            Fill.circle(e.x + x + Angles.trnsx(e.rotation + 180f, 3f), e.y + y + Angles.trnsy(e.rotation + 180f, 3f), e.fout() * 1.5f);
        });
    }),

    shardPressSparks = new Effect(40f, e -> {
        Draw.color(MelancholyPal.lightCyledge, MelancholyPal.darkCyledge, e.fin());

        rand.setSeed(e.id);
        for (int i = 0; i < 20; i++) {
            float dst = rand.range(8f);
            float offset = 5f * Mathf.sign(rand.nextBoolean());
            float angle = Angles.angle(offset, dst);
            float len = rand.random(9f);
            float rot = rand.random(360f) + (e.fin() * 60f);

            Drawf.tri(e.x + Angles.trnsx(angle, e.finpow() * len), e.y + Angles.trnsy(angle, e.finpow() * len) + dst, e.fout() * 1.5f, e.fout() * 1.5f, rot);
        }
    }),

    reductionFurnaceKaboom = new Effect(50f, e -> {
        Draw.color(MelancholyPal.lightSilica);
        Angles.randLenVectors(e.id, 15, e.finpow() * 8f, (x, y) -> {
            Fill.circle(e.x + x, e.y + y, e.fout() * 2.5f);
        });
    }),

    reductionFurnaceSmoke = new Effect(140f, e -> {
        Draw.color(MelancholyPal.lightSilica, MelancholyPal.darkSilica, e.fin());
        Draw.alpha(e.fslope() * 0.5f);

        for (int i = 0; i < 4; i++) {
            Angles.randLenVectors(e.id + i, 1, e.finpow() * 20f, i * 90f, 20f, (x, y) -> {
                Angles.randLenVectors(e.id, 3, 3f, (ix, iy) -> {
                    Fill.circle(e.x + x + ix, e.y + y + iy, e.fout() * 5f);
                });
            });
        }
    });
}
