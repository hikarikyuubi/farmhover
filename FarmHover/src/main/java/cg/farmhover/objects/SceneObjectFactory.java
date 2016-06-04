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
    static Model barn_model, farmhouse_model,shelter_model, scare_model,fence_model,corn_model,tractor_model,harvester_model;
   public enum ObjectType {
        BARN,
        FARMHOUSE,
        SHELTER,
        SCARE,
        FENCE,
        CORN,
        TRACTOR,
        HARVESTER
        
  };    
   public static void init(){
       barn_model = new Model(new File(".\\models\\barn3.obj"));
       farmhouse_model = new Model(new File(".\\models\\house3blender.obj"));
       shelter_model = new Model(new File(".\\models\\shelter.obj"));
       scare_model = new Model(new File(".\\models\\scarecrow.obj"));
       fence_model = new Model(new File(".\\models\\cerca2.obj"));
       //encontrar modelo de plantacao de milho
       corn_model = new  Model(new File(".\\models\\cube.obj"));
       harvester_model = new Model(new File(".\\models\\haver.obj"));
       tractor_model = new Model(new File(".\\models\\trator2.obj"));
   }
  public static SceneObject getInstance(ObjectType type, float x, float z) {
      // o y e as scales vai ser setada aqui dentro
    if (type == ObjectType.BARN) {
      return new SceneObject(barn_model, x, terrain.getHeightofTerrain(x, z), z, 5f, 5f, 5f);
    } else if (type == ObjectType.FARMHOUSE) {
      return new SceneObject(farmhouse_model, x, terrain.getHeightofTerrain(x,z)+5f, z, 5f, 5f, 5f);
    }else if (type == ObjectType.SHELTER) {
      return new SceneObject(shelter_model, x, terrain.getHeightofTerrain(x,z), z, 5f, 5f, 5f);
    }else if (type == ObjectType.SCARE) {
      return new SceneObject(scare_model, x, terrain.getHeightofTerrain(x,z), z, 2f, 2f, 2f);
    }else if (type == ObjectType.FENCE) {
      return new SceneObject(fence_model, x, 200f, z, 1f, 1f, 1f);
    }else if (type == ObjectType.CORN) {
      return new SceneObject(corn_model, x, terrain.getHeightofTerrain(x,z), z, 2f, 2f, 2f);
    }else if (type == ObjectType.HARVESTER) {
      return new SceneObject(harvester_model, x, terrain.getHeightofTerrain(x,z), z, 3f, 3f, 3f);
    }else if (type == ObjectType.TRACTOR) {
      return new SceneObject(tractor_model, x, terrain.getHeightofTerrain(x,z), z, 2.5f, 2.5f, 2.5f);
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
