package com.example.loader;

import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.game.patch.GameTransformer;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.util.Arguments;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * An example implementation of {@link GameProvider}.
 * <p>
 * It is important that this class is listed as a service, see {@link ServiceLoader}
 */
public final class ExampleModProvider implements GameProvider {
    private final GameTransformer transformer = new GameTransformer();

    /**
     * The class path for this mod.
     */
    private List<Path> classPath;

    /**
     * Fabric uses a wrapped version of the program arguments so it can take CLI args and strip them before it reaches
     * the target.
     */
    private Arguments arguments;

    /**
     * The entry point of the target, in this case it's "com.example.base.Launcher".
     */
    private String entryClass;

    /**
     * The version of the target.
     */
    private String version;

    @Override
    public String getGameId() {
        return "example";
    }

    @Override
    public String getGameName() {
        return "Example ModProvider project";
    }

    @Override
    public String getRawGameVersion() {
        return version;
    }

    @Override
    public String getNormalizedGameVersion() {
        return version;
    }

    @Override
    public Collection<BuiltinMod> getBuiltinMods() {
        // Fabric Loader takes care of the Java built-in mod, we only have to worry about the target.
        return List.of(
            new BuiltinMod(classPath, new ExampleMetadata(getNormalizedGameVersion()))
        );
    }

    @Override
    public String getEntrypoint() {
        return entryClass;
    }

    @Override
    public Path getLaunchDirectory() {
        try {
            return Paths.get(".").toRealPath();
        } catch(IOException e) {
            throw new RuntimeException("Failed to resolve launch dir", e);
        }
    }

    @Override
    public boolean isObfuscated() {
        return false;
    }

    @Override
    public boolean requiresUrlClassLoader() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean locateGame(FabricLauncher fabricLauncher, String[] args) {
        this.arguments = new Arguments();
        arguments.parse(args);

        // This should probably be done programmatically, but for this example hard-coding it is okay.
        entryClass = "com.example.base.Launcher";
        // Same for the version.
        version = "1.0.0";

        // This is a little messy and depends on the layout of this project, for a real provider write this in a way
        // that it can survive existing in production.
        var codeSource = ExampleModProvider.class.getProtectionDomain().getCodeSource();
        Path codePath;
        try {
            codePath = Paths.get(codeSource.getLocation().toURI());
        } catch(URISyntaxException e) {
            throw new RuntimeException("Failed to find source of ExampleModProvider?", e);
        }

        Path basePath;
        try {
            basePath = getLaunchDirectory()
                .resolve(Path.of("..", "modules", "base", "build", "libs", "base.jar"))
                .toRealPath();
        } catch(IOException e) {
            throw new RuntimeException("Failed to find base", e);
        }

        classPath = List.of(codePath, basePath);

        return true;
    }

    @Override
    public void initialize(FabricLauncher launcher) {
        var parentClassPath = Stream.of(System.getProperty("java.class.path").split(File.pathSeparator))
            .map(Path::of)
            .map((path) -> {
                try {
                    return path.toRealPath();
                } catch(IOException e) {
                    throw new RuntimeException("Failed to get real path of " + path, e);
                }
            })
            .filter((path) -> !classPath.contains(path))
            .toList();

        launcher.setValidParentClassPath(parentClassPath);

        transformer.locateEntrypoints(launcher, classPath);
    }

    @Override
    public GameTransformer getEntrypointTransformer() {
        return transformer;
    }

    @Override
    public void unlockClassPath(FabricLauncher launcher) {
        classPath.forEach(launcher::addToClassPath);
    }

    @Override
    public void launch(ClassLoader loader) {
        // This is where you need to do a touch of reflection to load the target. You can't directly reference classes
        // otherwise they won't be able to be transformed and will be loaded under the wrong ClassLoader.
        var targetName = getEntrypoint();

        MethodHandle invoker;
        try {
            Class<?> target = loader.loadClass(targetName);
            invoker = MethodHandles.lookup().findStatic(target, "main", MethodType.methodType(void.class, String[].class));
        } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Failed to find entry point", e);
        }

        try {
            // Idea doesn't understand that this is a polymorphic method.
            //noinspection ConfusingArgumentToVarargsMethod
            invoker.invokeExact(arguments.toArray());
        } catch(Throwable e) {
            throw new RuntimeException("Failed to launch", e);
        }
    }

    @Override
    public Arguments getArguments() {
        return arguments;
    }

    @Override
    public String[] getLaunchArguments(boolean sanitize) {
        // If there are sensitive arguments (user tokens for example) you should strip them here when sanitize is true.
        return arguments.toArray();
    }
}
