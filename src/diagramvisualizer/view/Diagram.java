package diagramvisualizer.view;

import diagramvisualizer.model.Regression;
import diagramvisualizer.model.DotSeries;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Milán
 */
public class Diagram extends JPanel {

    private Dimension dimension;
    private ArrayList<DotSeries> graphs;
    private double maxX = 200;
    private double maxY = 20.0;
    private double minX = -20;
    private double minY = -2.0;
    private double rasterX = 20;
    private double rasterY = 2;
    private double zerusX;
    private double zerusY;
    private double factorX;
    private double factorY;

    public Diagram(Dimension dimension) {
        this.dimension = dimension;
        calculateZerus();
        graphs = new ArrayList<>();
        double[] xPoints = new double[]{111, 31, 55, 65, 14, 32, 105, 82, 130, 88, 28, 61, 65, 98};
        double[] yPoints = new double[]{12.4, 5.2, 5.5, 7.6, 1.6, 4.3, 9, 7.8, 10.5, 9.8, 2, 3.7, 3.5, 7.6};
        DotSeries data = new DotSeries(xPoints, yPoints);
        DotSeries sortedData = data.getSorted();
        Regression regression = new Regression(sortedData);
        DotSeries trend = new DotSeries(
                new double[]{
                    sortedData.getPointX(0),
                    sortedData.getPointX(sortedData.getPiecesOfPoints() - 1)
                },
                new double[]{
                    regression.getSlope() * sortedData.getPointX(0) + regression.getIntersection(),
                    regression.getSlope() * sortedData.getPointX(sortedData.getPiecesOfPoints() - 1) + regression.getIntersection()
                });
        addGraph(sortedData);
        addGraph(trend);
    }

    public void addGraph(DotSeries graf) {
        graphs.add(graf);
    }

    public void removeGraph(int index) {
        if (index >= 0 && index < graphs.size()) {
            graphs.remove(index);
        }
    }

    public void removeGraph(DotSeries graf) {
        graphs.remove(graf);
    }

    private int calculateXCoordinate(double x) {
        int out = (int) (zerusX + x * factorX);
        return out;
    }

    private int calculateYCoordinate(double y) {
        int out = (int) (zerusY - y * factorY);
        return out;
    }

    private void calculateZerus() {
        factorX = dimension.getWidth() / (maxX + Math.abs(minX));
        zerusX = -1 * minX * factorX;
        factorY = dimension.getHeight() / (maxY - minY);
        zerusY = maxY * factorY;
    }

    private void drawCoordinateAxes(Graphics g, Color c) {
        g.setColor(c);
        g.drawLine((int) zerusX, 0, (int) zerusX, dimension.height);
        g.drawLine(0, (int) zerusY, dimension.width, (int) zerusY);
    }

    private void drawDiagramFrame(Graphics g, Color c) {
        int shift = 1;
        g.setColor(c);
        g.drawLine(0, 0, dimension.width, 0);
        g.drawLine(0, 0, 0, dimension.height);
        g.drawLine(0, dimension.height - shift, dimension.width, dimension.height - shift);
        g.drawLine(dimension.width - shift, 0, dimension.width - shift, dimension.height);
    }

    private void drawRaster(Graphics g, Color c) {
        g.setColor(c);
        for (int i = 0; i < maxX; i += rasterX) {
            g.drawLine(calculateXCoordinate(i), 0, calculateXCoordinate(i), dimension.height);
        }
        for (int i = 0; i > minX; i -= rasterX) {
            g.drawLine(calculateXCoordinate(i), 0, calculateXCoordinate(i), dimension.height);
        }
        for (int i = 0; i < maxY; i += rasterY) {
            g.drawLine(0, calculateYCoordinate(i), dimension.width, calculateYCoordinate(i));
        }
        for (int i = 0; i > minY; i -= rasterY) {
            g.drawLine(0, calculateYCoordinate(i), dimension.width, calculateYCoordinate(i));
        }
    }

    private void drawCaptionX(Graphics g, Color c, boolean upOrDown, boolean leftOrRight) {
        g.setColor(c);
        int length;
        double raster, rate, ending;
        int line = upOrDown ? 5 : -5;
        int size = upOrDown ? g.getFont().getSize() : -2;
        ending = leftOrRight ? maxX : minX;
        raster = leftOrRight ? rasterX : -1 * rasterX;
        rate = ending / raster;
        for (int i = 0; i < rate; i++) {
            length = g.getFontMetrics().stringWidth(Double.toString(i * raster));
            g.drawLine(calculateXCoordinate(i * raster),
                    (int) zerusY - line,
                    calculateXCoordinate(i * raster),
                    (int) zerusY + line);
            g.drawString(Double.toString(i * raster),
                    calculateXCoordinate(i * raster) - length / 2,
                    (int) zerusY + size + line);
        }
    }

    private void drawCaptionY(Graphics g, Color c, boolean upOrDown, boolean leftOrRight) {
        g.setColor(c);
        int length;
        double ending, raster, rate;
        int line = upOrDown ? 5 : -5;
        int size = g.getFont().getSize();
        ending = upOrDown ? maxY : minY;
        raster = upOrDown ? rasterY : -1 * rasterY;
        rate = ending / raster;
        for (int i = 0; i < rate; i++) {
            length = leftOrRight ? -8 : g.getFontMetrics().stringWidth(Double.toString(i * raster)) + 8;
            g.drawLine((int) zerusX - line,
                    calculateYCoordinate(i * raster),
                    (int) zerusX + line,
                    calculateYCoordinate(i * raster));
            g.drawString(Double.toString(i * raster),
                    (int) zerusX - length,
                    calculateYCoordinate(i * raster) + size / 2 - 2);
        }
    }

    private void drawCaption(Graphics g, Color c, int type) {
        g.setColor(c);
        switch (type) {
            case 2:
                drawCaptionX(g, c, true, true);
                drawCaptionX(g, c, true, false);
                drawCaptionY(g, c, true, true);
                drawCaptionY(g, c, false, true);
                break;
            case 3:
                drawCaptionX(g, c, false, true);
                drawCaptionX(g, c, false, false);
                drawCaptionY(g, c, true, false);
                drawCaptionY(g, c, false, false);
                break;
            case 1:
            default:
                drawCaptionX(g, c, true, true);
                drawCaptionX(g, c, false, false);
                drawCaptionY(g, c, true, true);
                drawCaptionY(g, c, false, false);
                break;
        }
    }

    private void drawNumbers(Graphics g, Color c, DotSeries dotSeries) {
        if (graphs.contains(dotSeries)) {
            g.setColor(c);
            String s;
            for (int i = 0; i < dotSeries.getPiecesOfPoints(); i++) {
                s = "(" + dotSeries.getPointX(i) + ";" + dotSeries.getPointY(i) + ")";
                g.drawString(s,
                        calculateXCoordinate(dotSeries.getPointX(i)),
                        calculateYCoordinate(dotSeries.getPointY(i)));
            }
        }
    }

    private void drawGraph(
            Graphics g,
            DotSeries graf,
            Color c,
            boolean lines,
            int type,
            int size,
            int lineSize
    ) {
        g.setColor(c);
        for (int i = 0; i < graf.getPiecesOfPoints(); i++) {
            switch (type) {
                case 2:
                    g.drawLine(
                            calculateXCoordinate(graf.getPointX(i)) - size,
                            calculateYCoordinate(graf.getPointY(i)) - size,
                            calculateXCoordinate(graf.getPointX(i)) + size,
                            calculateYCoordinate(graf.getPointY(i)) + size);
                    g.drawLine(
                            calculateXCoordinate(graf.getPointX(i)) - size,
                            calculateYCoordinate(graf.getPointY(i)) + size,
                            calculateXCoordinate(graf.getPointX(i)) + size,
                            calculateYCoordinate(graf.getPointY(i)) - size);
                    break;
                case 3:
                    g.drawLine(
                            calculateXCoordinate(graf.getPointX(i)) - size,
                            calculateYCoordinate(graf.getPointY(i)),
                            calculateXCoordinate(graf.getPointX(i)) + size,
                            calculateYCoordinate(graf.getPointY(i)));
                    g.drawLine(
                            calculateXCoordinate(graf.getPointX(i)),
                            calculateYCoordinate(graf.getPointY(i)) + size,
                            calculateXCoordinate(graf.getPointX(i)),
                            calculateYCoordinate(graf.getPointY(i)) - size);
                    break;
                case 1:
                default:
                    g.fillOval(
                            calculateXCoordinate(graf.getPointX(i)) - size,
                            calculateYCoordinate(graf.getPointY(i)) - size, 2 * size, 2 * size);
                    break;
            }
            if (lines) {
                if (i < graf.getPiecesOfPoints() - 1) {
                    if (lineSize <= 1) {
                        g.drawLine(
                                calculateXCoordinate(graf.getPointX(i)),
                                calculateYCoordinate(graf.getPointY(i)),
                                calculateXCoordinate(graf.getPointX(i + 1)),
                                calculateYCoordinate(graf.getPointY(i + 1)));
                    } else {
                        int x1, x2, y1, y2;
                        x1 = calculateXCoordinate(graf.getPointX(i));
                        x2 = calculateXCoordinate(graf.getPointX(i + 1));
                        y1 = calculateYCoordinate(graf.getPointY(i));
                        y2 = calculateYCoordinate(graf.getPointY(i + 1));
                        double dx = x2 - x1;
                        double dy = y2 - y1;
                        double angle = Math.atan2(dy, dx);
                        int realLength = (int) Math.sqrt(dx * dx + dy * dy);
                        g.fillOval(x1 - lineSize, y1 - lineSize, 2 * lineSize, 2 * lineSize);
                        g.fillOval(x2 - lineSize, y2 - lineSize, 2 * lineSize, 2 * lineSize);
                        Graphics2D g2D = (Graphics2D) g.create();
                        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
                        at.concatenate(AffineTransform.getRotateInstance(angle));
                        g2D.transform(at);
                        g2D.fillRect(0, -lineSize, realLength, 2 * lineSize);

                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dimension.width, dimension.height);
        drawRaster(g, Color.LIGHT_GRAY);
        drawCoordinateAxes(g, Color.BLACK);
        drawDiagramFrame(g, Color.BLACK);
        drawCaption(g, Color.BLACK, 1);
//        for (int i = 0; i < graphs.size(); i++) {
//            drawGraph(g, graphs.get(i), Color.red, true, 1, 5, true);
//        }
        drawNumbers(g, Color.red, graphs.get(0));
        drawGraph(g, graphs.get(0), Color.red, false, 1, 4, 4);
        drawGraph(g, graphs.get(1), Color.BLACK, true, 1, 0, 2);
    }
}
