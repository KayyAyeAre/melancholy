package melancholy.content;

import melancholy.graphics.*;
import melancholy.world.blocks.environment.*;
import melancholy.world.blocks.production.*;
import melancholy.world.draw.*;
import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;

public class MelancholyBlocks {
    public static Block
    // environment
    drySnow, capSnow, limestone, limestoneCrater, limestoneSheet, lushrock, lushmead, glowlush, bluerock, blueslate, chert, chertCrater,
    drySnowWall, limestoneWall, lushrockWall, bluerockWall, chertWall,
    drySnowPatch, bluerockSpikes,
    // crafting
    shardPress, reductionFurnace,
    // production
    kineticCrusher
    ;

    public static void load() {
        // region environment
        drySnow = new Floor("dry-snow", 5);

        capSnow = new Floor("cap-snow", 5) {{
            blendGroup = drySnow;
        }};

        limestone = new Floor("limestone", 5);

        limestoneCrater = new Floor("limestone-crater", 5) {{
            blendGroup = limestone;
        }};

        limestoneSheet = new Floor("limestone-sheet", 5);

        lushrock = new Floor("lushrock", 5);

        lushmead = new Floor("lushmead", 5) {{
            blendGroup = lushrock;
        }};

        glowlush = new GlowFloor("glowlush", 5) {{
            blendGroup = lushrock;
        }};

        bluerock = new Floor("bluerock", 5);

        blueslate = new Floor("blueslate", 5);

        chert = new Floor("chert", 5);

        chertCrater = new Floor("chert-crater", 5) {{
            blendGroup = chert;
        }};

        drySnowWall = new StaticWall("dry-snow-wall") {{
            variants = 3;
        }};

        limestoneWall = new StaticWall("limestone-wall") {{
            variants = 3;
        }};

        lushrockWall = new StaticWall("lushrock-wall") {{
            variants = 3;
        }};

        bluerockWall = new StaticWall("bluerock-wall") {{
            variants = 3;
        }};

        chertWall = new StaticWall("chert-wall") {{
            variants = 3;
        }};

        drySnowPatch = new OverlayFloor("dry-snow-patch");

        bluerockSpikes = new TallBlock("bluerock-spikes") {{
            variants = 3;
            clipSize = 120f;
        }};

        // endregion
        // region crafting
        shardPress = new EffectCrafter("shard-press") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;
            squareSprite = false;

            drawer = new DrawMulti(
                    new DrawVents(),
                    new DrawRegion("-bottom"),
                    new DrawCrusher(MelancholyPal.lightCyledge, MelancholyPal.darkCyledge),
                    new DrawRegion() {{
                        // aaaAAAAhghh
                        layer = Layer.block + 0.02f;
                    }}
            );

            // i could probably use some wacky equation for this but whatever
            effectPoints = new float[]{0.8113f, 0.8491f, 0.8868f, 0.9245f, 0.9623f, 1f};
            intervalSound = MelancholySounds.metalSlam;
            intervalEffect = new MultiEffect(
                    MelancholyFx.crusherCraftHeat.wrap(Pal.redLight.cpy().a(0.2f), 70f),
                    MelancholyFx.shardPressSparks
            );

            outputItem = new ItemStack(MelancholyItems.cyledge, 5);
            craftTime = 300f;
            itemCapacity = 30;
            consumeItem(MelancholyItems.silica, 8);
        }};

        reductionFurnace = new GenericCrafter("reduction-furnace") {{
            requirements(Category.crafting, ItemStack.with());
            size = 3;
            squareSprite = false;

            drawer = new DrawMulti(
                    new DrawVents(),
                    new DrawRegion("-bottom"),
                    new DrawAccumulation(MelancholyPal.darkSilica, MelancholyPal.lightSilica),
                    new DrawRegion() {{
                        layer = Layer.block + 0.02f;
                    }},
                    new DrawGlowAlternating(Pal.redLight, 0.3f, Layer.block + 0.02f)
            );

            outputItem = new ItemStack(Items.silicon, 6);
            outputLiquid = new LiquidStack(MelancholyLiquids.carbonyl, 5f / 60f);
            ignoreLiquidFullness = true;
            craftTime = 240f;
            itemCapacity = 20;
            craftEffect = new MultiEffect(
                    MelancholyFx.craftHeat.wrap(Pal.redLight, 160f),
                    MelancholyFx.reductionFurnaceSmoke,
                    MelancholyFx.reductionFurnaceKaboom
            );
            consumeItems(ItemStack.with(MelancholyItems.silica, 4, MelancholyItems.bitumen, 3));
        }};
        // endregion
        // region production
        kineticCrusher = new CrusherDrill("kinetic-crusher") {{
            requirements(Category.production, ItemStack.with());
            size = 2;
            squareSprite = false;
            mineEffect = new MultiEffect(
                    MelancholyFx.craftHeat.wrap(Pal.redLight, 120f),
                    MelancholyFx.oreCrusherSmoke
            );

            drawer = new DrawMulti(
                    new DrawVents(),
                    // because apparently default drawing draws the icon for the plan
                    new DrawRegion(),
                    new DrawSideRegionFlip(),
                    new DrawGlowProgress(Pal.redLight, "-glow-top"),
                    new DrawWallExtractor()
            );
        }};
        // endregion
    }
}
