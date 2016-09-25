package com.example.pruebas.interfaz2;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by pruebas on 8/09/16.
 */
public class JavaJs {

	private String contenido = "";
	private boolean estadoBloqueado = false;
	private int opcionNavegacion = 0;

	private WebView pagina = null;
	private PrimeraSeccion primeraSeccion = null;

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

	@JavascriptInterface
	public void recargaPagina(){
		if ( pagina != null ){
			pagina.reload();
			Log.i( "InfoEx-PuenteApp", "Recargando!!" );
		}

		Log.i( "InfoEx-PuenteApp", "Recargando2!!" );
	}

	public void setPagina ( WebView pagina ){
		this.pagina = pagina;
		Log.i( "InfoEx-PuenteApp", "pagina - configurada" );
	}

	public void setPrimeraSeccion ( PrimeraSeccion primeraSeccion ){
		this.primeraSeccion = primeraSeccion;
		Log.i( "InfoEx-PuenteApp", "controlador Android - configurado" );
	}

	@JavascriptInterface
	public int getOpcionNavegacion (){
		if ( pagina != null ){

			return primeraSeccion.getOpcionNavegacion();
		}

		return 0;
	}

	@JavascriptInterface
	public void setOpcionNavegacion ( int opcion ){
		primeraSeccion.setOpcionNavegacion( opcion );
	}

}