package diagramvisualizer.controller;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author szlavik.mi
 */
public class CaptionControlPanel extends JPanel implements ActionListener {

    private Menu menu;
    private JLabel mainLabel = new JLabel("Adjust Axes Caption:");
    private JLabel[] labels = new JLabel[4];
    private JComboBox[] combos = new JComboBox[4];
    private String[] labelNames = {"X plus:", "X minus:", "Y plus:", "Y minus:"};

    public CaptionControlPanel(Menu menu, int width) {
        this.menu = menu;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < combos.length; i++) {
            labels[i] = new JLabel(labelNames[i]);
            combos[i] = new JComboBox(getCaptionDrawTypes(i));
            combos[i].addActionListener(this);
        }
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;
        add(mainLabel, c);
        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0;
            c.gridy = i + 1;
            c.gridwidth = 4;
            add(labels[i], c);
            c.gridx = 4;
            c.gridy = i + 1;
            c.gridwidth = 6;
            add(combos[i], c);
        }
        setWitdth(width);
    }

    private void setWitdth(int width) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setPreferredSize(new Dimension(width / 2, labels[i].getPreferredSize().height));
            combos[i].setPreferredSize(new Dimension(width / 2, combos[i].getPreferredSize().height));
        }
        this.setPreferredSize(new Dimension(width, getPreferredSize().height));
    }

    private String getCaptionString() {
        StringBuilder caption = new StringBuilder("");
        for (int i = 0; i < combos.length; i++) {
            caption.append(combos[i].getSelectedIndex());
        }
        return caption.toString();
    }

    private String[] getCaptionDrawTypes(int index) {
        String[] types = new String[3];
        switch (index) {
            case 0:
            case 1:
                types[0] = "none";
                types[1] = "above axis";
                types[2] = "below axis";
                break;
            case 2:
            case 3:
                types[0] = "none";
                types[1] = "left side";
                types[2] = "right side";
                break;
        }
        return types;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menu.getView().setCaptionType(getCaptionString());
    }
}
