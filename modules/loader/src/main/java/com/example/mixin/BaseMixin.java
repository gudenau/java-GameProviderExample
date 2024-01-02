package com.example.mixin;

import com.example.base.Base;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * A super simple example mixin to make a visible change in the example program.
 */
@Mixin(Base.class)
public abstract class BaseMixin {
    // Replaces "Example program for Fabric's GameProvider" with "Replaced with an example mixin!"
    @ModifyConstant(
        method = "initUi",
        constant = @Constant(
            stringValue = "Example program for Fabric's GameProvider"
        )
    )
    private String modifyText(String original) {
        return "Replaced with an example mixin!";
    }
}
