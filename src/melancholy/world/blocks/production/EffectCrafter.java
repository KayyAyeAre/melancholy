package melancholy.world.blocks.production;

import arc.audio.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.*;

import java.util.*;

// wacky wild wurjghkgknbnn
public class EffectCrafter extends GenericCrafter {
    public float[] effectPoints = {};
    public Effect intervalEffect = Fx.none;
    public Sound intervalSound = Sounds.none;
    public float intervalSoundVolume = 0.2f;

    public EffectCrafter(String name) {
        super(name);
    }

    public class EffectCrafterBuild extends GenericCrafterBuild {
        public boolean[] effectChecker = new boolean[effectPoints.length];

        public void updateIntervalEffect() {
            for (int i = 0; i < effectPoints.length; i++) {
                if (progress > effectPoints[i] && !effectChecker[i]) {
                    intervalEffect.at(x, y);
                    intervalSound.at(x, y, Mathf.random(0.9f, 1.1f), intervalSoundVolume);
                    effectChecker[i] = true;
                }
            }
        }

        @Override
        public void updateTile() {
            super.updateTile();

            updateIntervalEffect();
        }

        @Override
        public void craft() {
            // eh
            updateIntervalEffect();

            super.craft();

            Arrays.fill(effectChecker, false); // i dunno, intellij suggested me this
        }
    }
}
