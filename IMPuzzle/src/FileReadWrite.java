
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MTI
 */
public class FileReadWrite {

    private int row=6, col=4;
   // private String[] scoreFile=new String[col];
    private String[][] scoreFileMatrix=new String[row][col];
     public FileReadWrite(){ 
//         scoreFile[0]="Player Name";
//         scoreFile[1]="Time"; //by default those will be in MatrixBar
//         scoreFile[2]="Click";
//         scoreFile[3]="Level";
         
         for(int i=0; i<row; i++){ //very first on games beggining, time and click will be 0
             for(int j=0; j<col-1; j++){
                 if(j==0){
                     scoreFileMatrix[i][j]="";
                 }else{
                    scoreFileMatrix[i][j]=0+"";
                 }
             }
         }
         scoreFileMatrix[0][3]="Easy";//by default those will be in Matrix
         scoreFileMatrix[1][3]="Easy";
         scoreFileMatrix[2][3]="Medium";
         scoreFileMatrix[3][3]="Medium";
         scoreFileMatrix[4][3]="Hard";
         scoreFileMatrix[5][3]="Hard";
         
     }
    public void writeFile(String[][] newScoreMatrix){//take a matrix of score 
//         String scoreUpdate[][]=newScoreMatrix;    //write those in dat file
         
         File f=new File("ScoreBoard.dat");
         try{
            PrintWriter pw=new PrintWriter(f);
            for(int r=0; r<row; r++){
                for(int c=0; c<col; c++){
                    pw.println(newScoreMatrix[r][c]);
                }
            }
            pw.close();
        }catch(Exception e){}
    }
    
    public String[][] readFile(){ //read data from dat file to a matrix
        File f=new File("ScoreBoard.dat"); //and return the matrix
        try{
            Scanner kb=new Scanner(f);
            for(int r=0; r<row; r++){
                for(int c=0; c<col; c++){
                    String st=kb.nextLine();
                    scoreFileMatrix[r][c]=st;
                }
            }
            kb.close();
        }catch(IOException e){}
        return scoreFileMatrix;
    }
//    public String[] getmatrixBar(){//return MatrixBar array containg bar names
//        return scoreFile;
//    }
}
