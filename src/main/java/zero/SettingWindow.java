package zero;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingWindow extends JFrame {
    private static final int WINDOW_WIDTH = 440;
    private static final int WINDOW_HEIGHT = 440;
    JButton btnStart = new JButton("Start New Game");
    JPanel panBottom;
    JLabel lbgenMode = new JLabel("Выберете режим игры:");
    JLabel lbSizeField = new JLabel("Выберете размер поля для игры:");
    JLabel lbSetFieldX = new JLabel("Установленный размер поля по горизонтали:");
    JLabel lbSetFieldY = new JLabel("Установленный размер поля по вертикали:");
    JLabel lbLength = new JLabel("Выберете длину для победы:");
    JLabel lbSetLength = new JLabel("Установленная длина:");
    JSlider sFieldX = new JSlider(3, 10);
    JSlider sFieldY = new JSlider(3, 10);
    JSlider sLength = new JSlider(3, 10);
    JRadioButton hVSai = new JRadioButton("Человек против компьютера");
    JRadioButton hVSh = new JRadioButton("Человек против человека");
    ButtonGroup bghVS = new ButtonGroup();
    SettingWindow(GameWindow gameWindow){
        setLocation(gameWindow.getWidth()/2-WINDOW_WIDTH/2+ gameWindow.getX(), gameWindow.getHeight()/2-WINDOW_HEIGHT/2+gameWindow.getY());
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        sLength.setMajorTickSpacing(1);
        sFieldY.setMajorTickSpacing(1);
        sFieldX.setMajorTickSpacing(1);
        sFieldX.setPaintLabels(true);
        sFieldX.setPaintTicks(true);
        sFieldY.setPaintLabels(true);
        sFieldY.setPaintTicks(true);
        sLength.setPaintLabels(true);
        sLength.setPaintTicks(true);
        panBottom = new JPanel(new GridLayout(11, 1));
        bghVS.add(hVSai);
        bghVS.add(hVSh);
        panBottom.add(lbgenMode);
        panBottom.add(hVSai);
        panBottom.add(hVSh);
        panBottom.add(lbSizeField);
        panBottom.add(lbSetFieldX);
        panBottom.add(sFieldX);
        panBottom.add(lbSetFieldY);
        panBottom.add(sFieldY);
        panBottom.add(lbLength);
        panBottom.add(lbSetLength);
        panBottom.add(sLength);
        sFieldX.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lbSetFieldX.setText("Установленный размер поля по горизонтали:  "+sFieldX.getValue());
                sLength.setMaximum((sFieldX.getValue()<=sFieldY.getValue())? sFieldX.getValue() : sFieldY.getValue());
            }
        });
        sFieldY.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lbSetFieldY.setText("Установленный размер поля по вертикали:  "+sFieldY.getValue());
                sLength.setMaximum((sFieldX.getValue()<=sFieldY.getValue())? sFieldX.getValue() : sFieldY.getValue());
            }
        });
        sLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lbSetLength.setText("Установленная длина: "+sLength.getValue());
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame();
                setVisible(false);
            }
        });
        add(panBottom, BorderLayout.WEST);
        add(btnStart, BorderLayout.SOUTH);
    }
}
