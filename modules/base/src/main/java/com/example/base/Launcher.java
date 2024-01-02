package com.example.base;

/**
 * A simple launcher class that launches the primary example program.
 * <p>
 * A mixin is used to hook into this for Fabric Loader to do some more work.
 */
public final class Launcher {
    /**
     * The entry point to the example program.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        var base = new Base();
        base.start(args);
    }
}
