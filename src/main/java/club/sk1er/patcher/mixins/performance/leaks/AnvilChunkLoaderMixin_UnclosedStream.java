package club.sk1er.patcher.mixins.performance.leaks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.DataInputStream;
import java.io.IOException;

@Mixin(AnvilChunkLoader.class)
public class AnvilChunkLoaderMixin_UnclosedStream {
    @Inject(
        method = "loadChunk__Async",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompressedStreamTools;read(Ljava/io/DataInputStream;)Lnet/minecraft/nbt/NBTTagCompound;", shift = At.Shift.AFTER),
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void patcher$closeStream(World w, int x, int z, CallbackInfoReturnable<Object[]> cir, ChunkCoordIntPair c, NBTTagCompound n, DataInputStream stream) throws IOException {
        stream.close();
    }
}
