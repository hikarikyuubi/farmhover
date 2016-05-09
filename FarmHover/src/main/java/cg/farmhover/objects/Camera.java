/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.farmhover.objects;

import cg.farmhover.Util;

/**
 *
 * @author Hikari Kyuubi
 */
public class Camera {
    private int front;
    private float x, y, z;
    private float lookUpX, lookUpY, lookUpZ;
    private final float fixedDist, fixedDistY;
    
    public Camera (Ufo ufo) {
        this.fixedDistY = 0f;
        this.fixedDist = 8f;
        this.x = ufo.getX();
        this.y = ufo.getY() + fixedDistY;
        this.z = ufo.getZ() + fixedDist;
        
        this.lookUpX = this.lookUpZ = 0;
        this.lookUpY = 1;
        this.front = 1;
    }
    
    /**
     * Atualiza posição da câmera e sua visão. Parâmetros são usados na 
     * matriz perspectiva. Sobre os processos feitos, tem-se:
     *  
     * Para ponto look-at (Povni): 
     *   Corresponde à posição atual do ufo;
     * 
     * Para ponto look-from (Pcamera):
     *   fixedDist = distância fixa ("atrás") que a câmera ficará em relação 
     *   ao ufo. O ufo sempre ficará a frente da visão da câmera, mas ela
     *   sempre verá sua parte traseira;
     *   fixedDistY = elevação fixa em relação ao "topo" do ufo que a câmera 
     *   ficará. O ufo sempre ficará "abaixo" da visão da câmera, mas ela 
     *   sempre verá sua parte superior;
     * 
     * Para ponto look-up:
     *   sin e cos de Rz = componentes XZ e Y  (respectivamente) do vetor
     *   partindo da origem com angulo Rz em relação a Z;
     *   sin e cos de Ry = componentes X e Z (respectivamente) do vetor
     *   partindo da origem com angulo Ry em relação a Y;
     * 
     * @param ufo objeto referente ao "personagem jogável", o ovni
     */
    public void updatePosition(Ufo ufo) {
        //this.y = ufo.getY() + fixedDistY;
        this.y = ufo.getY() + fixedDistY * Util.sin(90-ufo.getRx());
        
        this.x = ufo.getX() + fixedDist * 
                (-1 * Util.sin(ufo.getRy()));
//        this.x = ufo.getX() - fixedDist * Util.cos(ufo.getRx()) 
//                * Util.sin(ufo.getRy());
        
        this.z = ufo.getZ() + fixedDist * Util.cos(ufo.getRy());
//        this.z = ufo.getZ() + fixedDist * Util.cos(ufo.getRx()) 
//                * Util.cos(ufo.getRy());
        
        this.lookUpX = -1 * Util.sin(ufo.getRz()) * Util.cos(ufo.getRy());
        this.lookUpZ = -1 * Util.sin(ufo.getRz()) * Util.sin(ufo.getRy());
        this.lookUpY = Util.cos(ufo.getRz());
    }
   
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getLookUpX() {
        return lookUpX;
    }

    public void setLookUpX(float lookUpX) {
        this.lookUpX = lookUpX;
    }

    public float getLookUpY() {
        return lookUpY;
    }

    public void setLookUpY(float lookUpY) {
        this.lookUpY = lookUpY;
    }

    public float getLookUpZ() {
        return lookUpZ;
    }

    public void setLookUpZ(float lookUpZ) {
        this.lookUpZ = lookUpZ;
    }
    
    
}
