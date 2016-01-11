/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MTI
 */

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
class MyButton extends JButton {

    private boolean isEmptyButton;
    
    public MyButton() {
        super();
        initUI();
    }
    public MyButton(Image image) {//take image as parameter while creating myButton class object
        super(new ImageIcon(image));
        initUI();
    }
    private void initUI() {

        isEmptyButton = false;
        BorderFactory.createLineBorder(Color.DARK_GRAY);

        addMouseListener(new MouseAdapter() { //set property for myButton class type Buttons

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.yellow));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.gray));
            }
        });
    }
    public void setEmptyButton() {//make a button identity empty
        
        isEmptyButton = true;
    }
    public boolean isEmptyButton() {//check a button identity if it is empty button

        return isEmptyButton;
    }
}
