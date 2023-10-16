package melancholy.content;

import arc.audio.*;
import mindustry.*;

public class MelancholySounds {
    public static Sound
    metalSlam = new Sound()
    ;

    public static void load() {
        if (Vars.headless) return;

        metalSlam = Vars.tree.loadSound("metalSlam");
    }
}
