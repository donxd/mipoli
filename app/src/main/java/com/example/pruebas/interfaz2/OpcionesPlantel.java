package com.example.pruebas.interfaz2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pruebas on 8/09/16.
 */
public class OpcionesPlantel {

	List<String> opcionesPlanteles;

	public OpcionesPlantel (){
		opcionesPlanteles = new ArrayList<>();

		opcionesPlanteles.add( "UPIICSA" );
		opcionesPlanteles.add( "ESCOM" );
		opcionesPlanteles.add( "ESIMEAZC" );
		opcionesPlanteles.add( "ESIMECU" );
		opcionesPlanteles.add( "ESIMETIC" );
		opcionesPlanteles.add( "ESIMEZ" );
		opcionesPlanteles.add( "ESIATEC" );
		opcionesPlanteles.add( "ESIATIC" );
		opcionesPlanteles.add( "ESIAZ" );
		opcionesPlanteles.add( "CICSMA" );
		opcionesPlanteles.add( "CICSST" );
		opcionesPlanteles.add( "ESCASTO" );
		opcionesPlanteles.add( "ESCATEP" );
		opcionesPlanteles.add( "ENCB" );
		opcionesPlanteles.add( "ENMH" );
		opcionesPlanteles.add( "ESEO" );
		opcionesPlanteles.add( "ESM" );
		opcionesPlanteles.add( "ESE" );
		opcionesPlanteles.add( "EST" );
		opcionesPlanteles.add( "UPIBI" );
		opcionesPlanteles.add( "UPIITA" );
		opcionesPlanteles.add( "ESFM" );
		opcionesPlanteles.add( "ESIQIE" );
		opcionesPlanteles.add( "ESIT" );
		opcionesPlanteles.add( "UPIIG" );

		opcionesPlanteles.add( "CECYT1" );
		opcionesPlanteles.add( "CECYT2" );
		opcionesPlanteles.add( "CECYT3" );
		opcionesPlanteles.add( "CECYT4" );
		opcionesPlanteles.add( "CECYT5" );
		opcionesPlanteles.add( "CECYT6" );
		opcionesPlanteles.add( "CECYT7" );
		opcionesPlanteles.add( "CECYT8" );
		opcionesPlanteles.add( "CECYT9" );
		opcionesPlanteles.add( "CECYT10" );
		opcionesPlanteles.add( "CECYT11" );
		opcionesPlanteles.add( "CECYT12" );
		opcionesPlanteles.add( "CECYT13" );
		opcionesPlanteles.add( "CECYT14" );
		opcionesPlanteles.add( "CECYT15" );
		opcionesPlanteles.add( "CET1" );
	}

	public List<String> getListaOpcionesPlanteles (){
		return opcionesPlanteles;
	}

	public int getIndiceOpcionPlantel ( String nombrePlantel ){
		return opcionesPlanteles.indexOf( nombrePlantel );
	}

	public String getOpcionDefault (){
		return opcionesPlanteles.get( 0 );
	}

}