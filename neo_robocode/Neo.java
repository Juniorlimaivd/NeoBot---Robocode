/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neo_robocode;

import java.util.Vector;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResultsRow;
import robocode.AdvancedRobot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.awt.Color;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ribadas
 */

public class Neo extends TeamRobot implements Serializable {

    public static String FICHERO_REGLAS = "neo_robocode/reglas/reglas_robot.drl";
    public static String CONSULTA_ACCIONES = "consulta_acciones";
    
    private KnowledgeBuilder kbuilder;
    private KnowledgeBase kbase;   // Base de conocimeintos
    private StatefulKnowledgeSession ksession;  // Memoria activa
    private Vector<FactHandle> referenciasHechosActuales = new Vector<FactHandle>();
    
    // Variáveis globais
    public static final double TWO_PI = Math.PI * 2;
    public static Rectangle2D.Double _fieldRect;
    public static Point2D.Double _myLocation;
    public ArrayList _recentLocations;
    public double firepower = 0.0;
    public static HashMap _enemies;
    public static Point2D.Double _destination;
    
   // public List<WaveBullet> waves = new ArrayList<WaveBullet>();
    public int[] stats = new int[31]; // 31 is the number of unique GuessFactors we're using
	  // Note: this must be odd number so we can get
	  // GuessFactor 0 at middle.
    public int direction = 1;

    
    public Neo(){
    }
    
    @Override
    public void run() {
    	setColors(Color.black, Color.green, Color.green);
    	
    	_destination = null;
    	
    	// Define o campo de batalha como um retângulo
        _fieldRect = new Rectangle2D.Double(50, 50, getBattleFieldWidth() - 100, getBattleFieldHeight() - 100);
        // Cria uma lista com as localizações recentes
     	_recentLocations = new ArrayList();
        
     	_enemies = new HashMap();
    	DEBUG.habilitarModoDebug(System.getProperty("robot.debug", "true").equals("true"));    	

    	// Crear Base de Conocimiento y cargar reglas
    	crearBaseConocimiento();

        // Hacer que movimiento de tanque, radar y cañon sean independientes
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        //setAdjustRadarForRobotTurn(true);
		ksession.insert(this);
		

        while (true) {
        	DEBUG.mensaje("inicio turno");
            //cargarEventos();  // se hace en los metodos onXXXXXEvent()
        	//scan();
            cargarEstadoRobot();
            cargarEstadoBatalla();
            

            // Lanzar reglas
            DEBUG.mensaje("hechos en memoria activa");
            DEBUG.volcarHechos(ksession);           
            ksession.fireAllRules();
            limpiarHechosIteracionAnterior();

            // Recuperar acciones
            Vector<Accion> acciones = recuperarAcciones();
            DEBUG.mensaje("acciones resultantes");
            DEBUG.volcarAcciones(acciones);

            // Ejecutar Acciones
            ejecutarAcciones(acciones);
        	DEBUG.mensaje("fin turno\n");
            execute();  // Informa a robocode del fin del turno (llamada bloqueante)

        }

    }


    private void crearBaseConocimiento() {
        String ficheroReglas = System.getProperty("robot.reglas", Neo.FICHERO_REGLAS);

        DEBUG.mensaje("crear base de conocimientos");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        DEBUG.mensaje("cargar reglas desde "+ficheroReglas);
        kbuilder.add(ResourceFactory.newClassPathResource(ficheroReglas, Neo.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        DEBUG.mensaje("crear sesion (memoria activa)");
        ksession = kbase.newStatefulKnowledgeSession();
    }



    private void cargarEstadoRobot() {
    	EstadoRobot estadoRobot = new EstadoRobot(this);
        referenciasHechosActuales.add(ksession.insert(estadoRobot));
    }

    private void cargarEstadoBatalla() {
        EstadoBatalla estadoBatalla =
                new EstadoBatalla(getBattleFieldWidth(), getBattleFieldHeight(),
                getNumRounds(), getRoundNum(),
                getTime(),
                getOthers());
        referenciasHechosActuales.add(ksession.insert(estadoBatalla));
    }

    private void limpiarHechosIteracionAnterior() {
        for (FactHandle referenciaHecho : this.referenciasHechosActuales) {
            ksession.retract(referenciaHecho);
        }
        this.referenciasHechosActuales.clear();
    }

    private Vector<Accion> recuperarAcciones() {
        Accion accion;
        Vector<Accion> listaAcciones = new Vector<Accion>();

        for (QueryResultsRow resultado : ksession.getQueryResults(Neo.CONSULTA_ACCIONES)) {
            accion = (Accion) resultado.get("accion");  // Obtener el objeto accion
            accion.setRobot(this);                      // Vincularlo al robot actual
            listaAcciones.add(accion);
            ksession.retract(resultado.getFactHandle("accion")); // Eliminar el hecho de la memoria activa
        }

        return listaAcciones;
    }

    private void ejecutarAcciones(Vector<Accion> acciones) {
        for (Accion accion : acciones) {
            accion.iniciarEjecucion();
        }
    }

    // Insertar en la memoria activa los distintos tipos de eventos recibidos 
    @Override
    public void onBulletHit(BulletHitEvent event) {
          referenciasHechosActuales.add(ksession.insert(event));
      	try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }
    

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }

    @Override
    public void onHitWall(HitWallEvent event) {
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
    	DEBUG.mensaje("ACHEI ROBO");
        referenciasHechosActuales.add(ksession.insert(event));
        try {
  			// Send RobotColors object to our entire team
      		broadcastMessage(event);
  		} catch (IOException ignored) {}
    }
    
    @Override
	public void onMessageReceived(MessageEvent event) {
		DEBUG.mensaje("DROID RECEBEU MSG");
        referenciasHechosActuales.add(ksession.insert(event)); 
    }

}
