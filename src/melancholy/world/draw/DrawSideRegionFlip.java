package melancholy.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;

// its literally just drawsideregion but the up and down directions have flipped sprites
public class DrawSideRegionFlip extends DrawBlock {
    public TextureRegion top1, top2;

    @Override
    public void draw(Building build) {
        Draw.yscl = Mathf.sign(build.rotation % 2 == 0);
        Draw.rect(build.rotation > 1 ? top2 : top1, build.x, build.y, build.rotdeg());
        Draw.reset();
    }

    @Override
    public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.yscl = Mathf.sign(plan.rotation % 2 == 0);
        Draw.rect(plan.rotation > 1 ? top2 : top1, plan.drawx(), plan.drawy(), plan.rotation * 90);
        Draw.reset();
    }

    @Override
    public void load(Block block) {
        top1 = Core.atlas.find(block.name + "-top1");
        top2 = Core.atlas.find(block.name + "-top2");
    }

    @Override
    public TextureRegion[] icons(Block block) {
        return new TextureRegion[]{top1};
    }
}
