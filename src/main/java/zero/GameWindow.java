package zero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 560;
    private static final int WINDOW_WIDTH = 560;
    private static final int WINDOW_POSX = 800;
    private static final int WINDOW_POSY = 300;
    Map map;
    SettingWindow settingWindow;
    JButton btnStart = new JButton("New Game");
    JButton btnExit = new JButton("Exit");
    GameWindow(){
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("TicTacToe");
        setResizable(false);
        map = new Map();
        JPanel panBottom = new JPanel(new GridLayout(1,2));
        panBottom.add(btnStart);
        panBottom.add(btnExit);
        add(panBottom, BorderLayout.SOUTH);
        add(map, BorderLayout.CENTER);
        settingWindow = new SettingWindow(this);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingWindow.setVisible(true);
            }
        });
        setVisible(true);
    }
    public boolean settingsWindowShow(){
        return true;
    }
    public void startNewGame(){
        int genmode=0;
        if(this.settingWindow.hVSai.isSelected()){
            genmode=0;
        } else if (this.settingWindow.hVSh.isSelected()) {
            genmode=1;
        }
        int fieldsizeX = this.settingWindow.sFieldX.getValue();
        int fieldsizeY = this.settingWindow.sFieldY.getValue();

        int winLength = this.settingWindow.sLength.getValue();
        this.map.startNewGame(genmode, fieldsizeX, fieldsizeY, winLength);
    }

}
