package diagramvisualizer.model;

import diagramvisualizer.view.GraphToDraw;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Milán
 */
public class DataPointTableModel extends AbstractTableModel implements ClipboardOwner {

    private GraphToDraw graph;
    private final String[] columnNames = new String[]{"X", "Y"};
    private final Class[] columnClass = new Class[]{Double.class, Double.class};

    public DataPointTableModel(GraphToDraw graph) {
        this.graph = graph;
    }

    public GraphToDraw getGraph() {
        return graph;
    }

    public void setGraph(GraphToDraw graph) {
        this.graph = graph;
        fireTableDataChanged();
    }

    public void addRow() {
        addRow(new double[]{0.0, 0.0});
    }

    public void addRow(double[] point) {
        if (point.length == 2) {
            graph.getGraf().addPoint(point[0], point[1]);
        }
        fireTableDataChanged();
    }

    public void addRow(double[][] points) {
        for (int i = 0; i < points.length; i++) {
            addRow(points[i]);
        }
    }

    public void removeRow(int index) {
        if (index < graph.getGraf().getPiecesOfPoints() && index >= 0) {
            graph.getGraf().removePoint(index);
        }
        fireTableDataChanged();
    }

    public void removeRow(int[] indexes) {
        for (int i = (indexes.length - 1); i >= 0; i--) {
            removeRow(indexes[i]);
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return graph.getGraf().getPiecesOfPoints();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (0 == columnIndex) {
            return graph.getGraf().getPoint(rowIndex)[0];
        } else if (1 == columnIndex) {
            return graph.getGraf().getPoint(rowIndex)[1];
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        double x, y;
        if (0 == columnIndex) {
            y = graph.getGraf().getPointY(rowIndex);
            graph.getGraf().setPoint(rowIndex, (Double) aValue, y);
        } else if (1 == columnIndex) {
            x = graph.getGraf().getPointX(rowIndex);
            graph.getGraf().setPoint(rowIndex, x, (Double) aValue);
        }
    }

    @Override
    public void lostOwnership(Clipboard clpbrd, Transferable t) {
    }

    //http://www.javapractices.com/topic/TopicAction.do?Id=82    
    
    /**
     * Place a String on the clipboard, and make this class the owner of the
     * Clipboard's contents.
     */
    public void setClipboardContents(String aString) {
        StringSelection stringSelection = new StringSelection(aString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an empty
     * String.
     */
    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText
                = (contents != null)
                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }
}
