package melancholy.world.blocks.production;

import arc.*;
import arc.audio.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;

// its just a beamdrill that has to be up-close, also burst
public class CrusherDrill extends Block {
    // i miss working with lua aajkhakldlgjkhjhhh
    public static boolean has, multiple;
    public static Item placeItem, invalidItem;
    public int count;

    public float drillTime = 60f;
    public int tier = 1;
    public int itemMultiplier = 2;

    public Effect mineEffect = Fx.none;
    public Sound mineSound = Sounds.drillImpact;

    public DrawBlock drawer = new DrawDefault();

    public CrusherDrill(String name) {
        super(name);

        hasItems = true;
        rotate = true;
        rotateDraw = false;
        update = true;
        solid = true;

        ambientSoundVolume = 0.2f;
        ambientSound = Sounds.drillCharge;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    @Override
    public void setStats() {
        super.setStats();

        stats.add(Stat.drillTier, StatValues.drillables(drillTime, 0f, size, null, b -> (b instanceof Floor f && f.wallOre && f.itemDrop != null && f.itemDrop.hardness <= tier) || (b instanceof StaticWall w && w.itemDrop != null && w.itemDrop.hardness <= tier)));
        stats.add(Stat.drillSpeed, 60f / drillTime * size, StatUnit.itemsSecond);
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("drillspeed", (CrusherDrillBuild e) -> new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(e.lastDrillSpeed * 60 / drillTime, 2)), () -> Pal.ammo, () -> e.warmup));
    }

    @Override
    public boolean rotatedOutput(int x, int y) {
        return false;
    }

    @Override
    public void load() {
        super.load();
        drawer.load(this);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        drawer.drawPlan(this, plan, list);
    }

    @Override
    public TextureRegion[] icons() {
        return drawer.finalIcons(this);
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        drawer.getRegionsToOutline(this, out);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        placeItem = null;
        invalidItem = null;
        multiple = false;
        count = 0;

        getFacing(x, y, rotation, t -> {
            if (placeItem != t.wallDrop() && placeItem != null) {
                multiple = true;
            }
            if (t.wallDrop().hardness <= tier) {
                count++;
            } else {
                invalidItem = t.wallDrop();
            }
            placeItem = t.wallDrop();
        });

        if (placeItem != null) {
            float width = drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / drillTime * count, 2), x, y, valid);
            if (!multiple) {
                float dx = x * Vars.tilesize + offset - width / 2f - 4f, dy = y * Vars.tilesize + offset + size * Vars.tilesize / 2f + 5, s = Vars.iconSmall / 4f;
                Draw.mixcol(Color.darkGray, 1f);
                Draw.rect(placeItem.fullIcon, dx, dy - 1, s, s);
                Draw.reset();
                Draw.rect(placeItem.fullIcon, dx, dy, s, s);
            }
        } else if (invalidItem != null) {
            drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, false);
        }
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        has = false;
        getFacing(tile.x, tile.y, rotation, t -> {
            if (t.wallDrop().hardness <= tier) {
                has = true;
            }
        });
        return has;
    }

    public void getFacing(int tx, int ty, int rotation, Cons<Tile> tileCons) {
        int cornerX = tx - (size - 1) / 2, cornerY = ty - (size - 1) / 2, s = size;

        for (int i = 0; i < size; i++) {
            int rx = 0, ry = 0;

            switch (rotation) {
                case 0 -> {
                    rx = cornerX + s;
                    ry = cornerY + i;
                }
                case 1 -> {
                    rx = cornerX + i;
                    ry = cornerY + s;
                }
                case 2 -> {
                    rx = cornerX - 1;
                    ry = cornerY + i;
                }
                case 3 -> {
                    rx = cornerX + i;
                    ry = cornerY - 1;
                }
            }

            Tile other = Vars.world.tile(rx, ry);
            if (other != null && other.solid()) {
                Item drop = other.wallDrop();
                if (drop != null) {
                    tileCons.get(other);
                }
            }
        }
    }

    public class CrusherDrillBuild extends Building {
        public Tile[] facing = new Tile[size];
        public int facingAmount;
        public float warmup, progress, lastDrillSpeed;

        @Override
        public void updateTile() {
            boolean cons = shouldConsume();

            warmup = Mathf.approachDelta(warmup, Mathf.num(efficiency > 0), 1f / 60f);
            progress += getProgressIncrease(drillTime);
            lastDrillSpeed = (facingAmount * timeScale);

            if (cons && progress >= 1f) {
                for (Tile tile : facing) {
                    Item drop = tile == null ? null : tile.wallDrop();
                    if (items.total() < itemCapacity && drop != null) {
                        items.add(drop, itemMultiplier);
                        produced(drop);
                    }
                }

                mineEffect.at(x, y, rotdeg());
                mineSound.at(x, y);
                progress %= 1f;
            }

            if (timer(timerDump, dumpTime)) {
                dump();
            }
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public float progress() {
            return progress;
        }

        @Override
        public void draw() {
            drawer.draw(this);
        }

        @Override
        public void drawLight() {
            super.drawLight();
            drawer.drawLight(this);
        }

        @Override
        public boolean shouldConsume() {
            return items.total() < itemCapacity;
        }

        @Override
        public void onProximityUpdate() {
            facingAmount = 0;
            getFacing(tileX(), tileY(), rotation, tile -> {
                if (tile.wallDrop().hardness <= tier) {
                    facing[facingAmount++] = tile;
                }
            });
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(progress);
            write.f(warmup);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            progress = read.f();
            warmup = read.f();
        }
    }
}
