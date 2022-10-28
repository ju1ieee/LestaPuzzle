package com.company;

import com.javarush.engine.cell.Color;

public class GameObject {
    public int x;
    public int y;
    public boolean isBlocked;
    public Color color;

    GameObject(int x, int y, boolean isBlocked, Color color){
        this.x = x;
        this.y = y;
        this.isBlocked = isBlocked;
        this.color = color;
    }
}
