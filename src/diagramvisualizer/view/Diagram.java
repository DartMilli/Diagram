package diagramvisualizer.view;

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
    private ArrayList<GraphToDraw> graphs;
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
    private int captionRounding = 2;
    private boolean drawRaster = true;
    private boolean drawCaption = true;
    private String captionType = "0000";

    public Diagram(Dimension dimension) {
        this.dimension = dimension;
        calculateZerus();
        graphs = new ArrayList<>();

        int dataType = 1;
        double[] xPoints, yPoints;
        if (dataType == 1) {
            xPoints = new double[]{1, 2, 3, 4, 5};
            yPoints = new double[]{2, 3, 2, 0.5, 1.5};
        } else {
            xPoints = new double[]{111, 31, 55, 65, 14, 32, 105, 82, 130, 88, 28, 61, 65, 98};
            yPoints = new double[]{12.4, 5.2, 5.5, 7.6, 1.6, 4.3, 9, 7.8, 10.5, 9.8, 2, 3.7, 3.5, 7.6};
        } 
        DotSeries data = new DotSeries(xPoints, yPoints);

        addGraph(new GraphToDraw(data.getSorted(), "data", Color.red, false, 1, 4, 4));

        setAutoAxesUnits();
    }

    public void addGraph(GraphToDraw graf) {
        for (int i = 0; i < graphs.size(); i++) {
            if (graf.getName().equals(graphs.get(i).getName())) {
                graf.setName(graf.getName()+"_2");
            }            
        }
        graphs.add(graf);
    }

    public void removeGraph(int index) {
        if (index >= 0 && index < graphs.size()) {
            graphs.remove(index);
        }
    }

    public void removeGraph(GraphToDraw graf) {
        graphs.remove(graf);
    }

    public GraphToDraw[] getGraphs() {
        GraphToDraw[] out = new GraphToDraw[graphs.size()];
        return graphs.toArray(out);
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
        factorX = dimension.getWidth() / (maxX - minX);
        zerusX = -1 * minX * factorX;
        factorY = dimension.getHeight() / (maxY - minY);
        zerusY = maxY * factorY;
        repaint();
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
        for (double i = 0; i < maxX; i += rasterX) {
            g.drawLine(calculateXCoordinate(i), 0, calculateXCoordinate(i), dimension.height);
        }
        for (double i = 0; i > minX; i -= rasterX) {
            g.drawLine(calculateXCoordinate(i), 0, calculateXCoordinate(i), dimension.height);
        }
        for (double i = 0; i < maxY; i += rasterY) {
            g.drawLine(0, calculateYCoordinate(i), dimension.width, calculateYCoordinate(i));
        }
        for (double i = 0; i > minY; i -= rasterY) {
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
            length = g.getFontMetrics().stringWidth(Double.toString(Math.round(
                    i * raster * Math.pow(10, captionRounding) / Math.pow(10, captionRounding))
            ));
            g.drawLine(calculateXCoordinate(i * raster),
                    (int) zerusY - line,
                    calculateXCoordinate(i * raster),
                    (int) zerusY + line);
            g.drawString(Double.toString(Math.round(i * raster * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding)),
                    calculateXCoordinate(Math.round(i * raster * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding)) - length / 2,
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
            length = leftOrRight ? -8 : g.getFontMetrics().stringWidth(Double.toString(Math.round(
                    i * raster * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding))) + 8;
            g.drawLine((int) zerusX - line,
                    calculateYCoordinate(i * raster),
                    (int) zerusX + line,
                    calculateYCoordinate(i * raster));
            g.drawString(Double.toString(Math.round(i * raster * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding)),
                    (int) zerusX - length,
                    calculateYCoordinate(Math.round(i * raster * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding)) + size / 2 - 2);
        }
    }

    private void drawCaption(Graphics g, Color c) {
        g.setColor(c);
        int xPlusType = Character.getNumericValue(captionType.charAt(0));
        int xMinusType = Character.getNumericValue(captionType.charAt(1));
        int yPlusType = Character.getNumericValue(captionType.charAt(2));
        int yMinusType = Character.getNumericValue(captionType.charAt(3));
        switch (xPlusType) {
            case 0:
                break;
            case 1:
                drawCaptionX(g, c, false, true);
                break;
            case 2:
                drawCaptionX(g, c, true, true);
                break;
        }
        switch (xMinusType) {
            case 0:
                break;
            case 1:
                drawCaptionX(g, c, false, false);
                break;
            case 2:
                drawCaptionX(g, c, true, false);
                break;
        }
        switch (yPlusType) {
            case 0:
                break;
            case 1:
                drawCaptionY(g, c, true, false);
                break;
            case 2:
                drawCaptionY(g, c, true, true);
                break;
        }
        switch (yMinusType) {
            case 0:
                break;
            case 1:
                drawCaptionY(g, c, false, false);
                break;
            case 2:
                drawCaptionY(g, c, false, true);
                break;
        }
    }

    private void drawNumbers(Graphics g, GraphToDraw graphsToDraw) {
        if (graphs.contains(graphsToDraw) && graphsToDraw.isNumbers()) {
            g.setColor(graphsToDraw.getColor());
            String s;
            for (int i = 0; i < graphsToDraw.getGraf().getPiecesOfPoints(); i++) {
                s = "("
                        + Math.round(graphsToDraw.getGraf().getPointX(i) * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding)
                        + ";"
                        + Math.round(graphsToDraw.getGraf().getPointY(i) * Math.pow(10, captionRounding)) / Math.pow(10, captionRounding)
                        + ")";
                g.drawString(s,
                        calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)),
                        calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)));
            }
        }
    }

    private void drawGraph(Graphics g, GraphToDraw graphsToDraw) {
        g.setColor(graphsToDraw.getColor());
        for (int i = 0; i < graphsToDraw.getGraf().getPiecesOfPoints(); i++) {
            switch (graphsToDraw.getType()) {
                case 1:
                    g.fillOval(
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) - graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) - graphsToDraw.getSize(),
                            2 * graphsToDraw.getSize(),
                            2 * graphsToDraw.getSize());
                    break;
                case 2:
                    g.drawLine(
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) - graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) - graphsToDraw.getSize(),
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) + graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) + graphsToDraw.getSize());
                    g.drawLine(
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) - graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) + graphsToDraw.getSize(),
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) + graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) - graphsToDraw.getSize());
                    break;
                case 3:
                    g.drawLine(
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) - graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)),
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)) + graphsToDraw.getSize(),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)));
                    g.drawLine(
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) + graphsToDraw.getSize(),
                            calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)),
                            calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)) - graphsToDraw.getSize());
                    break;
            }
            if (graphsToDraw.isLines()) {
                if (i < graphsToDraw.getGraf().getPiecesOfPoints() - 1) {
                    if (graphsToDraw.getLineSize() < 1) {
                        g.drawLine(
                                calculateXCoordinate(graphsToDraw.getGraf().getPointX(i)),
                                calculateYCoordinate(graphsToDraw.getGraf().getPointY(i)),
                                calculateXCoordinate(graphsToDraw.getGraf().getPointX(i + 1)),
                                calculateYCoordinate(graphsToDraw.getGraf().getPointY(i + 1)));
                    } else {
                        int x1, x2, y1, y2;
                        x1 = calculateXCoordinate(graphsToDraw.getGraf().getPointX(i));
                        x2 = calculateXCoordinate(graphsToDraw.getGraf().getPointX(i + 1));
                        y1 = calculateYCoordinate(graphsToDraw.getGraf().getPointY(i));
                        y2 = calculateYCoordinate(graphsToDraw.getGraf().getPointY(i + 1));
                        double dx = (double)(x2 - x1);
                        double dy = (double)(y2 - y1);
                        double angle = Math.atan2(dy, dx);
                        int realLength = (int) Math.sqrt(dx * dx + dy * dy);
                        g.fillOval(x1 - graphsToDraw.getLineSize(),
                                y1 - graphsToDraw.getLineSize(),
                                2 * graphsToDraw.getLineSize(),
                                2 * graphsToDraw.getLineSize());
                        g.fillOval(x2 - graphsToDraw.getLineSize(),
                                y2 - graphsToDraw.getLineSize(),
                                2 * graphsToDraw.getLineSize(),
                                2 * graphsToDraw.getLineSize());
                        Graphics2D g2D = (Graphics2D) g.create();
                        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
                        at.concatenate(AffineTransform.getRotateInstance(angle));
                        g2D.transform(at);
                        g2D.fillRect(0,
                                -graphsToDraw.getLineSize(),
                                realLength,
                                2 * graphsToDraw.getLineSize());
                    }
                }
            }
        }
    }

    public void setAutoAxesUnits() {
        double xMax = 0.0, xMin = 0.0, yMax = 0.0, yMin = 0.0, x, y;
        int pieces;
        if (graphs.size() > 0) {
            for (int i = 0; i < graphs.size(); i++) {
                pieces = graphs.get(i).getGraf().getPiecesOfPoints();
                for (int j = 0; j < pieces; j++) {
                    x = graphs.get(i).getGraf().getPointX(j);
                    y = graphs.get(i).getGraf().getPointY(j);
                    if (x < xMin) {
                        xMin = x;
                    } else if (x > xMax) {
                        xMax = x;
                    }
                    if (y < yMin) {
                        yMin = y;
                    } else if (y > yMax) {
                        yMax = y;
                    }
                }
            }
            maxX = Math.round(xMax + 1);
            minX = Math.round(xMin - 1);
            maxY = Math.round(yMax + 1);
            minY = Math.round(yMin - 1);
            rasterX = (maxX - minX) / 10;
            rasterY = (maxY - minY) / 10;
        } else {
            maxX = 10;
            minX = -10;
            maxY = 10;
            minY = -10;
            rasterX = (maxX - minX) / 10;
            rasterY = (maxY - minY) / 10;
        }
        calculateZerus();
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        if (maxX > minX) {
            this.maxX = maxX;
        }
        calculateZerus();
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        if (maxY > minY) {
            this.maxY = maxY;
        }
        calculateZerus();
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        if (minX < maxX) {
            this.minX = minX;
        }
        calculateZerus();
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        if (minY < maxY) {
            this.minY = minY;
        }
        calculateZerus();
    }

    public double getRasterX() {
        return rasterX;
    }

    public void setRasterX(double rasterX) {
        this.rasterX = rasterX;
        calculateZerus();
    }

    public double getRasterY() {
        return rasterY;
    }

    public void setRasterY(double rasterY) {
        this.rasterY = rasterY;
        calculateZerus();
    }

    public boolean isDrawRaster() {
        return drawRaster;
    }

    public void setDrawRaster(boolean drawRaster) {
        this.drawRaster = drawRaster;
        repaint();
    }

    public boolean isDrawCaption() {
        return drawCaption;
    }

    public void setDrawCaption(boolean drawCaption) {
        this.drawCaption = drawCaption;
        repaint();
    }

    public String getCaptionType() {
        return captionType;
    }

    public void setCaptionType(String captionType) {
        if (captionType.equals("0000")) {
            drawCaption = false;
        } else {
            drawCaption = true;
        }
        this.captionType = captionType;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dimension.width, dimension.height);
        if (drawRaster) {
            drawRaster(g, Color.LIGHT_GRAY);
        }
        drawCoordinateAxes(g, Color.BLACK);
        drawDiagramFrame(g, Color.BLACK);
        if (drawCaption) {
            drawCaption(g, Color.BLACK);
        }
        for (int i = 0; i < graphs.size(); i++) {
            drawNumbers(g, graphs.get(i));
            drawGraph(g, graphs.get(i));
        }
    }
}
