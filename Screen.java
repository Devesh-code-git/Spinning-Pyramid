import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.util.Arrays;

public class Screen extends JPanel implements ActionListener{
    int d = 600;
    Color neon_blue = new Color(31, 81, 255);
    Color neon_red = new Color(255, 49, 49);
    Color neon_green = new Color(57, 255, 20);
    Color neon_orange = new Color(255, 81, 31);
    Color dark_grey = new Color(36, 36, 36);
    double[][] base_points = {{0, 200, 0},      // P1
                              {-200, -100, 0},  // P2 
                              {0, -100, 200},   // P3
                              {200, -100, 0},   // P4
                              {0, -100, -200}}; // P5
    double[][] points = new double[5][3];
    double A = 0.01;
    double B = 0.02;
    double C = 0.015;
    int screen_size = 800;
    Timer timer;
    double d1, d2, d3, d4, d5, face1_total, face2_total, face3_total, face4_total, face5_total;
    double[] order = {0, 0, 0, 0, 0};

    public static void main(String[] args) {
        new Frame();
    }

    public Screen() {
        this.setPreferredSize(new Dimension(screen_size, screen_size));
        this.setBackground(dark_grey);
        this.setLayout(null);

        timer = new Timer(20, this);
        timer.start();
    }

    // Rotate about x-axis
    public void rotate_X(double[] P) {
        double x = P[0];
        double y = P[1]*Math.cos(A) - P[2]*Math.sin(A);
        double z = P[1]*Math.sin(A) + P[2]*Math.cos(A);

        P[0] = x;
        P[1] = y;
        P[2] = z;
    }

    // Rotate about y-axis
    public void rotate_Y(double[] P) {
        double x = P[0]*Math.cos(B) + P[2]*Math.sin(B);
        double y = P[1];
        double z = -P[0]*Math.sin(B) + P[2]*Math.cos(B);

        P[0] = x;
        P[1] = y;
        P[2] = z;
    }

    // Rotate about z-axis
    public void rotate_Z(double[] P) {
        double x = P[0]*Math.cos(C) - P[1]*Math.sin(C);
        double y = P[0]*Math.sin(C) + P[1]*Math.cos(C);
        double z = P[2];

        P[0] = x;
        P[1] = y;
        P[2] = z;
    }

    public int update_coordinates(double n, double z) {
        return (int)((n * d) / (d - z)); // Focal length formula
    }

    // Calculating distance from refrence point to each point on the pyramid
    public void distance(double[] P1, double[] P2, double[] P3, double[] P4, double[] P5) {
        d1 = Math.sqrt(Math.pow((0 - P1[0]), 2) + Math.pow((0 - P1[1]), 2) + Math.pow((1200 - P1[2]), 2));
        d2 = Math.sqrt(Math.pow((0 - P2[0]), 2) + Math.pow((0 - P2[1]), 2) + Math.pow((1200 - P2[2]), 2));
        d3 = Math.sqrt(Math.pow((0 - P3[0]), 2) + Math.pow((0 - P3[1]), 2) + Math.pow((1200 - P3[2]), 2));
        d4 = Math.sqrt(Math.pow((0 - P4[0]), 2) + Math.pow((0 - P4[1]), 2) + Math.pow((1200 - P4[2]), 2));
        d5 = Math.sqrt(Math.pow((0 - P5[0]), 2) + Math.pow((0 - P5[1]), 2) + Math.pow((1200 - P5[2]), 2));

        face1_total = (d1 + d3 + d4) / 3;
        face2_total = (d1 + d3 + d2) / 3;
        face3_total = (d1 + d5 + d2) / 3;
        face4_total = (d1 + d5 + d4) / 3;
        face5_total = (d2 + d3 + d4 + d5) / 4;

        order[0] = face1_total;
        order[1] = face2_total;
        order[2] = face3_total;
        order[3] = face4_total;
        order[4] = face5_total;

        Arrays.sort(order);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);
        g2D.translate(getWidth() / 2, getHeight() / 2);
        g2D.scale(1, -1);

        for (int i = order.length - 1; i >= 0; i--) {
            if (order[i] == face1_total) {
                g2D.setColor(neon_green);
                Path2D face1 = new Path2D.Double();

                face1.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face1.lineTo(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));  
                face1.lineTo(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2])); 

                face1.closePath(); // Drawing face
                g2D.fill(face1);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));

                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));

                g2D.drawLine(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]), 
                update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));
            } else if (order[i] == face2_total) {
                g2D.setColor(neon_red);
                Path2D face2 = new Path2D.Double();

                face2.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face2.lineTo(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));  
                face2.lineTo(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));

                face2.closePath(); // Drawing face
                g2D.fill(face2);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));

                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));

                g2D.drawLine(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]), 
                update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));
            } else if (order[i] == face3_total) {
                g2D.setColor(neon_blue);
                Path2D face3 = new Path2D.Double();

                face3.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face3.lineTo(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));  
                face3.lineTo(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));

                face3.closePath(); // Drawing face
                g2D.fill(face3);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));

                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));

                g2D.drawLine(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]), 
                update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));
            } else if (order[i] == face4_total) {
                g2D.setColor(neon_orange);
                Path2D face4 = new Path2D.Double();

                face4.moveTo(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]));   
                face4.lineTo(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));  
                face4.lineTo(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));

                face4.closePath(); // Drawing face
                g2D.fill(face4);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));

                g2D.drawLine(update_coordinates(points[0][0], points[0][2]), update_coordinates(points[0][1], points[0][2]), 
                update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));

                g2D.drawLine(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]), 
                update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));
            } else if (order[i] == face5_total) {
                g2D.setColor(Color.YELLOW);
                Path2D face5 = new Path2D.Double();

                face5.moveTo(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));   
                face5.lineTo(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));  
                face5.lineTo(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));
                face5.lineTo(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));  

                face5.closePath(); // Drawing face
                g2D.fill(face5);

                g2D.setColor(Color.WHITE);
                g2D.setStroke(new BasicStroke(3));

                // Drawing outline
                g2D.drawLine(update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]), 
                update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]));

                g2D.drawLine(update_coordinates(points[2][0], points[2][2]), update_coordinates(points[2][1], points[2][2]), 
                update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]));

                g2D.drawLine(update_coordinates(points[3][0], points[3][2]), update_coordinates(points[3][1], points[3][2]), 
                update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]));

                g2D.drawLine(update_coordinates(points[4][0], points[4][2]), update_coordinates(points[4][1], points[4][2]), 
                update_coordinates(points[1][0], points[1][2]), update_coordinates(points[1][1], points[1][2]));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If angles are greater than or equal to 2π reset
        if (A >= 6.28) {A = 0.01;}
        if (B >= 6.28) {B = 0.02;}
        if (C >= 6.28) {C = 0.015;}

        // Making rotated points back into base points
        for (int i = 0; i < 5; i++) {
            points[i][0] = base_points[i][0];
            points[i][1] = base_points[i][1];
            points[i][2] = base_points[i][2];
        }

        // Rotating all points
        for (int i = 0; i < 5; i++) {
            rotate_X(points[i]);
            rotate_Y(points[i]);
            rotate_Z(points[i]);
        }

        distance(points[0], points[1], points[2], points[3], points[4]);

        // Accumulating Angles
        A += 0.0;
        B += 0.02;
        C += 0.0;

        repaint();
    }
}
