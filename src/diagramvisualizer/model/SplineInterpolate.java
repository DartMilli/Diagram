package diagramvisualizer.model;

/**
 *
 * @author szlavik.mi
 */
public class SplineInterpolate {

    private DotSeries data;
    private DotSeries interpolatedData;
    private int raster = 10;
    private int noOfOriginalPoints;
    private int noOfInterpolatedPoints;
    private double[][] coefficients;
    private boolean isVisualizable;

    public SplineInterpolate(DotSeries data) {
        this.data = data.getSorted();
        noOfOriginalPoints = this.data.getPointX().length;
        coefficients = new double[noOfOriginalPoints - 1][4];
        isVisualizable = false;
        interpolate();
        generateInterpolatedData();
    }

    private void interpolate() {
        int noOfFunctions = noOfOriginalPoints - 1;
        int noOfCoefficients = noOfFunctions * 4;
        Matrix m = new Matrix(noOfCoefficients, noOfCoefficients + 1);
        
        for (int i = 0; i < m.getRow(); i++) {
            for (int j = 0; j < m.getColumn(); j++) {
                m.setMatrix(i, j, 0.0);
            }
        }
        
        for (int i = 0; i < noOfFunctions; i++) {
            for (int j = 0; j < 4; j++) {
                m.setMatrix(i, j + 4 * i, Math.pow(data.getPointX(i), 3 - j));
                m.setMatrix(i + noOfFunctions, j + 4 * i, Math.pow(data.getPointX(i + 1), 3 - j));
            }
            m.setMatrix(i, noOfCoefficients, data.getPointY(i));
            m.setMatrix(i + noOfFunctions, noOfCoefficients, data.getPointY(i + 1));
        }
        
        
        System.out.println("matrix:");
        m.getMatrixConsole();
    }

    private void generateInterpolatedData() {
        double min, max, step;
        double[] x = new double[noOfInterpolatedPoints + 1];
        double[] y = new double[noOfInterpolatedPoints + 1];
        min = data.getPointX(0);
        max = data.getPointX(noOfOriginalPoints - 1);
        step = (max - min) / noOfInterpolatedPoints;

        if (isVisualizable) {
            interpolatedData = new DotSeries(x, y);
        } else {
            interpolatedData = new DotSeries(data.getPointX(), data.getPointY());
        }
    }

    public DotSeries getInterpolatedData() {
        return interpolatedData;
    }

    public static void main(String[] args) {
        double[] x;
        double[] y;
        if (true) {
            if (false) {
                x = new double[]{1, 2, 3, 4, 5};
                y = new double[]{2, 3, 2, 0.5, 1.5};
            } else {
                x = new double[]{1, 2, 3, 4};
                y = new double[]{2, 3, 2, 0.5};
            }

        } else {
            x = new double[]{111, 31, 55, 65, 14, 32, 105, 82, 130, 88, 28, 61, 65, 98};
            y = new double[]{12.4, 5.2, 5.5, 7.6, 1.6, 4.3, 9, 7.8, 10.5, 9.8, 2, 3.7, 3.5, 7.6};
        }
        SplineInterpolate s = new SplineInterpolate(new DotSeries(x, y));
    }

}
