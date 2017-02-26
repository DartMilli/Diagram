package diagramvisualizer.model;

import diagramvisualizer.view.GraphToDraw;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Milán
 */
public class DataPointTableModel extends AbstractTableModel {

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
    
    public void addRow(){
       addRow(new double[]{0.0,0.0});
    }
    
    public void addRow(double[] point){
        if (point.length==2) {
            graph.getGraf().addPoint(point[0], point[1]);
        }
        fireTableDataChanged();
    }
    
    public void addRow(double[][] points){    
        for (int i = 0; i < points.length; i++) {
            addRow(points[i]);            
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

}
