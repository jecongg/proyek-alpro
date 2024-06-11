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
    public BufferedImage springtrap;
    public BufferedImage teleport;
    public BufferedImage heal;
    public BufferedImage batu;
    public BufferedImage start;
    public BufferedImage finish;
    public char tipe;
    
    public Tile(BufferedImage rumput, BufferedImage api, BufferedImage es, BufferedImage springtrap, BufferedImage teleport, BufferedImage heal, BufferedImage batu, BufferedImage start, BufferedImage finish) {
        this.rumput = rumput;
        this.api=api;
        this.es=es;
        this.springtrap=springtrap;
        this.teleport=teleport;
        this.heal=heal;
        this.batu=batu;
        this.start=start;
        this.finish=finish;
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
        else if(tipe=='s'){
            if (springtrap != null) {
                g.drawImage(springtrap, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='t'){
            if (teleport != null) {
                g.drawImage(teleport, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='h'){
            if (heal != null) {
                g.drawImage(heal, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='b'){
            if (batu != null) {
                g.drawImage(batu, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='g'){
            if (start != null) {
                g.drawImage(start, 0, 0, 30, 30, null);
            }
        }
        else if(tipe=='f'){
            if (finish != null) {
                g.drawImage(finish, 0, 0, 30, 30, null);
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

