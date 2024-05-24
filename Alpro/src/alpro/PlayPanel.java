package alpro;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class PlayPanel extends JPanel {
    char map[][];
    BufferedImage rumput;
    BufferedImage api;
    BufferedImage es;
    BufferedImage player;
    int tileSize = 50;
    int worldPlayerX, worldPlayerY, screenPlayerX, screenPlayerY;
    int startPlayerX, startPlayerY;

    public PlayPanel() {
        bacaFile();
        importGambar();
        screenPlayerX=(714/2) -(tileSize/2);
        screenPlayerY=(508/2) -(tileSize/2);
        worldPlayerX=3*tileSize;
        worldPlayerY=3*tileSize;
    }

    public void importGambar() {
        try {
            rumput = ImageIO.read(new File("src/Assets/rumput.jpeg"));
            es = ImageIO.read(new File("src/Assets/es.jpeg"));
            api = ImageIO.read(new File("src/Assets/api.jpeg"));
            player = ImageIO.read(new File("src/Assets/player.png"));
        } catch (IOException ex) {
            Logger.getLogger(PlayPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void atas(){
        worldPlayerY-=50;
        repaint();
    }

    public void bacaFile() {
        try {
            File f = new File("src/File/output.txt");
            Scanner s = new Scanner(f);
            ArrayList<String> fileBentukString = new ArrayList<>();
            while (s.hasNextLine()) {
                fileBentukString.add(s.nextLine());
            }
            map = new char[fileBentukString.size()][fileBentukString.get(0).length()];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = fileBentukString.get(i).charAt(j);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rumput, 20, 20, 30, 30, null);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int worldX = j*tileSize;
                int worldY = i*tileSize;
                int screenX = worldX - worldPlayerX + screenPlayerX;
                int screenY = worldY - worldPlayerY + screenPlayerY;
                if (map[i][j] == 'r') {
                    g.drawImage(rumput, screenX, screenY, tileSize, tileSize, null);
                }
                else if (map[i][j] == 'a') {
                    g.drawImage(api, screenX, screenY, tileSize, tileSize, null);
                }
                else if (map[i][j] == 'e') {
                    g.drawImage(es, screenX, screenY, tileSize, tileSize, null);
                }
            }
        }
        g.drawImage(player, screenPlayerX, screenPlayerY, tileSize, tileSize, null);
    }
}
