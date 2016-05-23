package cg.farmhover;

import cg.farmhover.gl.jWaveFront.JWavefrontObject;
import cg.farmhover.objects.SceneObject;
import java.io.File;

public class Model extends JWavefrontObject {

    public Model(File file) {
        super(file);
    }

    public void unitize(SceneObject obj) {
        assert (vertices != null);

        float maxx, minx, maxy, miny, maxz, minz;
        float cx, cy, cz, w, h, d;
        float scale;

        /*
         * get the max/mins
         */
        maxx = minx = vertices.get(1).x;
        maxy = miny = vertices.get(1).y;
        maxz = minz = vertices.get(1).z;

        for (int i = 1; i <= vertices.size(); i++) {
            if (maxx < vertices.get(i).x) {
                maxx = vertices.get(i).x;
            }
            if (minx > vertices.get(i).x) {
                minx = vertices.get(i).x;
            }

            if (maxy < vertices.get(i).y) {
                maxy = vertices.get(i).y;
            }
            if (miny > vertices.get(i).y) {
                miny = vertices.get(i).y;
            }

            if (maxz < vertices.get(i).z) {
                maxz = vertices.get(i).z;
            }
            if (minz > vertices.get(i).z) {
                minz = vertices.get(i).z;
            }
        }

        /*
         * calculate model width, height, and depth
         */
        w = Math.abs(maxx - minx);
        h = Math.abs(maxy - miny);
        d = Math.abs(maxz - minz);

        /*
         * calculate center of the model
         */
        cx = (maxx + minx) / 2.0f;
        cy = (maxy + miny) / 2.0f;
        cz = (maxz + minz) / 2.0f;

        /*
         * calculate unitizing scale factor
         */
        scale = 2.0f / Math.max(Math.max(w, h), d);

        /*
         * translate around center then scale
         */
        for (int i = 1; i <= vertices.size(); i++) {
            vertices.get(i).x -= cx;
            vertices.get(i).y -= cy;
            vertices.get(i).z -= cz;
            vertices.get(i).x *= scale;
            vertices.get(i).y *= scale;
            vertices.get(i).z *= scale;
        }
        System.out.println("minx = "+minx+" maxx = "+maxx+" miny = "+miny+" maxy = "+maxy+"minz = "+minz+" maxz = "+maxz);
        obj.setWidth((maxx - minx) * scale);
        obj.setHeight((maxy - miny) * scale);
        obj.setDepth((maxz - minz) * scale);
    }
}
