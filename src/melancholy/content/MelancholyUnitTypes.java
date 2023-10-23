package melancholy.content;

import melancholy.entities.units.*;
import melancholy.type.weapons.*;
import mindustry.*;
import mindustry.ai.types.*;
import mindustry.gen.*;
import mindustry.type.*;

public class MelancholyUnitTypes {
    public static UnitType aleph;

    public static void load() {
        aleph = new MelancholyUnitType("aleph") {{
            constructor = PayloadUnit::create;
            coreUnitDock = true;
            isEnemy = false;
            controller = u -> new BuilderAI(true, 500f);
            flying = true;
            alwaysUnlocked = true;

            buildSpeed = 2f;
            drawBuildBeam = false;
            hitSize = 11f;
            itemCapacity = 300;
            engineSize = 0f;

            accel = 0.1f;
            drag = 0.05f;
            speed = 8f;
            rotateSpeed = 12f;

            mineWalls = true;
            mineSpeed = 10f;
            mineTier = 1;

            // should i even do this?
            payloadCapacity = 2f * 2f * Vars.tilePayload;

            targetable = false;
            hittable = false;

            setEnginesMirror(
                    new UnitEngine(37f / 4f, 10f / 4f, 3f, 315f),
                    new UnitEngine(39f / 4f, -10f / 4f, 3f, 315f),
                    new UnitEngine(14f / 4f, -37f / 4f, 3f, 315f)
            );

            weapons.add(new MelancholyBuildWeapon("melancholy-aleph-weapon") {{
                x = 15f / 4f;
                y = 33f / 4f;
                layerOffset = -0.001f;
                shootY = 10f / 4f;
            }});
        }};
    }
}
