package at.gotzi.orechain.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BreakBlockListener implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();

        if (player.isSneaking() && event.isDropItems() && isOre(event.getBlock().getType())) {
            List<Block> blockChain = findBlockChain(event.getBlock(), new ArrayList<>());
            if (!itemStack.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
                blockChain.forEach(block -> {
                    block.breakNaturally(itemStack);
                });
            } else {
                blockChain.forEach(block -> {
                    block.setType(Material.AIR);
                    block.getWorld().dropItem(block.getLocation(), event.getBlock().getState().getData().toItemStack(1));
                });
            }
            ExperienceOrb experienceOrb = player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
            experienceOrb.setExperience(blockChain.size() * event.getExpToDrop());
        }
    }

    private List<Block> findBlockChain(Block block, List<Block> blockChain) {
        findBlock(block, blockChain, 1, 0, 0);
        findBlock(block, blockChain, 0, 1, 0);
        findBlock(block, blockChain, 0, 0, 1);
        findBlock(block, blockChain, -1, 0, 0);
        findBlock(block, blockChain, 0, -1, 0);
        findBlock(block, blockChain, 0, 0, -1);

        return blockChain;
    }

    private void findBlock(Block origin, List<Block> blockChain, double x, double y, double z) {
        Block block = origin.getWorld().getBlockAt(origin.getLocation().add(x, y, z));
        if (block.getType() == origin.getType()) {
            if (!blockChain.contains(block)) {
                blockChain.add(block);
                findBlockChain(block, blockChain);
            }
        }
    }
    
    private boolean isOre(Material ore) {
        switch(ore) {
            case DEEPSLATE_COAL_ORE:
            case DEEPSLATE_GOLD_ORE:
            case DEEPSLATE_COPPER_ORE:
            case DEEPSLATE_EMERALD_ORE:
            case DEEPSLATE_IRON_ORE:
            case DEEPSLATE_DIAMOND_ORE:
            case DEEPSLATE_REDSTONE_ORE:
            case DEEPSLATE_LAPIS_ORE:
            case COAL_ORE:
            case GOLD_ORE:
            case COPPER_ORE:
            case EMERALD_ORE:
            case IRON_ORE:
            case DIAMOND_ORE:
            case REDSTONE_ORE:
            case LAPIS_ORE:
            case NETHER_QUARTZ_ORE:
            case NETHER_GOLD_ORE: return true;
            default: return false;
        }
    }
}
