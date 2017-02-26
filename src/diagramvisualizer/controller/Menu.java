package diagramvisualizer.controller;

import diagramvisualizer.view.Diagram;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Milán
 */
public class Menu extends JPanel {

    private final Diagram view;
    private Dimension dmsn;
    private JSplitPane split1;
    private JSplitPane split2;
    private JSplitPane split3;
    private MinMaxControlPanel controlMinAndMax;
    private CaptionControlPanel captionControl;
    private GraphControlPanel graphcontrol;
    private JPanel emptyPanel;

    public Menu(final Diagram view, Dimension dmsn){
        this.view = view;
        this.dmsn = dmsn;
        
        controlMinAndMax = new MinMaxControlPanel(this, dmsn.width - 2);
        captionControl = new CaptionControlPanel(this, dmsn.width - 2);
        graphcontrol = new GraphControlPanel(this, dmsn.width - 2);

        split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controlMinAndMax, captionControl);
        split1.setOneTouchExpandable(true);
        emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(
                dmsn.width - 2,
                dmsn.height
                - controlMinAndMax.getPreferredSize().height
                - captionControl.getPreferredSize().height
                - graphcontrol.getPreferredSize().height-40));
        split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphcontrol, emptyPanel);
        split2.setOneTouchExpandable(true);
        split3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split1, split2);
        split3.setOneTouchExpandable(true);
        add(split3);
    }

    public Diagram getView() {
        return view;
    }
}
