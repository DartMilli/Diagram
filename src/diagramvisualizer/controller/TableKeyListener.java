package diagramvisualizer.controller;

import diagramvisualizer.model.DataPointTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTable;

/**
 *
 * @author Milán
 */
public class TableKeyListener implements KeyListener {

    private JTable table;

    public TableKeyListener(JTable table) {
        this.table = table;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_C) {
            int[] rows = table.getSelectedRows();
            StringBuilder content = new StringBuilder();
            DataPointTableModel model = (DataPointTableModel) table.getModel();
            double num1, num2;
            for (int i = 0; i < rows.length; i++) {
                num1 = (Double) (model.getValueAt(i, 0));
                num2 = (Double) (model.getValueAt(i, 1));
                content.append(num1).append("\t").append(num2).append("\n");
            }
            model.setClipboardContents(content.toString());
        } else if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_V) {
            DataPointTableModel model = (DataPointTableModel) table.getModel();
            String tmp = model.getClipboardContents();
            String[] row = tmp.split("\n");
            String[] col;
            for (int i = 0; i < row.length; i++) {
                col = row[i].split("\t");
                double[] point = new double[col.length];
                for (int j = 0; j < col.length; j++) {
                    point[j] = Double.parseDouble(col[j]);
                }
                model.addRow(point);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

}
