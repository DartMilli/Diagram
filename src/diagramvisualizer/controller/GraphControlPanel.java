package diagramvisualizer.controller;

import diagramvisualizer.model.DataPointTableModel;
import diagramvisualizer.view.GraphToDraw;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author szlavik.mi
 */
public class GraphControlPanel extends JPanel
        implements
        ActionListener,
        ChangeListener,
        ItemListener,
        PropertyChangeListener {

    private final Menu menu;
    private GraphToDraw adjustedGraph;
    private JComboBox graphs;
    private JComboBox graphPointsView;
    private JSpinner sizeSpinner;
    private JSpinner lineSizeSpinner;
    private JButton colorButton;
    private JButton addRowButton;
    private DefaultComboBoxModel comboModel;
    private JCheckBox chkDrawLine;
    private JCheckBox chkDrawNumber;
    private JLabel mainLabel = new JLabel("Adjust Graphs:");
    private JLabel[] labels;
    private String[] labelNames = {"Selected:", "Marker:", "Color:", "Size:", "Draw Line:", "Line size:", "Draw Num.:"};
    private JScrollPane pointsTableScroll;
    private JTable pointsTable;

    private enum Names {

        DATA,
        POINTVIEW,
        COLOR,
        SIZE,
        LINESIZE,
        DRAWLINE,
        DRAWNUMBER,
        TABLE,
        ADDBUTTON
    }

    public GraphControlPanel(Menu menu, int width) {
        this.menu = menu;
        this.labels = new JLabel[labelNames.length];

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

        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 6;
        comboModel = new DefaultComboBoxModel<GraphToDraw>(menu.getView().getGraphs());
        graphs = new JComboBox(comboModel);
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        graphs.addActionListener(this);
        graphs.setName(Names.DATA.name());
        add(graphs, c);

        c.gridy = 2;
        String[] tmp = {"none", "O", "X", "+"};
        graphPointsView = new JComboBox(tmp);
        graphPointsView.addActionListener(this);
        graphPointsView.setName(Names.POINTVIEW.name());
        add(graphPointsView, c);

        c.gridy = 3;
        colorButton = new JButton("Change");
        colorButton.addActionListener(this);
        colorButton.setName(Names.COLOR.name());
        add(colorButton, c);

        c.gridy = 4;
        sizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        sizeSpinner.addChangeListener(this);
        sizeSpinner.setName(Names.SIZE.name());
        add(sizeSpinner, c);

        c.gridy = 5;
        chkDrawLine = new JCheckBox("", false);
        chkDrawLine.addItemListener(this);
        chkDrawLine.setName(Names.DRAWLINE.name());
        add(chkDrawLine, c);

        c.gridy = 6;
        lineSizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        lineSizeSpinner.addChangeListener(this);
        lineSizeSpinner.setName(Names.LINESIZE.name());
        add(lineSizeSpinner, c);

        c.gridy = 7;
        chkDrawNumber = new JCheckBox("", false);
        chkDrawNumber.addItemListener(this);
        chkDrawNumber.setName(Names.DRAWNUMBER.name());
        add(chkDrawNumber, c);

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 10;
        //c.gridheight = 2;
        pointsTable = new JTable(new DataPointTableModel(adjustedGraph));
        pointsTable.addPropertyChangeListener(this);
        pointsTable.setName(Names.TABLE.name());
        pointsTableScroll = new JScrollPane(pointsTable);
        //pointsTableScroll.add(addRowButton);
        pointsTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pointsTableScroll, c);

        c.gridy = 9;
        addRowButton = new JButton("Add row...");
        addRowButton.setName(Names.ADDBUTTON.name());
        addRowButton.addActionListener(this);
        add(addRowButton,c);

        initInitialValues();
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
        lineSizeSpinner.setPreferredSize(new Dimension(width / 2, lineSizeSpinner.getPreferredSize().height));
        chkDrawLine.setPreferredSize(new Dimension(width / 2, chkDrawLine.getPreferredSize().height));
        chkDrawNumber.setPreferredSize(new Dimension(width / 2, chkDrawNumber.getPreferredSize().height));
        pointsTableScroll.setPreferredSize(new Dimension(width, 7 * pointsTable.getRowHeight()));
        addRowButton.setPreferredSize(new Dimension(width, addRowButton.getPreferredSize().height));
        this.setPreferredSize(new Dimension(width, getPreferredSize().height));
    }

    private void initInitialValues() {
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        graphPointsView.setSelectedIndex(adjustedGraph.getType());
        colorButton.setForeground(adjustedGraph.getColor());
        sizeSpinner.setValue(adjustedGraph.getSize());
        lineSizeSpinner.setValue(adjustedGraph.getLineSize());
        chkDrawLine.setSelected(adjustedGraph.isLines());
        chkDrawNumber.setSelected(adjustedGraph.isNumbers());
        ((DataPointTableModel) pointsTable.getModel()).setGraph(adjustedGraph);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        JComponent component = (JComponent) e.getSource();
        if (component.getName() == Names.DATA.name()) {
            initInitialValues();
        } else if (component.getName() == Names.POINTVIEW.name()) {
            adjustedGraph.setType(graphPointsView.getSelectedIndex());
        } else if (component.getName() == Names.COLOR.name()) {
            Color initialColor = adjustedGraph.getColor();
            Color choosenColor = JColorChooser.showDialog(null, "Change Color of " + adjustedGraph.getName(),
                    initialColor);
            if (choosenColor != null) {
                adjustedGraph.setColor(choosenColor);
                colorButton.setForeground(choosenColor);
            }
        }else if (component.getName() == Names.ADDBUTTON.name()) {
            ((DataPointTableModel)pointsTable.getModel()).addRow();
        }
        menu.getView().repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        JComponent component = (JComponent) e.getSource();
        if (component.getName() == Names.SIZE.name()) {
            adjustedGraph.setSize((Integer) sizeSpinner.getValue());
        } else if (component.getName() == Names.LINESIZE.name()) {
            adjustedGraph.setLineSize((Integer) lineSizeSpinner.getValue());
        }
        menu.getView().repaint();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        JComponent component = (JComponent) e.getSource();
        if (component.getName() == Names.DRAWLINE.name()) {
            adjustedGraph.setLines(e.getStateChange() == ItemEvent.SELECTED);
        } else if (component.getName() == Names.DRAWNUMBER.name()) {
            adjustedGraph.setNumbers(e.getStateChange() == ItemEvent.SELECTED);
        }
        menu.getView().repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        menu.getView().repaint();
    }

}
