/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neo_robocode;

import java.util.List;
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

import robocode.*;

/**
 *
 * @author ribadas
 */
public class ComprobarReglas {

    public static String FICHERO_REGLAS = "neo_robocode/reglas/reglas_robot.drl";
    public static String CONSULTA_ACCIONES = "consulta_acciones";
    private KnowledgeBuilder kbuilder;
    private KnowledgeBase kbase;                // Base de conocimientos
    private StatefulKnowledgeSession ksession;  // Memoria activa
    private Vector<FactHandle> referenciasHechosActuales = new Vector<FactHandle>();

    public ComprobarReglas() {
    	String modoDebug = System.getProperty("robot.debug", "true");
    	DEBUG.habilitarModoDebug(modoDebug.equals("true"));
        crearBaseConocimiento();
        cargarEventos();
    }

	public void cargarEventos() {
		ScannedRobotEvent e = new ScannedRobotEvent("pepe", 100, 10, 10, 10, 10);
        FactHandle referenciaHecho = ksession.insert(e);
        referenciasHechosActuales.add(referenciaHecho);
        // anadir mas hechos ....
        
        DEBUG.mensaje("hechos en memoria activa");
        DEBUG.volcarHechos(ksession);
        ksession.fireAllRules();
        List<Accion> acciones = recuperarAcciones();
        DEBUG.mensaje("acciones resultantes");
        DEBUG.volcarAcciones(acciones);
        
	}

    private void crearBaseConocimiento() {
    	String ficheroReglas;
    	ficheroReglas = System.getProperty("robot.reglas", ComprobarReglas.FICHERO_REGLAS);
    	
    	DEBUG.mensaje("crear base de conocimientos");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
    	DEBUG.mensaje("cargar reglas desde "+ficheroReglas);
        kbuilder.add(ResourceFactory.newClassPathResource(ficheroReglas,ComprobarReglas.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        DEBUG.mensaje("crear sesion (memoria activa)");
        ksession = kbase.newStatefulKnowledgeSession();
    }

    public static void main(String args[]) {
        ComprobarReglas d = new ComprobarReglas();
    }

    private List<Accion> recuperarAcciones() {
        Accion accion;
        Vector<Accion> listaAcciones = new Vector<Accion>();

        for (QueryResultsRow resultado : ksession.getQueryResults(Neo.CONSULTA_ACCIONES)) {
            accion = (Accion) resultado.get("accion");  // Obtener el objeto accion
            accion.setRobot(null);                      // Vincularlo al robot actual
            listaAcciones.add(accion);
            ksession.retract(resultado.getFactHandle("accion")); // Eliminar el hecho de la memoria activa
        }

        return listaAcciones;
    }
}


