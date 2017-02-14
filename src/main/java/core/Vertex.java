package core;

import javafx.beans.NamedArg;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by NF on 2/13/2017.
 */
public class Vertex extends Point3D {

    public Sphere getSphere() {
        return sphere;
    }

    public void setSphere(Sphere sphere) {
        this.sphere = sphere;
    }

    private Sphere sphere;
    private final double DEFAULT_RADIUS = 6;

    /**
     * Creates a new instance of {@code Point3D}.
     *
     * @param x The X coordinate of the {@code Point3D}
     * @param y The Y coordinate of the {@code Point3D}
     * @param z The Z coordinate of the {@code Point3D}
     */
    public Vertex(@NamedArg("x") double x, @NamedArg("y") double y, @NamedArg("z") double z) {
        super(x, y, z);
        sphere = new Sphere(DEFAULT_RADIUS);

        //TODO: find a better way to provide material
        PhongMaterial redMaterial = (new PhongMaterial());
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        sphere.setMaterial(redMaterial);
        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);
    }


}
