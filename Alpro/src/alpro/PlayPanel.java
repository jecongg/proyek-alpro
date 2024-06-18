package alpro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
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
    BufferedImage rumput, api, es, player, teleport, heal, start, goal, batu, trap, health, water;
    BufferedImage[] up, left, down, right, healthImage;
    String namaFile;
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
    ArrayList<Integer> jalanX;
    ArrayList<Integer> jalanY;
    ArrayList<Integer> tercepatX;
    ArrayList<Integer> tercepatY;
    JButton backtrackButton;

    public PlayPanel(String namaFile, JButton b) {
        this.namaFile=namaFile;
        backtrackButton=b;
        up=new BufferedImage[3];
        down=new BufferedImage[3];
        left=new BufferedImage[3];
        right=new BufferedImage[3];
        healthImage = new BufferedImage[5];
        jalanX=new ArrayList<>();
        jalanY = new ArrayList<>();
        bacaFile();
        importGambar();
        health=healthImage[4];
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
            System.out.println(backtrack(startPlayerX, startPlayerY, 0, 4, true));
        }).start();
    }

    public int backtrack(int x, int y, int langkah, int lives, boolean level1) {
        if(lives>4){
            lives=4;
        }
        jalanX.add(x);
        jalanY.add(y);
        health=healthImage[lives];
        if (lives <= 0) {
            jalanX.remove(jalanX.size()-1);
            jalanY.remove(jalanY.size()-1);
            return -1;
        } else if ("g".equals(map[y][x])) {
            if (langkahTercepat == -1) {
                langkahTercepat = langkah;
                tercepatX = (ArrayList<Integer>) jalanX.clone();
                tercepatY = (ArrayList<Integer>) jalanY.clone();
            }
            else if (langkah < langkahTercepat) {
                langkahTercepat=langkah;
                tercepatY = (ArrayList<Integer>) jalanY.clone();
                tercepatX = (ArrayList<Integer>) jalanX.clone();
            }
            jalanX.remove(jalanX.size()-1);
            jalanY.remove(jalanY.size()-1);
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
//                        move(i);
                        while (moving) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        int temp = 0;
                        if ("a".equals(map[yTemp][xTemp])) {
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives - 1, false);
                        } 
                        else if ("h".equals(map[yTemp][xTemp])) {
                            map[yTemp][xTemp] = "r";
//                            boolean tempVisited[][] = isVisited;
//                            isVisited=new boolean[map.length][map[0].length];
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives + 1, false);
//                            isVisited=tempVisited;
                            map[yTemp][xTemp] = "h";
                        }
                        else if ("t".equals(map[yTemp][xTemp])) {
                            map[yTemp][xTemp] = "r";
//                            boolean tempVisited[][] = isVisited;
//                            isVisited=new boolean[map.length][map[0].length];
                            int tempX=worldPlayerX;
                            int tempY=worldPlayerY;
                            worldPlayerX=startPlayerX*tileSize;
                            worldPlayerY=startPlayerY*tileSize;
                            repaint();
                            temp = backtrack(startPlayerX, startPlayerY, langkah + 1, lives, false);
                            worldPlayerX=tempX;
                            worldPlayerY=tempY;
                            repaint();
//                            isVisited=tempVisited;
                            map[yTemp][xTemp] = "t";
                        }
                        else if ("e".equals(map[yTemp][xTemp])) {
                            temp = backtrack(xTemp, yTemp, langkah + 2, lives, false);
                        }
                        else if ("r".equals(map[yTemp][xTemp])) {
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives, false);
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
//                            boolean[][] tempVisited=isVisited.clone();
//                            isVisited=new boolean[map.length][map[0].length];
                            temp = backtrack(xCari, yCari, langkah + 1, lives, false);
                            worldPlayerX=tempX;
                            worldPlayerY=tempY;
                            repaint();
//                            isVisited=tempVisited;
                            map[yTemp][xTemp]=tempTele;
                            map[yCari][xCari]=tempTele;
                        }
                        else{
                            temp = backtrack(xTemp, yTemp, langkah + 1, lives, false);
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
//                        move((i + 2) % 4);
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
            if(level1){
                backtrackButton.setEnabled(true);
            }
            jalanX.remove(jalanX.size()-1);
            jalanY.remove(jalanY.size()-1);
            if (cek) {
                return tempLangkah;
            }
            return -1;
        }
    }
    
    public boolean[][] cloneArray(){
        boolean temp[][] = isVisited.clone();
        for(int i=1; i<10; i++){
            temp[jalanY.get(jalanY.size()-i)][jalanX.get(jalanX.size()-i)]=false;
        }
        return temp;
    }
    
    public void mulaiReplay() {
        new Thread(() -> {
            replay();
        }).start();
    }
    
    public void replay(){
        backtrackButton.setEnabled(false);
        worldPlayerX=startPlayerX*tileSize;
        worldPlayerY=startPlayerY*tileSize;
        int x=startPlayerX;
        int y=startPlayerY;
        int lives=4;
        repaint();
        for(int i=1; i<tercepatX.size(); i++){
            int move=0;
            health=healthImage[lives];
            if(tercepatY.get(i)<y){
                move=0;
            }
            else if(tercepatX.get(i)>x){
                move=1;
            }
            else if(tercepatY.get(i)>y){
                move=2;
            }
            else{
                move=3;
            }
            while (moving) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            if(map[tercepatY.get(i)][tercepatX.get(i)].equals("h")){
                lives++;
                if(lives>4){
                    lives=4;
                }
            }
            else if(map[tercepatY.get(i)][tercepatX.get(i)].equals("a")){
                lives--;
            }
            x=tercepatX.get(i);
            y=tercepatY.get(i);
            if(map[tercepatY.get(i)][tercepatX.get(i)].equals("t") || Character.isDigit(map[tercepatY.get(i)][tercepatX.get(i)].charAt(0))){
                worldPlayerX=x*tileSize;
                worldPlayerY=y*tileSize;
            }
            else{
                move(move);
            }
        }
        backtrackButton.setEnabled(true);
    }

    public void importGambar() {
        try {
            rumput = ImageIO.read(new File("src/Assets/rumput.jpeg"));
            es = ImageIO.read(new File("src/Assets/es.jpeg"));
            api = ImageIO.read(new File("src/Assets/api.jpeg"));
            player = ImageIO.read(new File("src/Assets/down1.png"));
            teleport = ImageIO.read(new File("src/Assets/Teleport.jpg"));
            goal = ImageIO.read(new File("src/Assets/Finish.png"));
            batu = ImageIO.read(new File("src/Assets/batu.jpg"));
            heal = ImageIO.read(new File("src/Assets/heal.png"));
            start = ImageIO.read(new File("src/Assets/Start.png"));
            trap = ImageIO.read(new File("src/Assets/SpringTrap.jpg"));
            water = ImageIO.read(new File("src/Assets/Water.png"));
            
            for(int i=0; i<4; i++){
                for(int j=0; j<3; j++){
                    if(i==0){
                        up[j]=ImageIO.read(new File("src/Assets/up"+j+".png"));
                    }
                    else if(i==1){
                        right[j]=ImageIO.read(new File("src/Assets/right"+j+".png"));
                    }
                    else if(i==2){
                        down[j]=ImageIO.read(new File("src/Assets/down"+j+".png"));
                    }
                    else if(i==3){
                        left[j]=ImageIO.read(new File("src/Assets/left"+j+".png"));
                    }
                }
            }
            for(int i=0; i<5; i++){
                healthImage[i] = ImageIO.read(new File("src/Assets/health"+i+".png"));
            }
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
    
    
    public void zoomIn(){
//        while (moving) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
//        }
        tileSize+=10;
        if(tileSize>80){
            tileSize=80;
        }
        repaint();
    }
    public void zoomOut(){
//        while (moving) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
//        }
        tileSize-=10;
        if(tileSize<30){
            tileSize=30;
        }
        repaint();
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
            int counter = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerY -= tambah;
                progress += tambah;
                player=up[counter%3];
                counter++;
                
                if (progress >= tileSize) {
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
            int counter=0;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerY += tambah;
                progress += tambah;
                player=down[counter%3];
                counter++;
                
                if (progress >= tileSize) {
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
            int counter=0;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerX += tambah;
                progress += tambah;
                player=right[counter%3];
                counter++;

                if (progress >= tileSize) {
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
            int counter=0;

            @Override
            public void actionPerformed(ActionEvent e) {
                worldPlayerX -= tambah;
                progress += tambah;
                player=left[counter%3];
                counter++;

                if (progress >= tileSize) {
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
            File f = new File("src/File/" + namaFile);
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
                    if(map[i][j].equals("s")){
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arcSize = 20; // Size of the arc for rounded corners

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                g2d.drawImage(water, j * 320, i * 80, 320, 80, null);
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int worldX = j * tileSize;
                int worldY = i * tileSize;
                int screenX = worldX - worldPlayerX + screenPlayerX;
                int screenY = worldY - worldPlayerY + screenPlayerY;

                boolean isTopLeftEdgeTile = (i == 0 && j == 0);
                boolean isTopRightEdgeTile = (i == 0 && j == map[0].length - 1);
                boolean isBottomLeftEdgeTile = (i == map.length - 1 && j == 0);
                boolean isBottomRightEdgeTile = (i == map.length - 1 && j == map[0].length - 1);

                Image image = null;
                if ("r".equals(map[i][j])) {
                    image = rumput;
                } else if ("a".equals(map[i][j])) {
                    image = api;
                } else if ("e".equals(map[i][j])) {
                    image = es;
                } else if ("s".equals(map[i][j])) {
                    image = start;
                } else if ("g".equals(map[i][j])) {
                    image = goal;
                } else if ("t".equals(map[i][j])) {
                    image = trap;
                } else if ("h".equals(map[i][j])) {
                    image = heal;
                } else if ("b".equals(map[i][j])) {
                    image = batu;
                } else {
                    image = teleport;
                }

                Path2D path = new Path2D.Float();
                if (isTopLeftEdgeTile) {
                    path.moveTo(screenX + arcSize, screenY);
                    path.lineTo(screenX + tileSize, screenY);
                    path.lineTo(screenX + tileSize, screenY + tileSize);
                    path.lineTo(screenX, screenY + tileSize);
                    path.lineTo(screenX, screenY + arcSize);
                    path.quadTo(screenX, screenY, screenX + arcSize, screenY);
                    path.closePath();
                    g2d.setClip(path);
                } else if (isTopRightEdgeTile) {
                    path.moveTo(screenX, screenY);
                    path.lineTo(screenX + tileSize - arcSize, screenY);
                    path.quadTo(screenX + tileSize, screenY, screenX + tileSize, screenY + arcSize);
                    path.lineTo(screenX + tileSize, screenY + tileSize);
                    path.lineTo(screenX, screenY + tileSize);
                    path.closePath();
                    g2d.setClip(path);
                } else if (isBottomLeftEdgeTile) {
                    path.moveTo(screenX, screenY);
                    path.lineTo(screenX + tileSize, screenY);
                    path.lineTo(screenX + tileSize, screenY + tileSize);
                    path.lineTo(screenX + arcSize, screenY + tileSize);
                    path.quadTo(screenX, screenY + tileSize, screenX, screenY + tileSize - arcSize);
                    path.closePath();
                    g2d.setClip(path);
                } else if (isBottomRightEdgeTile) {
                    path.moveTo(screenX, screenY);
                    path.lineTo(screenX + tileSize, screenY);
                    path.lineTo(screenX + tileSize, screenY + tileSize - arcSize);
                    path.quadTo(screenX + tileSize, screenY + tileSize, screenX + tileSize - arcSize, screenY + tileSize);
                    path.lineTo(screenX, screenY + tileSize);
                    path.closePath();
                    g2d.setClip(path);
                }

                g2d.drawImage(image, screenX, screenY, tileSize, tileSize, null);

                if (isTopLeftEdgeTile || isTopRightEdgeTile || isBottomLeftEdgeTile || isBottomRightEdgeTile) {
                    g2d.setClip(null);
                }
            }
        }

        g2d.drawImage(health, 20, 450, 120, 39, null);
        g2d.drawImage(player, screenPlayerX, screenPlayerY, tileSize, tileSize, null);
    }

}
