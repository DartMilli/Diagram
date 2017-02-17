package diagramvisualizer.model;

import java.math.BigDecimal;

/**
 *
 * @author Milán
 */
public class LagrangeInterpolate {

    private DotSeries data;
    private DotSeries interpolatedData;   
    private int raster = 10;
    private int noOfOriginalPoints;
    private int noOfInterpolatedPoints;
    private double[] coefficients;
    private boolean isVisualizable;

    public LagrangeInterpolate(DotSeries data) {
        this.data = data.getSorted();
        noOfOriginalPoints = this.data.getPointX().length;
        noOfInterpolatedPoints = noOfOriginalPoints * raster;
        coefficients = new double[noOfOriginalPoints];
        isVisualizable = true;
        interpolate();
        generateInterpolatedData();
    }

    private void interpolate() {
        Matrix gm, m = new Matrix(noOfOriginalPoints, noOfOriginalPoints + 1);
        for (int i = 0; i < noOfOriginalPoints; i++) {
            for (int j = 0; j < noOfOriginalPoints; j++) {
                m.setMatrix(i, j, Math.pow(i + 1, j));
            }
            m.setMatrix(i, noOfOriginalPoints, data.getPointY(i));
        }
        System.out.println("matrix");
        m.getMatrixConsole();
        gm = m.getFullGauss();
        System.out.println("degauss matrix");
        gm.getMatrixConsole();
        System.out.println("Coeffs");
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = gm.getMatrix(i, noOfOriginalPoints);
            System.out.println(coefficients[i]);
        }
    }

    private void generateInterpolatedData() {
        double min, max, step;
        double[] x = new double[noOfInterpolatedPoints + 1];
        double[] y = new double[noOfInterpolatedPoints + 1];
        min = data.getPointX(0);
        max = data.getPointX(noOfOriginalPoints - 1);
        step = (max - min) / noOfInterpolatedPoints;
        System.out.println("points");
        for (int i = 0; i <= noOfInterpolatedPoints; i++) {
            x[i] = min + i * step;
            for (int j = 0; j < coefficients.length; j++) {
                y[i] += coefficients[j] * Math.pow(x[i], j);
            }
            System.out.println(x[i] + "," + y[i]);
            if (x[i] > 10E16 || x[i] < 10E-16 || y[i] > 10E16 || y[i] < 10E-16) {
                isVisualizable = false;
            }
        }
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
            x = new double[]{1, 2, 3, 4, 5};
            y = new double[]{2, 3, 2, 0.5, 1.5};
        } else {
            x = new double[]{111, 31, 55, 65, 14, 32, 105, 82, 130, 88, 28, 61, 65, 98};
            y = new double[]{12.4, 5.2, 5.5, 7.6, 1.6, 4.3, 9, 7.8, 10.5, 9.8, 2, 3.7, 3.5, 7.6};
        }
        LagrangeInterpolate l = new LagrangeInterpolate(new DotSeries(x, y));
    }

}
