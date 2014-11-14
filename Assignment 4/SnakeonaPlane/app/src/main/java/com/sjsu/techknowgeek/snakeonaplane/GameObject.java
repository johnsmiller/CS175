package com.sjsu.techknowgeek.snakeonaplane;

/**
 * Created by John on 11/13/2014.
 */
public class GameObject {
    //direction
        //0 = stationary
        //1 = WEST
        //2 = NORTH
        //3 = EAST
        //4 = SOUTH
        int direction;
    //speed
        //increment by 1
        int speed;
    //location
        int x;
        int y;

    public GameObject(int initDir, int initSpd, int initX, int initY)
    {
        direction = initDir;
        speed = initSpd;
        x = initX;
        y = initY;
    }

    public void move() {
        switch (direction) {
            case 1:
                x += speed;
            case 2:
                y += speed;
            case 3:
                x -= speed;
            case 4:
                y -= speed;
            default:
                break;
        }
    }

    public void rotateRight() {
        switch (direction) {
            case 0:
                break;
            case 1:
                direction = 4;
            default:
                direction--;
        }
    }

    public void rotateLeft() {
        switch (direction) {
            case 0:
                break;
            case 4:
                direction = 1;
            default:
                direction++;
        }
    }

    public void increaseSpeed() {
        speed++;
    }

    public void decreaseSpeed() {
        speed--;
    }

    public void resetSpeed() {
        speed = 0;
    }

}
