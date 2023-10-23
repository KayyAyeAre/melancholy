package melancholy.type.weapons;

import arc.scene.ui.layout.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import mindustry.world.meta.*;

// just buildweapon with proper stats because it looks wonky in the database
// this could just be an anonymous class actually idk
public class MelancholyBuildWeapon extends BuildWeapon {
    public MelancholyBuildWeapon(String name) {
        super(name);
        display = true;
        showStatSprite = false;
    }

    @Override
    public void addStats(UnitType u, Table t) {
        t.row();
        t.add("[lightgray]" + Stat.buildSpeed.localized() + ": [white]" + (int)(u.buildSpeed * 100f) + StatUnit.percent.localized());
    }
}
