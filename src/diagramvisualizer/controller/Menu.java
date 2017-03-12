package diagramvisualizer.controller;

import diagramvisualizer.view.Diagram;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Milán
 */
public class Menu extends JPanel {

    private final Diagram view;
    private Dimension dmsn;
    private MinMaxControlPanel controlMinAndMax;
    private CaptionControlPanel captionControl;
    private GraphControlPanel graphcontrol;
    private SpinPanel panels[] = new SpinPanel[3];

    public Menu(final Diagram view, Dimension dmsn) {
        this.view = view;
        this.dmsn = dmsn;

        controlMinAndMax = new MinMaxControlPanel(this, dmsn.width - 2);
        captionControl = new CaptionControlPanel(this, dmsn.width - 2);
        graphcontrol = new GraphControlPanel(this, dmsn.width - 2);

        panels[0] = new SpinPanel("Adjust Axis Values:", dmsn.width - 4);
        panels[0].add(controlMinAndMax);
        panels[0].close();
        panels[1] = new SpinPanel("Adjust Axes Caption:", dmsn.width - 4);
        panels[1].add(captionControl);
        panels[1].close();
        panels[2] = new SpinPanel("Adjust Graphs:", dmsn.width - 4);
        panels[2].add(graphcontrol);
        for (int i = 0; i < panels.length; i++) {
            add(panels[i]);
        }
    }

    public Diagram getView() {
        return view;
    }
}
