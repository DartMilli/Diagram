package diagramvisualizer.controller;

import diagramvisualizer.view.Diagram;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *
 * @author Milán
 */
public class DiagramVisualizer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame();
                window.setDefaultCloseOperation(EXIT_ON_CLOSE);
                Dimension dmsn = new Dimension(
                        GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize()
                );
                window.setSize(new Dimension(dmsn));
                window.setTitle("Diagram Visualizer 1.0.0");
                window.setLayout(null);
                window.setResizable(false);
                int margin = 10;
                Dimension viewDmsn = new Dimension(dmsn.width - 2 * margin - 6, dmsn.height - 2 * margin - 28);
                Rectangle rctl = new Rectangle(new Point(margin, margin), viewDmsn);
                JPanel veiw = new Diagram(viewDmsn);                
                veiw.setBounds(rctl);
                window.add(veiw);
                window.setVisible(true);
            }
        });
    }

}
