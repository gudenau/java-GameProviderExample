package com.example.loader;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.fabricmc.loader.impl.metadata.AbstractModMetadata.TYPE_BUILTIN;

/**
 * A basic metadata implementation for the target of a {@link net.fabricmc.loader.impl.game.GameProvider}.
 */
public final class ExampleMetadata implements ModMetadata {
    /**
     * The version of the target.
     */
    private final Version version;

    /**
     * Constructs a new {@link ExampleMetadata} instance with the provided version.
     *
     * @param version The version of the target
     *
     * @throws RuntimeException If the version string could not be parsed
     */
    ExampleMetadata(@NotNull String version) {
        try {
            this.version = Version.parse(version);
        } catch(VersionParsingException e) {
            throw new RuntimeException("Failed to parse version", e);
        }
    }

    @Override
    public String getType() {
        // This should be the type that is used for "built-in mods" like the target.
        return TYPE_BUILTIN;
    }

    @Override
    public String getId() {
        return "example";
    }

    @Override
    public Collection<String> getProvides() {
        return List.of();
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public ModEnvironment getEnvironment() {
        // If there is a dedicated server for your target the client should return CLIENT and the server should return
        // SERVER. Since this example doesn't have a split we can use UNIVERSAL.
        return ModEnvironment.UNIVERSAL;
    }

    @Override
    public Collection<ModDependency> getDependencies() {
        return List.of();
    }

    @Override
    public String getName() {
        return "Example GameProvider";
    }

    @Override
    public String getDescription() {
        return "A simple example GameProvider target";
    }

    @Override
    public Collection<Person> getAuthors() {
        return List.of();
    }

    @Override
    public Collection<Person> getContributors() {
        return List.of();
    }

    @Override
    public ContactInformation getContact() {
        return ContactInformation.EMPTY;
    }

    @Override
    public Collection<String> getLicense() {
        return List.of("CC0");
    }

    @Override
    public Optional<String> getIconPath(int size) {
        return Optional.empty();
    }

    @Override
    public boolean containsCustomValue(String s) {
        return false;
    }

    @Override
    public CustomValue getCustomValue(String s) {
        return null;
    }

    @Override
    public Map<String, CustomValue> getCustomValues() {
        return Map.of();
    }

    @Override
    public boolean containsCustomElement(String key) {
        return false;
    }
}
