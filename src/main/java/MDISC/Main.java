package MDISC;

import MDISC.UI.RailwayUI;

/**
 * Entry point for the application.
 * <p>
 * Responsibilities:
 * <ol>
 *   <li>Configures GraphStream to use Swing before any graphical display calls.</li>
 *   <li>Delegates the user interface control to {@link RailwayUI}.</li>
 * </ol>
 */
public class Main {

    /**
     * Main method invoked when the application starts.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Sets system properties to instruct GraphStream to use the Swing toolkit
     *       and the J2D renderer for graph visualisation.</li>
     *   <li>Delegates the remainder of the user interface flow to {@link RailwayUI#main(String[])}.</li>
     * </ol>
     *
     * @param args command‐line arguments (passed through to {@code RailwayUI.main}); not used directly here
     */
    public static void main(String[] args) {
        // 1) Configure GraphStream to use Swing (before any display() calls)
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty(
                "org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer"
        );

        // 2) Delegate all UI control to RailwayUI
        RailwayUI.main(args);
    }
}
