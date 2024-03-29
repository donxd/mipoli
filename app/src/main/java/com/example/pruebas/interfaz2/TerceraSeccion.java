package com.example.pruebas.interfaz2;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class TerceraSeccion extends Fragment {

	private View vista = null;
	private WebView pagina = null;
	private Context context;

	private Spinner controlListaAccesos;
	private Spinner controlListaPlanteles;
	private SharedPreferences preferencias;
	private PestaniasFragment pestanias;

	private final String marcaLog = "InfoEx";
	private final String rutaScript = "js/core.js";

	private static boolean paginaConfigurada = false;
	private boolean paginaCargada = true;
	private boolean paginaError = false;
	private boolean estadoSesionCompartida = false;

	private float ultimoZoom = 0;
	private int zoomAnterior = 0;

	private JavaJs javaJs = JavaJs.getInstancia();

	private TextView mensajesPagina = null;
	private AlertDialog.Builder constructorAlertas = null;
	private AlertDialog mensajeAlerta = null;

	@Nullable
	@Override
	public View onCreateView (LayoutInflater layoutInflater, ViewGroup contenedor, Bundle estadoInstanciaGuardada ){
		if ( vista == null ){

			return layoutInflater.inflate( R.layout.tercera_seccion, null );
		}

		return vista;
	}

	@Override
	public void onViewCreated ( View view, Bundle savedInstanceState ){
		vista = view;
		vista.setTag( "s3" );

		if ( !paginaConfigurada ){
			cargaPaginaLugares();
			paginaConfigurada = true;
			configuraBotonReintentar();
		}
	}

	public void cargaPaginaLugares (){
		configuraPaginaLugares();
		cargaPreferencias();
		// configuraControlFlotante();

		String paginaCargar = estadoSesionCompartida ? getPaginaAcceso( OpcionesAccesos.OPCION_OCUPABILIDAD ) : getPaginaCargar();
		// String paginaCargar = getPaginaCargarPreferencia();
		configuraConexionJs();

		paginaCargada = false;
		paginaError = false;
		pagina.loadUrl( paginaCargar );
		// pestanias.fijaPlantelSeleccionado();
		// Log.i( "Info", "Seguimiento - cargaPaginaLugares", new Exception() );
		// ( ( MainActivity ) pestanias.getActivity() ).setCargandoSaes();
	}

	private void cargaPreferencias () {
		preferencias = PreferenceManager.getDefaultSharedPreferences( this.context );
	}

	private String getPaginaCargarPreferencia (){
		return getPaginaCargar();
	}

	private String getPaginaCargar (){
		// cargaPreferencias();
		String plantel = preferencias.getString( "plantel", "" );
		if ( plantel.length() == 0 ) {
			plantel = controlListaPlanteles.getSelectedItem().toString();
		}

		return String.format( "https://www.saes.%s.ipn.mx", plantel.toLowerCase() );
	}

	private void configuraConexionJs (){
		javaJs.setTerceraSeccion( (TerceraSeccion) this );
		javaJs.setPagina( pagina );
		pagina.addJavascriptInterface( javaJs, "androidJs" );
	}

	public String getPaginaAcceso ( String acceso ){
		return String.format(
			"%s%s"
			, getPaginaCargar()
			, OpcionesAccesos.getDireccionAcceso( acceso )
		);
	}

	private void configuraPaginaLugares (){
		if ( pagina == null ){

			pagina = (WebView) vista.findViewById( R.id.webLugares );
			pagina.getSettings().setDomStorageEnabled( true );
			pagina.getSettings().setBuiltInZoomControls( true );
			pagina.getSettings().setJavaScriptEnabled( true );
			pagina.getSettings().setPluginState( WebSettings.PluginState.ON );
			pagina.getSettings().setDatabasePath( context.getDir("database", Context.MODE_PRIVATE).getPath() );
			pagina.getSettings().setSaveFormData(false);
			pagina.getSettings().setDatabaseEnabled( true );
			pagina.getSettings().setSupportZoom( true );
			// pagina.clearFormData();
			configuraZoom( pagina );
			pagina.setWebViewClient( getConfiguracionPaginaLugares() );

		}

	}

	/*
		// pagina.setOnTouchListener( new View.OnTouchListener(){
		// 	@Override
		// 	public boolean onTouch(View v, MotionEvent event ){

		// 		switch (event.getAction()) {
		// 			case MotionEvent.ACTION_DOWN:
		// 			case MotionEvent.ACTION_UP:
		// 				if ( !v.hasFocus() ){
		// 					v.requestFocus();
		// 					InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 					imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

		// 				}

		// 				break;
		// 		}

		// 		return false;
		// 	}

		// });

		String paginaCargar =  estadoSesionCompartida ? getPaginaAcceso( OpcionesAccesos.OPCION_OCUPABILIDAD ) : getPaginaCargar();

		paginaCargada = false;
		paginaError = false;
		pagina.loadUrl( paginaCargar );
		// ( ( MainActivity ) pestanias.getActivity() ).setCargandoLugares();
	*/

	private void configuraZoom ( WebView pagina ){
		pagina.getSettings().setLoadWithOverviewMode( true );
		pagina.getSettings().setUseWideViewPort( true );
	}

	private WebViewClient getConfiguracionPaginaLugares (){
		return new WebViewClient() {

			@Override
			public void onPageFinished ( WebView webview, String url ){
				super.onPageFinished( webview, url );
				paginaCargada = true;

				if ( paginaError ){
					( ( MainActivity ) pestanias.getActivity() ).ocultaPaginaPresentada();
					paginaError = false;
				} else {
					// ( ( MainActivity ) pestanias.getActivity() ).setLugaresCargado();
					controlaPaginaCargada( webview, url );
				}

			}

			@Override
			public void onScaleChanged( WebView vista, float viejaEscala, float nuevaEscala ){
				super.onScaleChanged( vista, viejaEscala, nuevaEscala );
				controlaEscalamientoPagina( vista, viejaEscala, nuevaEscala );
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				controlaErrorPagina( view, errorCode, description, failingUrl );
			}

		};
	}

	private void controlaPaginaCargada ( WebView webview, String url ){
		if ( paginaCargada && paginaNoEsVisible() ){
			muestraPagina();
		}
		controlaZoomPagina();
		controlaContenidoPagina( webview, url );
		// detectaSeccionSaes( url );
		//guardarNavegacion( url );
	}

	private boolean paginaNoEsVisible (){
		return pagina.getVisibility() == View.GONE;
	}

	private void muestraPagina (){
		pagina.setVisibility( View.VISIBLE );
	}

	@SuppressWarnings( "deprecation" )
	private void controlaZoomPagina (){
		if ( esAndroidSuperiorKitkat() && ultimoZoom > 0.01 ){
			controlZoomActual();
		} else {
			controlZoomAnterior();
		}
	}

	@TargetApi(21)
	private void controlZoomActual (){
		pagina.zoomBy( ultimoZoom );
		// Log.i( marcaLog, "zoom e : " + ultimoZoom );
	}

	private void controlZoomAnterior (){
		// Log.i( marcaLog, "zoom ant : " + zoomAnterior );
		// Log.i( marcaLog, "zoom 3 : " + pagina.getScale() );
		if ( zoomAnterior != pagina.getScale() ){
			zoomAnterior = (int) ( pagina.getScale() * 100 );
			// Log.i( marcaLog, "zoom f : " + zoomAnterior );
			pagina.setInitialScale( zoomAnterior );
		}
	}

	private boolean esAndroidSuperiorKitkat (){
		return android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH;
	}

	/*

					controlaZoomPagina( webview );

					if ( url.contains( "/PDF/" ) ){
						// setOpcionNavegacion( 1 );
					} else {
						// setOpcionNavegacion( 0 );

						inyectaJs( webview );
						javaJs.getContenido();
						CookieSyncManager.getInstance().sync();
					}

					// detectaSeccionSaes( url );
					// guardarNavegacion( url );

	*/

	private void controlaContenidoPagina ( WebView webview, String url ){
		if ( !paginaContienePDF( url ) ){
			inyectaJs( webview );
			javaJs.getContenido();
			CookieSyncManager.getInstance().sync();
		}
	}

	private boolean paginaContienePDF ( String url ){
		return url.contains( "/PDF/" );
	}

	private void controlaEscalamientoPagina ( WebView vista, float viejaEscala, float nuevaEscala ){
		// Log.i( marcaLog, "zoom escala : " + viejaEscala +" -> "+ nuevaEscala );
		if ( viejaEscala != nuevaEscala ){
			viejaEscala = nuevaEscala;
		}
	}

	private void controlaErrorPagina ( WebView view, int errorCode, String description, String failingUrl ){
		paginaCargada = false;
		paginaError = true;
		// ( ( MainActivity ) pestanias.getActivity() ).resetearListadoCargado( "error ts" );
		// ocultaControlPagina( view );
		if ( noHayAlertasActivas() ){
			muestraMensajeErrorPagina( errorCode );
			indicarMostrandoAlerta();
		}
	}

	private boolean noHayAlertasActivas (){
		return !( ( MainActivity ) pestanias.getActivity() ).getMostrandoAlerta();
	}

	private void muestraMensajeErrorPagina ( int codigoError ){
		String mensajeError = getMensajeErrorPagina( codigoError );
		presentaMensajeError( mensajeError );
	}

	private void indicarMostrandoAlerta(){
		( ( MainActivity ) pestanias.getActivity() ).setMostrandoAlerta( true );
	}

	private void ocultaControlPagina (){
		pagina.setVisibility( View.GONE );
	}

	private void ocultaControlPagina ( WebView pagina ){
		pagina.setVisibility( View.GONE );
	}

	private void presentaMensajeError ( String mensajeError ){
		if ( mensajeAlerta == null ){
			creaContenedorMensajesAlerta();
		}

		constructorAlertas.setMessage( mensajeError );
		mensajeAlerta = constructorAlertas.create();
		mensajeAlerta.show();
	}

	private void creaContenedorMensajesAlerta (){
		constructorAlertas = new AlertDialog.Builder( context );

		constructorAlertas
			.setTitle( "Error" )
			.setCancelable( false )
			.setPositiveButton( "OK", getComportamientoOkMensajeError() );
	}

	private DialogInterface.OnClickListener getComportamientoOkMensajeError (){
		return new DialogInterface.OnClickListener(){
			public void onClick ( DialogInterface dialog, int id ){
				dialog.cancel();
				( ( MainActivity ) pestanias.getActivity() ).setMostrandoAlerta( false );
			}
		};
	}

	private String getMensajeErrorPagina ( int codigoError ){
		switch ( codigoError ){
			case WebViewClient.ERROR_CONNECT        : return "Hubo un error en la conexión de la página.";
			case WebViewClient.ERROR_TIMEOUT        : return "La página del plantel está tardando mucho en responder.";
			case WebViewClient.ERROR_FILE_NOT_FOUND : return "La página solicitada no ha sido encontrada [404].";
			default                                 : return "Hubo un error al conectar con la página.";
		}
	}

	// private void controlaZoomPagina ( WebView webview ){
	// 	if ( android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH && ultimoZoom > 0.01 ){
	// 		webview.zoomBy( ultimoZoom );
	// 		// Log.i( marcaLog, "zoom e : " + ultimoZoom );
	// 	} else {
	// 		// Log.i( marcaLog, "zoom ant : " + zoomAnterior );
	// 		// Log.i( marcaLog, "zoom 3 : " + pagina.getScale() );
	// 		if ( zoomAnterior != pagina.getScale() ){
	// 			zoomAnterior = (int) ( pagina.getScale() * 100 );
	// 			// Log.i( marcaLog, "zoom f : " + zoomAnterior );
	// 			pagina.setInitialScale( zoomAnterior );
	// 		}

	// 	}
	// }

	public void detectaSeccionSaes ( String url ){
		// String direccionSaes = pagina.getUrl();
		String direccionSaes = url;
		String paginaCargada = getPaginaCargar();

		String seccionSaes = getRutaPagina( direccionSaes, paginaCargada );
		marcarSeccionSaes( seccionSaes );
		// if ( OpcionesAccesos.tieneSeccion( seccionSaes ) ){
		// 	marcarSeccionSaes( seccionSaes );
		// }
		// guardarNavegacion( direccionSaes );
	}

	private String getRutaPagina ( String url, String dominio ){

		// Log.i( marcaLog, String.format( " url : [%s] - dominio : [%s]", url, dominio ) );

		if ( url.contains( dominio ) ){

			return url.substring( dominio.length() );
		}

		return "";
	}

	private void marcarSeccionSaes ( String seccion ){
		Log.i( marcaLog, String.format( " seccion : [%s]", seccion ) );
		int posicionAcceso = OpcionesAccesos.getAcceso( seccion );
		Log.i( marcaLog, String.format( " posicion : [%d]", posicionAcceso ) );

		controlListaAccesos.setActivated( false );
		controlListaAccesos.setSelection( posicionAcceso );
		controlListaAccesos.setActivated( true );
	}

	private void inyectaJs ( WebView webview ){
		//if ( !inyeccionRealizada ){

		String contenidoInyeccionJS = getCadenaInyeccionJs();

		if ( contenidoInyeccionJS.length() > 0 ){
			webview.loadUrl( contenidoInyeccionJS );
			webview.loadUrl( "javascript:setVersionAndroid(" + Build.VERSION.SDK_INT +")");
			Log.i( marcaLog, "Version android : " + Build.VERSION.SDK_INT );
			//inyeccionRealizada = true;
		}

		//}

	}

	public String getCadenaInyeccionJs () {
		StringBuilder cadena = new StringBuilder();
		String contenidoScript = getContenidoArchivo();

		if ( contenidoScript.length() > 0 ){

			cadena.append( "javascript:(function(){" );
			cadena.append( "var script=document.createElement('script');" );
			cadena.append( "script.type='text/javascript';" );
			cadena.append( "script.innerHTML=decodeURIComponent(escape(window.atob('" );
			cadena.append( contenidoScript );
			cadena.append( "')));" );
			cadena.append( "document.querySelector('head').appendChild(script);" );
			cadena.append( "})()" );

		}
		//cadena = new StringBuilder();
		//cadena.append( "javascript:(function(){document.querySelector('h2').innerHTML='aksjdlfkasdf';})()" );
		Log.i( marcaLog, "insertando : " + cadena.toString() );

		return cadena.toString();
	}

	private String getContenidoArchivo (){

		try {

			InputStream inputStream = context.getAssets().open( rutaScript );
			byte [] contenido = new byte[ inputStream.available() ];

			inputStream.read( contenido );
			inputStream.close();

			String textoArchivo = new String( contenido );

			// textoArchivo = textoArchivo.replaceAll( "é", "\u00E9" );

			// return Base64.encodeToString( textoArchivo.getBytes(), Base64.NO_WRAP );
			// return new String( contenido );
			return Base64.encodeToString( contenido, Base64.NO_WRAP );

		} catch ( IOException error ){
			Log.i( marcaLog, "No se pudo leer el archivo de script" );
		}

		return "";
	}

	public void setControlPlanteles ( Spinner controlListaPlanteles ){
		this.controlListaPlanteles = controlListaPlanteles;
	}

	public void setControlAccesos ( Spinner controlListaAccesos ){
		this.controlListaAccesos = controlListaAccesos;
	}

	public void setContext( Context context ){
		this.context = context;
	}

	public void controlaPaginaLugares (){
		if ( !paginaCargada ){
			paginaCargada = true;
			paginaError = false;
			pagina.reload();
		}
	}

	public WebView getControlLugares (){
		return pagina;
	}

	public void setEstadoSesionCompartida ( boolean estadoSesionCompartida ){
		this.estadoSesionCompartida = estadoSesionCompartida;
	}

	public boolean esPaginaCargada (){
		return paginaCargada;
	}

	public void setPestanias ( PestaniasFragment pestanias ){
		this.pestanias = pestanias;
		javaJs.setPestanias( pestanias );
	}

	public void redirigePlantel (){
		if ( pagina != null ){
			pagina.stopLoading();
			String paginaCargar = estadoSesionCompartida ? getPaginaAcceso( OpcionesAccesos.OPCION_OCUPABILIDAD ) : getPaginaCargar();
			// if ( pagina == null ){
			// 	pagina = (WebView) vista.findViewById(R.id.nav_web);
			// }
			paginaError = false;
			pagina.loadUrl( paginaCargar );
		}
	}

	public boolean cargaConError (){
		return paginaError;
	}

	private void configuraBotonReintentar (){
		Button reintentar = (Button) vista.findViewById( R.id.boton_reintentar_3 );
		reintentar.setOnClickListener( getComportamientoReintentar() );
	}

	private View.OnClickListener getComportamientoReintentar (){
		return new View.OnClickListener(){

			public void onClick ( View vista ){
				( ( MainActivity ) pestanias.getActivity() ).comportamientoRecargar();
			}

		};
	}
}