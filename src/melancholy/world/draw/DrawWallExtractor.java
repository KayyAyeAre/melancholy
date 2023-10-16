package melancholy.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawWallExtractor extends DrawBlock {
    public TextureRegion extractor1, extractor2, extractorHeat;
    public float offset = 2f;

    @Override
    public void draw(Building build) {
        float z = Draw.z();

        Draw.rect(
                build.rotation > 1 ? extractor2 : extractor1,
                build.x + Angles.trnsx(build.rotdeg(), offset * (1f - Interp.pow2Out.apply(build.progress()))),
                build.y + Angles.trnsy(build.rotdeg(), offset * (1f - Interp.pow2Out.apply(build.progress()))),
                build.rotdeg()
        );

        Draw.z(Layer.blockAdditive);
        Draw.color(Pal.redLight, 1f - Interp.pow2Out.apply(build.progress()));
        Draw.rect(
                extractorHeat,
                build.x + Angles.trnsx(build.rotdeg(), offset * (1f - Interp.pow2Out.apply(build.progress()))),
                build.y + Angles.trnsy(build.rotdeg(), offset * (1f - Interp.pow2Out.apply(build.progress()))),
                build.rotdeg()
        );
        Draw.z(z);
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{};
    }

    @Override
    public void load(Block block) {
        extractor1 = Core.atlas.find(block.name + "-extractor1");
        extractor2 = Core.atlas.find(block.name + "-extractor2");
        extractorHeat = Core.atlas.find(block.name + "-extractor-heat");
    }
}
