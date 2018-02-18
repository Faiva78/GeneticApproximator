/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Approximator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author fauiva78
 */
public final class JDataPanelFunc extends JPanel {


    public Gene gene;
   // public DataSample sample ;
    public String function;
    private Image img;
    private int old_XX, old_test_Y, old_out_y,old_test1_Y;

    public JDataPanelFunc() {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (function != null) {

            Graphics2D g2d = (Graphics2D) g;
            //if (img == null) {
            img = createImage(this.getWidth(), this.getHeight());
            //}
            Graphics2D g2dBuffer = (Graphics2D) img.getGraphics();

            //vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
            
            
            double[][] res = gene.getFunctionData(function);
            double[][] res1 = gene.getFunctionData(gene.gg);

            for (int i = 0; i < gene.steps; i++) {

                // ics
                double test = res[0][i];
                int testYY = (int) (test * (this.getHeight()));

                // formula
                double out = res[1][i];
                int outYY = (int) (out * (this.getHeight()));

               
                // gene formula
                double out1 = res1[1][i];
                int out1YY = (int) (out1 * (this.getHeight()));


                int XX = (int) (((this.getWidth()) /  res[0].length) * i);

                g2dBuffer.setColor(Color.red);
                g2dBuffer.drawLine(old_XX, old_test_Y, XX, testYY);

                g2dBuffer.setColor(Color.blue);
                g2dBuffer.drawLine(old_XX, old_out_y, XX, outYY);

                g2dBuffer.setColor(Color.black);
                g2dBuffer.drawLine(old_XX, old_test1_Y, XX, out1YY);


                old_XX = XX;
                old_test_Y = testYY;
                old_out_y = outYY;
                old_test1_Y = out1YY;

            }

            old_XX = 0;
            old_out_y = 0;
            old_test_Y = 0;

            //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            g2d.drawImage(img, 0, 0, null);
        }
    }

}
