/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover.objects;

import cg.farmhover.Model;
import cg.farmhover.TestScene;
import static cg.farmhover.TestScene.ORIGIN;
import static cg.farmhover.TestScene.terrain;
import cg.farmhover.gl.util.Shader;
import java.io.File;

/**
 *
 * @author Barbara
 */
public class SceneObjectFactory {
    static Model barn_model, old_house_model;
   public enum ObjectType {
        BARN,
        OLD_HOUSE
  };
   public static void init(){
       barn_model = new Model(new File(".\\models\\barnblender.obj"));
       old_house_model = new Model(new File(".\\models\\house3blender.obj"));
   }
  public static SceneObject getInstance(ObjectType type, float x, float z) {
      // o y e as scales vai ser setada aqui dentro
    if (type == ObjectType.BARN) {
      return new SceneObject(barn_model, x, terrain.getHeightofTerrain(x, z), z, 5f, 5f, 5f);
    } else if (type == ObjectType.OLD_HOUSE) {
      return new SceneObject(old_house_model, x, terrain.getHeightofTerrain(x,z), z, 5f, 5f, 5f);
    }
    return null;
  }
}

//barn = new SceneObject(".\\models\\barnblender.obj", 500f, terrain.getHeightofTerrain(500,510), 510f, 5f, 5f, 5f);
//        house3 = new SceneObject(".\\models\\house3blender.obj", 515f, terrain.getHeightofTerrain(515,500), 500f, 5f, 5f, 5f);
//        objects.add(barn);
//        objects.add(house3);
//
//        ufo = new Ufo();
//        cam = new Camera(ufo);
//        farm = new JWavefrontObject(new File(".\\models\\cube.obj"));
