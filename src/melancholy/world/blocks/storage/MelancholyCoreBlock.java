package melancholy.world.blocks.storage;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.world.blocks.storage.*;

// literally just coreblock but with proper icons
public class MelancholyCoreBlock extends CoreBlock {
    public MelancholyCoreBlock(String name) {
        super(name);
    }

    @Override
    public void createIcons(MultiPacker packer) {
        super.createIcons(packer);

        PixmapRegion base = Core.atlas.getPixmap(region);
        Pixmap out = base.crop();

        out.draw(packer.get(name + "-team-sharded"), true);

        packer.add(PageType.main, "block-" + name + "-full", out);
    }

    @Override
    public boolean isHidden() {
        // blerp
        return false;
    }
}
