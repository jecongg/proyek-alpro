/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tile;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author jason
 */
public class Tile extends JPanel{
    public BufferedImage rumput;
    public BufferedImage api;
    public BufferedImage es;
    public char tipe;
    
    public Tile(BufferedImage rumput, BufferedImage api, BufferedImage es) {
        this.rumput = rumput;
        this.api=api;
        this.es=es;
        tipe='k';
        repaint();
    }
    
    public void ubah(char tipe){
        this.tipe=tipe;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(tipe=='r'){
            if (rumput != null) {
                g.drawImage(rumput, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='a'){
            if (api != null) {
                g.drawImage(api, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='e'){
            if (es != null) {
                g.drawImage(es, 0, 0, 30, 30, null);
            }
        }
        else{
            super.paintComponent(g);
        }
    }

    @Override
    public String toString() {
        return tipe + "";
    }
    
}

