package diagramvisualizer.view;

import diagramvisualizer.model.DotSeries;
import java.awt.Color;

/**
 *
 * @author Milán
 */
public class GraphToDraw {

    private DotSeries graf;
    private Color color;
    private boolean lines;
    private int type;
    private int size;
    private int lineSize;
    private String name;

    public GraphToDraw(DotSeries graf, String name, Color color, boolean lines, int type, int size, int lineSize) {
        this.graf = graf;
        this.name = name;
        this.color = color;
        this.lines = lines;
        this.type = type;
        this.size = size;
        this.lineSize = lineSize;
    }

    public DotSeries getGraf() {
        return graf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isLines() {
        return lines;
    }

    public void setLines(boolean lines) {
        this.lines = lines;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    @Override
    public String toString() {
        return name;
    }    
}
