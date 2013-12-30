package collision;

public class Detector {

	public static boolean checkCollision(Obj a, Obj b) {
		return a.getBounds().intersects(b.getBounds());
	}

}
