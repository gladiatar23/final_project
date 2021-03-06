package com.gameapps.alex.singlethreadgame.ready_sprites;

import android.graphics.Rect;

import com.gameapps.alex.singlethreadgame.activities.GameActivity;
import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.MyMath;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.sprite_definition.LosingNPCSprite;
import com.gameapps.alex.singlethreadgame.sprite_definition.Sprite;

/**
 * Created by user on 11/01/2017.
 */

public class Enemy extends Sprite implements LosingNPCSprite {
    private static int ITERATIONS_PERSISTING_VETICAL;//ITERATIONS PERSISTING VERTICAL (fps)

    private boolean isFramedForKillingPoorHuman;
    private boolean isStillMoving;

    private long iterationsExisted;

    private int MAX_VERTICAL_SPEED;
    private int horizontalSpeed;
    private int verticalSpeed;

    protected long hitPoints;
//Function creates enemies, some of the features of their logical (as vertical speed), and adds a picture
    public Enemy(EnemyType type , GameActivity.SpriteEssentialData spriteEssentialData, int centerX, int centerY, int width, int height) {
        super(spriteEssentialData, centerX, centerY, width, height);

        ITERATIONS_PERSISTING_VETICAL = type.iterationsToChangeVertical;
        MAX_VERTICAL_SPEED = type.maxVerticalSpeed;
        horizontalSpeed = type.maxHorizontalSpeed;

        ctor(type.drawableID);

    }

    public Enemy(GameSession.Human humanEnum , GameActivity.SpriteEssentialData spriteEssentialData, int centerX, int centerY, int width, int height) {
        super(spriteEssentialData, centerX, centerY, width, height);
    }

    private void ctor(int imageId) {

        iterationsExisted = 0;
        verticalSpeed = 0;
        isRemovedWhenOffScreen = false;
        isStillMoving = true;
        isFramedForKillingPoorHuman = false;

        hitPoints = 1;

        setImage(imageId);
    }

    @Override
    public void change() {
        if(!isStillMoving)
            return;


        if(isFramedForKillingPoorHuman) {
            if(moveToMiddle()) {
                spriteEssentialData.spriteCreator.createBars(this);
                isStillMoving = false;
            }
        }
        else {
            location.setX(location.getX() - horizontalSpeed);
            if ((++iterationsExisted) % ITERATIONS_PERSISTING_VETICAL == 0) {
                verticalSpeed = MyMath.getRandomUpToIncludingNegative(MAX_VERTICAL_SPEED);
            }
            location.setY(location.getY() + verticalSpeed);
        }

    }

    public void frameForKillingPoorHuman() {isFramedForKillingPoorHuman = true;}
//The function returns parameters (middle of the screen) to go to the enemy who killed the hero
    @Override
    public boolean moveToMiddle() {
        //a single point
        Rect r = new Rect(
                spriteEssentialData.canvasSize.x / 2 ,
                spriteEssentialData.canvasSize.y / 2 ,
                spriteEssentialData.canvasSize.x / 2 ,
                spriteEssentialData.canvasSize.y / 2
        );
        if(MyMath.areRectanglesIntersecting(r , this.getMinimalInsideRect()))
            return true;   //reached middle


//Calculated Correction to Arrival way to center of the screen to the enemy that hit the main hero
        int directionX = (spriteEssentialData.canvasSize.x / 2) - location.getX();
        int directionY = (spriteEssentialData.canvasSize.y / 2) - location.getY();
        if(directionX < 0) {directionX = Math.min((directionX/MINIMAL_INTERSECTIONS_SIZE)+1 , -MINIMAL_MOVE_SPEED);}
        else {directionX = Math.max((directionX/MINIMAL_INTERSECTIONS_SIZE)+1 , MINIMAL_MOVE_SPEED);}
        if(directionY < 0) {directionY = Math.min((directionY/MINIMAL_INTERSECTIONS_SIZE)+1 , -MINIMAL_MOVE_SPEED);}
        else {directionY = Math.max((directionY/MINIMAL_INTERSECTIONS_SIZE)+1 , MINIMAL_MOVE_SPEED);}


        location.setX(location.getX() + directionX);
        location.setY(location.getY() + directionY);


        return false;

    }

    public enum EnemyType {
        JIHADIST(R.drawable.karatedoglr , 70 , 3 , 6),
        TURTLE(R.drawable.cheripashka_mario, 80 , 3 , 6),
        SHVARCNEGER(R.drawable.terminatorh_r , 90 , 3 , 6),
        WAZE(R.drawable.waze , 90 , 5 , 10),
        VAMINYON(R.drawable.minyon , 90 , 6 , 14),
        BENDEL(R.drawable.bendel_r , 90 , 7 , 16),
        HAUNTER(R.drawable.haunter, 90 , 8 , 18),
        SOLDIER(R.drawable.soldier, 90, 9, 20)
        ;

        int drawableID;
        int iterationsToChangeVertical;//ITERATIONS PERSISTING VERTICAL (fps)

        int maxVerticalSpeed;
        int maxHorizontalSpeed;

        EnemyType(int drawableID , int iterationsToChangeVertical , int maxVerticalSpeed , int maxHorizontalSpeed) {
            this.drawableID = drawableID;
            this.iterationsToChangeVertical = iterationsToChangeVertical;
            this.maxVerticalSpeed = maxVerticalSpeed;
            this.maxHorizontalSpeed = maxHorizontalSpeed;
        }
    }

    public void flagForRemovalDead() {
        super.flagForRemoval();
        spriteEssentialData.gameSession.handleOnEnemySpriteRemoval(this);
    }

    public void flagForRemovalScreenConstraints() {
        super.flagForRemoval();
    }

    public long getHitPoints() {return hitPoints;}

    public void decHitPoints() {
        this.hitPoints--;

        if(hitPoints <= 0) {
            flagForRemovalDead();
        }
    }

    public enum EnemyBoss {
        TERMINATOR(R.drawable.waze , 100 , 5 , 7),
        ROBORABI(R.drawable.frady_l, 1000 , 3 , 26),
        MARIO(R.drawable.karatedoglr , 1000 , 3 , 26),
        MINYON(R.drawable.terminatorr_r , 1000 , 3 , 26),
        MOTARO(R.drawable.motaro_r, 1000 , 3 , 26),
        BEAR(R.drawable.goblin_r , 1000 , 6 , 7),
        ;

        int drawableID;
        int iterationsToChangeVertical;//ITERATIONS PERSISTING VERTICAL (fps)

        int maxVerticalSpeed;
        int maxHorizontalSpeed;

        EnemyBoss(int drawableID , int iterationsToChangeVertical , int maxVerticalSpeed , int maxHorizontalSpeed) {
            this.drawableID = drawableID;
            this.iterationsToChangeVertical = iterationsToChangeVertical;
            this.maxVerticalSpeed = maxVerticalSpeed;
            this.maxHorizontalSpeed = maxHorizontalSpeed;
        }
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "isFramedForKillingPoorHuman=" + isFramedForKillingPoorHuman +
                ", isStillMoving=" + isStillMoving +
                ", iterationsExisted=" + iterationsExisted +
                ", MAX_VERTICAL_SPEED=" + MAX_VERTICAL_SPEED +
                ", horizontalSpeed=" + horizontalSpeed +
                ", verticalSpeed=" + verticalSpeed +
                ", hitPoints=" + hitPoints +
                '}';
    }
}
