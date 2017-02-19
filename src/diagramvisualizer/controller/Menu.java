package diagramvisualizer.controller;

import diagramvisualizer.view.Diagram;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Milán
 */
public class Menu extends JPanel {

    private final Diagram view;
    private Dimension dmsn;
    private MinMaxControlItem[] controlMinAndMax = new MinMaxControlItem[6];
    private JCheckBox chkRaster;
    private JRadioButton[] rdobCaption = new JRadioButton[4];

    public Menu(final Diagram view, Dimension dmsn) {
        this.view = view;
        this.dmsn = dmsn;
        for (int i = 0; i < 6; i++) {
            controlMinAndMax[i] = new MinMaxControlItem(this, i + 1, dmsn.width - 2);
            add(controlMinAndMax[i]);
        }
        chkRaster = new JCheckBox("Draw raster", true);
        chkRaster.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                view.setDrawRaster(ie.getStateChange() == ItemEvent.SELECTED);
            }
        });
        add(chkRaster);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < rdobCaption.length; i++) {
            rdobCaption[i] = new JRadioButton("Axis caption type: " + i);
            rdobCaption[i].setActionCommand(i + "");
            rdobCaption[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    view.setCaptionType(Integer.parseInt(ae.getActionCommand()));
                }
            });
            group.add(rdobCaption[i]);
            add(rdobCaption[i]);
        }
    }

    public Diagram getView() {
        return view;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(
                this.getBounds().x,
                this.getBounds().y,
                dmsn.width,
                dmsn.height
        );
    }

}

class MinMaxControlItem extends JPanel implements ChangeListener {

    private Menu menu;
    private JLabel label;
    private JSpinner spinner;
    private int id;

    public MinMaxControlItem(Menu menu, int id, int width) {
        this.menu = menu;
        this.id = id;
        this.label = new JLabel(getActualName(id), JLabel.CENTER);
        this.spinner = new JSpinner(new SpinnerNumberModel(getActualValue(id), -10E10, 10E10, 10E-3));
        this.label.setPreferredSize(new Dimension(width / 2, 20));
        this.spinner.setPreferredSize(new Dimension(width / 2, 20));

        spinner.addChangeListener(this);
        add(label);
        add(spinner);
    }

    private double getActualValue(int id) {
        double value = 0.0;
        switch (id) {
            case 1:
                value = menu.getView().getMinX();
                break;
            case 2:
                value = menu.getView().getMaxX();
                break;
            case 3:
                value = menu.getView().getRasterX();
                break;
            case 4:
                value = menu.getView().getMinY();
                break;
            case 5:
                value = menu.getView().getMaxY();
                break;
            case 6:
                value = menu.getView().getRasterY();
                break;

        }
        return value;
    }

    private String getActualName(int id) {
        String value = "N/A";
        switch (id) {
            case 1:
                value = "X min.:";
                break;
            case 2:
                value = "X max.:";
                break;
            case 3:
                value = "X raster:";
                break;
            case 4:
                value = "Y min.:";
                break;
            case 5:
                value = "Y max.:";
                break;
            case 6:
                value = "Y raster:";
                break;

        }
        return value;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        switch (id) {
            case 1:
                menu.getView().setMinX((double) spinner.getValue());
                break;
            case 2:
                menu.getView().setMaxX((double) spinner.getValue());
                break;
            case 3:
                menu.getView().setRasterX((double) spinner.getValue());
                break;
            case 4:
                menu.getView().setMinY((double) spinner.getValue());
                break;
            case 5:
                menu.getView().setMaxY((double) spinner.getValue());
                break;
            case 6:
                menu.getView().setRasterY((double) spinner.getValue());
                break;
        }

    }

}
