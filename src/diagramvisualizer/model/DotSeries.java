package diagramvisualizer.model;

import java.util.ArrayList;

/**
 *
 * @author Milán
 */
public class DotSeries {

    private ArrayList<Double> xCoordinates;
    private ArrayList<Double> yCoordinates;
    private int pieces;

    public DotSeries() {
        xCoordinates = new ArrayList<>();
        yCoordinates = new ArrayList<>();
        pieces = 0;
    }

    public DotSeries(double[] x, double[] y) {
        xCoordinates = new ArrayList<>();
        yCoordinates = new ArrayList<>();
        pieces = 0;
        addPoint(x, y);
    }

    public void addPoint(double[] x, double[] y) {
        if (x.length == y.length) {
            for (int i = 0; i < y.length; i++) {
                addPoint(x[i], y[i]);
            }
        }
    }

    public void addPoint(double x, double y) {
        xCoordinates.add(x);
        yCoordinates.add(y);
        pieces++;
    }

    public void removePoint(int index) {
        if (index >= 0 && index < pieces) {
            xCoordinates.remove(index);
            yCoordinates.remove(index);
            pieces--;
        }
    }

    public void removePoint(double x, double y) {
        for (int i = 0; i < pieces; i++) {
            if (Double.compare(getPointX(i), x) == 0
                    && Double.compare(getPointY(i), y) == 0) {
                removePoint(i);
            }
        }
    }

    public void setPoint(int index, double x, double y) {
        if (index >= 0 && index < pieces) {
            xCoordinates.set(index, x);
            yCoordinates.set(index, y);
        }
    }

    public int getPiecesOfPoints() {
        return pieces;
    }

    public double[][] getPoints() {
        double[][] out = new double[2][pieces];
        for (int i = 0; i < pieces; i++) {
            out[0][i] = xCoordinates.get(i);
            out[1][i] = yCoordinates.get(i);
        }
        return out;
    }

    public double[] getPoint(int index) {
        double[] out = new double[2];
        out[0] = getPointX(index);
        out[1] = getPointY(index);
        return out;
    }

    public double getPointX(int index) {
        Double out = null;
        if (index < pieces) {
            out = xCoordinates.get(index);
        }
        return out;
    }

    public double[] getPointX() {
        double[] out = new double[pieces];
        for (int i = 0; i < out.length; i++) {
            out[i] = xCoordinates.get(i);
        }
        return out;
    }

    public double getPointY(int index) {
        Double out = null;
        if (index < pieces) {
            out = yCoordinates.get(index);
        }
        return out;
    }

    public double[] getPointY() {
        double[] out = new double[pieces];
        for (int i = 0; i < out.length; i++) {
            out[i] = yCoordinates.get(i);
        }
        return out;
    }

    public DotSeries getSorted() {
        DotSeries out = new DotSeries(this.getPointX(), this.getPointY());
        out.sortMe();
        return out;
    }
    
    public void sortMe(){
        for (int i = getPiecesOfPoints() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (getPointX(j) > getPointX(j + 1)) {
                    double tmpX = getPointX(j);
                    double tmpY = getPointY(j);
                    setPoint(j, getPointX(j + 1), getPointY(j + 1));
                    setPoint(j + 1, tmpX, tmpY);
                }
            }
        }
    }
}
