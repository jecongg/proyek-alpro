/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alpro;

import tile.AssetParent;
import tile.Rumput;
import tile.Api;
import tile.Es;
import tile.SpringTrap;
import tile.Teleport;
import tile.Heal;
import tile.Batu;
import tile.Start;
import tile.Finish;
import tile.Tile;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import tile.Eraser;

/**
 *
 * @author jason
 */
public class Editor extends javax.swing.JFrame {
    BufferedImage rumput, es, api, springtrap, teleport, heal, batu, start, finish, eraser;
    ArrayList<ArrayList<Tile>> listTile;
    ArrayList<Tile> template;
    ArrayList<EdgeTele> listEdge;
    int x, y, nomor;
    String lagiMegang;
    boolean pertama, kedua;
    boolean klik;
    boolean startDone, finishDone;
    Tile tempTele;
    
    public Editor() {
        listTile=new ArrayList<>();
        template=new ArrayList<>();
        listEdge=new ArrayList<>();
        nomor=0;
        klik=false;
        pertama=true;
        lagiMegang="k";
        try {
            rumput = ImageIO.read(new File("src/Assets/rumput.jpeg"));
            es = ImageIO.read(new File("src/Assets/es.jpeg"));
            api = ImageIO.read(new File("src/Assets/api.jpeg"));
            springtrap = ImageIO.read(new File("src/Assets/SpringTrap.jpg"));
            teleport = ImageIO.read(new File("src/Assets/Teleport.jpg"));
            heal = ImageIO.read(new File("src/Assets/heal.png"));
            batu = ImageIO.read(new File("src/Assets/batu.jpg"));
            start = ImageIO.read(new File("src/Assets/start.png"));
            finish = ImageIO.read(new File("src/Assets/finish.png"));
            eraser = ImageIO.read(new File("src/Assets/eraser.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        x=0;
        y=0;
        initComponents();
        initAwal();
    }
  
    
    public void initAwal(){
        bawahButton.setText("\u2227");
        for(int i=0; i<15; i++){
            listTile.add(new ArrayList<>());
            for(int j=0; j<16; j++){
                Tile p = buatTile();
                template.add(p);
                listTile.get(i).add(p);
            }
        }
        printTile();
        int counter=0;
        for(int j=0; j<5; j++){
            for(int i=0; i<2; i++){
                AssetParent ap=null;
                if(counter==0){
                    ap=new Rumput(rumput);
                }
                else if(counter==1){
                    ap=new Api(api);
                }
                else if(counter==2){
                    ap=new Es(es);
                }
                else if(counter==3){
                    ap=new SpringTrap(springtrap);
                }
                else if(counter==4){
                    ap=new Teleport(teleport);
                }
                else if(counter==5){
                    ap=new Heal(heal);
                }
                else if(counter==6){
                    ap=new Batu(batu);
                }
                else if(counter==7){
                    ap=new Start(start);
                }
                else if(counter==8){
                    ap=new Finish(finish);
                }
                else if(counter==9){
                    ap=new Eraser(eraser);
                }
                if(counter<=9){
                    ap.setBounds(560 + ap.size*i + 10*i, 30 + ap.size*j + 10*j, ap.size, ap.size);
                    String temp=ap.tipe;
                    ap.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            if(!pertama){
                                tempTele.ubah("k");
                                pertama=true;
                            }
                            lagiMegang=temp;
                            System.out.println(lagiMegang);
                        }
                    });
                    this.add(ap);
                    counter++;
                }
            }
        }
        
    }
    
    public Tile buatTile(){
        Border b = BorderFactory.createLineBorder(Color.WHITE);
        Tile p = new Tile(rumput, api, es, springtrap, teleport, heal, batu, start, finish);
        p.setBackground(Color.BLACK);
        p.setBorder(b);

        p.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                klik=true;
                ubahTile(p);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                klik=false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(klik && !lagiMegang.equals("tele")){
                    ubahTile(p);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        return p;
    }
    
    public void ubahTile(Tile p){
        if("tele".equals(lagiMegang)){
            if(pertama){
                p.ubah(nomor+"");
                tempTele=p;
                pertama=false;
            }
            else if (!p.equals(tempTele)){
                p.ubah(nomor+"");
                pertama=true;
                listEdge.add(new EdgeTele(nomor, tempTele, p));
                nomor++;
            }
        }
        else {
            if(Character.isDigit(p.tipe.charAt(0))){
                EdgeTele temp=null;
                for(EdgeTele ed : listEdge){
                    if(p.tipe.equals(ed.nomor+"")){
                        ed.a.ubah("k");
                        ed.b.ubah("k");
                        temp=ed;
                    }
                }
                listEdge.remove(temp);
            }
            else if(p.tipe.equals("s") && !lagiMegang.equals("s")){
                startDone=false;
                p.ubah("k");
            }
            else if(p.tipe.equals("g") && !lagiMegang.equals("g")){
                finishDone=false;
                p.ubah("k");
            }
            if(lagiMegang.equals("s") && !startDone){
                startDone=true;
                p.ubah(lagiMegang);
            }
            else if(lagiMegang.equals("g") && !finishDone){
                finishDone=true;
                p.ubah(lagiMegang);
            }
            else if(!lagiMegang.equals("g") && !lagiMegang.equals("s")){
                p.ubah(lagiMegang);
            }
        }
    }
    
    public void printTile(){
        for(int i=0; i<15; i++){
            for(int j=0; j<16; j++){
                Tile t = listTile.get(y+i).get(x+j);
                t.setBounds(j*30, i*30, 30, 30);
                utamaPanel.add(t);
            }
        }
        repaint();
    }
    
    public void removeTile(){
        for(int i=0; i<15; i++){
            for(int j=0; j<16; j++){
                utamaPanel.remove(listTile.get(y+i).get(x+j));
            }
        }
    }
    
    public void atas(){
        removeTile();
        listTile.add(0, new ArrayList<>());
        for(int i=0; i<listTile.get(1).size(); i++){
            listTile.get(0).add(buatTile());
        }
        printTile();
    }
    
    public void bawah(){
        removeTile();
        listTile.add(new ArrayList<>());
        for(int i=0; i<listTile.get(0).size(); i++){
            listTile.get(listTile.size()-1).add(buatTile());
        }
        y++;
        printTile();
    }
    
    public void kanan(){
        removeTile();
        for(int i=0; i<listTile.size(); i++){
            listTile.get(i).add(buatTile());
        }
        x++;
        printTile();
    }
    
    public void kiri(){
        removeTile();
        for(int i=0; i<listTile.size(); i++){
            listTile.get(i).add(0, buatTile());
        }
        printTile();
    }
    
    public void save(){
        int atas=-1;
        int bawah=-1;
        boolean ketemuAtas=false;
        for(int i=0; i<listTile.size(); i++){
            for(int j=0; j<listTile.get(0).size(); j++){
                if(listTile.get(i).get(j).tipe!="k"){
                    if(!ketemuAtas){
                        ketemuAtas=true;
                        atas=i;
                    }
                    bawah=i;
                }
            }
        }
        boolean ketemuKiri=false;
        int kiri=-1;
        int kanan=-1;
        for(int i=0; i<listTile.get(0).size(); i++){
            for(int j=0; j<listTile.size(); j++){
                if(listTile.get(j).get(i).tipe!="k"){
                    if(!ketemuKiri){
                        ketemuKiri=true;
                        kiri=i;
                    }
                    kanan=i;
                }
            }
        }
        
        
        
        String fileName = "src/File/output.txt";

        // Create the directory if it doesn't exist
        File directory = new File("src/File");
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory
        }
        
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            for(int i=atas; i<=bawah; i++){
                for(int j=kiri; j<=kanan; j++){
                    String temp = listTile.get(i).get(j).tipe + " ";
                    fos.write(temp.getBytes());
                }
                fos.write(System.lineSeparator().getBytes());
            }
            System.out.println("Array has been written to " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        utamaPanel = new javax.swing.JPanel();
        atasButton = new javax.swing.JButton();
        kiriButton = new javax.swing.JButton();
        bawahButton = new javax.swing.JButton();
        kananButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        utamaPanel.setBackground(new java.awt.Color(102, 204, 0));
        utamaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                utamaPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout utamaPanelLayout = new javax.swing.GroupLayout(utamaPanel);
        utamaPanel.setLayout(utamaPanelLayout);
        utamaPanelLayout.setHorizontalGroup(
            utamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );
        utamaPanelLayout.setVerticalGroup(
            utamaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        atasButton.setText("^");
        atasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atasButtonActionPerformed(evt);
            }
        });

        kiriButton.setText("<");
        kiriButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kiriButtonActionPerformed(evt);
            }
        });

        bawahButton.setText("^");
        bawahButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bawahButtonActionPerformed(evt);
            }
        });

        kananButton.setText(">");
        kananButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kananButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(kiriButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(utamaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addComponent(saveButton)
                        .addGap(48, 48, 48))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kananButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(atasButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(266, 266, 266)
                        .addComponent(bawahButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(atasButton)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(utamaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bawahButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(kananButton)
                                .addGap(106, 106, 106)
                                .addComponent(saveButton)
                                .addGap(93, 93, 93))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(kiriButton)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void utamaPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_utamaPanelMouseClicked
        
    }//GEN-LAST:event_utamaPanelMouseClicked

    private void atasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atasButtonActionPerformed
        atas();
    }//GEN-LAST:event_atasButtonActionPerformed

    private void bawahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bawahButtonActionPerformed
        bawah();
    }//GEN-LAST:event_bawahButtonActionPerformed

    private void kiriButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kiriButtonActionPerformed
        kiri();
    }//GEN-LAST:event_kiriButtonActionPerformed

    private void kananButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kananButtonActionPerformed
        kanan();
    }//GEN-LAST:event_kananButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        save();
    }//GEN-LAST:event_saveButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Editor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton atasButton;
    private javax.swing.JButton bawahButton;
    private javax.swing.JButton kananButton;
    private javax.swing.JButton kiriButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel utamaPanel;
    // End of variables declaration//GEN-END:variables
}
