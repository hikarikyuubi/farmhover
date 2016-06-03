package cg.farmhover.gl.util;


import com.jogamp.opengl.util.awt.ImageUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;



public class TextureLoader {

    /** The colour model including alpha for the GL image */
    private ColorModel glAlphaColorModel;

    /** The colour model for the GL image */
    private ColorModel glColorModel;
    private GL3 gl;
    /**
     * Create a new texture loader based on the game panel
     *
     * @param gl The GL content in which the textures should be loaded
     */
    public TextureLoader(GL3 gl) {
        this.gl = gl;
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);

        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
    }

    /**
     * Create a new texture ID
     *
     * @return A new texture ID
     */
    private int createTextureID()
    {
        int tmp[] = new int[1];
        gl.glGenTextures(1,tmp,0);
        return tmp[0];
    }
    public Texture getTexture(String resourceName, int target, int dstPixelFormat,
                              int minFilter,
                              String extension) throws IOException
    {
        int srcPixelFormat;
        BufferedImage bufferedImage;
        ByteBuffer textureBuffer;
        // create the texture ID for this texture
        int textureID = createTextureID();
        Texture texture = new Texture(gl,target,textureID);
        gl.glBindTexture(target, textureID);

        bufferedImage = loadImage(".\\images\\" + resourceName + extension);
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());

        srcPixelFormat = bufferedImage.getColorModel().hasAlpha() ? GL.GL_RGBA : GL.GL_RGB;

        // convert that image into a byte buffer of texture data
        textureBuffer = convertImageData(bufferedImage, texture);
        gl.glTexImage2D(target, 0, dstPixelFormat,
                get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()),
                0, srcPixelFormat, GL.GL_UNSIGNED_BYTE, textureBuffer);

        gl.glGenerateMipmap(target);
        gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, minFilter);
        gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
        gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S,minFilter);
        gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, minFilter);
        gl.glTexParameteri(target, GL3.GL_TEXTURE_WRAP_R, minFilter);
        return texture;
    }

    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getCubeMapTexture(String[] resourceName, int target, int dstPixelFormat,
                                     int minFilter,
                                     String extension) throws IOException {
        int srcPixelFormat;
        BufferedImage bufferedImage;
        ByteBuffer textureBuffer;
        // create the texture ID for this texture
        int textureID = createTextureID();
        Texture texture = new Texture(gl, target, textureID);
        //gl.glActiveTexture(GL3.GL_TEXTURE0);
        gl.glBindTexture(target, textureID);

        for (int i = 0; i < resourceName.length; i++) {

            bufferedImage = loadImage(".\\images\\" + resourceName[i] + extension);
            texture.setWidth(bufferedImage.getWidth());
            texture.setHeight(bufferedImage.getHeight());

            srcPixelFormat = bufferedImage.getColorModel().hasAlpha() ? GL.GL_RGBA : GL.GL_RGB;

            // convert that image into a byte buffer of texture data
            textureBuffer = convertImageData(bufferedImage, texture);
            gl.glTexImage2D(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, dstPixelFormat,
                    get2Fold(bufferedImage.getWidth()), get2Fold(bufferedImage.getHeight()),
                    0, srcPixelFormat, GL.GL_UNSIGNED_BYTE, textureBuffer);

            gl.glGenerateMipmap(target);
            gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, minFilter);
            gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
            gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_S, minFilter);
            gl.glTexParameteri(target, GL.GL_TEXTURE_WRAP_T, minFilter);
            gl.glTexParameteri(target, GL3.GL_TEXTURE_WRAP_R, minFilter);

        }
        return texture;
    }

    /**
     * Get the closest greater power of 2 to the fold number
     *
     * @param fold The target number
     * @return The power of 2
     */
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }

    /**
     * Convert the buffered image to a texture
     *
     * @param bufferedImage The image to convert to a texture
     * @param texture The texture to store the data into
     * @return A buffer containing the data
     */
    private ByteBuffer convertImageData(BufferedImage bufferedImage, Texture texture) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = 2;
        int texHeight = 2;

        // find the closest power of 2 for the width and height

        // of the produced texture

        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }

        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);

        // create a raster that can be used by OpenGL as a source

        // for a texture

        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }

        // copy the source image into the produced image

        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);

        // build a byte buffer from the temporary image

        // that be used by OpenGL to produce a texture.

        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    /**
     * Load a given resource as a buffered image
     *
     * @param ref The location of the resource to load
     * @return The loaded buffered image
     * @throws IOException Indicates a failure to find a resource
     */
    private BufferedImage loadImage(String ref) throws IOException
    {
        URL url = TextureLoader.class.getClassLoader().getResource(ref);

        if (url == null) {
            throw new IOException("Cannot find: "+ref);
        }

        return ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref)));
    }
}