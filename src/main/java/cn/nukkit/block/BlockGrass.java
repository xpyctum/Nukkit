package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.object.ObjectTallGrass;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

import java.util.Random;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockGrass extends BlockDirt {

    public BlockGrass() {
        this(0);
    }

    public BlockGrass(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return GRASS;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public String getName() {
        return "Grass";
    }

    @Override
    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == Item.DYE && item.getDamage() == 0x0F) {
            item.count--;
            ObjectTallGrass.growGrass(this.getLevel(), this, new NukkitRandom(), 15, 10);
            return true;
        } else if (item.isHoe()) {
            item.useOn(this);
            this.getLevel().setBlock(this, new BlockFarmland());
            return true;
        } else if (item.isShovel()) {
            item.useOn(this);
            this.getLevel().setBlock(this, new BlockGrassPath());
            return true;
        }

        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            //todo: light levels
            Random random = new Random();
            int x = ((int) this.x) - 1 + random.nextInt() % 3;
            int y = ((int) this.y) - 2 + random.nextInt() % 5;
            int z = ((int) this.z) - 1 + random.nextInt() % 3;
            Block block = this.getLevel().getBlock(new Vector3(x, y, z));
            if (block.getId() == Block.DIRT) {
                if ((block.getSide(Vector3.SIDE_UP) instanceof BlockTransparent) && !(block.getSide(Vector3.SIDE_UP) instanceof BlockLiquid)) {
                    BlockSpreadEvent ev = new BlockSpreadEvent(block, this, new BlockGrass());
                    getLevel().getServer().getPluginManager().callEvent(ev);
                    if (!ev.isCancelled()) {
                        this.getLevel().setBlock(block, ev.getNewState(), true, true);
                    }
                }
            }

            if (!(this.getSide(Vector3.SIDE_UP) instanceof BlockTransparent) || (this.getSide(Vector3.SIDE_UP) instanceof BlockLiquid)) {
                BlockUpdateEvent ev = new BlockUpdateEvent(new BlockDirt());
                getLevel().getServer().getPluginManager().callEvent(ev);
                if (!ev.isCancelled()) {
                    this.getLevel().setBlock(this, ev.getBlock(), true, true);
                }
            }
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRASS_BLOCK_COLOR;
    }
}
