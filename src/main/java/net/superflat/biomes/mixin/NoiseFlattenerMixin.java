package net.superflat.biomes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkGenerator.class)
public abstract class NoiseFlattenerMixin{
    private static BlockState[] blacklist = new BlockState[] { Blocks.SMOOTH_BASALT.getDefaultState(), Blocks.AMETHYST_BLOCK.getDefaultState(),
        Blocks.AMETHYST_CLUSTER.getDefaultState(), Blocks.SMALL_AMETHYST_BUD.getDefaultState(), Blocks.MEDIUM_AMETHYST_BUD.getDefaultState(), 
        Blocks.LARGE_AMETHYST_BUD.getDefaultState(), Blocks.BUDDING_AMETHYST.getDefaultState() };

    private static BlockState[] first16 = new BlockState[] { Blocks.BEDROCK.getDefaultState(), Blocks.DIRT.getDefaultState(),
        Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState() };

    @Inject(method = "generateFeatures", at = @At("HEAD"))
    private void clear(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor, CallbackInfo info) {
        if (world.getDimension().bedWorks() == true){
            for (ChunkSection chunkSection : chunk.getSectionArray()) {
                BlockPos.iterate(0, 0, 0, 15, 15, 15).forEach(pos -> {
                    for (BlockState state : blacklist){
                        if(chunkSection.getBlockState(((BlockPos)pos).getX(), ((BlockPos)pos).getY(), ((BlockPos)pos).getZ()) == state){
                            return;
                        }
                    }
                    if(chunkSection.getYOffset() == 0 && pos.getY() < first16.length){
                        chunkSection.setBlockState(((BlockPos)pos).getX(), ((BlockPos)pos).getY(), ((BlockPos)pos).getZ(), first16[pos.getY()]);
                    }
                    else{
                        chunkSection.setBlockState(((BlockPos)pos).getX(), ((BlockPos)pos).getY(), ((BlockPos)pos).getZ(), Blocks.AIR.getDefaultState());
                    }
                });
            }
        }
        return;
    }
}
