package melancholy.content;

import arc.graphics.*;
import mindustry.type.*;

public class MelancholyItems {
    public static Item
    // 0 crafter items
    crystone, silica, bitumen, vasteel, irresnant, talc,
    // 1 crafter items
    cyledge, crethene, // +silicon
    // 2 crafter items
    condant, heavyPlate,
    // 3 crafter items
    centerate, tentum, perisle, recon, fluxShard
    ;

    public static void load() {
        crystone = new Item("crystone", Color.valueOf("769a9f")) {{
            cost = 0.5f;
            alwaysUnlocked = true;
        }};

        silica = new Item("silica", Color.valueOf("e9bfd3"));

        bitumen = new Item("bitumen", Color.valueOf("4e5046"));

        vasteel = new Item("vasteel", Color.valueOf("91c5d6"));

        cyledge = new Item("cyledge", Color.valueOf("c5b8e1"));

        crethene = new Item("crethene", Color.valueOf("b8e47f"));
    }
}