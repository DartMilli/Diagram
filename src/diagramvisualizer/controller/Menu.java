package diagramvisualizer.controller;

import diagramvisualizer.view.Diagram;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author Milán
 */
public class Menu extends JPanel {

    private final Diagram view;
    private Dimension dmsn;
    private MinMaxControlPanel controlMinAndMax;
    private JCheckBox chkRaster;
    private CaptionControlPanel captionControl;
    private GraphControlPanel graphcontrol;

    public Menu(final Diagram view, Dimension dmsn) {
        this.view = view;
        this.dmsn = dmsn;
        controlMinAndMax = new MinMaxControlPanel(this, dmsn.width - 2);
        add(controlMinAndMax);

        captionControl = new CaptionControlPanel(this, dmsn.width - 2);
        add(captionControl);

        chkRaster = new JCheckBox("Draw raster", true);
        chkRaster.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                view.setDrawRaster(ie.getStateChange() == ItemEvent.SELECTED);
            }
        });
        add(chkRaster);

        graphcontrol = new GraphControlPanel(this, dmsn.width - 2);
        add(graphcontrol);

    }

    public Diagram getView() {
        return view;
    }
}

