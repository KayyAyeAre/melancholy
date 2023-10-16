package melancholy.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawGlowAlternating extends DrawBlock {
    public Color color;
    public TextureRegion glow1, glow2;
    // fraction of build progress
    public float altLength;
    public float alpha1 = 0.6f, alpha2 = 1f;
    public Interp interp = Interp.pow2Out;
    public float layer;

    public DrawGlowAlternating(Color color, float altLength, float layer) {
        this.color = color;
        this.altLength = altLength;
        this.layer = layer;
    }

    @Override
    public void draw(Building build) {
        float prog = interp.apply(Mathf.slope(Mathf.curve(build.progress(), 1f - altLength)));
        float progUnder = 1f - prog;
        float z = Draw.z();

        Draw.blend(Blending.additive);
        Draw.z(layer);

        Draw.color(color, progUnder * build.warmup() * alpha1);
        Draw.rect(glow1, build.x, build.y);
        Draw.color(color, prog * build.warmup() * alpha2);
        Draw.rect(glow2, build.x, build.y);

        Draw.blend();
        Draw.z(z);
        Draw.color();
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{};
    }

    @Override
    public void load(Block block) {
        glow1 = Core.atlas.find(block.name + "-glow1");
        glow2 = Core.atlas.find(block.name + "-glow2");
    }
}
