package melancholy.content;

import arc.graphics.*;
import mindustry.type.*;

public class MelancholyLiquids {
    public static Liquid carbonyl;

    public static void load() {
        carbonyl = new Liquid("carbonyl", Color.valueOf("677c9c")) {{
            gas = true;
        }};
    }
}
