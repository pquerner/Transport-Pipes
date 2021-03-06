package de.robotricker.transportpipes.duct.pipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import de.robotricker.transportpipes.TransportPipes;
import de.robotricker.transportpipes.duct.manager.GlobalDuctManager;
import de.robotricker.transportpipes.duct.pipe.filter.ItemDistributorService;
import de.robotricker.transportpipes.duct.pipe.items.PipeItem;
import de.robotricker.transportpipes.duct.types.DuctType;
import de.robotricker.transportpipes.duct.types.pipetype.ColoredPipeType;
import de.robotricker.transportpipes.inventory.DuctSettingsInventory;
import de.robotricker.transportpipes.location.BlockLocation;
import de.robotricker.transportpipes.location.TPDirection;

public class ColoredPipe extends Pipe {

    public ColoredPipe(DuctType ductType, BlockLocation blockLoc, World world, Chunk chunk, DuctSettingsInventory settingsInv, GlobalDuctManager globalDuctManager, ItemDistributorService itemDistributor) {
        super(ductType, blockLoc, world, chunk, settingsInv, globalDuctManager, itemDistributor);
    }

    @Override
    public Material getBreakParticleData() {
        return ((ColoredPipeType) getDuctType()).getColoringMaterial();
    }
    
    @Override
    protected Map<TPDirection, Integer> calculateItemDistribution(PipeItem pipeItem, TPDirection movingDir, List<TPDirection> dirs, TransportPipes transportPipes) {
    	BlockLocation location = getBlockLoc();
    	List<TPDirection> newDirs = dirs.stream().filter(dir -> pipeItem.hasMovedDirs(location) && !pipeItem.getMovedDirs(location).contains(dir)).collect(Collectors.toList());
    	if (newDirs.isEmpty()) {
    		newDirs = pipeItem.hasSourceDir(location) ? Arrays.asList(pipeItem.getSourceDir(location)) : dirs;
    	}
		Map<TPDirection, Integer> absWeights = new HashMap<>();
		newDirs.stream().forEach(dir -> absWeights.put(dir, 1));
		return itemDistributor.splitPipeItem(pipeItem.getItem(), absWeights, this);
	}

}
