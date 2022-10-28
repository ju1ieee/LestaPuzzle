package com.company;

import com.javarush.engine.cell.*;
import javafx.stage.Stage;

import java.util.*;

public class LestaGame extends Game {
    private static final int SIDE = 7;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private boolean isGameStopped=false;
    private int score = 0;
    private static final String SELECTED = "\u2728";
    private ArrayList<Color> colorArrayList = new ArrayList<Color>();
    private static GameObject objForSwap;

    @Override
    public void start(Stage primaryStage){
        super.start(primaryStage);
        primaryStage.setTitle("LestaPuzzle");
    }

    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();

    }

    private void createGame() {
        colorArrayFill();
        createField();
        score = 1000;
        setScore(score);
        drawScene();
    }

    private void createField(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                gameField[y][x] = creationGameObject(x, y);
            }
        }
    }

    private GameObject creationGameObject(int x, int y){
        boolean isBlocked = isBlockedInfo(x, y);
        Color color = isColoredInfo(x,y);
        if (color==null) color=isBlocked?Color.BLACK:getColor();
        return new GameObject(x,y,isBlocked,color);
    }

    private Boolean isBlockedInfo(int x, int y){
        if ((x==1 || x==3 || x==5)&& y>1) return false;     //фишки
        if ((x==2 && y==3) || (x==2 && y==5) || (x==4 && y==3) || (x==4 && y==5)) return false; //пустые клетки
        return true; //остальное заблочено
    }
    private Color isColoredInfo(int x, int y){
        if (x==1 && y==0) return Color.RED;
        if (x==3 && y==0) return Color.GREEN;
        if (x==5 && y==0) return Color.BLUE;
        if ((x==2 && y==3)||(x==2 && y==5)||(x==4 && y==3)||(x==4 && y==5)) return Color.WHITE; //пустые клетки
        if ((x==2 && y==2)||(x==2 && y==4)||(x==2 && y==6)||(x==4 && y==2)||(x==4 && y==4)||(x==4 && y==6)) return Color.BLACK;
        return null;
    }

    private void drawScene() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColor(x, y, gameField[y][x].color);
            }
        }
    }

    public Color getColor () {
        Color color = colorArrayList.get(0);
        colorArrayList.remove(0);
        return color;
    }

    public void colorArrayFill(){
        for (int i = 0; i < 5; i++) {
            colorArrayList.add(Color.RED);
            colorArrayList.add(Color.GREEN);
            colorArrayList.add(Color.BLUE);
        }
        Collections.shuffle(colorArrayList);
    }

    @Override
    public void onKeyPress(Key key){
        if ((isGameStopped)&&(key==Key.SPACE)) {
            isGameStopped=false;
            createGame();
            drawScene();}
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!isGameStopped) play(x, y);
    }

    private void play(int x, int y){
        GameObject go = gameField[y][x];
        if (go.isBlocked) return;
        if (objForSwap==null) {
                  setCellValue(go.x, go.y, SELECTED);
                  objForSwap=go;
                }
        else   { if (canSwap(go, objForSwap)) swap(go);}
        drawScene();
    }

    private boolean canSwap(GameObject o1, GameObject o2){
        if ((o1.color == Color.WHITE || o2.color == Color.WHITE) && !(o1.color.equals(o2.color)) &&
            ((o1.x==o2.x)&&(Math.abs(o1.y-o2.y)==1)  || ((Math.abs(o1.x-o2.x)==1)&&(o1.y==o2.y)&&(o1.y==3 || o1.y == 5)))      )
        {return true;}

        objForSwap=null;
        setCellValue(o1.x, o1.y, "");
        setCellValue(o2.x, o2.y, "");
        return false;
    }

    private void swap(GameObject go1){
        int x1 = go1.x;
        int y1 = go1.y;
        GameObject go2 = objForSwap;
        int x2 = go2.x;
        int y2 = go2.y;
        gameField[y1][x1] = new GameObject(x1, y1, go2.isBlocked, go2.color);
        setCellValue(x1, y1, "");
        gameField[y2][x2] = new GameObject(x2, y2, go1.isBlocked, go1.color);
        setCellValue(x2, y2, "");

        objForSwap=null;
        score-=10;
        setScore(score);
        if (isWin()) {win();}
        if (score==0) {gameOver();}
    }

    private boolean isWin(){
        boolean isWin = false;
        Set<Color> redSet = new HashSet<>();
        Set<Color> greenSet = new HashSet<>();
        Set<Color> blueSet = new HashSet<>();

        for (int y = 2; y < SIDE; y++) {
            for (int x = 1; x < 6; x+=2) {
                if (x==1) redSet.add(gameField[y][x].color);
                if (x==3) greenSet.add(gameField[y][x].color);
                if (x==5) blueSet.add(gameField[y][x].color);
            }
        }
        if (redSet.size()==1 && redSet.contains(Color.RED)){
            if (greenSet.size()==1 && greenSet.contains(Color.GREEN)) {
                if (blueSet.size() == 1 && blueSet.contains(Color.BLUE)) {
                    isWin=true;
                }
            }
        }
        return isWin;
    }

    private void win(){
        showMessageDialog(Color.ALICEBLUE, "WIN! \n Your score = " + score + " \n Press *space* to start new game!", Color.RED, 40);
        isGameStopped=true;
    }

    private void gameOver(){
        showMessageDialog(Color.RED, "GAME OVER \n Your score = 0 \n Press *space* to start new game!", Color.WHITESMOKE, 40);
        isGameStopped=true;
    }
}