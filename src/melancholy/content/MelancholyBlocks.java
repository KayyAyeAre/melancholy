package melancholy.content;

import melancholy.graphics.*;
import melancholy.world.blocks.distribution.*;
import melancholy.world.blocks.environment.*;
import melancholy.world.blocks.production.*;
import melancholy.world.blocks.storage.*;
import melancholy.world.draw.*;
import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

public class MelancholyBlocks {
    public static Block
    // environment
    drySnow, capSnow, limestone, limestoneCrater, limestoneSheet, lushrock, lushmead, glowlush, bluerock, blueslate, chert, chertCrater,
    drySnowWall, limestoneWall, lushrockWall, bluerockWall, chertWall,
    drySnowPatch, bluerockSpikes, lushGrowth, lushBranches,
    drySnowBoulder, limestoneBoulder, limestoneStrata, lushrockBoulder, lushSprout, bluerockBoulder, chertBoulder,
    // environment - resources
    crystoneNode, vasteelNode, irresnantNode, silicaVein, bitumenVein, talcVein, iceDeposit,
    // crafting
    shardPress, reductionFurnace,
    // production
    kineticCrusher,
    // transport - items
    pulseBelt, walledPulseBelt, pulseRouter, pulseOverflowGate, pulseUnderflowGate, pulseBridge, freightStation, freightReceiver,
    // campaign - blueprint scanners
    crudeScanner,
    // storage
    industryHub, transferHub
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

        drySnowPatch = new BigOverlayFloor("dry-snow-patch");

        bluerockSpikes = new TallBlock("bluerock-spikes") {{
            variants = 2;
            clipSize = 120f;
        }};

        lushGrowth = new TreeBlock("lush-growth");

        lushBranches = new SeaBush("lush-branches") {{
            lobesMin = 6;
            lobesMax = 10;
            magMin = 3f;
            magMax = 6f;
            spread = 20f;
            // like a tree thing
            breakable = false;
        }};

        drySnowBoulder = new Prop("dry-snow-boulder") {{
            variants = 2;
        }};

        limestoneBoulder = new Prop("limestone-boulder") {{
            variants = 2;
        }};

        limestoneStrata = new WideTilingProp("limestone-strata") {{
            variants = 2;
        }};

        lushrockBoulder = new Prop("lushrock-boulder") {{
            variants = 2;
        }};

        lushSprout = new SeaBush("lush-sprout") {{
            lobesMin = 3;
            lobesMax = 5;
            spread = 20f;
        }};

        bluerockBoulder = new Prop("bluerock-boulder") {{
            variants = 2;
        }};

        chertBoulder = new Prop("chert-boulder") {{
            variants = 2;
        }};
        // endregion
        // region resources
        crystoneNode = new OreNode("crystone-node") {{
            itemDrop = MelancholyItems.crystone;
            variants = 2; // TODO more variants should but im really tired of spriting environment blocks
            customShadow = true;
            fillsTile = false;
        }};

        // just blank sprites because i REALLY dont wanna go through all of that again
        vasteelNode = new OreNode("vasteel-node") {{
            itemDrop = MelancholyItems.vasteel;
            variants = 1;
            customShadow = true;
            fillsTile = false;
        }};

        irresnantNode = new OreNode("irresnant-node") {{
            itemDrop = Items.thorium; // shhhhhhhh
            variants = 1;
            customShadow = true;
            fillsTile = false;
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

            outputItem = new ItemStack(MelancholyItems.cyledge, 10);
            craftTime = 300f;
            itemCapacity = 30;
            consumeItem(MelancholyItems.silica, 15);
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

            outputItem = new ItemStack(Items.silicon, 10);
            outputLiquid = new LiquidStack(MelancholyLiquids.carbonyl, 5f / 60f);
            ignoreLiquidFullness = true;
            craftTime = 240f;
            itemCapacity = 20;
            craftEffect = new MultiEffect(
                    MelancholyFx.craftHeat.wrap(Pal.redLight, 160f),
                    MelancholyFx.reductionFurnaceSmoke,
                    MelancholyFx.reductionFurnaceKaboom
            );
            consumeItems(ItemStack.with(MelancholyItems.silica, 8, MelancholyItems.bitumen, 4));
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
        // region item transport
        pulseBelt = new Belt("pulse-belt") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 3f;
        }};

        walledPulseBelt = new Belt("walled-pulse-belt") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 3f;
            armored = true;
        }};

        pulseRouter = new DuctRouter("pulse-router") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 3f;
            regionRotated1 = 1;
            solid = false;
            squareSprite = false;
        }};

        pulseOverflowGate = new OverflowDuct("pulse-overflow-gate") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 3f;
            solid = false;
            squareSprite = false;
        }};

        pulseUnderflowGate = new OverflowDuct("pulse-underflow-gate") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 3f;
            solid = false;
            squareSprite = false;
            invert = true;
        }};

        pulseBridge = new DuctBridge("pulse-bridge") {{
            requirements(Category.distribution, ItemStack.with());
            speed = 3f;
            range = 6;
            solid = false;
            squareSprite = false;

            // dunno if i have to put it inside here but i may as well
            ((Duct) pulseBelt).bridgeReplacement = ((Duct) walledPulseBelt).bridgeReplacement = this;
        }};
        // endregion
        // region storage
        industryHub = new MelancholyCoreBlock("industry-hub") {{
            requirements(Category.effect, BuildVisibility.editorOnly, ItemStack.with());
            unitType = MelancholyUnitTypes.aleph;
            squareSprite = false;
            size = 5;
            // do i even need to?
            health = 10000;
            itemCapacity = 10000;
            alwaysUnlocked = true;
            isFirstTier = true;
            unitCapModifier = 40;
        }};
        // endregion
    }
}
