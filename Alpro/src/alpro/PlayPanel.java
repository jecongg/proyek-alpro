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
    int tileSize = 70;
    int worldPlayerX, worldPlayerY, screenPlayerX, screenPlayerY;
    int startPlayerX, startPlayerY, currentX, currentY;
    int tambahY[]={-1,0,1,0};
    int tambahX[]={0,1,0,-1};
    boolean isVisited[][];

    public PlayPanel() {
        bacaFile();
        importGambar();
        screenPlayerX=(714/2) -(tileSize/2);
        screenPlayerY=(508/2) -(tileSize/2);

        currentX=startPlayerX;
        currentY=startPlayerY;
        isVisited=new boolean[map.length][map[0].length];
        worldPlayerX=startPlayerX*tileSize;
        worldPlayerY=startPlayerY*tileSize;
        
        System.out.println(backtrack(startPlayerX, startPlayerY, 0, 4));
    }
    
    public int backtrack(int x, int y, int langkah, int lives){
        if(lives<=0){
            return-1;
        }
        else if(map[y][x]=='g'){
            return langkah;
        }
        else{
            int tempLangkah=0;
//            System.out.println(langkah);
            boolean cek=false;
            boolean pertama=true;
            for(int i=0; i<4; i++){
                int yTemp=y+tambahY[i];
                int xTemp=x+tambahX[i];
                if(xTemp>=0 && yTemp>=0 && xTemp<map[0].length && yTemp<map.length){
                    if(!isVisited[yTemp][xTemp] && map[yTemp][xTemp]!='b'){
                        isVisited[yTemp][xTemp]=true;
                        int temp=0;
                        if(map[yTemp][xTemp]=='a'){
                            temp=backtrack(xTemp, yTemp, langkah+1, lives-1);
                        }
                        else if(map[yTemp][xTemp]=='h'){
                            temp=backtrack(xTemp, yTemp, langkah+1, lives+1);
                        }
                        else{
                            temp=backtrack(xTemp, yTemp, langkah+1, lives);
                        }
                        if(pertama && temp!=-1 && temp!=0){
                            tempLangkah=temp;
                            cek=true;
                            pertama=false;
                        }
                        else{
                            if(temp<tempLangkah && temp!=-1){
                                tempLangkah=temp;
                            }
                        }
                        isVisited[yTemp][xTemp]=false;
                    }
                }
            }
            if(cek){
                return tempLangkah;
            }
            return -1;
            
        }
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
        worldPlayerY-=tileSize;
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
                    if(map[i][j]=='s'){
                        startPlayerX=j;
                        startPlayerY=i;
                    }
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
