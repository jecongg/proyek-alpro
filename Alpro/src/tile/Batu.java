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
public class Batu extends AssetParent {

    public Batu(BufferedImage gambar) {
        this.gambar = gambar;
        repaint();
        tipe="b";
    }
    
}
