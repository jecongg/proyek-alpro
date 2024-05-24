/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author jason
 */
public class AssetParent extends JPanel{
    public BufferedImage gambar;
    public int size = 50;
    public char tipe='k';
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gambar != null) {
            g.drawImage(gambar, 0, 0, size, size, null);
        }
    }
    
    
}
