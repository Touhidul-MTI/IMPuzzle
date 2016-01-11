
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MTI
 */
public class Time implements Runnable{
    private JLabel timeLabel;
    private int timeCount;
    private boolean timeRun=true;
    Thread t;
    public Time(JLabel timeLabel){ //take a JLabel as parameter
        t=new Thread(this);  //create new thread
        this.timeLabel=timeLabel;
        t.start();
    }
    public void run(){
        try{
            while(timeRun){  //while timeRun true, loop continue
                timeCount++;
                timeLabel.setText("Time: "+timeCount+" seconds");//set timeCount on label
                t.sleep(1000);  //sleep 1 seconds after each timeCount set
            }
        }catch(InterruptedException e){
            JOptionPane.showMessageDialog(timeLabel, e);
        }
    }
    public void stopTime(){ //stop the timeCount
        timeRun=false;
    }
    public void resetTime(){ //reset timeCount to 0
        timeCount=0;
        timeLabel.setText("Time: "+timeCount+" seconds");
    }
    public int getTime(){ //return timeCount and reset timeCount to 0
        int sendTime=timeCount;
        resetTime();
        return sendTime;
    }
}
