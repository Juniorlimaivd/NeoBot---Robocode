/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package neo_robocode;

import robocode.AdvancedRobot;

/**
 *
 * @author ribadas
 */
public class EstadoRobot {
    private final double distanceRemaining;
    private final double energy;
    private final double gunHeading;
    private final double gunHeat;
    private final double gunTurnRemaining;
    private final double heading;
    private final int numRounds;
    private final int others;
    private final double radarHeading;
    private final double radarTurnRemaining;
    private final int roundNum;
    private final long time;
    private final double y;
    private final double turnRemaining;
    private final double velocity;
    private final double x;

    public EstadoRobot(AdvancedRobot robot){
        distanceRemaining = robot.getDistanceRemaining();
        energy = robot.getEnergy();
        gunHeading = robot.getGunHeading();
        gunHeat = robot.getGunHeat();
        gunTurnRemaining = robot.getGunTurnRemaining();
        heading = robot.getHeading();
        numRounds = robot.getNumRounds();
        others = robot.getOthers();
        radarHeading = robot.getRadarHeading();
        radarTurnRemaining = robot.getRadarTurnRemaining();
        roundNum = robot.getRoundNum();
        time = robot.getTime();
        turnRemaining = robot.getTurnRemaining();
        velocity = robot.getVelocity();
        x = robot.getX();
        y = robot.getY();
    }

    public double getDistanceRemaining() {
        return distanceRemaining;
    }

    public double getEnergy() {
        return energy;
    }

    public double getGunHeading() {
        return gunHeading;
    }

    public double getGunHeat() {
        return gunHeat;
    }

    public double getGunTurnRemaining() {
        return gunTurnRemaining;
    }

    public double getHeading() {
        return heading;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public int getOthers() {
        return others;
    }

    public double getRadarHeading() {
        return radarHeading;
    }

    public double getRadarTurnRemaining() {
        return radarTurnRemaining;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public long getTime() {
        return time;
    }

    public double getTurnRemaining() {
        return turnRemaining;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString(){
    	return "Estado(x:"+x+", y:"+y+", velocity:"+velocity+
    	                     ", energy:"+energy+", heading:"+heading+
    	                     ", distanceRemaining:"+distanceRemaining+")";
    	
    }

}
