/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.game;

import com.bruynhuis.galago.games.basic.BasicGame;
import com.bruynhuis.galago.games.basic.BasicPlayer;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.Stack;

/**
 *
 * @author NideBruyn
 */
public class Player extends BasicPlayer {

    private Spatial model;
    private Stack<Vector3f> waypoints;
    private float waypointReachDistance = 1f;
    private Vector3f direction;
    private float speed = 0;
    private float accel = 0.05f;
    private float targetSpeed = 10;
    private float maxSpeed = 16;
    private float turnSpeed = 0.12f;
    private Node tempNode;
    private int lapCount = 1;
    private boolean gameEnded = false;

    public Player(BasicGame basicGame) {
        super(basicGame);
    }

    public void increaseSpeed() {
        this.targetSpeed += 0.2f;
        if (this.targetSpeed > this.maxSpeed) {
            this.targetSpeed = this.maxSpeed;
        }
    }

    public void decreaseSpeed() {
        this.targetSpeed -= 0.2f;
        if (this.targetSpeed < 0) {
            this.targetSpeed = 0;
        }
    }

    @Override
    protected void init() {

        model = game.getBaseApplication().getAssetManager().loadModel("Models/player/player.j3o");
        model.setShadowMode(RenderQueue.ShadowMode.Cast);
        model.scale(0.4f);
        model.rotate(0, 0, 0);
        model.move(-1f, 0, 0);

        playerNode.attachChild(model);

        tempNode = new Node();
        
        waypoints = ((Game) game).getCopyOfWaypoints();
        playerNode.setLocalTranslation(waypoints.peek());
        playerNode.lookAt(waypoints.get(waypoints.size() - 2), Vector3f.UNIT_Y);

        playerNode.addControl(new AbstractControl() {
            @Override
            protected void controlUpdate(float tpf) {

                if ((game.isStarted() && !game.isPaused()) || gameEnded) {

                    //Adjust the speed
                    if (speed < targetSpeed) {
                        speed += accel;

                    } else if (speed > targetSpeed) {
                        speed -= accel;

                    }
                    
                    if (gameEnded && waypoints.isEmpty()) {
                        waypoints = ((Game) game).getCopyOfWaypoints();
                    }

                    if (getPosition().distance(waypoints.peek()) > waypointReachDistance) {

                        tempNode.setLocalTranslation(playerNode.getLocalTranslation().clone());
                        tempNode.setLocalRotation(playerNode.getLocalRotation().clone());
                        tempNode.lookAt(waypoints.peek(), Vector3f.UNIT_Y);

                        playerNode.getLocalRotation().slerp(tempNode.getLocalRotation(), turnSpeed);

                        //Move in the direction the player is facing
                        direction = playerNode.getLocalRotation().getRotationColumn(2).normalize();
                        playerNode.move(direction.mult(tpf * speed));

                    } else {
//                        Debug.log("Waypoint reached");
                        waypoints.pop();

                        if (waypoints.isEmpty()) {
                            if (((Game) game).getTotalLaps() == lapCount && !gameEnded) {
                                ((Game) game).setWinner(Player.this);
                                game.doGameCompleted();

                            } else {
                                waypoints = ((Game) game).getCopyOfWaypoints();
                                lapCount++;
                                addScore(1);
                            }
                        }

                    }

                }

            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {

            }
        });
    }

    @Override
    public Vector3f getPosition() {
        return playerNode.getWorldTranslation();
    }

    @Override
    public void doDie() {
    }

    public int getLapCount() {
        return lapCount;
    }

    public int getSpeed() {
        return (int) (speed * 10);
    }
    
    public void setGameEnded() {
        gameEnded = true;
        targetSpeed = 0;
    }
}
