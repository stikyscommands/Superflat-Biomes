package net.superflat.biomes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

@Mixin(VineBlock.class)
public abstract class VineRemoverMixin {

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void RemoveIfDisconnected(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info){
        if(!((VineBlock)(Object)this).canPlaceAt(state, world, pos)){
            world.removeBlock(pos, false);
        }
    }
}
