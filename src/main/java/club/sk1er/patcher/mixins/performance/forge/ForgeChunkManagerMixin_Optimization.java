package club.sk1er.patcher.mixins.performance.forge;

import com.google.common.collect.ImmutableSetMultimap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@Mixin(ForgeChunkManager.class)
public class ForgeChunkManagerMixin_Optimization {
    //#if MC==1.8.9
    @Shadow
    private static Map<World, ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket>> forcedChunks = Collections.synchronizedMap(new WeakHashMap<>());

    @Inject(method = "unloadWorld", at = @At("HEAD"))
    private static void patcher$alwaysClearForcedChunks(World world, CallbackInfo ci) {
        forcedChunks.remove(world);
    }

    /**
     * @author asbyth, Oondanomala
     * @reason Optimize retrieval
     */
    @Overwrite
    public static ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> getPersistentChunksFor(World world) {
        if (world.isRemote) {
            return ImmutableSetMultimap.of();
        }
        ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> persistentChunks = forcedChunks.get(world);
        return persistentChunks != null ? persistentChunks : ImmutableSetMultimap.of();
    }
    //#endif
}
