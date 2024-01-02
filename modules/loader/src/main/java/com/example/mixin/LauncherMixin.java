package com.example.mixin;

import com.example.base.Base;
import com.example.base.Launcher;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * An example mixin that calls into Fabric Loader so it can finish initializing itself.
 */
@Mixin(Launcher.class)
public abstract class LauncherMixin {
    // This allows Fabric loader to do the rest of the initialization work that it needs to do.
    @Inject(
        method = "main",
        at = @At(
            value = "INVOKE",
            target = "Lcom/example/base/Base;start([Ljava/lang/String;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void main(String[] args, CallbackInfo ci, Base base) {
        FabricLoaderImpl.INSTANCE.prepareModInit(FabricLoader.getInstance().getGameDir(), base);
    }
}
