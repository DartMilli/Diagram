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

    public Menu(final Diagram view, Dimension dmsn) {
        this.view = view;
        this.dmsn = dmsn;
        controlMinAndMax = new MinMaxControlPanel(this, dmsn.width - 2);
        add(controlMinAndMax);

        captionControl = new CaptionControlPanel(this, dmsn.width - 2);
        add(captionControl);

        graphcontrol = new GraphControlPanel(this, dmsn.width - 2);
        add(graphcontrol);

    }

    public Diagram getView() {
        return view;
    }
}

