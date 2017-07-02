/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package neo_robocode;

import robocode.TeamRobot;
import robocode.TeamRobot;
import java.io.IOException;

/**
 *
 * @author ribadas
 */
public class Accion {
    private int        tipo;
    private double     parametro;
    private String 	   msg;
    private int        prioridad;

    private TeamRobot robot;   // Referncia al robot que ejecutara la accion

    public static final int AVANZAR=1;
    public static final int RETROCEDER=2;
    public static final int STOP=3;
    public static final int DISPARAR=4;
    public static final int GIRAR_TANQUE_DER=5;
    public static final int GIRAR_TANQUE_IZQ=6;
    public static final int GIRAR_RADAR_DER=7;
    public static final int GIRAR_RADAR_IZQ=8;
    public static final int GIRAR_CANON_DER=9;
    public static final int GIRAR_CANON_IZQ=10;
    public static final int BROADCAST=11;


    public Accion() {
    }

    public Accion(int tipo, double parametro, int prioridad) {
        this.tipo = tipo;
        this.parametro = parametro;
        this.prioridad = prioridad;
    }
    

    public double getParametro() {
        return parametro;
    }

    public void setParametro(double parametro) {
        this.parametro = parametro; 
    }
    
    public Accion(int tipo, String msg, int prioridad ) {
    	this.tipo = tipo;
        this.msg = msg; 
        this.prioridad = prioridad;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }



    public void iniciarEjecucion() {
        if (this.robot != null) {
            switch (this.tipo) {
                case Accion.DISPARAR: robot.setFire(parametro); break;
                case Accion.AVANZAR: robot.setAhead(parametro); break;
                case Accion.RETROCEDER: robot.setBack(parametro); break;
                case Accion.STOP: robot.setStop(); break;
                case Accion.GIRAR_CANON_DER: robot.setTurnGunRightRadians(parametro); break;
                case Accion.GIRAR_CANON_IZQ: robot.setTurnGunLeft(parametro); break;
                case Accion.GIRAR_RADAR_DER: robot.setTurnRadarRight(parametro); break;
                case Accion.GIRAR_RADAR_IZQ: robot.setTurnRadarLeft(parametro); break;
                case Accion.GIRAR_TANQUE_DER: robot.setTurnRight(parametro); break;
                case Accion.GIRAR_TANQUE_IZQ: robot.setTurnLeft(parametro); break;
                case Accion.BROADCAST: 
                	try {
            			// Send RobotColors object to our entire team
                		robot.broadcastMessage(msg); break;
            		} catch (IOException ignored) {}
                	
            }
        }
    }

    void setRobot(TeamRobot robot) {
        this.robot = robot;
    }

    public String toString(){
        String etqTipo="";
            switch (this.tipo) {
                case Accion.DISPARAR:etqTipo="Disparar"; break;
                case Accion.AVANZAR: etqTipo="Avanzar"; break;
                case Accion.RETROCEDER: etqTipo="Retroceder"; break;
                case Accion.STOP: etqTipo="Stop"; break;
                case Accion.GIRAR_CANON_DER: etqTipo="Girar cañón derecha"; break;
                case Accion.GIRAR_CANON_IZQ: etqTipo="Girar cañón izquierda"; break;
                case Accion.GIRAR_RADAR_DER: etqTipo="Girar radar derecha"; break;
                case Accion.GIRAR_RADAR_IZQ: etqTipo="Girar radar izquierda"; break;
                case Accion.GIRAR_TANQUE_DER: etqTipo="Girar tanque derecha"; break;
                case Accion.GIRAR_TANQUE_IZQ: etqTipo="Girar tanque izquierda"; break;
                case Accion.BROADCAST: 
                	etqTipo="Broadcast message"; 
                	return "Accion[tipo:"+etqTipo+", msg:" + msg +", prioridad:"+prioridad+"]";         	
            }
	return "Accion[tipo:"+etqTipo+", param:"+parametro+", prioridad:"+prioridad+"]";
    }

}
