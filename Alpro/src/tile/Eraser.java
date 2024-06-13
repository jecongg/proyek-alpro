/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tile;

import java.awt.image.BufferedImage;

/**
 *
 * @author jason
 */
public class Eraser extends AssetParent {
    
    public Eraser(BufferedImage gambar) {
        this.gambar = gambar;
        repaint();
        tipe="k";
    }
    
}
