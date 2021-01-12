/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41.game;

import com.bruynhuis.galago.app.BaseApplication;
import com.bruynhuis.galago.games.basic.BasicGame;
import com.bruynhuis.galago.games.basic.BasicPlayer;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.Collections;
import java.util.Stack;

/**
 *
 * @author NideBruyn
 */
public class Game extends BasicGame {

    private static final String TYPE = "type";
    private static final String MARKER = "marker";
    private Stack<Vector3f> waypoints;
    private Vector3f sunDirection = new Vector3f(0.3f, -0.8f, -0.65f);
    private int laps = 10;
    private BasicPlayer winner;
    private DirectionalLightShadowRenderer dlsf;
    private PointLight light;

    public Game(BaseApplication baseApplication, Node rootNode, int laps) {
        super(baseApplication, rootNode);
        this.laps = laps;
    }

    public int getTotalLaps() {
        return laps;
    }

    public BasicPlayer getWinner() {
        return winner;
    }

    public void setWinner(BasicPlayer winner) {
        this.winner = winner;
    }

    @Override
    public void init() {

        Spatial scene = baseApplication.getAssetManager().loadModel("Scenes/track.j3o");
        scene.setShadowMode(RenderQueue.ShadowMode.Receive);
        levelNode.attachChild(scene);

        waypoints = new Stack<>();
        registerWaypoints();
        Collections.reverse(waypoints);

//        light = new PointLight(new Vector3f(0, 50, 0));
//        rootNode.addLight(light);
        sunLight = new DirectionalLight();
        sunLight.setColor(ColorRGBA.White);
        sunLight.setDirection(sunDirection.normalizeLocal());
        levelNode.addLight(sunLight);

        dlsf = new DirectionalLightShadowRenderer(baseApplication.getAssetManager(), 512, 1);
        dlsf.setLight(sunLight);
        baseApplication.getViewPort().addProcessor(dlsf);

    }

    private void registerWaypoints() {
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            public void visit(Spatial spatial) {
//                log("Spatial: " + spatial.getName());

                if (spatial.getUserData(TYPE) != null && spatial.getUserData(TYPE).equals(MARKER)) {

//                    log("Waypoint found: " + spatial.getWorldTranslation());
                    waypoints.push(spatial.getWorldTranslation().clone());
                    spatial.removeFromParent();

                }

            }
        };

        rootNode.depthFirstTraversal(sgv);
    }

    public Stack<Vector3f> getCopyOfWaypoints() {
        //TODO: We need to make a copy of the waypoints

        return (Stack<Vector3f>) waypoints.clone();
    }

    @Override
    public void close() {
        baseApplication.getViewPort().removeProcessor(dlsf);
        super.close();
    }

    public boolean isGameOver() {
        return winner != null;
    }

}
