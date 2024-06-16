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
    public String tipe;
    
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
        tipe="k";
        repaint();
    }
    
    public void ubah(String tipe){
        this.tipe=tipe;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if("r".equals(tipe)){
            if (rumput != null) {
                g.drawImage(rumput, 0, 0, 30, 30, null);
            }
        }
        else if("a".equals(tipe)){
            if (api != null) {
                g.drawImage(api, 0, 0, 30, 30, null);
            }
        }
        else if("e".equals(tipe)){
            if (es != null) {
                g.drawImage(es, 0, 0, 30, 30, null);
            }
        }
        else if("t".equals(tipe)){
            if (springtrap != null) {
                g.drawImage(springtrap, 0, 0, 30, 30, null);
            }
        }
        else if("h".equals(tipe)){
            if (heal != null) {
                g.drawImage(heal, 0, 0, 30, 30, null);
            }
        }
        else if("b".equals(tipe)){
            if (batu != null) {
                g.drawImage(batu, 0, 0, 30, 30, null);
            }
        }
        else if("s".equals(tipe)){
            if (start != null) {
                g.drawImage(start, 0, 0, 30, 30, null);
            }
        }
        else if("g".equals(tipe)){
            if (finish != null) {
                g.drawImage(finish, 0, 0, 30, 30, null);
            }
        }
        else if("k".equals(tipe)){
            super.paintComponent(g);
        }
        else{
            if (teleport != null) {
                g.drawImage(teleport, 0, 0, 30, 30, null);
            }
        }
    }
    
    @Override
    public String toString() {
        return tipe;
    }
    
}

