package com.example.pruebas.interfaz2;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by pruebas on 8/09/16.
 */
public class JavaJs {

	private static JavaJs instancia;

	private String contenido = "";
	private boolean estadoBloqueado = false;
	private int opcionNavegacion = 0;

	private WebView pagina = null;
	private WebView webReferencias = null;
	private PrimeraSeccion primeraSeccion = null;
	private PestaniasFragment pestanias = null;

	private JavaJs (){
	}

	static {
		instancia = new JavaJs();
	}

	public static JavaJs getInstancia (){
		return instancia;
	}

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

	@JavascriptInterface
	public void ocultaAccesosRapidos (){
		if ( pestanias != null ){
			( ( MainActivity ) pestanias.getActivity() ).ocultaAccesos();
		}
	}

	@JavascriptInterface
	public void muestraAccesosRapidos (){
		if ( pestanias != null ){
			( ( MainActivity ) pestanias.getActivity() ).muestraAccesos();
		}
	}

	public void setPestanias ( PestaniasFragment pestanias ){
		this.pestanias = pestanias;
	}

	@JavascriptInterface
	public void verComentario ( String pagina ){
		Log.i( "InfoEx-PuenteApp", "referencias - cargando : " + pagina );
		if ( pestanias != null ){
			( ( MainActivity ) pestanias.getActivity() ).muestraReferencias();
			webReferencias.loadUrl( pagina );
		}
	}

	public void setWebReferencias ( WebView webReferencias ){
		this.webReferencias = webReferencias;
		Log.i( "InfoEx-PuenteApp", "referencias - configurada" );
	}

}