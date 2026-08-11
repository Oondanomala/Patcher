package club.sk1er.patcher.mixins.performance.leaks;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.client.GuiModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Mixin(GuiModList.class)
public class GuiModListMixin_CloseStream {
    //#if MC==1.8.9
    @Redirect(method = "updateCache", at = @At(value = "INVOKE", target = "Ljavax/imageio/ImageIO;read(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;"))
    private BufferedImage patcher$closeStream(InputStream inputStream) throws IOException {
        return TextureUtil.readBufferedImage(inputStream);
    }
    //#endif
}
