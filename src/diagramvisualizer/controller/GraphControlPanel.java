package diagramvisualizer.controller;

import diagramvisualizer.view.GraphToDraw;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
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
public class GraphControlPanel extends JPanel {

    private final Menu menu;
    private JComboBox graphs;
    private JComboBox graphPointsView;
    private JSpinner sizeSpinner;
    private JButton colorButton;
    private DefaultComboBoxModel comboModel;
    private JCheckBox chkDrawLine;
    private JSpinner lineSizeSpinner;
    private JLabel mainLabel = new JLabel("Adjust Graphs:");
    private JLabel[] labels = new JLabel[6];
    private String[] labelNames = {"Selected:", "Marker:", "Color:", "Size:", "Draw Line", "Line size:"};

    public GraphControlPanel(final Menu menu, int width) {
        this.menu = menu;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;
        add(mainLabel, c);

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(labelNames[i]);
            c.gridy = i + 1;
            c.gridwidth = 4;
            add(labels[i], c);
        }

        comboModel = new DefaultComboBoxModel<GraphToDraw>(menu.getView().getGraphs());
        graphs = new JComboBox(comboModel);
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 6;
        add(graphs, c);

        String[] tmp = {"none", "O", "X", "+"};
        graphPointsView = new JComboBox(tmp);
        graphPointsView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphToDraw selectedItem = (GraphToDraw) comboModel.getSelectedItem();
                selectedItem.setType(graphPointsView.getSelectedIndex());
                menu.getView().repaint();
            }
        });
        c.gridy = 2;
        add(graphPointsView, c);
        c.gridy = 3;
        colorButton = new JButton("Choose");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphToDraw selectedItem = (GraphToDraw) comboModel.getSelectedItem();
                Color initialColor = selectedItem.getColor();
                Color choosenColor = JColorChooser.showDialog(null, "Change Color of " + selectedItem.getName(),
                        initialColor);
                if (choosenColor != null) {
                    selectedItem.setColor(choosenColor);
                    menu.getView().repaint();
                }
            }
        });
        add(colorButton, c);
        c.gridy = 4;
        sizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        sizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                GraphToDraw selectedItem = (GraphToDraw) comboModel.getSelectedItem();
                selectedItem.setSize((Integer) sizeSpinner.getValue());
                menu.getView().repaint();
            }
        });
        add(sizeSpinner, c);
        
        c.gridy = 5;
        chkDrawLine = new JCheckBox("", false);
        chkDrawLine.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                GraphToDraw selectedItem = (GraphToDraw) comboModel.getSelectedItem();
                selectedItem.setLines(ie.getStateChange() == ItemEvent.SELECTED);
                menu.getView().repaint();
            }
        });
        add(chkDrawLine,c);
        
        c.gridy = 6;
        lineSizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        lineSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                GraphToDraw selectedItem = (GraphToDraw) comboModel.getSelectedItem();
                selectedItem.setLineSize((Integer) lineSizeSpinner.getValue());
                menu.getView().repaint();
            }
        });
        add(lineSizeSpinner, c);
        
        setWidth(width);
    }

    private void setWidth(int width) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setPreferredSize(new Dimension(width / 2, labels[i].getPreferredSize().height));
        }
        graphs.setPreferredSize(new Dimension(width / 2, graphs.getPreferredSize().height));
        graphPointsView.setPreferredSize(new Dimension(width / 2, graphPointsView.getPreferredSize().height));
        colorButton.setPreferredSize(new Dimension(width / 2, colorButton.getPreferredSize().height));
        sizeSpinner.setPreferredSize(new Dimension(width / 2, sizeSpinner.getPreferredSize().height));
        this.setPreferredSize(new Dimension(width, getPreferredSize().height));
    }

}
