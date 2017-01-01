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

	static final String OPCION_INICIO                  = "Inicio";
	static final String OPCION_CALIFICACIONES_SEMESTRE = "Calificaciones del Semestre";
	static final String OPCION_KARDEX                  = "Kárdex";
	static final String OPCION_HORARIO                 = "Horario";
	static final String OPCION_COMPROBANTE_HORARIO     = "Comprobante de Horario";
	static final String OPCION_CITA_REINSCRIPCION      = "Cita de Reinscripción";
	static final String OPCION_HORARIOS_DISPONIBLES    = "Horarios Disponibles";
	static final String OPCION_OCUPABILIDAD            = "Ocupabilidad";
	static final String OPCION_EQUIVALENCIAS           = "Equivalencias";
	static final String OPCION_REINSCRIPCION           = "Reinscripción";
	static final String OPCION_EVALUACION_PROFESORES   = "Evaluación de Profesores";
	static final String OPCION_ESTADO_GENERAL          = "Estado General";
	static final String OPCION_CALIFICACIONES_ETS      = "Calificaciones de ETS";
	static final String OPCION_INSCRIBIR_ETS           = "Inscribir de ETS";
	static final String OPCION_DICTAMEN                = "Dictamen";
	static final String OPCION_SOLICITUD_DICTAMEN      = "Solicitud de Dictamen";

	static final String PAGINA_INICIO                  = "";
	static final String PAGINA_CALIFICACIONES_SEMESTRE = "/Alumnos/Informacion_semestral/calificaciones_sem.aspx";
	static final String PAGINA_KARDEX                  = "/Alumnos/boleta/kardex.aspx";
	static final String PAGINA_HORARIO                 = "/Alumnos/Informacion_semestral/Horario_Alumno.aspx";
	static final String PAGINA_COMPROBANTE_HORARIO     = "/Alumnos/Reinscripciones/Comprobante_Horario.aspx";
	static final String PAGINA_CITA_REINSCRIPCION      = "/Alumnos/Reinscripciones/fichas_reinscripcion.aspx";
	static final String PAGINA_HORARIOS_DISPONIBLES    = "/Academica/horarios.aspx";
	static final String PAGINA_OCUPABILIDAD            = "/Academica/Ocupabilidad_grupos.aspx";
	static final String PAGINA_EQUIVALENCIAS           = "/Academica/Equivalencias.aspx";
	static final String PAGINA_REINSCRIPCION           = "/Alumnos/Reinscripciones/reinscribir.aspx";
	static final String PAGINA_EVALUACION_PROFESORES   = "/Alumnos/Evaluacion_docente/califica_profe.aspx";
	static final String PAGINA_ESTADO_GENERAL          = "/Alumnos/boleta/Estado_Alumno.aspx";
	static final String PAGINA_CALIFICACIONES_ETS      = "/Alumnos/ETS/calificaciones_ets.aspx";
	static final String PAGINA_INSCRIBIR_ETS           = "/Alumnos/ETS/inscripcion_ets.aspx";
	static final String PAGINA_DICTAMEN                = "/Alumnos/Dictamenes/respuesta_dictamen.aspx";
	static final String PAGINA_SOLICITUD_DICTAMEN      = "/Alumnos/Dictamenes/Candidato.aspx";

	public OpcionesAccesos (){
		inicializaAccesos();
		creaListaAccesos();
	}

	private void inicializaAccesos (){
		accesos = new LinkedHashMap<>();

		accesos.put( OPCION_INICIO                  , PAGINA_INICIO );
		accesos.put( OPCION_CALIFICACIONES_SEMESTRE , PAGINA_CALIFICACIONES_SEMESTRE );
		accesos.put( OPCION_KARDEX                  , PAGINA_KARDEX );
		accesos.put( OPCION_HORARIO                 , PAGINA_HORARIO );
		accesos.put( OPCION_COMPROBANTE_HORARIO     , PAGINA_COMPROBANTE_HORARIO );
		accesos.put( OPCION_CITA_REINSCRIPCION      , PAGINA_CITA_REINSCRIPCION );
		accesos.put( OPCION_HORARIOS_DISPONIBLES    , PAGINA_HORARIOS_DISPONIBLES );
		accesos.put( OPCION_OCUPABILIDAD            , PAGINA_OCUPABILIDAD );
		accesos.put( OPCION_EQUIVALENCIAS           , PAGINA_EQUIVALENCIAS );
		accesos.put( OPCION_REINSCRIPCION           , PAGINA_REINSCRIPCION );
		accesos.put( OPCION_EVALUACION_PROFESORES   , PAGINA_EVALUACION_PROFESORES );
		accesos.put( OPCION_ESTADO_GENERAL          , PAGINA_ESTADO_GENERAL );
		accesos.put( OPCION_CALIFICACIONES_ETS      , PAGINA_CALIFICACIONES_ETS );
		accesos.put( OPCION_INSCRIBIR_ETS           , PAGINA_INSCRIBIR_ETS );
		accesos.put( OPCION_DICTAMEN                , PAGINA_DICTAMEN );
		accesos.put( OPCION_SOLICITUD_DICTAMEN      , PAGINA_SOLICITUD_DICTAMEN );
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

	public static String getNombreSeccion ( int posicion ){
		return opcionesAccesos.get( posicion );
	}

}