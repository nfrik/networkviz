package core;

import javafx.beans.NamedArg;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by NF on 2/13/2017.
 */
public class Vertex extends Sphere {

    private Point3D velocity = new Point3D(0,0,0);

    public Point3D getPoint3D(){
        return new Point3D(super.getTranslateX(),super.getTranslateY(),super.getTranslateZ());
    }

    public Vertex(double x, double y, double z){
        super(10);

        super.setTranslateX(x);
        super.setTranslateY(y);
        super.setTranslateZ(z);

//        //TODO: find a better way to provide material
//        PhongMaterial redMaterial = (new PhongMaterial());
//        redMaterial.setDiffuseColor(Color.YELLOW);
//        redMaterial.setSpecularColor(Color.AQUA);
//
//
//        super.setMaterial(redMaterial);
    }

    public Vertex(double x, double y, double z, double radius, PhongMaterial material ){
        super(radius);
        super.setTranslateX(x);
        super.setTranslateY(y);
        super.setTranslateZ(z);
        super.setMaterial(material);
    }

    public Vertex(Point3D point){
        super(10);
        super.setTranslateX(point.getX());
        super.setTranslateY(point.getY());
        super.setTranslateZ(point.getZ());
    }

    public Vertex(Point3D point, double radius, PhongMaterial material){
        this(point.getX(),point.getY(),point.getZ(),radius,material);
    }

    @Override
    public boolean equals(Object v){
        if(v instanceof Vertex){
            return this.getPoint3D().distance(((Vertex) v).getPoint3D())==0;
        }else if (v instanceof Point3D){
            return this.getPoint3D().distance((Point3D) v) == 0;
        }

        return false;
    }

    public Point3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point3D velocity) {
        this.velocity = velocity;
    }

//    @Override
//    public int hashCode(){
//        return getPoint3D().hashCode() ;
//    }


//    public Sphere getSphere() {
//        return sphere;
//    }
//
//    public void setSphere(Sphere sphere) {
//        this.sphere = sphere;
//    }
//
//    private Sphere sphere;
//    private final double DEFAULT_RADIUS = 6;
//
//    /**
//     * Creates a new instance of {@code Point3D}.
//     *
//     * @param x The X coordinate of the {@code Point3D}
//     * @param y The Y coordinate of the {@code Point3D}
//     * @param z The Z coordinate of the {@code Point3D}
//     */
//    public Vertex(@NamedArg("x") double x, @NamedArg("y") double y, @NamedArg("z") double z) {
//        super(x, y, z);
//        sphere = new Sphere(DEFAULT_RADIUS);
//
//        //TODO: find a better way to provide material
//        PhongMaterial redMaterial = (new PhongMaterial());
//        redMaterial.setDiffuseColor(Color.DARKRED);
//        redMaterial.setSpecularColor(Color.RED);
//
//        sphere.setMaterial(redMaterial);
//        sphere.setTranslateX(x);
//        sphere.setTranslateY(y);
//        sphere.setTranslateZ(z);
//    }


}
