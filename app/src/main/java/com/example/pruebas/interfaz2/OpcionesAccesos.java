package com.example.pruebas.interfaz2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pruebas on 8/09/16.
 */
public class OpcionesAccesos {

	static List<String> opcionesAccesos;
	static Map<String, String> accesos;

	public OpcionesAccesos (){
		inicializaAccesos();
		creaListaAccesos();
	}

	private void inicializaAccesos (){
		accesos = new LinkedHashMap<>();

		accesos.put( "Inicio"                      , "" );
		accesos.put( "Calificaciones del Semestre" , "/Alumnos/Informacion_semestral/calificaciones_sem.aspx" );
		accesos.put( "Kárdex"                      , "/Alumnos/boleta/kardex.aspx" );
		accesos.put( "Estado General"              , "/Alumnos/boleta/Estado_Alumno.aspx" );
		accesos.put( "Calificaciones de ETS"       , "/Alumnos/ETS/calificaciones_ets.aspx" );
		accesos.put( "Inscribir de ETS"            , "/Alumnos/ETS/inscripcion_ets.aspx" );
		accesos.put( "Horario"                     , "/Alumnos/Informacion_semestral/Horario_Alumno.aspx" );
		accesos.put( "Dictamen"                    , "/Alumnos/Dictamenes/respuesta_dictamen.aspx" );
		accesos.put( "Solicitud de Dictamen"       , "/Alumnos/Dictamenes/Candidato.aspx" );
		accesos.put( "Cita de Reinscripción"       , "/Alumnos/Reinscripciones/fichas_reinscripcion.aspx" );
		accesos.put( "Reinscripción"               , "/Alumnos/Reinscripciones/reinscribir.aspx" );
		accesos.put( "Comprobante de Horario"      , "/Alumnos/Reinscripciones/Comprobante_Horario.aspx" );
		accesos.put( "Evaluación de Profesores"    , "/Alumnos/Evaluacion_docente/califica_profe.aspx" );
		accesos.put( "Horarios Disponibles"        , "/Academica/horarios.aspx" );
		accesos.put( "Ocupabilidad"                , "/Academica/Ocupabilidad_grupos.aspx" );
		accesos.put( "Equivalencias"               , "/Academica/Equivalencias.aspx" );
	}

	private void creaListaAccesos (){
		opcionesAccesos = new ArrayList<>();

		opcionesAccesos.addAll( accesos.keySet() );
	}

	public List<String> getListaOpcionesAccesos(){
		return opcionesAccesos;
	}

	public static String getDireccionAcceso ( String nombreAcceso ){
		return accesos.get( nombreAcceso );
	}

	public static boolean tieneSeccion ( String seccion ){
		return accesos.containsValue( seccion );
	}

	public static int getAcceso ( String seccion ){
		int posicion = 0;
		for( Map.Entry<String, String> registro : accesos.entrySet() ){
			if ( registro.getValue().equals( seccion ) ){

				return posicion;
			}
			posicion++;
		}

		return 0;
	}

}