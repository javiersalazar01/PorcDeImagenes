/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author javi_
 */
public class canvasCrear extends JComponent{
    
    @Override
    public void paintComponent( Graphics g ) {
          super.paintComponent(g);
          g.setColor(Color.BLUE);
          g.fillOval(20, 20, 200, 200);
    }
          
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
    
}
