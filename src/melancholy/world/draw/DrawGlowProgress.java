package melancholy.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import melancholy.math.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawGlowProgress extends DrawBlock {
    public Color color;
    public TextureRegion glow;
    public String suffix;
    public float spikePoint = 0.9f;

    public DrawGlowProgress(Color color, String suffix) {
        this.color = color;
        this.suffix = suffix;
    }

    @Override
    public void draw(Building build) {
        Draw.blend(Blending.additive);
        Draw.color(color, Mathm.spike(build.progress(), spikePoint));
        Draw.rect(glow, build.x, build.y, build.rotdeg());
        Draw.blend();
        Draw.color();
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{};
    }

    @Override
    public void load(Block block) {
        glow = Core.atlas.find(block.name + suffix);
    }
}
