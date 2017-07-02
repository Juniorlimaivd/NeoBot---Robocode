/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neo_robocode;
import java.awt.geom.Point2D;

/**
 * Funciones auxiliares para menejar angulos, distancias, puntos, etc.
 * Extraidas de <a href="http://www.robocode-argentina.com.ar/descargas/trig.doc">Robocode Argentina</a>
 * @author ribadas
 */
public class Auxiliar {
	
	public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
        // Calcula o bearing relativo entre a origem e o alvo
		return Math.atan2(target.x - source.x, target.y - source.y);
    }
	
	/**
	 * Calcula el angulo absolulo a partir de un angulo absoluto base y un angulo relativo (desplazamiento)
	 * @param anguloBase
	 * @param anguloRelativo
	 * @return
	 */
    public static double anguloAbsoluto(double anguloBase, double anguloRelativo) {
        double angulo = (anguloBase + anguloRelativo) % 360;

        if (angulo < 0) {
            angulo += 360;
        }

        return angulo;
    }

    /**
     * Transforma un ángulo absoluto en un ángulo relativo a partir de un angulo base absoluto
     * @param anguloBase
     * @param anguloDestino
     * @return
     */
    public static double anguloRelativo(double anguloBase, double anguloDestino) {
        double angulo = (anguloDestino - anguloBase) % 360;
        if (angulo > 180) {
            angulo -= 360;
        } else if (angulo < -180) {
            angulo += 360;
        }

        return angulo;
    }

    /**
     * Calcula la coordenada X de un punto a partir de una coordenada X base, un angulo y una distancia
     * @param xBase
     * @param anguloAbsoluto
     * @param distancia
     * @return
     */
    public static double calcularX(double xBase, double anguloAbsoluto, double distancia) {
        double offsetX = (Math.sin(Math.toRadians(anguloAbsoluto)) * distancia);
        return xBase + offsetX;
    }

    /**
     * Calcula la coordenada Y de un punto a partir de una coordenada Y base, un angulo y una distancia
     * @param yBase
     * @param anguloAbsoluto
     * @param distancia
     * @return
     */
    public static double calcularY(double yBase, double anguloAbsoluto, double distancia) {
        double offsetY = (Math.cos(Math.toRadians(anguloAbsoluto)) * distancia);
        return yBase + offsetY;
    }

    /**
     * Calcula el angulo absoluto entre dos pares de puntos (origen y destino)
     * @param xOrigen
     * @param yOrigen
     * @param xDestino
     * @param yDestino
     * @return
     */
    public static double anguloAbsoluto(double xOrigen, double yOrigen, double xDestino, double yDestino) {
        double offsetX = xDestino - xOrigen;
        double offsetY = yDestino - yOrigen;

        return Math.toDegrees(Math.atan2(offsetX, offsetY));
    }

    /**
     * Calcula la distancia entre dos puntos
     * @param xOrigen
     * @param yOrigen
     * @param xDestino
     * @param yDestino
     * @return
     */
    public static double distancia(double xOrigen, double yOrigen, double xDestino, double yDestino) {
        double offsetX = xDestino - xOrigen;
        double offsetY = yDestino - yOrigen;

        return Math.sqrt(offsetX*offsetX + offsetY*offsetY);
    }

    
}
