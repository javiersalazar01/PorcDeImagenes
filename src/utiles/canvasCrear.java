package utiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 *
 * @author javi_
 */
public class canvasCrear extends JComponent {

    Boolean dCirculo = false;
    Boolean dCuadrado = false;
    Boolean dGrises = false;
    Boolean dColores = false;

    public void paint(String dibujo) {
        switch (dibujo) {
            case "circulo":
                dCirculo = true;
                break;
            case "cuadrado":
                dCuadrado = true;
                break;
            case "grises":
                dGrises = true;
                break;
            case "colores":
                dColores = true;
                break;
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        if (dCirculo) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, 200, 200);

        } else if (dCuadrado) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 200, 200);
            
        } else if (dGrises) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, Color.BLACK, 200,
                    200, Color.WHITE, false));
            Rectangle r = new Rectangle(0, 0, 200, 200);
            g2.fill(r);
            
        } else if (dColores){
            super.paintComponent(g);
        
        final java.awt.Graphics2D G2 = (Graphics2D) g.create();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        final Color UPPER_LEFT = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        final Color UPPER_RIGHT = new Color(1.0f, 1.0f, 0.0f, 1.0f);
        final Color LOWER_LEFT = new Color(0.0f, 0.0f, 1.0f, 1.0f);
        final Color LOWER_RIGHT = new Color(0.0f, 1.0f, 1.0f, 1.0f);
        
        final Rectangle RECT = new Rectangle(0, 0, 400, 400);
        
        BiLinearGradientPaint BILINEAR_GRADIENT = new BiLinearGradientPaint(RECT, UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT);
        
        G2.setPaint(BILINEAR_GRADIENT);
        G2.fill(RECT);
        
        G2.dispose();
        
            
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

}
