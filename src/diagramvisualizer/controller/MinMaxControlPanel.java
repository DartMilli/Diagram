package diagramvisualizer.controller;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author szlavik.mi
 */
public class MinMaxControlPanel extends JPanel implements ChangeListener, ItemListener, ActionListener {

    private Menu menu;
    private JLabel[] labels;
    private JSpinner[] spinners = new JSpinner[6];
    private JButton autoRaster;
    private JCheckBox chkRaster;
    private String[] labelNames = {"X min.:", "X max.:", "X raster:", "Y min.:", "Y max.:", "Y raster:", "Auto values:", "Draw raster:"};

    public MinMaxControlPanel(Menu menu, int width) {
        this.menu = menu;
        this.labels = new JLabel[labelNames.length];

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < spinners.length; i++) {
            labels[i] = new JLabel(labelNames[i]);
            spinners[i] = new JSpinner(new SpinnerNumberModel(getActualValue(i), -10E10, 10E10, 10E-3));
            spinners[i].addChangeListener(this);
            spinners[i].setName(Integer.toString(i));
        }
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;
        int i;
        for (i = 0; i < spinners.length; i++) {
            c.gridx = 0;
            c.gridy = i;
            c.gridwidth = 4;
            add(labels[i], c);
            c.gridx = 4;
            c.gridwidth = 6;
            add(spinners[i], c);
        }
        c.gridy = i;
        c.gridx = 0;
        c.gridwidth = 4;
        labels[i] = new JLabel(labelNames[i]);
        add(labels[i], c);

        c.gridx = 4;
        c.gridwidth = 4;
        autoRaster = new JButton("Auto");
        autoRaster.addActionListener(this);
        add(autoRaster, c);
        i++;

        c.gridy = i;
        c.gridx = 0;
        c.gridwidth = 4;
        labels[i] = new JLabel(labelNames[i]);
        add(labels[i], c);
        c.gridx = 4;
        c.gridwidth = 4;
        chkRaster = new JCheckBox("", true);
        chkRaster.addItemListener(this);
        add(chkRaster, c);

        setWidth(width - 12);
    }

    private void setWidth(int width) {
        int i;
        for (i = 0; i < spinners.length; i++) {
            labels[i].setPreferredSize(new Dimension(width / 2, labels[i].getPreferredSize().height));
            spinners[i].setPreferredSize(new Dimension(width / 2, spinners[i].getPreferredSize().height));
        }
        labels[i].setPreferredSize(new Dimension(width / 2, labels[i].getPreferredSize().height));
        labels[i + 1].setPreferredSize(new Dimension(width / 2, labels[i + 1].getPreferredSize().height));
        autoRaster.setPreferredSize(new Dimension(width / 2, autoRaster.getPreferredSize().height));
        chkRaster.setPreferredSize(new Dimension(width / 2, chkRaster.getPreferredSize().height));
        this.setPreferredSize(new Dimension(width, getPreferredSize().height));
    }

    private double getActualValue(int id) {
        double value = 0.0;
        switch (id) {
            case 0:
                value = menu.getView().getMinX();
                break;
            case 1:
                value = menu.getView().getMaxX();
                break;
            case 2:
                value = menu.getView().getRasterX();
                break;
            case 3:
                value = menu.getView().getMinY();
                break;
            case 4:
                value = menu.getView().getMaxY();
                break;
            case 5:
                value = menu.getView().getRasterY();
                break;
        }
        return value;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        JSpinner sp = (JSpinner) ce.getSource();
        switch (sp.getName()) {
            case "0":
                menu.getView().setMinX((double) spinners[0].getValue());
                break;
            case "1":
                menu.getView().setMaxX((double) spinners[1].getValue());
                break;
            case "2":
                menu.getView().setRasterX((double) spinners[2].getValue());
                break;
            case "3":
                menu.getView().setMinY((double) spinners[3].getValue());
                break;
            case "4":
                menu.getView().setMaxY((double) spinners[4].getValue());
                break;
            case "5":
                menu.getView().setRasterY((double) spinners[5].getValue());
                break;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        menu.getView().setDrawRaster(e.getStateChange() == ItemEvent.SELECTED);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        menu.getView().setAutoAxesUnits();
        for (int i = 0; i < spinners.length; i++) {
            spinners[i].getModel().setValue(getActualValue(i));
        }
    }

}
