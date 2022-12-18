package io.github.adamson;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.List;

public class BedrockBreakEventListener implements Listener {

    private static final int bedrockHeight = -60;

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.BEDROCK)
            return;

        if (event.getBlock().getLocation().getBlockY() <= bedrockHeight)
            return;

        destroyBedrock(new HashSet<>(List.of(event.getBlock())), event.getPlayer(), 0);
    }

    record Position(int x, int y, int z) {
    }

    private static final Position[] neighbors = new Position[]{
            new Position(1, 1, 1),
            new Position(-1, 1, 1),
            new Position(0, 1, 1),
            new Position(1, 1, -1),
            new Position(-1, 1, -1),
            new Position(0, 1, -1),
            new Position(1, 1, 0),
            new Position(-1, 1, 0),
            new Position(0, 1, 0),

            new Position(1, -1, 1),
            new Position(-1, -1, 1),
            new Position(0, -1, 1),
            new Position(1, -1, -1),
            new Position(-1, -1, -1),
            new Position(0, -1, -1),
            new Position(1, -1, 0),
            new Position(-1, -1, 0),
            new Position(0, -1, 0),

            new Position(1, 0, 1),
            new Position(-1, 0, 1),
            new Position(0, 0, 1),
            new Position(1, 0, -1),
            new Position(-1, 0, -1),
            new Position(0, 0, -1),
            new Position(1, 0, 0),
            new Position(-1, 0, 0),
    };

    private void destroyBedrock(HashSet<Block> openList, Player player, long replacedBlocks) {
        int limit = 10000;
        while (!openList.isEmpty() && limit > 0) {
            Block block = openList.iterator().next();
            openList.remove(block);
            block.setType(Material.STONE);
            Block neighbor = null;
            for (Position n : neighbors) {
                neighbor = block.getRelative(n.x, n.y, n.z);
                if (neighbor.getType() == Material.BEDROCK && neighbor.getLocation().getBlockY() > bedrockHeight) {
                    openList.add(neighbor);
                }
            }

            limit--;
            replacedBlocks++;
        }

        if (!openList.isEmpty()) {
            long finalReplacedBlocks = replacedBlocks;
            Bukkit.getScheduler().runTaskLater(BedrockReplacer.getPlugin(BedrockReplacer.class), () -> {
                destroyBedrock(openList, player, finalReplacedBlocks);
            }, 1);
        } else {
            player.sendMessage(replacedBlocks + " bedrock blocks replaced!");
        }
    }
}
