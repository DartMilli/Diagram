package diagramvisualizer.model;

import diagramvisualizer.model.DotSeries;

/**
 *
 * @author Milán
 */
public class Regression {

    private DotSeries data;

    private Double sumData_X = 0.0;
    private Double sumData_Y = 0.0;
    private Double avgData_X = 0.0;
    private Double avgData_Y = 0.0;
    private Double sumDataXY = 0.0;
    private Double sumDataDXDY = 0.0;
    private Double sumDataSqr_X = 0.0;
    private Double sumDataSqr_Y = 0.0;
    private Double sumDerivative_X = 0.0;
    private Double sumDerivative_Y = 0.0;
    private Double sumDerivativeSqr_X = 0.0;
    private Double sumDerivativeSqr_Y = 0.0;
    private Double sumE2 = 0.0;
    private Double sumYHut = 0.0;

    private double slope;
    private double intersection;
    private double r2;
    private double stdFault;
    private double slopeFault;
    private double intersectionFault;

    private boolean isLinearDataCalculated;

    public Regression(DotSeries data) {
        this.data = data.getSorted();
        isLinearDataCalculated = false;
    }

    private void generateData() {
        double x2, y2, dx, dy, dx2, dy2, xy, dxdy, e2, yHut;
        for (int i = 0; i < data.getPiecesOfPoints(); i++) {
            sumData_X = sumData_X + data.getPointX(i);
            sumData_Y = sumData_Y + data.getPointY(i);
        }

        double pieces = data.getPiecesOfPoints();
        avgData_X = sumData_X / (pieces);
        avgData_Y = sumData_Y / (pieces);

        for (int i = 0; i < pieces; i++) {
            x2 = Math.pow(data.getPointX(i), 2);
            y2 = Math.pow(data.getPointY(i), 2);
            dx = data.getPointX(i) - avgData_X;
            dy = data.getPointY(i) - avgData_Y;
            dx2 = Math.pow(dx, 2);
            dy2 = Math.pow(dy, 2);
            xy = data.getPointX(i) * data.getPointY(i);
            dxdy = dx * dy;

            sumDataXY = sumDataXY += xy;
            sumDataDXDY += dxdy;
            sumDataSqr_X += x2;
            sumDataSqr_Y += y2;
            sumDerivative_X += dx;
            sumDerivative_Y += dy;
            sumDerivativeSqr_X += dx2;
            sumDerivativeSqr_Y += dy2;
        }

        //m = (sumDataXY - avgData_Y * sumData_X) / (sumDataSqr_X - avgData_X * sumData_X);
        slope = sumDataDXDY / sumDerivativeSqr_X;
        intersection = avgData_Y - avgData_X * slope;

        for (int i = 0; i < data.getPiecesOfPoints(); i++) {
            yHut = slope * data.getPointX(i) + intersection;
            e2 = Math.pow(data.getPointY(i) - yHut, 2);
            sumYHut += yHut;
            sumE2 += e2;
        }

        r2 = 1 - (sumE2 / sumDerivativeSqr_Y);
        stdFault = Math.pow(sumE2 / (double) ((data.getPiecesOfPoints() - data.getPoints().length)), 0.5);
        slopeFault = stdFault * Math.pow(1.0 / sumDerivativeSqr_X, 0.5);
        intersectionFault = stdFault * Math.pow((1.0 / (double) data.getPiecesOfPoints() + Math.pow(avgData_X, 2) / sumDerivativeSqr_X), 0.5);
    }

    private void calculateLinearRegresion() {
        if (!isLinearDataCalculated) {
            generateData();
            isLinearDataCalculated = true;
        }
    }

    public double getSlope() {
        calculateLinearRegresion();
        return slope;
    }

    public double getIntersection() {
        calculateLinearRegresion();
        return intersection;
    }

    public double getR2() {
        calculateLinearRegresion();
        return r2;
    }

    public double getStdFault() {
        calculateLinearRegresion();
        return stdFault;
    }

    public double getSlopeFault() {
        calculateLinearRegresion();
        return slopeFault;
    }

    public double getIntersectionFault() {
        calculateLinearRegresion();
        return intersectionFault;
    }

    public DotSeries getRegressionStraight() {
        calculateLinearRegresion();
        double[] x = new double[2];
        double[] y = new double[2];

        x[0] = data.getPointX(0);
        x[1] = data.getPointX(data.getPointX().length - 1);
        y[0] = slope * x[0] + intersection;
        y[1] = slope * x[1] + intersection;

        DotSeries out = new DotSeries(x, y);
        return out;
    }

    public static void main(String[] args) {

        double[] xPontok = new double[]{111, 31, 55, 65, 14, 32, 105, 82, 130, 88, 28, 61, 65, 98};
        double[] yPontok = new double[]{12.4, 5.2, 5.5, 7.6, 1.6, 4.3, 9, 7.8, 10.5, 9.8, 2, 3.7, 3.5, 7.6};

        DotSeries adatok = new DotSeries(xPontok, yPontok);

        Regression reg = new Regression(adatok);
        System.out.println("A meredekség: " + reg.getSlope());
        System.out.println("A tengelymetszet: " + reg.getIntersection());
        System.out.println("Az R2: " + reg.getR2());
        System.out.println("A standard hiba: " + reg.getStdFault());
        System.out.println("Az m hiba: " + reg.getSlopeFault());
        System.out.println("A b hiba: " + reg.getIntersectionFault());
    }

}
