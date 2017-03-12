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
        noOfInterpolatedPoints = noOfOriginalPoints * raster;
        coefficients = new double[noOfOriginalPoints - 1][4];
        isVisualizable = true;
        System.out.println("Spline");
        interpolate();
        generateInterpolatedData();
    }

    private void interpolate() {
        int noOfFunctions = noOfOriginalPoints - 1;
        int noOfCoefficients = noOfFunctions * 4;
        int index = 0;
        Matrix gm, m = new Matrix(noOfCoefficients, noOfCoefficients + 1);

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

        for (int i = 0; i < noOfFunctions - 1; i++) {
            m.setMatrix(i + 2 * noOfFunctions, index + 0, 3 * Math.pow(data.getPointX(i + 1), 2));
            m.setMatrix(i + 2 * noOfFunctions, index + 1, 2 * data.getPointX(i + 1));
            m.setMatrix(i + 2 * noOfFunctions, index + 2, 1);
            m.setMatrix(i + 2 * noOfFunctions, index + 4, -3 * Math.pow(data.getPointX(i + 1), 2));
            m.setMatrix(i + 2 * noOfFunctions, index + 5, -2 * data.getPointX(i + 1));
            m.setMatrix(i + 2 * noOfFunctions, index + 6, -1);
            m.setMatrix(i + 3 * noOfFunctions - 1, index + 0, 6 * data.getPointX(i + 1));
            m.setMatrix(i + 3 * noOfFunctions - 1, index + 1, 2);
            m.setMatrix(i + 3 * noOfFunctions - 1, index + 4, -6 * data.getPointX(i + 1));
            m.setMatrix(i + 3 * noOfFunctions - 1, index + 5, -2);
            index += 4;
        }

        m.setMatrix(noOfCoefficients - 2, 0, 6 * data.getPointX(0));
        m.setMatrix(noOfCoefficients - 2, 1, 2);
        m.setMatrix(noOfCoefficients - 1, (noOfFunctions - 1) * 4, 6 * data.getPointX(data.getPiecesOfPoints() - 1));
        m.setMatrix(noOfCoefficients - 1, (noOfFunctions - 1) * 4 + 1, 2);

        System.out.println("matrix:");
        m.getMatrixConsole();
        System.out.println("degauss matrix:");
        gm = m.getFullGauss();
        gm.getMatrixConsole();
        for (int i = 0; i < coefficients.length; i++) {
            for (int j = 0; j < 4; j++) {
                coefficients[i][j] = gm.getMatrix(4 * i + j, noOfCoefficients);
            }
        }
        System.out.println("coeffs");
        Matrix c = new Matrix(coefficients);
        c.getMatrixConsole();
    }

    private void generateInterpolatedData() {
        double min, max, step;
        double[] x = new double[noOfInterpolatedPoints + 1];
        double[] y = new double[noOfInterpolatedPoints + 1];
        int coeffIndex;
        min = data.getPointX(0);
        max = data.getPointX(noOfOriginalPoints - 1);
        step = (max - min) / noOfInterpolatedPoints;
        System.out.println("points");
        for (int i = 0; i <= noOfInterpolatedPoints; i++) {
            x[i] = min + i * step;
            coeffIndex = whichIsTheActualFunction(x[i]);
            for (int j = 0; j < 4; j++) {
                y[i] += coefficients[coeffIndex][3 - j] * Math.pow(x[i], j);
            }
            System.out.println(x[i] + "," + y[i]);
            if (x[i] > 10E16 || x[i] < -1 * 10E16 || y[i] > 10E16 || y[i] < -1 * 10E-16) {
                isVisualizable = false;
            }
        }

        if (isVisualizable) {
            interpolatedData = new DotSeries(x, y);
        } else {
            interpolatedData = new DotSeries(data.getPointX(), data.getPointY());
        }
    }

    private int whichIsTheActualFunction(double xValue) {
        double[] x = data.getPointX();
        int noOfFunction = 0;
        for (int i = 0; i < x.length - 1; i++) {
            if (x[i] <= xValue) {
                noOfFunction++;
            }
        }
        return noOfFunction - 1;
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
