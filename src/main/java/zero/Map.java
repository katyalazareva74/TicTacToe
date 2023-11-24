package zero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Map extends JPanel {
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;
    private static final int DOT_PADDING=5;
    public static final int MODE_HVSAI=0;
    public static final int MODE_HVSH=1;
    private static final int STATE_DRAW=0;
    private static final int STATE_WIN_HUMAN=1;
    private static final int STATE_WIN_AI=2;
    private static final String MSG_WIN_HUMAN="Победил игрок!";
    private static final String MSG_WIN_AI="Победил компьютер!";
    private static final String MSG_DRAW="Ничья!";
    private int fieldSizeY;
    private  int fieldSizeX;
    private  int wLen;
    private  int xE=1;
    private  int yE=1;
    private  int a, b, c, d;
    private  int countd;
    private  int countgd;
    private char[][] field;
    private int panelWidth;
    private int panelHeight;
    private int cellWidth;
    private int cellHeight;
    private int gameOverType;
    private boolean isgameOver;
    private boolean isInitialized;

    Map(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
        isInitialized=false;
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        render(g);
    }
    private  void update(MouseEvent e){
        if(isgameOver||!isInitialized)
            return;
        int cellX = e.getX()/cellWidth;
        int cellY= e.getY()/cellHeight;
        if(!isValidCell(cellX, cellY)||!isEmptyCell(cellX, cellY))
            return;
        field[cellY][cellX]=HUMAN_DOT;
        if(checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN))
            return;
        aiTurn(cellY, cellX);
        repaint();
        if(checkEndGame(AI_DOT, STATE_WIN_AI))
            return;
        repaint();
    }
    public boolean checkEndGame(int dot, int gameOverType){
        if(checkWin(dot)){
            this.gameOverType=gameOverType;
            isgameOver=true;
            repaint();
            return  true;
        }
        if(isMapFull()){
            this.gameOverType=STATE_DRAW;
            isgameOver=true;
            repaint();
            return true;
        }
        return false;
    }
    private void render(Graphics g){
        if(!isInitialized)
            return;
        panelWidth=getWidth();
        panelHeight=getHeight();
        cellHeight=panelHeight/fieldSizeY;
        cellWidth=panelWidth/fieldSizeX;
        g.setColor(Color.BLACK);
        for(int h=0; h<fieldSizeY; h++){
            int y=h*cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }
        for(int w=0; w<fieldSizeX; w++){
            int x=w*cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }
        for (int y = 0; y < fieldSizeY ; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if(field[y][x]==EMPTY_DOT) continue;
                if(field[y][x]==HUMAN_DOT){
                    g.setColor(Color.ORANGE);
                    g.fillOval(x*cellWidth+DOT_PADDING, y*cellHeight+DOT_PADDING, cellWidth-DOT_PADDING*2, cellHeight-DOT_PADDING*2);
                } else if (field[y][x]==AI_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x*cellWidth+DOT_PADDING, y*cellHeight+DOT_PADDING, cellWidth-DOT_PADDING*2, cellHeight-DOT_PADDING*2);
                }
                else{
                    throw new RuntimeException("Unexpectied value"+field[y][x]+" in cell x="+x+" y="+y);
                }
            }

        }
        if(isgameOver)
            showMessageGameOver(g);
    }
    private void showMessageGameOver(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(0, 200, getHeight()+80, 70);
        g.setColor(Color.RED);
        g.setFont(new Font("Times new roman", Font.BOLD, 40));
        switch (gameOverType) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW,70, getHeight()/2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN,20, getHeight()/2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI,10, getHeight()/2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state "+gameOverType);
        }
    }
    void startNewGame(int mode, int fSzX, int fSzY, int wLn){
        initMap(fSzY, fSzX);
        fieldSizeY=fSzY;
        fieldSizeX=fSzX;
        wLen=wLn;
        isgameOver=false;
        isInitialized=true;
        xE=fSzX-wLn+1;
        yE=fSzY-wLn+1;
        if(fSzX>=fSzY){
            c=yE;
            countgd=fSzX-fSzY+1;
            countd=fSzY-wLen;
            a=1;
            b=countgd;
        }
        else{
            c=xE;
            countgd=fSzY-fSzX+1;
            countd=fSzX-wLen;
            a=countgd;
            b=1;
        }
        System.out.printf("Mode: %d;\nSize X=%d, Y=%d;\nWinLength: %d\n", mode, fSzX, fSzY, wLen);
        System.out.println("initialized");
        repaint();
    }
    private void initMap(int fieldSizeY, int fieldSizeX){
        field = new char[fieldSizeY][fieldSizeX];
        for (int i = 0; i < fieldSizeY ; i++) {
            for (int j = 0; j < fieldSizeX ; j++) {
                field[i][j]=EMPTY_DOT;
            }
        }
    }
    private boolean isValidCell(int x, int y){
        return (x>=0)&&(x<fieldSizeX)&&(y>=0)&&(y<fieldSizeY);
    }
    private boolean isEmptyCell(int x, int y){
        return  field[y][x]==EMPTY_DOT;
    }
    private void aiTurn(int y, int x){
        if((x-1)>=0 && (y-1)>=0 && (x+1)<fieldSizeX && (y+1)<fieldSizeY){
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    if(isEmptyCell(i, j)){
                        field[j][i] = AI_DOT;
                        return;
                    }
                }
            }
        }
        if((x-1)<0){
            if((y-1)<0){
                for (int i = x; i <= x+1; i++) {
                    for (int j = y; j <= y+1; j++) {
                        if(isEmptyCell(i, j)) {
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }
            } else if ((y+1)<=(fieldSizeY-1)) {
                for (int i = x; i <= x+1; i++) {
                    for (int j = y-1; j <= y+1; j++) {
                        if(isEmptyCell(i, j)){
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }
            } else if ((y+1)>(fieldSizeY-1)) {
                for (int i = x; i <= x+1; i++) {
                    for (int j = y-1; j <= y; j++) {
                        if(isEmptyCell(i, j)){
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }
            }

        }
        if((y+1)>(fieldSizeY-1)){
            if(((x+1)<fieldSizeX)&&((x-1)>=0)){
                for (int i = x-1; i <= x+1; i++) {
                    for (int j = y-1; j <= y; j++) {
                        if(isEmptyCell(i, j)){
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }
            }
        }
        if((x+1)>(fieldSizeX-1)){
            if((y-1)<0){
                for (int i = x-1; i <= x; i++) {
                    for (int j = y; j <= y+1; j++) {
                        if(isEmptyCell(i, j)) {
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }

            } else if ((y+1)<=(fieldSizeY-1)) {
                for (int i = x-1; i <= x; i++) {
                    for (int j = y-1; j <= y+1; j++) {
                        if(isEmptyCell(i, j)){
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }

            } else if ((y+1)>(fieldSizeY-1)) {
                for (int i = x-1; i <= x; i++) {
                    for (int j = y-1; j <= y; j++) {
                        if(isEmptyCell(i, j)) {
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }
                }

            }

        }
        if((y-1)<0){
            if(((x+1)<fieldSizeX)&&((x-1)>=0)){
                for (int i = x-1; i <= x+1; i++) {
                    for (int j = y; j <= y+1; j++) {
                        if(isEmptyCell(i, j)){
                            field[j][i] = AI_DOT;
                            return;
                        }
                    }

                }
            }
        }
    }
    private boolean checkWin(int d){
        int wd=0;
        for (int x = 0; x < fieldSizeX ; x++) {
            for (int i = 0; i < yE; i++) {
                for (int y = 0; y < wLen; y++) {
                    if(field[y+i][x]==d)
                        wd++;
                }
                if(wd==wLen){
                    return true;}
                wd=0;
            }
            wd=0;
        }
        wd=0;
        for (int y = 0; y < fieldSizeY ; y++) {
            for (int i = 0; i < xE; i++) {
                for (int x = 0; x < wLen; x++) {
                    if(field[y][x+i]==d)
                        wd++;
                }
                if(wd==wLen){
                    return true;}
                wd=0;
            }
        }
        wd=0;
        int ye=c-1;
        for (int i = 0; i < countd; i++) {
            for (int j = 0; j < ye; j++) {
                for (int y = 0; y < wLen; y++) {
                    if (field[y+j+i+a][fieldSizeX-1-y-j]==d)
                        wd++;
                }
                if(wd==wLen)
                    return true;
                wd=0;
            }
            wd=0;
            ye--;
        }
        wd=0;
        ye=c-1;
        for (int i = 0; i < countd; i++) {
            for (int j = 0; j < ye; j++) {
                for (int y = 0; y < wLen; y++) {
                    if (field[y+j][fieldSizeX-1-y-j-b-i]==d)
                        wd++;
                }
                if(wd==wLen)
                    return true;
                wd=0;
            }
            wd=0;
            ye--;
        }
        wd=0;
        ye=c-1;
        for (int i = 0; i < countd; i++) {
            for (int j = 0; j < ye; j++) {
                for (int y = 0; y < wLen; y++) {
                    if (field[y+j+i+a][y+j]==d)
                        wd++;
                }
                if(wd==wLen)
                    return true;
                wd=0;
            }
            wd=0;
            ye--;
        }
        wd=0;
        ye=c-1;
        for (int i = 0; i < countd; i++) {
            for (int j = 0; j < ye; j++) {
                for (int y = 0; y < wLen; y++) {
                    if (field[y+j][b+y+j+i]==d)
                        wd++;
                }
                if(wd==wLen)
                    return true;
                wd=0;
            }
            wd=0;
            ye--;
        }
        if(fieldSizeX>=fieldSizeY){
            wd=0;
            for (int i = 0; i < countgd; i++) {
                for (int j = 0; j <yE; j++) {
                    for (int y = 0; y < wLen; y++) {
                        if(field[y+j][fieldSizeX-1-y-j-i]==d)
                            wd++;
                    }
                    if(wd==wLen){
                        return true;}
                    wd=0;
                }
            }
            wd=0;
            for (int i = 0; i < countgd; i++) {
                for (int j = 0; j <yE; j++) {
                    for (int y = 0; y < wLen; y++) {
                        if(field[y+j][y+j+i]==d)
                            wd++;
                    }
                    if(wd==wLen){
                        return true;}
                    wd=0;
                }
            }
        }
        if(fieldSizeY>fieldSizeX){
            wd=0;
            for (int i = 0; i < countgd; i++) {
                for (int j = 0; j <xE; j++) {
                    for (int y = 0; y < wLen; y++) {
                        if(field[y+j+i][fieldSizeX-1-y-j]==d)
                            wd++;
                    }
                    if(wd==wLen){
                        return true;}
                    wd=0;
                }
            }
            wd=0;
            for (int i = 0; i < countgd; i++) {
                for (int j = 0; j <xE; j++) {
                    for (int y = 0; y < wLen; y++) {
                        if(field[y+j+i][y+j]==d)
                            wd++;
                    }
                    if(wd==wLen){
                        return true;}
                    wd=0;
                }
            }
        }
        return  false;
    }
    private boolean isMapFull(){
        for (int i = 0; i < fieldSizeY ; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if(field[i][j]==EMPTY_DOT)
                    return  false;
            }
        }
        return true;
    }
}
