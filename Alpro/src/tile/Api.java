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
public class Api extends AssetParent {

    public Api(BufferedImage gambar) {
        this.gambar = gambar;
        repaint();
        tipe="a";
    }
    
}
