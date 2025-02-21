package a2;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class Camera {
    private Vector3f u, v, n;
    private Vector3f location;
    private Matrix4f view, viewT, viewR;

    public Camera() {
        Vector3f defaultLocation = new Vector3f(0.0f, 0.0f, 1.0f);
        Vector3f defaultU = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f defaultV = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f defaultN = new Vector3f(0.0f, 0.0f, -1.0f);

        location = new Vector3f(defaultLocation);
        u = new Vector3f(defaultU);
        v = new Vector3f(defaultV);
        n = new Vector3f(defaultN);

        view = new Matrix4f();
        viewT = new Matrix4f();
        viewR = new Matrix4f();
    }

    public void setLocation(Vector3f newLocation) { location.set(newLocation); }
    public void setU(Vector3f newU) { location.set(newU); }
    public void setV(Vector3f newV) { location.set(newV); }
    public void setN(Vector3f newN) { location.set(newN); }
    public Vector3f getLocation() { return new Vector3f(location); }
    public Vector3f getU() { return new Vector3f(u); }
    public Vector3f getV() { return new Vector3f(v); }
    public Vector3f getN() { return new Vector3f(n); }

    public Matrix4f getViewMatrix() { 
        viewT.set(1.0f, 0.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f, 0.0f,
		0.0f, 0.0f, 1.0f, 0.0f,
		-location.x(), -location.y(), -location.z(), 1.0f);

		viewR.set(u.x(), v.x(), -n.x(), 0.0f,
		u.y(), v.y(), -n.y(), 0.0f,
		u.z(), v.z(), -n.z(), 0.0f,
		0.0f, 0.0f, 0.0f, 1.0f);

		view.identity();
		view.mul(viewR);
		view.mul(viewT);

		return(view);
    }
    
    public void MoveForward(float velocity) {
		Vector3f fwd = this.getN();
		Vector3f loc = this.getLocation();
		Vector3f newLocation = loc.add(fwd.mul(velocity));
		this.setLocation(newLocation);
	}

    public void MoveUp(float velocity) {
		Vector3f up = this.getV();
        Vector3f loc = this.getLocation();
        Vector3f newLocation = loc.add(up.mul(velocity));
		this.setLocation(newLocation);
	}

    public void MoveRight(float velocity) {
		Vector3f right = this.getU();
		Vector3f loc = this.getLocation();
		Vector3f newLocation = loc.add(right.mul(velocity));
		this.setLocation(newLocation);
	}

    public void Yaw(float angularVelocity) {
        Quaternionf quat = new Quaternionf().rotateAxis(angularVelocity, 0, 1, 0);
        viewR.rotate(quat);
        /* 
		Vector3f right = this.getU();
		Vector3f up = this.getV();
		Vector3f fwd = this.getN();
		right.rotateAxis(angularVelocity, up.x(), up.y(), up.z());
		fwd.rotateAxis(angularVelocity, up.x(), up.y(), up.z());
		this.setU(right);
		this.setN(fwd);
        */
	}

    public void Pitch(float angularVelocity) {
		Vector3f right = this.getU();
		Vector3f up = this.getV();
		Vector3f fwd = this.getN();
		up.rotateAxis(angularVelocity, right.x(), right.y(), right.z());
		fwd.rotateAxis(angularVelocity, right.x(), right.y(), right.z());
		this.setV(up);
		this.setN(fwd);
	}

    // Alternate way to do??? idk looking at raylib maybe test later

    public Matrix4f getViewMatrixTesting() { 
        return view.lookAt(location, new Vector3f(0, 0, 0), v);
    }

    public void MoveUpTesting(float velocity) {
		Vector3f up = this.getV();
        Vector3f loc = this.getLocation();
        Vector3f newLocation = loc.add(up.mul(velocity));
		this.setLocation(newLocation);
	}
    
}
