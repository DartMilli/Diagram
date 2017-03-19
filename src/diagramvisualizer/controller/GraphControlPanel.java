package diagramvisualizer.controller;

import diagramvisualizer.model.DataPointTableModel;
import diagramvisualizer.model.DotSeries;
import diagramvisualizer.model.LagrangeInterpolate;
import diagramvisualizer.model.Regression;
import diagramvisualizer.model.SplineInterpolate;
import diagramvisualizer.view.GraphToDraw;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JTextField;
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
    private JSpinner spAddX;
    private JSpinner spAddY;
    private JButton colorButton;
    private JButton addRowButton;
    private JButton removeRowButton;
    private JButton removeDataButton;
    private JButton addNewButton;
    private JButton addRegressionButton;
    private JButton addLagrangeButton;
    private JButton addSplineButton;
    private DefaultComboBoxModel comboModel;
    private JCheckBox chkDrawLine;
    private JCheckBox chkDrawNumber;
    private JLabel[] labels;
    private JLabel[] labels2;
    private JTextField nameOfNewData;
    private String[] labelNames = {"Selected:", "Marker:", "Color:", "Draw:", "Marker size:", "Line size:"};
    private String[] labelNames2 = {"Add new:", "Remove:", "Add:"};
    private JScrollPane pointsTableScroll;
    private JTable pointsTable;
    private JPanel addPanel;

    private enum Names {

        DATA,
        POINTVIEW,
        COLOR,
        SIZE,
        LINESIZE,
        DRAWLINE,
        DRAWNUMBER,
        TABLE,
        ADDPOINT,
        REMOVEPOINT,
        REMOVEDATA,
        ADDNEW,
        ADDREGRESSION,
        ADDLAGRANGE,
        ADDSPLINE
    }

    public GraphControlPanel(Menu menu, int width) {
        this.menu = menu;
        this.labels = new JLabel[labelNames.length];
        this.labels2 = new JLabel[labelNames2.length];

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(labelNames[i]);
            c.gridy = i;
            c.gridwidth = 4;
            add(labels[i], c);
        }

        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 6;
        comboModel = new DefaultComboBoxModel<GraphToDraw>(menu.getView().getGraphs());
        graphs = new JComboBox(comboModel);
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        graphs.addActionListener(this);
        graphs.setName(Names.DATA.name());
        add(graphs, c);

        c.gridy = 1;
        String[] tmp = {"none", "O", "X", "+"};
        graphPointsView = new JComboBox(tmp);
        graphPointsView.addActionListener(this);
        graphPointsView.setName(Names.POINTVIEW.name());
        add(graphPointsView, c);

        c.gridy = 2;
        colorButton = new JButton("Change");
        colorButton.addActionListener(this);
        colorButton.setName(Names.COLOR.name());
        add(colorButton, c);

        c.gridy = 3;
        c.gridwidth = 3;
        chkDrawLine = new JCheckBox("Line", false);
        chkDrawLine.addItemListener(this);
        chkDrawLine.setName(Names.DRAWLINE.name());
        chkDrawLine.setMargin(new Insets(0, 0, 0, 0));
        add(chkDrawLine, c);

        c.gridy = 3;
        c.gridx = 7;
        c.gridwidth = 3;
        chkDrawNumber = new JCheckBox("No.", false);
        chkDrawNumber.addItemListener(this);
        chkDrawNumber.setName(Names.DRAWNUMBER.name());
        chkDrawNumber.setMargin(new Insets(0, 0, 0, 0));
        add(chkDrawNumber, c);

        c.gridy = 4;
        c.gridx = 4;
        c.gridwidth = 6;
        sizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        sizeSpinner.addChangeListener(this);
        sizeSpinner.setName(Names.SIZE.name());
        add(sizeSpinner, c);

        c.gridy = 5;
        lineSizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
        lineSizeSpinner.addChangeListener(this);
        lineSizeSpinner.setName(Names.LINESIZE.name());
        add(lineSizeSpinner, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 10;
        DataPointTableModel pointModel = new DataPointTableModel(adjustedGraph);
        pointsTable = new JTable(pointModel);
        TableKeyListener keyListener = new TableKeyListener(pointsTable);
        pointsTable.addPropertyChangeListener(this);
        pointsTable.setName(Names.TABLE.name());
        pointsTable.addKeyListener(keyListener);
        pointsTableScroll = new JScrollPane(pointsTable);
        pointsTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pointsTableScroll, c);

        c.gridx = 0;
        c.gridy = 7;
        spAddX = new JSpinner(new SpinnerNumberModel(0.0, -10E10, 10E10, 10E-3));
        spAddY = new JSpinner(new SpinnerNumberModel(0.0, -10E10, 10E10, 10E-3));
        addRowButton = new JButton("+");
        addRowButton.setMargin(new Insets(0, 0, 0, 0));
        addRowButton.setName(Names.ADDPOINT.name());
        addRowButton.addActionListener(this);
        addPanel = new JPanel(new BorderLayout());
        addPanel.add(spAddX, BorderLayout.WEST);
        addPanel.add(spAddY, BorderLayout.CENTER);
        addPanel.add(addRowButton, BorderLayout.EAST);
        add(addPanel, c);

        for (int i = 0; i < labels2.length; i++) {
            labels2[i] = new JLabel(labelNames2[i]);
            c.gridy = i + 8;
            c.gridwidth = 4;
            add(labels2[i], c);
        }

        c.gridx = 4;
        c.gridy = 8;
        c.gridwidth = 6;
        addNewButton = new JButton();
        nameOfNewData = new JTextField("New...");
        addNewButton.setName(Names.ADDNEW.name());
        addNewButton.addActionListener(this);
        addNewButton.add(nameOfNewData);
        add(addNewButton, c);

        c.gridx = 4;
        c.gridy = 9;
        c.gridwidth = 3;
        removeRowButton = new JButton("Point");
        removeRowButton.setName(Names.REMOVEPOINT.name());
        removeRowButton.addActionListener(this);
        removeRowButton.setMargin(new Insets(0, 0, 0, 0));
        add(removeRowButton, c);

        c.gridx = 7;
        removeDataButton = new JButton("Data");
        removeDataButton.setName(Names.REMOVEDATA.name());
        removeDataButton.addActionListener(this);
        removeDataButton.setMargin(new Insets(0, 0, 0, 0));
        add(removeDataButton, c);

        c.gridx = 4;
        c.gridy = 10;
        c.gridwidth = 6;
        addRegressionButton = new JButton("Regression");
        addRegressionButton.setName(Names.ADDREGRESSION.name());
        addRegressionButton.addActionListener(this);
        addRegressionButton.setMargin(new Insets(0, 0, 0, 0));
        add(addRegressionButton, c);

        c.gridy = 11;
        addLagrangeButton = new JButton("Lagrange");
        addLagrangeButton.setName(Names.ADDLAGRANGE.name());
        addLagrangeButton.addActionListener(this);
        addLagrangeButton.setMargin(new Insets(0, 0, 0, 0));
        add(addLagrangeButton, c);

        c.gridy = 12;
        addSplineButton = new JButton("Spline");
        addSplineButton.setName(Names.ADDSPLINE.name());
        addSplineButton.addActionListener(this);
        addSplineButton.setMargin(new Insets(0, 0, 0, 0));
        add(addSplineButton, c);

        initInitialValues();
        setWidth(width - 12);
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
        chkDrawLine.setPreferredSize(new Dimension(width / 4, chkDrawLine.getPreferredSize().height));
        chkDrawNumber.setPreferredSize(new Dimension(width / 4, chkDrawNumber.getPreferredSize().height));
        pointsTableScroll.setPreferredSize(new Dimension(width, 7 * pointsTable.getRowHeight()));
        int addButtonWith = 17;
        addRowButton.setPreferredSize(new Dimension(addButtonWith, addRowButton.getPreferredSize().height));
        spAddX.setPreferredSize(new Dimension((width - addButtonWith) / 2, spAddX.getPreferredSize().height));
        spAddY.setPreferredSize(new Dimension((width - addButtonWith) / 2, spAddY.getPreferredSize().height));
        addNewButton.setPreferredSize(new Dimension(width / 2, addNewButton.getPreferredSize().height));
        nameOfNewData.setPreferredSize(new Dimension(addNewButton.getWidth() / 2, nameOfNewData.getPreferredSize().height));
        removeRowButton.setPreferredSize(new Dimension(width / 4, removeRowButton.getPreferredSize().height));
        removeDataButton.setPreferredSize(new Dimension(width / 4, removeDataButton.getPreferredSize().height));
        addRegressionButton.setPreferredSize(new Dimension(width / 2, addRegressionButton.getPreferredSize().height));
        addLagrangeButton.setPreferredSize(new Dimension(width / 2, addLagrangeButton.getPreferredSize().height));
        addSplineButton.setPreferredSize(new Dimension(width / 2, addSplineButton.getPreferredSize().height));

        this.setPreferredSize(new Dimension(width, getPreferredSize().height));
    }

    private void initInitialValues() {
        adjustedGraph = (GraphToDraw) comboModel.getSelectedItem();
        if (adjustedGraph != null) {
            graphPointsView.setSelectedIndex(adjustedGraph.getType());
            colorButton.setForeground(adjustedGraph.getColor());
            sizeSpinner.setValue(adjustedGraph.getSize());
            lineSizeSpinner.setValue(adjustedGraph.getLineSize());
            chkDrawLine.setSelected(adjustedGraph.isLines());
            chkDrawNumber.setSelected(adjustedGraph.isNumbers());
            ((DataPointTableModel) pointsTable.getModel()).setGraph(adjustedGraph);
        }
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
        } else if (component.getName() == Names.ADDPOINT.name()) {
            double[] p = new double[2];
            p[0] = (double) spAddX.getValue();
            p[1] = (double) spAddY.getValue();
            ((DataPointTableModel) pointsTable.getModel()).addRow(p);
        } else if (component.getName() == Names.ADDNEW.name()) {
            double[] tmp = {0};
            adjustedGraph = new GraphToDraw(new DotSeries(tmp, tmp), nameOfNewData.getText(), Color.yellow, true, 0, 0, 0);
            nameOfNewData.setText("New...");
            menu.getView().addGraph(adjustedGraph);
            comboModel.addElement(adjustedGraph);
        } else if (component.getName() == Names.REMOVEDATA.name()) {
            menu.getView().removeGraph(adjustedGraph);
            comboModel.removeElement(adjustedGraph);
        } else if (component.getName() == Names.REMOVEPOINT.name()) {
            ((DataPointTableModel) pointsTable.getModel()).removeRow(pointsTable.getSelectedRows());
        } else if (component.getName() == Names.ADDREGRESSION.name()) {
            Regression regression = new Regression(adjustedGraph.getGraf());
            DotSeries trend = regression.getRegressionStraight();
            adjustedGraph = new GraphToDraw(trend, adjustedGraph.getName() + "_reg", Color.BLACK, true, 0, 0, 0);
            menu.getView().addGraph(adjustedGraph);
            comboModel.addElement(adjustedGraph);
        } else if (component.getName() == Names.ADDLAGRANGE.name()) {
            LagrangeInterpolate li = new LagrangeInterpolate(adjustedGraph.getGraf());
            DotSeries lagrangeInterpoltedData = li.getInterpolatedData();
            adjustedGraph = new GraphToDraw(lagrangeInterpoltedData, adjustedGraph.getName() + "_lag", Color.BLACK, true, 0, 0, 0);
            menu.getView().addGraph(adjustedGraph);
            comboModel.addElement(adjustedGraph);
        } else if (component.getName() == Names.ADDSPLINE.name()) {
            SplineInterpolate si = new SplineInterpolate(adjustedGraph.getGraf());
            DotSeries splineInterpoltedData = si.getInterpolatedData();
            adjustedGraph = new GraphToDraw(splineInterpoltedData, adjustedGraph.getName() + "_spl", Color.BLACK, true, 0, 0, 0);
            menu.getView().addGraph(adjustedGraph);
            comboModel.addElement(adjustedGraph);
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
