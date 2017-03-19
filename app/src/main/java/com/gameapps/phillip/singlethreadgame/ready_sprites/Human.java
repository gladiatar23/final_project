package com.gameapps.phillip.singlethreadgame.ready_sprites;

import android.util.Size;

import com.gameapps.phillip.singlethreadgame.GameActivity;
import com.gameapps.phillip.singlethreadgame.R;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Location;
import com.gameapps.phillip.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by user on 11/01/2017.
 */

public class Human extends Sprite {

//This function provides definitions (height and width, position on the screen, an image), the main character.
    public Human(GameActivity.SpriteEssentialData spriteEssentialData) {
        super(spriteEssentialData, 0, 0, 0, 0);

        this.location = new Location(spriteEssentialData.canvasSize.x/14 , spriteEssentialData.canvasSize.y * 4/7);
        double hToWRatio = 2;
        int height = spriteEssentialData.canvasSize.y/3;
        this.size = new Size((int)(height/hToWRatio) , height);

        setImage(R.drawable.obama);


    }


    @Override
    public void change() {

    }
//The function receives location coordinates of the click on the screen and calculates arc tangent a bullet fired.
    public void shootBullet(int xToShootAt , int yToShootAt) {

        double distanceX = xToShootAt - location.getX();
        double distanceY = yToShootAt - location.getY();

        double angle = Math.atan(distanceY / distanceX);

        spriteEssentialData.spriteCreator.createBullet(location.getX() ,
                location.getY(),
                angle
                );
    }

}
