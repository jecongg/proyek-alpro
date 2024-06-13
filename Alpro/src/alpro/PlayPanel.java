package alpro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    String map[][];
    BufferedImage rumput, api, es, player, teleport, heal, start, goal, batu, trap;
    int tileSize = 70;
    int interval = 1;
    int tambah = 1;
    int worldPlayerX, worldPlayerY, screenPlayerX, screenPlayerY;
    int startPlayerX, startPlayerY, currentX, currentY;
    int langkahTercepat;
    int tambahY[]={-1,0,1,0};
    int tambahX[]={0,1,0,-1};
    boolean isVisited[][];
    boolean jalanTercepat[][];
    boolean moving;

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
        langkahTercepat=-1;
        moving = false;
        repaint();
    }
    
    public void mulaiBacktrack() {
        new Thread(() -> {
            System.out.println(backtrack(startPlayerX, startPlayerY, 0, 4));
        }).start();
    }

    public int backtrack(int x, int y, int langkah, int lives) {
        if (lives <= 0) {
            return -1;
        } else if ("g".equals(map[y][x])) {
            if (langkahTercepat == -1) {
                langkahTercepat = langkah;
            }
            if (langkahTercepat < langkah) {
                jalanTercepat = isVisited.clone();
            }
            return langkah;
        } else {
            int tempLangkah = 0;
            boolean cek = false;
            boolean pertama = true;
            for (int i = 0; i < 4; i++) {
                int yTemp = y + tambahY[i];
                int xTemp = x + tambahX[i];
                if (xTemp >= 0 && yTemp >= 0 && xTemp < map[0].length && yTemp < map.length) {
                    if (!isVisited[yTemp][xTemp] && !map[yTemp][xTemp].equals("b") && !map[yTemp][xTemp].equals("k")) {
                        isVisited[yTemp][xTemp] = true;
                        move(i);
                        while (moving) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        int temp = 0;
                        if ("a".equals(map[yTemp][xTemp])) {
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives - 1);
                        } 
                        else if ("h".equals(map[yTemp][xTemp])) {
                            map[yTemp][xTemp] = "r";
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives + 1);
                            map[yTemp][xTemp] = "h";
                        }
                        else if ("t".equals(map[yTemp][xTemp])) {
                            map[yTemp][xTemp] = "r";
                            boolean tempVisited[][] = isVisited;
                            isVisited=new boolean[map.length][map[0].length];
                            int tempX=worldPlayerX;
                            int tempY=worldPlayerY;
                            worldPlayerX=startPlayerX*tileSize;
                            worldPlayerY=startPlayerY*tileSize;
                            repaint();
                            temp = backtrack(startPlayerX, startPlayerY, langkah + 1, lives);
                            worldPlayerX=tempX;
                            worldPlayerY=tempY;
                            repaint();
                            isVisited=tempVisited;
                            map[yTemp][xTemp] = "t";
                        }
                        else if ("e".equals(map[yTemp][xTemp])) {
                            temp = backtrack(xTemp, yTemp, langkah + 2, lives);
                        }
                        else if ("r".equals(map[yTemp][xTemp])) {
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives);
                        }
                        else if(Character.isDigit(map[yTemp][xTemp].charAt(0))){
                            int xCari=0;
                            int yCari=0;
                            for(int k=0; k<map.length; k++){
                                for(int j=0; j<map[0].length; j++){
                                    if(map[yTemp][xTemp].equals(map[k][j])){
                                        if(k!=yTemp && j!=xTemp){
                                            xCari=j;
                                            yCari=k;
                                            System.out.println(xCari + " " + yCari);
                                        }
                                    }
                                }
                            }
                            int tempX=worldPlayerX;
                            int tempY=worldPlayerY;
                            worldPlayerX=xCari*tileSize;
                            worldPlayerY=yCari*tileSize;
                            repaint();
                            String tempTele=map[yTemp][xTemp];
                            map[yTemp][xTemp]="r";
                            map[yCari][xCari]="r";
                            boolean[][] tempVisited=isVisited.clone();
                            isVisited=new boolean[map.length][map[0].length];
                            temp = backtrack(xCari, yCari, langkah + 1, lives);
                            worldPlayerX=tempX;
                            worldPlayerY=tempY;
                            repaint();
                            isVisited=tempVisited;
                            map[yTemp][xTemp]=tempTele;
                            map[yCari][xCari]=tempTele;
                        }
                        else{
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives);
                        }
                        if (pertama && temp != -1 && temp != 0) {
                            tempLangkah = temp;
                            cek = true;
                            pertama = false;
                        } 
                        else {
                            if (temp < tempLangkah && temp != -1) {
                                tempLangkah = temp;
                            }
                        }
                        move((i + 2) % 4);
                        while (moving) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        isVisited[yTemp][xTemp] = false;
                    }
                }
            }
            if (cek) {
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
            teleport = ImageIO.read(new File("src/Assets/Teleport.jpg"));
            goal = ImageIO.read(new File("src/Assets/Finish.png"));
            batu = ImageIO.read(new File("src/Assets/batu.jpg"));
            heal = ImageIO.read(new File("src/Assets/heal.png"));
            start = ImageIO.read(new File("src/Assets/Start.png"));
            trap = ImageIO.read(new File("src/Assets/SpringTrap.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(PlayPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void speedUp(){
        tambah+=10;
        if(tambah>tileSize){
            tambah=tileSize;
        }
    }
    public void speedDown(){
        tambah-=10;
        if(tambah<1){
            tambah=1;
        }
    }

    public void move(int arah) {
        if (arah != -1) {
            moving = true;
            if (arah == 0) {
                atas();
            } else if (arah == 1) {
                kanan();
            } else if (arah == 2) {
                bawah();
            } else if (arah == 3) {
                kiri();
            }
        }
    }

    public void atas() {
        Timer moveTimer = new Timer(interval, new ActionListener() {
            int progress = 0;
            int done = tileSize;
            int awal = worldPlayerY;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerY -= tambah;
                progress += tambah;

                if (progress >= done) {
                    worldPlayerY = awal-tileSize;
                    ((Timer)e.getSource()).stop();
                    moving = false;
                }
                repaint();
            }
        });
        moveTimer.start();
    }

    public void bawah() {
        Timer moveTimer = new Timer(interval, new ActionListener() {
            int progress = 0;
            int done = tileSize;
            int awal = worldPlayerY;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerY += tambah;
                progress += tambah;

                if (progress >= done) {
                    worldPlayerY = awal+tileSize;
                    ((Timer)e.getSource()).stop();
                    moving = false;
                }
                repaint();
            }
        });
        moveTimer.start();
    }

    public void kanan() {
        Timer moveTimer = new Timer(interval, new ActionListener() {
            int progress = 0;
            int done = tileSize;
            int awal = worldPlayerX;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerX += tambah;
                progress += tambah;

                if (progress >= done) {
                    worldPlayerX = awal+tileSize;
                    ((Timer)e.getSource()).stop();
                    moving = false;
                }
                repaint();
            }
        });
        moveTimer.start();
    }

    public void kiri() {
        Timer moveTimer = new Timer(interval, new ActionListener() {
            int progress = 0;
            int done = tileSize;
            int awal = worldPlayerX;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerX -= tambah;
                progress += tambah;

                if (progress >= done) {
                    worldPlayerX = awal-tileSize;
                    ((Timer)e.getSource()).stop();
                    moving = false;
                }
                repaint();
            }
        });
        moveTimer.start();
    }

    public void bacaFile() {
        try {
            File f = new File("src/File/output.txt");
            Scanner s = new Scanner(f);
            ArrayList<String> fileBentukString = new ArrayList<>();
            while (s.hasNextLine()) {
                fileBentukString.add(s.nextLine());
            }
            map = new String[fileBentukString.size()][fileBentukString.get(0).split(" ").length];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    String[] temp = fileBentukString.get(i).split(" ");
                    map[i][j] = temp[j];
                    if(map[i][j]=="s"){
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
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int worldX = j*tileSize;
                int worldY = i*tileSize;
                int screenX = worldX - worldPlayerX + screenPlayerX;
                int screenY = worldY - worldPlayerY + screenPlayerY;
                if ("r".equals(map[i][j])) {
                    g.drawImage(rumput, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("a".equals(map[i][j])) {
                    g.drawImage(api, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("e".equals(map[i][j])) {
                    g.drawImage(es, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("s".equals(map[i][j])) {
                    g.drawImage(start, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("g".equals(map[i][j])) {
                    g.drawImage(goal, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("t".equals(map[i][j])) {
                    g.drawImage(trap, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("h".equals(map[i][j])) {
                    g.drawImage(heal, screenX, screenY, tileSize, tileSize, null);
                }
                else if ("b".equals(map[i][j])) {
                    g.drawImage(batu, screenX, screenY, tileSize, tileSize, null);
                }
                else {
                    g.drawImage(teleport, screenX, screenY, tileSize, tileSize, null);
                }
            }
        }
        g.drawImage(player, screenPlayerX, screenPlayerY, tileSize, tileSize, null);
    }
}
