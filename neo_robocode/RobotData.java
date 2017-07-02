package neo_robocode;
import java.io.Serializable;

public class RobotData implements Serializable{
	double x;
	double y;
	double bearing;
	double heading;
	
	public RobotData(double x, double y, double bearing, double heading) {
		this.x = x;
		this.y = y;
		this.bearing = bearing;
		this.heading = heading;
	}
}
