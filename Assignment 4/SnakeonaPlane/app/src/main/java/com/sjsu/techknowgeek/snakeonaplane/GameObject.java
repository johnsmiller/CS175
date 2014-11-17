package com.sjsu.techknowgeek.snakeonaplane;

/**
 * Created by John on 11/13/2014.
 */
public class GameObject {
    //direction
        //0 = stationary
        //1 = EAST
        //2 = NORTH
        //3 = WEST
        //4 = SOUTH
        private int direction;
    //speed
        //increment by 1
        private int speed;
    //location
        private int x;
        private int y;
        
    //type
        private objectType type;

    /**
     *
     * @param initDir
     * @param initSpd
     * @param initX
     * @param initY
     * @param objType 0 for snake head, 1 for tail, 2 for wall
     */
    public GameObject(int initDir, int initSpd, int initX, int initY, int objType)
    {
        direction = initDir;
        speed = initSpd;
        x = initX;
        y = initY;
        
        switch(objType) {
            case 0: 
                type = objectType.head;
                break;
            case 1: 
                type = objectType.tail;
                break;
            default: 
                type = objectType.wall;
                break;
        }
    }

    /**
     * move *speed* units in current direction
     */
    public void move() {
        switch (direction) {
            case 1:
                x += speed;
                break;
            case 2:
                y += speed;
                break;
            case 3:
                x -= speed;
                break;
            case 4:
                y -= speed;
                break;
            default:
                break;
        }
    }

    /**
     * rotate direction of travel 90 degrees clockwise
     */
    public void rotateRight() {
        switch (direction) {
            case 0:
                break;
            case 1:
                direction = 4;
                break;
            default:
                direction--;
                break;
        }
    }

    /**
     * rotate direction of travel 90 degrees counter-clockwise
     */
    public void rotateLeft() {
        switch (direction) {
            case 0:
                break;
            case 4:
                direction = 1;
                break;
            default:
                direction++;
                break;
        }
    }

    /**
     * increase speed by 1 unit
     */
    public void increaseSpeed() {
        speed++;
    }

    /**
     * decrease speed by 1 unit
     */
    public void decreaseSpeed() {
        speed--;
    }

    /**
     * reset speed to 1
     */
    public void resetSpeed() {
        speed = 1;
    }

    public void resetDirection()
    {
        direction = 1;
    }

    /**
     * Check if current object and given object currently occupy the same space
     * @param obj object to compare location with
     * @return true if collision has occured
     */
    public boolean isCollision(GameObject obj)
    {
        return (x == obj.x && y == obj.y);
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public objectType getType() {
        return type;
    }
    
    public enum objectType{
        head, tail, wall;
    }
}
