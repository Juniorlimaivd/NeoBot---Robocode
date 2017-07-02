package neo_robocode;
import java.awt.geom.*;
import robocode.util.Utils;

public class WaveBullet {
	public double startX, startY, startBearing, power;
	public long   fireTime;
	public int    direction;
	public int[]  returnSegment;
 
	public WaveBullet(double x, double y, double bearing, double power,
			int direction, long time, int[] segment)
	{
		startX         = x;
		startY         = y;
		startBearing   = bearing;
		this.power     = power;
		this.direction = direction;
		fireTime       = time;
		returnSegment  = segment;
	}
}
