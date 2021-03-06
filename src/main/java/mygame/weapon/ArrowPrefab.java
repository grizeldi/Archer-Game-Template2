package mygame.weapon;

import com.capdevon.engine.FVector;
import com.jme3.app.Application;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;

public class ArrowPrefab extends RangedBullet {
	
	float radius = 0.05f;
	
	public ArrowPrefab(Application app, String name) {
		super(app);
		this.mass = 6f;
		this.name = name;
	}

	@Override
	public Spatial getAssetModel() {
		// TODO Auto-generated method stub
		Node model = new Node();
		
		Geometry g1 = new Geometry("Arrow.GeoMesh", new Sphere(16, 16, radius));
		Material mat1 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Green.clone());
		g1.setMaterial(mat1);
		model.attachChild(g1);
		
        Geometry g2 = new Geometry("Axis.Z", new Arrow(Vector3f.UNIT_Z));
        Material mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Blue.clone());
        mat2.getAdditionalRenderState().setLineWidth(2f);
        g2.setMaterial(mat2);
        g2.setLocalTranslation(FVector.forward(g1).negate());
		model.attachChild(g2);
		
		return model;
	}
	
	@Override
	public Spatial instantiate(Vector3f position, Quaternion rotation, Node parent) {
    	Spatial model = getAssetModel();
    	model.setName(name + "-" + nextSeqId());
    	model.setLocalTranslation(position);
    	model.setLocalRotation(rotation);
    	parent.attachChild(model);
    	
    	// Add Physics.
    	SphereCollisionShape shape = new SphereCollisionShape(radius);
        RigidBodyControl rgb = new RigidBodyControl(shape, mass);
        model.addControl(rgb);
        PhysicsSpace.getPhysicsSpace().add(rgb);
        
        rgb.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        rgb.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        rgb.setCcdMotionThreshold(0.001f);
//        rgb.setCcdSweptSphereRadius(0.001f);
        
        ArrowControl arrow = new ArrowControl();
        model.addControl(arrow);
    	
    	return model;
    }

}
