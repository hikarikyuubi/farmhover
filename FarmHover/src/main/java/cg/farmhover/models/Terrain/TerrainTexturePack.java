package cg.farmhover.models.Terrain;


import cg.farmhover.gl.util.Texture;

public class TerrainTexturePack {

    private Texture backgroundTexture;
    private Texture rTexture;
    private Texture gTexture;
    private Texture bTexture;

    public TerrainTexturePack() {

    }
    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public void setBackgroundTexture(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public Texture getrTexture() {
        return rTexture;
    }

    public void setrTexture(Texture rTexture) {
        this.rTexture = rTexture;
    }

    public Texture getgTexture() {
        return gTexture;
    }

    public void setgTexture(Texture gTexture) {
        this.gTexture = gTexture;
    }

    public Texture getbTexture() {
        return bTexture;
    }

    public void setbTexture(Texture bTexture) {
        this.bTexture = bTexture;
    }
}
