package com.example.pruebas.interfaz2;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by pruebas on 8/09/16.
 */
public class JavaJs {

	private String contenido = "";
	private boolean estadoBloqueado = false;

	@JavascriptInterface
	public void guardaContenido ( String contenido ){

		if ( !estadoBloqueado ){
			this.contenido = contenido;
			Log.i( "InfoEx-PuenteApp", "Valor asignado : [" + contenido + "]" );

			//estadoBloqueado = true;
		}

	}

	@JavascriptInterface
	public String getContenido (){
		if ( estadoBloqueado || contenido != null ){
			Log.i( "InfoEx-PuenteApp", "Valor guardado : [" + contenido + "]" );
		} else {
			Log.i( "InfoEx-PuenteApp", "Valor guardado : [N/A]" );
		}

		return contenido;
	}
}