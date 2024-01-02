package com.example.base;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;

/**
 * A super simple example program that creates a very simple GUI.
 */
public final class Base {
    /**
     * The primary entry point that the launcher calls.
     *
     * @param args The command line arguments
     */
    void start(String[] args) {
        try {
            // Initialize the Swing stuff on the AWT thread
            SwingUtilities.invokeAndWait(() -> initUi(args));
        } catch(InterruptedException | InvocationTargetException e) {
            throw new RuntimeException("Failed to wait for swing task", e);
        }
    }

    /**
     * The JFrame for the example program.
     */
    private JFrame frame;

    /**
     * Initializes the example program GUI.
     *
     * @param args The command line arguments
     */
    private void initUi(String[] args) {
        /*
        This creates a super simple GUI that will look something like this:
        +---------------------+
        |□ Example Base ^ V X |
        +---------------------+
        | Example program...  |
        | Command line args   |
        |      [ Close ]      |
        +---------------------+
         */

        var content = new JPanel(new BorderLayout());
        content.add(new JLabel("Example program for Fabric's GameProvider"), BorderLayout.NORTH);

        var argsArea = new JTextArea();
        argsArea.setEditable(false);
        argsArea.setText(String.join("\n", args));
        content.add(argsArea, BorderLayout.CENTER);

        var button = new JButton("Close");
        button.addActionListener((event) -> frame.dispose());
        content.add(button, BorderLayout.SOUTH);

        frame = new JFrame("Example Base");
        frame.setContentPane(content);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        center(frame);
        frame.setVisible(true);
    }

    /**
     * Centers a Winder on the monitor that the cursor is in (on supported platforms).
     *
     * @param window The window to center.
     */
    private void center(Window window) {
        var mouseInfo = MouseInfo.getPointerInfo();
        var mouseDevice = mouseInfo.getDevice();
        var screenBounds = mouseDevice.getDefaultConfiguration().getBounds();
        window.setLocation(
            ((screenBounds.width - window.getWidth()) >> 1) + screenBounds.x,
            ((screenBounds.height - window.getHeight()) >> 1) + screenBounds.y
        );
    }
}
