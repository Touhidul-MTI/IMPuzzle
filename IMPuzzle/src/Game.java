/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MTI
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class Game extends JFrame{
    private JPanel gamePanel1,gamePanel2,gamePanel3,defaultPanel,wcPanel,
            timePanel,scorePanel,aboutPanel,easyImagePanel,mediumImagePanel,hardImagePanel;
    private JLabel time, click, newPlayer;
    
    private BufferedImage img1, img2,img3;
    
    private ArrayList<MyButton> buttons1;
    private ArrayList<MyButton> buttons2;
    private ArrayList<MyButton> buttons3;

    ArrayList<Point> buttonPoints1 = new ArrayList<Point>();
    ArrayList<Point> buttonPoints2 = new ArrayList<Point>();
    ArrayList<Point> buttonPoints3 = new ArrayList<Point>();

    private Image buttonImage1, buttonImage2, buttonImage3;
    private MyButton emptyButton1, emptyButton2, emptyButton3;
    private int width1, height1, width2, height2, width3, height3;
    private final int newImageWidth = 605;
    private BufferedImage resizedImage1, resizedImage2, resizedImage3;

    private JMenuBar menuBar1;
    private JMenu option,viewImages,playSelectLevel;
    private JMenuItem play,highScores,exit,about, playEasyLevel,
            playMediumLevel,playHardLevel,easyImage,mediumImage,hardImage;
    
    private String[] matrixBar={"Player Name","Time","Click","Level"};
    private String[][] scoreFileMatrix;
    
    final private FileReadWrite frw=new FileReadWrite();
    
    private int clickCount=-1, timeCount=0;
    private String newPlayerName="Anonymous";
    private Time timeObject;
    boolean isTimeThreadCreated=false;
    
    private int gameLevelDetect=0;
            
    public Game() {
        //matrixBar=frw.getmatrixBar();//take the MatrixBar Names
        scoreFileMatrix=frw.readFile();//initially read file from saved data to scoreFileMatrix table
        initUI();
    }
    private void initUI() {
        //frw.writeFile(a);
        
        
        //pack();
        setTitle("IMPuzzle");
        setSize(610,400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        defaultPanel =new JPanel();//set container panel which contains all other panel
                                   //    except timePanel
        setWelcomePanel();//set welcome panel which appear first
        //setWcPanel();
        setMenu();//set all play options
        setGame1();//set easy level game panel1
        setGame2();//set medium level game panel2
        setGame3();//set hard level game panel3
       // setTimePanel();
        setScorePanel();//set score board panel
        setAboutPanel();//set panel about owner info
        setViewImagePanel();
    }
    
    // Game Panel1 setting from here
    private void setGame1(){ //set game panel
        buttonPoints1.add(new Point(0, 0));
        buttonPoints1.add(new Point(0, 1));
        buttonPoints1.add(new Point(0, 2));
        buttonPoints1.add(new Point(1, 0));
        buttonPoints1.add(new Point(1, 1));
        buttonPoints1.add(new Point(1, 2));
        buttonPoints1.add(new Point(2, 0));
        buttonPoints1.add(new Point(2, 1));
        buttonPoints1.add(new Point(2, 2));

        buttons1 = new ArrayList<MyButton>();

        gamePanel1 = new JPanel();
        gamePanel1.setBorder(BorderFactory.createLineBorder(Color.gray));
        gamePanel1.setLayout(new GridLayout(3, 3, 0, 0));

        try {
            img1 = loadImage1(); //call for import image
            int h = getNewHeight(img1.getWidth(), img1.getHeight());//new hight following desired width1
            resizedImage1 = resizeImage(img1, newImageWidth, h, //resize image with new hight width1
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width1 = resizedImage1.getWidth(null);
        height1 = resizedImage1.getHeight(null);

        //add(gamePanel1, BorderLayout.CENTER);

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {//crop image

                buttonImage1 = createImage(new FilteredImageSource(resizedImage1.getSource(),
                        new CropImageFilter(j * width1 / 3, i * height1 / 3,
                                (width1 / 3), height1 / 3)));
                MyButton button = new MyButton(buttonImage1);//create myButton type buttons and send part of image
                button.putClientProperty("position", new Point(i, j));//keep original point of cropped image parts

                if (i == 2 && j == 2) { //for one empty button in puzzle
                    emptyButton1 = new MyButton();
                    emptyButton1.setBorderPainted(false);
                    emptyButton1.setContentAreaFilled(false);
                    emptyButton1.setEmptyButton();
                    emptyButton1.putClientProperty("position", new Point(i, j));
                } else {
                    buttons1.add(button);//add button to arraylist
                }
            }
        }
        Collections.shuffle(buttons1);//randomly distribute position of image part
        buttons1.add(emptyButton1); //outside, keep empty button fixed

        for (int i = 0; i < 9; i++) {

            MyButton btn = buttons1.get(i);
            gamePanel1.add(btn); //add buttons to panel
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction1());//set actionlistener
        }
         
        pack();
        //defaultPanel=new JPanel();
        //defaultPanel.add(gamePanel1);
        //add(defaultPanel);
    }
    public void makeShuffle1(){
       Collections.shuffle(buttons1);//randomly distribute position of image part
       //update all button on panel after swaping in arraylist
            gamePanel1.removeAll();
            for (JComponent btn : buttons1) {
                gamePanel1.add(btn);
            }
            gamePanel1.validate();
    }
   public void makeShuffle2(){
       Collections.shuffle(buttons2);//randomly distribute position of image part
       //update all button on panel after swaping in arraylist
            gamePanel2.removeAll();
            for (JComponent btn : buttons2) {
                gamePanel2.add(btn);
            }
            gamePanel2.validate();
    }
 public void makeShuffle3(){
       Collections.shuffle(buttons3);//randomly distribute position of image part
       //update all button on panel after swaping in arraylist
            gamePanel3.removeAll();
            for (JComponent btn : buttons3) {
                gamePanel3.add(btn);
            }
            gamePanel3.validate();
    }
    private BufferedImage loadImage1() throws IOException {//read image

        BufferedImage bimg = ImageIO.read(new File("pic/messi.jpg"));
        return bimg;
    }

    private class ClickAction1 extends AbstractAction {//while each game button clicked

        @Override
        public void actionPerformed(ActionEvent e) {
            
            startClickCount(); //any button clicked, then one clcked count
            
            checkButton1(e); //call checking to move button if neighbor is empty button
            varifyMatching1();//call for varify, if puzzle is solved
        }

        private void checkButton1(ActionEvent e) {//check buttons neighbor

            int emptyButtonIndex = 0;
            for (MyButton button : buttons1) {
                if (button.isEmptyButton()) {//check for empty button index
                    emptyButtonIndex = buttons1.indexOf(button);
                }
            }

            JButton button = (JButton) e.getSource();
            int clickedButtonIndex = buttons1.indexOf(button);//clicked button index

            //checking if empty button index is any side one of clicked button index
            if ((clickedButtonIndex - 1 == emptyButtonIndex) || (clickedButtonIndex + 1 == emptyButtonIndex)
                    || (clickedButtonIndex - 3 == emptyButtonIndex) || (clickedButtonIndex + 3 == emptyButtonIndex)) {
                //if yes, swap clicked button with empty button in arraylist
                Collections.swap(buttons1, clickedButtonIndex, emptyButtonIndex);
                updateButtons1();//call for update all buttons in panel
            }
        }

        private void updateButtons1() {//update all button on panel after swaping in arraylist
            gamePanel1.removeAll();
            for (JComponent btn : buttons1) {
                gamePanel1.add(btn);
            }
            gamePanel1.validate();
        }
    }

    private void varifyMatching1() {//varify solution
        
        ArrayList<Point> current = new ArrayList<Point>();

        for (JComponent btn : buttons1) { //copying buttons
            current.add((Point) btn.getClientProperty("position"));
        }
        if (compareList(buttonPoints1, current)) {//check if match two list
            timeObject.stopTime();//stop time while finished playing
            timeCount=timeObject.getTime();//get game played time and reset timeCount to 
            
            checkHighScore(); //go for check if this is a new high score 
            resetClickCount();//reset clickCount to 0
                    
        //    JOptionPane.showMessageDialog(gamePanel1, newPlayerName+" You have done well!",
        //            "Congratulation", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // Game Panel1 setting ends  here
    private int getNewHeight(int w, int h) {//return new height1 according to desired widh

        double ratio = newImageWidth / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int width,
        int height, int type) throws IOException {//resize image using new height1 and width1

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }
       
    public static boolean compareList(List ls1, List ls2) {//compare two list to check matching
        return ls1.toString().contentEquals(ls2.toString());
    }
    
    
    
    public void setMenu(){//set all play options
        menuBar1=new JMenuBar();
        menuBar1.setBackground(new Color(204,204,255));
        
        option = new JMenu(" ☰ Game Options    ");
        option.setForeground(new Color(0,128,0));
        option.setToolTipText("Click to See All Game Options");
        
        playSelectLevel=new JMenu("   ► Play");
        playSelectLevel.setToolTipText("Click to Play Puzzle Games in Different Level");
        playSelectLevel.setForeground(new Color(0,128,0));
         
        
        ImageIcon playIcon=new ImageIcon("pic/playIcon.png");
        ImageIcon imageIcon=new ImageIcon("pic/image.png");
        ImageIcon scoreIcon=new ImageIcon("pic/score.png");
        ImageIcon aboutIcon=new ImageIcon("pic/about.png");
        ImageIcon exitIcon=new ImageIcon("pic/exit.png");
        
        
        viewImages=new JMenu("View Level Images");
        viewImages.setToolTipText("Click to View Images of Different Games Level");
        viewImages.setForeground(new Color(85,107,47));
        
//        play=new JMenuItem("Play New Game");
//        play.setToolTipText("Click to Start New Game");
//        play.setForeground(new Color(255,128,0));
        
        highScores=new JMenuItem("High Scores",scoreIcon);
        highScores.setToolTipText("Click to See ScoreBoard");
        highScores.setForeground(new Color(85,107,47));
        
        about=new JMenuItem("Who Made This?", aboutIcon);
        about.setToolTipText("Click to View the Programmers Profile");
        about.setForeground(new Color(0,128,0));
         
        exit=new JMenuItem("Exit", exitIcon);
        exit.setToolTipText("Click to Quit the Game");
        exit.setForeground(new Color(255,51,51));
        
        
        playEasyLevel=new JMenuItem("Play Easy Level (3*3)", playIcon);
        playEasyLevel.setToolTipText("Click to Play Easy Level");
        playEasyLevel.setForeground(new Color(0,128,0));
        playMediumLevel=new JMenuItem("Play Medium Level (4*4)", playIcon);
        playMediumLevel.setToolTipText("Click to Play Medium Level");
        playMediumLevel.setForeground(new Color(255,128,0));
        playHardLevel=new JMenuItem("Play Hard Level (5*5)",playIcon);
        playHardLevel.setToolTipText("Click to Play Hard Level");
        playHardLevel.setForeground(new Color(255,51,51));
        
        
        MenuActionListener mal=new MenuActionListener();
        
//        play.addActionListener(mal);
        highScores.addActionListener(mal);
        //viewImages.addActionListener(mal);
        about.addActionListener(mal);
        exit.addActionListener(mal);
        
        playEasyLevel.addActionListener(mal);
        playMediumLevel.addActionListener(mal);
        playHardLevel.addActionListener(mal);
        
        playSelectLevel.add(playEasyLevel);
        playSelectLevel.add(playMediumLevel);
        playSelectLevel.add(playHardLevel);
        
        easyImage=new JMenuItem("Easy Level Image (3*3)", imageIcon);
        easyImage.setToolTipText("Click to View Easy Level Image");
        easyImage.setForeground(new Color(0,128,0));
        mediumImage=new JMenuItem("Medium Level Image (4*4)", imageIcon);
        mediumImage.setToolTipText("Click to View Medium Level Image");
        mediumImage.setForeground(new Color(255,128,0));
        hardImage=new JMenuItem("Hard Level Image (5*5)", imageIcon);
        hardImage.setToolTipText("Click to View Hard Level Image");
        hardImage.setForeground(new Color(255,51,51));
        
        easyImage.addActionListener(mal);
        mediumImage.addActionListener(mal);
        hardImage.addActionListener(mal);
        
        viewImages.add(easyImage);
        viewImages.add(mediumImage);
        viewImages.add(hardImage);
        
        //option.add(play);
        option.add(highScores);
        option.add(viewImages);
        //option.add(playSelectLevel);
        option.add(about);
        option.add(exit);
           
        menuBar1.add(option);
        menuBar1.add(playSelectLevel);
        setJMenuBar(menuBar1);
    }
//    public void setTimePanel(){//set time panel which display time and click 
//        timePanel=new JPanel();
//        timePanel.setBackground(Color.pink);
//        time=new JLabel("Time");
//        click =new JLabel("Click");
//        
//        timePanel.add(time);
//        timePanel.add(click);
//        add(timePanel,BorderLayout.NORTH);
//        timePanel.setBounds(0, 0, 500,20);
//    }
//    public void setWcPanel(){
//        wcPanel=new JPanel();
//        wcPanel.setBackground(Color.LIGHT_GRAY);
//        ImageIcon ic1=new ImageIcon("messi.jpg");
//        JLabel wc=new JLabel(ic1);
//        wcPanel.add(wc);
//        defaultPanel.add(wcPanel);
//        add(defaultPanel, BorderLayout.CENTER);
//        
//    }
    public void setWelcomePanel(){//first lookof opening game
        timePanel=new JPanel();

        timePanel.setBackground(Color.pink);
        BoxLayout boxLayout1 = new BoxLayout(timePanel, BoxLayout.X_AXIS);
        timePanel.setLayout(new GridLayout(1, 3, 0, 0));
        
        newPlayer=new JLabel("  Player: "+newPlayerName);
        time=new JLabel("Time: 0 seconds");
        click =new JLabel("Clicked: 0 times");
        
        timePanel.add(newPlayer);
        timePanel.add(time);
        timePanel.add(click);

        wcPanel=new JPanel();

        defaultPanel.setBackground(Color.LIGHT_GRAY);
        
        ImageIcon ic1=new ImageIcon("pic/welcome.jpg");
        JLabel wc=new JLabel(ic1);
        
        wcPanel.add(wc);
        defaultPanel.add(wcPanel);
        
        add(timePanel,BorderLayout.NORTH);
        add(defaultPanel,BorderLayout.CENTER);
    }
    public void setScorePanel(){//set score board for player
        scorePanel=new JPanel();
        
        JTable table=new JTable(scoreFileMatrix, matrixBar);

        table.setBounds(5,5,200,200);
        JScrollPane jspane=new JScrollPane(table);
        scorePanel.add(jspane);
    }
    public void setAboutPanel(){//set information panel about who made this game
        aboutPanel=new JPanel();
        aboutPanel.setBackground(new Color(85,107,47));
        aboutPanel.setLayout(new GridLayout(2, 2, 5, 5));
        
        ImageIcon touhidPic=new ImageIcon("pic/touhid.jpg");
        ImageIcon shahidPic=new ImageIcon("pic/shahid.jpg");
        
        JLabel touhid=new JLabel(touhidPic);
        JLabel shahid=new JLabel(shahidPic);
        
        String aboutTouhid="\n  Muhammad Touhidul Islam\n\n"
                + "  School of Computer Science and Engineering,  \n"
                + "  Department of Computer Science and Engineering,  \n"
                + "  BRAC University\n"
                + "  ID:13201021\n\n"
                + "  Email: mdtislam94@gmail.com\n"
                + "  Facebook.com/touhid.mtis";
        String boutShaahid="  Md Shahidul Islam Majumdar\n\n"
                + "  School of Computer Science and Engineering,  \n"
                + "  Department of Computer Science and Engineering,  \n"
                + "  BRAC University\n"
                + "  ID:13201022\n\n"
                + "  Email: musafirshahid@gmail.com\n"
                + "  Facebook.com/musafirshahid05";
        
        JTextArea touhidText=new JTextArea(aboutTouhid);
        JTextArea shahidText=new JTextArea(boutShaahid);
        touhidText.setBackground(new Color(120,170,90));
        shahidText.setBackground(new Color(120,170,100));
                
        aboutPanel.add(touhid);
        aboutPanel.add(touhidText);
        aboutPanel.add(shahid);
        aboutPanel.add(shahidText);
        
    }
    public void startEasyLevel(){
        gameLevelDetect=1; //detecting level type to keep scores correctly
                
                if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                    makeShuffle1();
                 }
                setPlayerName();
                defaultPanel.removeAll();
                defaultPanel.add(gamePanel1);
                
                defaultPanel.updateUI();
                
                startTimeCount();
                startClickCount();
    }
    public void startMediumLevel(){
        gameLevelDetect=2; //detecting level type to keep scores correctly
                
                if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                    makeShuffle2();
                 }
                setPlayerName();
                defaultPanel.removeAll();
                defaultPanel.add(gamePanel2);
                
                defaultPanel.updateUI();
                
                startTimeCount();
                startClickCount();
    }
    public void startHardLevel(){
        gameLevelDetect=3; //detecting level type to keep scores correctly
                
                
                if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                    makeShuffle3();
                 }
                setPlayerName();
                defaultPanel.removeAll();
                defaultPanel.add(gamePanel3);
                
                defaultPanel.updateUI();
                
                startTimeCount();
                startClickCount();
    }
    private class MenuActionListener implements ActionListener{//while options-menu-menuitem clicked
 
        public void actionPerformed(ActionEvent ev){
            if(ev.getSource().equals(playEasyLevel)){
                
                startEasyLevel();
            }
            else if(ev.getSource().equals(playMediumLevel)){
                
                startMediumLevel();
            }
            else if(ev.getSource().equals(playHardLevel)){
                
                startHardLevel();
            }
            else if(ev.getSource().equals(highScores)){
                  defaultPanel.removeAll();
                  defaultPanel.add(scorePanel);
                  defaultPanel.updateUI();
                  
                  if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                  }
            }
            else if(ev.getSource().equals(easyImage)){
                  defaultPanel.removeAll();
                  defaultPanel.add(easyImagePanel);
                  defaultPanel.updateUI();
                  
                  if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                  }
            }
            else if(ev.getSource().equals(mediumImage)){
                  defaultPanel.removeAll();
                  defaultPanel.add(mediumImagePanel);
                  defaultPanel.updateUI();
                  
                  if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                  }
            }
            else if(ev.getSource().equals(hardImage)){
                  defaultPanel.removeAll();
                  defaultPanel.add(hardImagePanel);
                  defaultPanel.updateUI();
                  
                  if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                  }
            }
            else if(ev.getSource().equals(about)){
                defaultPanel.removeAll();
                defaultPanel.add(aboutPanel);
                defaultPanel.updateUI();
                if(isTimeThreadCreated){ //if already created once then do (trying to play once)
                    timeObject.stopTime();//while leaving playing before finished
                    timeObject.resetTime();//reseting timeCount to 0
                    resetClickCount();
                 }
            }
            else if(ev.getSource().equals(exit)){
                System.exit(0);
            }
         }
    }
    public void setPlayerName(){//take new player name
        try{
        newPlayerName=JOptionPane.showInputDialog(newPlayer,
                     "Enter Your Name", "Player Name", JOptionPane.DEFAULT_OPTION);
        if(newPlayerName.equals("")){
            newPlayerName="Anonymous";
            newPlayer.setText("  Player: "+newPlayerName);
        }else{
            newPlayer.setText("  Player: "+newPlayerName);
        }}catch (Exception e){}
    }
    public void startTimeCount(){//start timeCount 
        timeObject=new Time(time);//create thread Time class object
        isTimeThreadCreated=true;//set that thread created, need to handle unwanted game leaving stoping time
    }
    public void startClickCount(){//start clickCount
        clickCount++;
        click.setText("Clicked: "+clickCount+" times");
    }
    public void resetClickCount(){//reset clickCount to 0
        clickCount=-1;
        click.setText("Clicked: 0 times");
    }
    public void checkHighScore(){//check new high score
        if(gameLevelDetect==1){
                if((Integer.parseInt(scoreFileMatrix[0][1])==0) || ((timeCount<Integer.parseInt(scoreFileMatrix[0][1]))
                    && (clickCount<Integer.parseInt(scoreFileMatrix[1][2])))){//check if highscore both on time and click
                 JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score both in "
                         +timeCount+" Seconds and "+clickCount+" Click","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[0][0]=newPlayerName;//upadating new new name
                scoreFileMatrix[0][1]=timeCount+"";//updating new score
                scoreFileMatrix[0][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }
            else if((Integer.parseInt(scoreFileMatrix[0][1])==0) || timeCount<Integer.parseInt(scoreFileMatrix[0][1])){//check if highscore on time
                 JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score in "
                         +timeCount+" Seconds","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[0][0]=newPlayerName;//upadating new player name
                scoreFileMatrix[0][1]=timeCount+"";//updating new score
                scoreFileMatrix[0][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }else if((Integer.parseInt(scoreFileMatrix[1][2])==0) || clickCount<Integer.parseInt(scoreFileMatrix[1][2])){//check if highscore on click
                JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score in "
                        +clickCount+" Click!","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[1][0]=newPlayerName;//upadating new player name
                scoreFileMatrix[1][1]=timeCount+"";//updating new score
                scoreFileMatrix[1][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }else{//just finished game ,no highscore
                JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Solved the First Puzzle! Go for Medium Level",
                        "Congratulation", JOptionPane.INFORMATION_MESSAGE);
            }
            startMediumLevel();//when finished easy, going for medium
        }
        else if(gameLevelDetect==2){
            if((Integer.parseInt(scoreFileMatrix[2][1])==0) || ((timeCount<Integer.parseInt(scoreFileMatrix[2][1]))
                    && (clickCount<Integer.parseInt(scoreFileMatrix[3][2])))){//check if highscore both on time and click
                 JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score both in "
                         +timeCount+" Seconds and "+clickCount+" Click","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[2][0]=newPlayerName;//upadating new new name
                scoreFileMatrix[2][1]=timeCount+"";//updating new score
                scoreFileMatrix[2][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }
            else if((Integer.parseInt(scoreFileMatrix[2][1])==0) || timeCount<Integer.parseInt(scoreFileMatrix[2][1])){//check if highscore on time
                 JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score in "
                         +timeCount+" Seconds","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[2][0]=newPlayerName;//upadating new player name
                scoreFileMatrix[2][1]=timeCount+"";//updating new score
                scoreFileMatrix[2][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }else if((Integer.parseInt(scoreFileMatrix[3][2])==0) || clickCount<Integer.parseInt(scoreFileMatrix[3][2])){//check if highscore on click
                JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score in "
                        +clickCount+" Click!","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[3][0]=newPlayerName;//upadating new player name
                scoreFileMatrix[3][1]=timeCount+"";//updating new score
                scoreFileMatrix[3][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }else{//just finished game ,no highscore
                JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Solved the Second Puzzle! Go for Hard Level",
                        "Congratulation", JOptionPane.INFORMATION_MESSAGE);
            }
            startHardLevel();//when finished medium, going for hard level
        }
        else if(gameLevelDetect==3){
            if((Integer.parseInt(scoreFileMatrix[4][1])==0) || ((timeCount<Integer.parseInt(scoreFileMatrix[4][1]))
                    && (clickCount<Integer.parseInt(scoreFileMatrix[5][2])))){//check if highscore both on time and click
                 JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score both in "
                         +timeCount+" Seconds and "+clickCount+" Click","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[4][0]=newPlayerName;//upadating new new name
                scoreFileMatrix[4][1]=timeCount+"";//updating new score
                scoreFileMatrix[4][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }
            else if((Integer.parseInt(scoreFileMatrix[4][1])==0) || timeCount<Integer.parseInt(scoreFileMatrix[4][1])){//check if highscore on time
                 JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score in "
                         +timeCount+" Seconds","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[4][0]=newPlayerName;//upadating new player name
                scoreFileMatrix[4][1]=timeCount+"";//updating new score
                scoreFileMatrix[4][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }else if((Integer.parseInt(scoreFileMatrix[5][2])==0) || clickCount<Integer.parseInt(scoreFileMatrix[5][2])){//check if highscore on click
                JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", You Made a New High Score in "
                        +clickCount+" Click!","Congratulation", JOptionPane.INFORMATION_MESSAGE);

                scoreFileMatrix[5][0]=newPlayerName;//upadating new player name
                scoreFileMatrix[5][1]=timeCount+"";//updating new score
                scoreFileMatrix[5][2]=clickCount+"";
                frw.writeFile(scoreFileMatrix);//new data saving to dat file

            }else{//just finished game ,no highscore
                JOptionPane.showMessageDialog(gamePanel1, newPlayerName+", Now You are a Genius! You Solved the Puzzle!",
                        "Congratulation", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
    }
    public void setViewImagePanel(){
        easyImagePanel=new JPanel();
        mediumImagePanel=new JPanel();
        hardImagePanel=new JPanel();
        
        ImageIcon easyImg=new ImageIcon("pic/messi.jpg");
        ImageIcon mediumImg=new ImageIcon("pic/messiRonaldo.jpg");
        ImageIcon hardImg=new ImageIcon("pic/sachin.jpg");
        
        JLabel easyL=new JLabel(easyImg);
        JLabel mediumL=new JLabel(mediumImg);
        JLabel hardL=new JLabel(hardImg);
        
        easyImagePanel.add(easyL);
        mediumImagePanel.add(mediumL);
        hardImagePanel.add(hardL);
    }

 // Game Panel2 setting start from here
    private void setGame2(){ //set game panel
        buttonPoints2.add(new Point(0, 0));
        buttonPoints2.add(new Point(0, 1));
        buttonPoints2.add(new Point(0, 2));
        buttonPoints2.add(new Point(0, 3));
        buttonPoints2.add(new Point(1, 0));
        buttonPoints2.add(new Point(1, 1));
        buttonPoints2.add(new Point(1, 2));
        buttonPoints2.add(new Point(1, 3));
        buttonPoints2.add(new Point(2, 0));
        buttonPoints2.add(new Point(2, 1));
        buttonPoints2.add(new Point(2, 2));
        buttonPoints2.add(new Point(2, 3));
        buttonPoints2.add(new Point(3, 0));
        buttonPoints2.add(new Point(3, 1));
        buttonPoints2.add(new Point(3, 2));
        buttonPoints2.add(new Point(3, 3));
        

        buttons2 = new ArrayList<MyButton>();

        gamePanel2 = new JPanel();
        gamePanel2.setBorder(BorderFactory.createLineBorder(Color.gray));
        gamePanel2.setLayout(new GridLayout(4, 4, 0, 0));

        try {
            img2 = loadImage2(); //call for import image
            int h = getNewHeight(img2.getWidth(), img2.getHeight());//new hight following desired width1
            resizedImage2 = resizeImage(img2, newImageWidth, h, //resize image with new hight width1
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width2 = resizedImage2.getWidth(null);
        height2 = resizedImage2.getHeight(null);

        //add(gamePanel1, BorderLayout.CENTER);

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {//crop image

                buttonImage2 = createImage(new FilteredImageSource(resizedImage2.getSource(),
                        new CropImageFilter(j * width2 / 4, i * height2 / 4,
                                (width2 / 4), height2 / 4)));
                MyButton button = new MyButton(buttonImage2);//create myButton type buttons and send part of image
                button.putClientProperty("position", new Point(i, j));//keep original point of cropped image parts

                if (i == 3 && j == 3) { //for one empty button in puzzle
                    emptyButton2 = new MyButton();
                    emptyButton2.setBorderPainted(false);
                    emptyButton2.setContentAreaFilled(false);
                    emptyButton2.setEmptyButton();
                    emptyButton2.putClientProperty("position", new Point(i, j));
                } else {
                    buttons2.add(button);//add button to arraylist
                }
            }
        }
        //makeShuffle();
        Collections.shuffle(buttons2);//randomly distribute position of image part
        buttons2.add(emptyButton2); //outside, keep empty button fixed

        for (int i = 0; i < 16; i++) {

            MyButton btn = buttons2.get(i);
            gamePanel2.add(btn); //add buttons to panel
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction2());//set actionlistener
        }
         
        pack();
    }

    private BufferedImage loadImage2() throws IOException {//read image

        BufferedImage bimg = ImageIO.read(new File("pic/messiRonaldo.jpg"));
        return bimg;
    }


    private class ClickAction2 extends AbstractAction {//while each game button clicked

        @Override
        public void actionPerformed(ActionEvent e) {
            
            startClickCount(); //any button clicked, then one clcked count
            
            checkButton2(e); //call checking to move button if neighbor is empty button
            varifyMatching2();//call for varify, if puzzle is solved
        }

        private void checkButton2(ActionEvent e) {//check buttons neighbor

            int emptyButtonIndex = 0;
            for (MyButton button : buttons2) {
                if (button.isEmptyButton()) {//check for empty button index
                    emptyButtonIndex = buttons2.indexOf(button);
                }
            }

            JButton button = (JButton) e.getSource();
            int clickedButtonIndex = buttons2.indexOf(button);//clicked button index

            //checking if empty button index is any side one of clicked button index
            if ((clickedButtonIndex - 1 == emptyButtonIndex) || (clickedButtonIndex + 1 == emptyButtonIndex)
                    || (clickedButtonIndex - 4 == emptyButtonIndex) || (clickedButtonIndex + 4 == emptyButtonIndex)) {
                //if yes, swap clicked button with empty button in arraylist
                Collections.swap(buttons2, clickedButtonIndex, emptyButtonIndex);
                updateButtons2();//call for update all buttons in panel
            }
        }

        private void updateButtons2() {//update all button on panel after swaping in arraylist
            gamePanel2.removeAll();
            for (JComponent btn : buttons2) {
                gamePanel2.add(btn);
            }
            gamePanel2.validate();
        }
    }

    private void varifyMatching2() {//varify solution
        
        ArrayList<Point> current = new ArrayList<Point>();

        for (JComponent btn : buttons2) { //copying buttons
            current.add((Point) btn.getClientProperty("position"));
        }
        if (compareList(buttonPoints2, current)) {//check if match two list
            timeObject.stopTime();//stop time while finished playing
            timeCount=timeObject.getTime();//get game played time and reset timeCount to 
            
            checkHighScore(); //go for check if this is a new high score 
            resetClickCount();//reset clickCount to 0
                    
        //    JOptionPane.showMessageDialog(gamePanel1, newPlayerName+" You have done well!",
        //            "Congratulation", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    // Game Panel2 setting ends here
    
    // Game Panel3 setting from here
    private void setGame3(){ //set game panel
        buttonPoints3.add(new Point(0, 0));
        buttonPoints3.add(new Point(0, 1));
        buttonPoints3.add(new Point(0, 2));
        buttonPoints3.add(new Point(0, 3));
        buttonPoints3.add(new Point(0, 4));
        buttonPoints3.add(new Point(1, 0));
        buttonPoints3.add(new Point(1, 1));
        buttonPoints3.add(new Point(1, 2));
        buttonPoints3.add(new Point(1, 3));
        buttonPoints3.add(new Point(1, 4));
        buttonPoints3.add(new Point(2, 0));
        buttonPoints3.add(new Point(2, 1));
        buttonPoints3.add(new Point(2, 2));
        buttonPoints3.add(new Point(2, 3));
        buttonPoints3.add(new Point(2, 4));
        buttonPoints3.add(new Point(3, 0));
        buttonPoints3.add(new Point(3, 1));
        buttonPoints3.add(new Point(3, 2));
        buttonPoints3.add(new Point(3, 3));
        buttonPoints3.add(new Point(3, 4));
        buttonPoints3.add(new Point(4, 0));
        buttonPoints3.add(new Point(4, 1));
        buttonPoints3.add(new Point(4, 2));
        buttonPoints3.add(new Point(4, 3));
        buttonPoints3.add(new Point(4, 4));

        buttons3 = new ArrayList<MyButton>();

        gamePanel3 = new JPanel();
        gamePanel3.setBorder(BorderFactory.createLineBorder(Color.gray));
        gamePanel3.setLayout(new GridLayout(5, 5, 0, 0));

        try {
            img3 = loadImage3(); //call for import image
            int h = getNewHeight(img3.getWidth(), img3.getHeight());//new hight following desired width1
            resizedImage3 = resizeImage(img3, newImageWidth, h, //resize image with new hight width1
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width3 = resizedImage3.getWidth(null);
        height3 = resizedImage3.getHeight(null);

        //add(gamePanel1, BorderLayout.CENTER);

        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {//crop image

                buttonImage3 = createImage(new FilteredImageSource(resizedImage3.getSource(),
                        new CropImageFilter(j * width3 / 5, i * height3 / 5,
                                (width3 / 5), height3 / 5)));
                MyButton button = new MyButton(buttonImage3);//create myButton type buttons and send part of image
                button.putClientProperty("position", new Point(i, j));//keep original point of cropped image parts

                if (i == 4 && j == 4) { //for one empty button in puzzle
                    emptyButton3 = new MyButton();
                    emptyButton3.setBorderPainted(false);
                    emptyButton3.setContentAreaFilled(false);
                    emptyButton3.setEmptyButton();
                    emptyButton3.putClientProperty("position", new Point(i, j));
                } else {
                    buttons3.add(button);//add button to arraylist
                }
            }
        }
        //makeShuffle();
        Collections.shuffle(buttons3);//randomly distribute position of image part
        buttons3.add(emptyButton3); //outside, keep empty button fixed

        for (int i = 0; i < 25; i++) {

            MyButton btn = buttons3.get(i);
            gamePanel3.add(btn); //add buttons to panel
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction3());//set actionlistener
        }
         
        pack();
    }

    private BufferedImage loadImage3() throws IOException {//read image

        BufferedImage bimg = ImageIO.read(new File("pic/sachin.jpg"));
        return bimg;
    }


    private class ClickAction3 extends AbstractAction {//while each game button clicked

        @Override
        public void actionPerformed(ActionEvent e) {
            
            startClickCount(); //any button clicked, then one clcked count
            
            checkButton3(e); //call checking to move button if neighbor is empty button
            varifyMatching3();//call for varify, if puzzle is solved
        }

        private void checkButton3(ActionEvent e) {//check buttons neighbor

            int emptyButtonIndex = 0;
            for (MyButton button : buttons3) {
                if (button.isEmptyButton()) {//check for empty button index
                    emptyButtonIndex = buttons3.indexOf(button);
                }
            }

            JButton button = (JButton) e.getSource();
            int clickedButtonIndex = buttons3.indexOf(button);//clicked button index

            //checking if empty button index is any side one of clicked button index
            if ((clickedButtonIndex - 1 == emptyButtonIndex) || (clickedButtonIndex + 1 == emptyButtonIndex)
                    || (clickedButtonIndex - 5 == emptyButtonIndex) || (clickedButtonIndex + 5 == emptyButtonIndex)) {
                //if yes, swap clicked button with empty button in arraylist
                Collections.swap(buttons3, clickedButtonIndex, emptyButtonIndex);
                updateButtons3();//call for update all buttons in panel
            }
        }

        private void updateButtons3() {//update all button on panel after swaping in arraylist
            gamePanel3.removeAll();
            for (JComponent btn : buttons3) {
                gamePanel3.add(btn);
            }
            gamePanel3.validate();
        }
    }

    private void varifyMatching3() {//varify solution
        
        ArrayList<Point> current = new ArrayList<Point>();

        for (JComponent btn : buttons3) { //copying buttons
            current.add((Point) btn.getClientProperty("position"));
        }
        if (compareList(buttonPoints3, current)) {//check if match two list
            timeObject.stopTime();//stop time while finished playing
            timeCount=timeObject.getTime();//get game played time and reset timeCount to 
            
            checkHighScore(); //go for check if this is a new high score 
            resetClickCount();//reset clickCount to 0
                    
        //    JOptionPane.showMessageDialog(gamePanel1, newPlayerName+" You have done well!",
        //            "Congratulation", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    //Game Panel3 setting ends here
}