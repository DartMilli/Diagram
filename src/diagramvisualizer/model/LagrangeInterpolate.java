package diagramvisualizer.model;

/**
 *
 * @author Milán
 */
public class LagrangeInterpolate {

    private DotSeries data;
    private DotSeries interpolatedData;
    private int raster;
    private int noOfOriginalPoints;
    double[] coefficients;

    public LagrangeInterpolate(DotSeries data) {
        this.data = data.getSorted();
        noOfOriginalPoints = this.data.getPointX().length;
        raster = noOfOriginalPoints * 10;
        coefficients = new double[noOfOriginalPoints];
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
        gm = m.getFullGauss();
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = gm.getMatrix(i, noOfOriginalPoints);
        }
    }

    private void generateInterpolatedData() {
        double min, max, step;
        double[] x = new double[raster + 1];
        double[] y = new double[raster + 1];
        min = data.getPointX(0);
        max = data.getPointX(noOfOriginalPoints - 1);
        step = (max - min) / raster;
        for (int i = 0; i <= raster; i++) {
            x[i] = min + i * step;
            for (int j = 0; j < coefficients.length; j++) {
                y[i] += coefficients[j] * Math.pow(x[i], j);
            }
        }
        interpolatedData = new DotSeries(x, y);
    }

    public DotSeries getInterpolatedData() {
        return interpolatedData;
    }

    public static void main(String[] args) {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 3, 2, 0.5, 1.5};
        LagrangeInterpolate l = new LagrangeInterpolate(new DotSeries(x, y));
        l.interpolate();
        l.generateInterpolatedData();
    }

}
