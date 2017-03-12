package diagramvisualizer.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author szlavik.mi
 */
public class SpinPanel extends JPanel implements ActionListener {

    private JLabel title;
    private JButton spinner;
    private JPanel captionPanel;
    private JPanel container;
    private int width;
    private boolean closed;
    private String[] upAndDownArrow = new String[]{"<html>&#x2227</html>", "<html>&#x2228</html>"};

    public SpinPanel(String caption, int width) {
        this.title = new JLabel(caption);
        this.spinner = new JButton(upAndDownArrow[0]);
        this.container = new JPanel();
        this.captionPanel = new JPanel(new BorderLayout());
        this.closed = false;
        this.width = width;
        this.setLayout(new BorderLayout());

        this.captionPanel.add(title, BorderLayout.WEST);
        this.captionPanel.add(spinner, BorderLayout.EAST);

        add(this.captionPanel, BorderLayout.NORTH);
        add(container, BorderLayout.SOUTH);

        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        setBorder(loweredetched);
        captionPanel.setBorder(loweredetched);

        spinner.addActionListener(this);
        captionPanel.setPreferredSize(new Dimension(width - 1, 2 * title.getFont().getSize()));
    }

    public void close() {
        container.setVisible(false);
        closed = true;
        spinner.setText(upAndDownArrow[1]);
    }

    public void open() {
        container.setVisible(true);
        closed = false;
        spinner.setText(upAndDownArrow[0]);
    }

    @Override
    public Component add(Component comp) {
        return container.add(comp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (closed) {
            open();
        } else {
            close();
        }
    }
}
