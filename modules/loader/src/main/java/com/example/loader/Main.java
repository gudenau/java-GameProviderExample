package com.example.loader;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.knot.Knot;

import static net.fabricmc.loader.impl.util.SystemProperties.SKIP_MC_PROVIDER;

/**
 * The entrypoint that sets up Fabric Loader.
 */
public final class Main {
    public static void main(String[] args) {
        // This is required because we are not using Minecraft and that provider will always be used if enabled.
        System.setProperty(SKIP_MC_PROVIDER, "true");

        // Start up Fabric Loader
        Knot.launch(args, EnvType.CLIENT);
    }
}
