package group9.belaya;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import javax.swing.JPanel;

public class GraphicsDisplay extends JPanel {

    private Double[][] graphicsData;// Список координат точек для построения графика

    private boolean showAxis = false;
    private boolean showMarkers = false;
    private boolean showMinMax = false;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;// Используемый масштаб отображения
    private Font axisFont;// Шрифт отображения подписей к осям координат

    // Различные стили черчения линий
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;

    public GraphicsDisplay () {
        setBackground(Color.WHITE);// Цвет заднего фона области отображения
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[]{10,4,10,4,10,4,4,4,4,4,4,4} , 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    // Методы-модификаторы для изменения параметров отображения графика
    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }
    public void setShowMinMax(boolean showMinMax){
        this.showMinMax = showMinMax;
        repaint();
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke); //Линия для рисования графика
        canvas.setColor(Color.BLUE);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i ++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0)
                graphics.lineTo(point.getX(), point.getY());
            else
                graphics.moveTo(point.getX(), point.getY());
        }
        canvas.draw(graphics);
    }

    protected void paintMinMax(Graphics2D canvas){
        Point2D.Double zero = new Point2D.Double(0,0);
        zero = xyToPoint(0,0);
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.red);
        canvas.setPaint(Color.red);
        Ellipse2D.Double marker = new Ellipse2D.Double();
        Point2D.Double corner = shiftPoint(zero, 5, 5);
        marker.setFrameFromCenter(zero, corner);
        canvas.draw(marker);
        canvas.fill(marker);

        Font myFont = new Font("Upi",Font.BOLD, 20);
        canvas.setFont(myFont);
        FontRenderContext context = canvas.getFontRenderContext();
        Rectangle2D bounds = myFont.getStringBounds("(0,0)",context);
        Point2D.Double labelPos = xyToPoint(0,0);
        canvas.drawString("(0,0)", (float)labelPos.getX()+5, (float)(labelPos.getY()-bounds.getY()));

        double maxFunction = graphicsData[0][0];
        double minFunction = 0;
        for(int i=0; i < graphicsData.length; i++) {
            if(graphicsData[i][1] > maxFunction)
                maxFunction = graphicsData[i][1];
            if(graphicsData[i][1] < minFunction)
                minFunction = graphicsData[i][1];
        }
        Point2D.Double maxFunc = xyToPoint(0,maxFunction);
        corner = shiftPoint(maxFunc, 5, 5);
        marker.setFrameFromCenter(maxFunc, corner);
        canvas.draw(marker);
        canvas.fill(marker);
        Point2D.Double minFunc = xyToPoint(0,minFunction);
        corner = shiftPoint(minFunc, 5, 5);
        marker.setFrameFromCenter(minFunc, corner);
        canvas.draw(marker);
        canvas.fill(marker);

        labelPos = xyToPoint(0,maxFunction);
        canvas.drawString("(0,"+maxFunction+")",(float)labelPos.getX()+5, (float)(labelPos.getY()-bounds.getY()));
        labelPos = xyToPoint(0,minFunction);
        canvas.drawString("(0,"+minFunction+")",(float)labelPos.getX()+5, (float)(labelPos.getY()-bounds.getY()));

        double maxArgument = graphicsData[0][0];
        double minArgument = graphicsData[0][0];
        for(int i=0; i<graphicsData.length; i++){
            if(graphicsData[i][0]>maxArgument)
                maxArgument = graphicsData[i][0];
            if(graphicsData[i][0]<minArgument)
                minArgument = graphicsData[i][0];
        }
        Point2D.Double maxArg = xyToPoint(maxArgument, 0);
        corner = shiftPoint(maxArg, 5, 5);
        marker.setFrameFromCenter(maxArg, corner);
        canvas.draw(marker);
        canvas.fill(marker);
        Point2D.Double minArg = xyToPoint(minArgument, 0);
        corner = shiftPoint(minArg, 5, 5);
        marker.setFrameFromCenter(minArg, corner);
        canvas.draw(marker);
        canvas.fill(marker);

        labelPos = xyToPoint(maxArgument,0);
        canvas.drawString("("+maxArgument+",0)",(float)labelPos.getX()+5, (float)(labelPos.getY()-bounds.getY()));
        labelPos = xyToPoint(minArgument,0);
        canvas.drawString("("+minArgument+",0)",(float)labelPos.getX()+5, (float)(labelPos.getY()-bounds.getY()));

    }

    //отображения осей координат
    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.darkGray); //оси
        canvas.setPaint(Color.DARK_GRAY); //стрелки
        canvas.setFont(axisFont); //шрифт подписи
        FontRenderContext context = canvas.getFontRenderContext();

        if (minX <= 0.0 && maxX >= 0.0){
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
        }

        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0.0), xyToPoint(maxX, 0.0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0.0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
        }
    }

    //отображение маркеров точек графика
    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        for (Double[] point: graphicsData) {
            double y = point[0];
            int y_int = (int)y;
            if((y_int%2)==0){
                canvas.setPaint(Color.blue);
            }
            else canvas.setPaint(Color.yellow);

            Ellipse2D.Double marker = new Ellipse2D.Double();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            Point2D.Double corner = shiftPoint(center, 5, 5);
            marker.setFrameFromCenter(center, corner);

            GeneralPath marker1 = new GeneralPath();
            marker1.moveTo(center.getX()+5, center.getY());
            marker1.lineTo(center.getX()-5,center.getY());
            marker1.moveTo(center.getX(), center.getY()+5);
            marker1.lineTo(center.getX(), center.getY()-5);
            marker1.closePath();
            canvas.draw(marker);
            //canvas.fill(marker);
            canvas.draw(marker1);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        for (int i = 0; i < graphicsData.length; i ++) {
            if (graphicsData[i][1] < minY)
                minY = graphicsData[i][1];
            if (graphicsData[i][1] > maxY)
                maxY = graphicsData[i][1];
        }

        double scaleX = getSize().getWidth()/(maxX - minX);
        double scaleY = getSize().getHeight()/(maxY - minY);

        scale = Math.min(scaleX, scaleY);

        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight()/scale - (maxY - minY))/2;
            maxY += yIncrement;
            minY -= yIncrement;
        }

        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth()/scale - (maxX - minX))/2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Font oldFont = canvas.getFont();
        Paint oldPaint = canvas.getPaint();

        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        if (showMinMax) paintMinMax(canvas);

        canvas.setColor(oldColor);
        canvas.setFont(oldFont);
        canvas.setStroke(oldStroke);
        canvas.setPaint(oldPaint);
    }

}
