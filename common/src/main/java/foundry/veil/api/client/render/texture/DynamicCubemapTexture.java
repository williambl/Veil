package foundry.veil.api.client.render.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.mixin.accessor.NativeImageAccessor;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

import static org.lwjgl.opengl.GL11C.*;

public class DynamicCubemapTexture extends CubemapTexture {

    public void upload(Direction face, NativeImage image) {
        this.upload(getGlFace(face), image);
    }

    public void upload(int face, NativeImage image) {
        this.bind();

        int width = image.getWidth();
        int height = image.getHeight();
        RenderSystem.assertOnRenderThreadOrInit();
        NativeImageAccessor accessor = (NativeImageAccessor) (Object) image;
        accessor.invokeCheckAllocated();
        GlStateManager._pixelStore(GL_UNPACK_ROW_LENGTH, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_ROWS, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_PIXELS, 0);
        image.format().setUnpackPixelStoreState();
        GlStateManager._texSubImage2D(face, 0, 0, 0, width, height, image.format().glFormat(), GL_UNSIGNED_BYTE, accessor.getPixels());
    }

    @Override
    public void load(ResourceManager resourceManager) throws IOException {
    }
}
