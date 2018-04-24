/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld41;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;

/**
 *  
 *  
 *
 * @author NideBruyn 
 */
public class Test extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        rootNode.attachChild(assetManager.loadModel("Scenes/test.j3o"));

        cam.setLocation(new Vector3f(-20, 20, 20));
        cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.start();
    }

}
