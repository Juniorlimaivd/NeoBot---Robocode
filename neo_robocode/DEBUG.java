package neo_robocode;


import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

public final class DEBUG {
	public static boolean modoDebugHabilitado = false;

	public static void habilitarModoDebug(boolean b) {
		modoDebugHabilitado = b;
	}

	public static void mensaje(String string) {
		if (modoDebugHabilitado) {
			System.out.println("DEBUG:"+string);			
		}		
	}

	public static void volcarHechos(StatefulKnowledgeSession ksession) {
		if (modoDebugHabilitado){
			for (FactHandle f: ksession.getFactHandles()){
				System.out.println("  "+ksession.getObject(f));				
			}			
		}		
	}

	public static void volcarAcciones(List<Accion> acciones) {
		if (modoDebugHabilitado){
			for (Accion a: acciones){
				System.out.println("  "+a.toString());				
			}
		}		
	}
}
