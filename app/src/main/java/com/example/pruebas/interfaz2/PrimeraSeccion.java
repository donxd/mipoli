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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class PrimeraSeccion extends Fragment {

	private View vista = null;
	private WebView pagina = null;

	private Context context;
	private Spinner controlListaPlanteles;
	private Spinner controlListaAccesos;
	private SharedPreferences preferencias;
	private ScaleGestureDetector scaleGestureDetector;

	private Integer contador;
	private String paginaCarga;

	private static boolean paginaConfigurada = false;
	private boolean paginaCargada = false;

	private final String marcaLog = "InfoEx";
	private final String rutaScript = "js/core.js";

	private JavaJs javaJs = JavaJs.getInstancia();

	private FloatingActionButton botonFlotante;
	private int opcionNavegacion = 0;
	private static final int ENCONTRADO = -1;

	private float ultimoZoom = 0;
	private int zoomAnterior = 0;

	private String ultimaPagina = "";
	private PestaniasFragment pestanias;

	private TextView mensajesPagina = null;
	private AlertDialog.Builder constructorAlertas = null;
	private AlertDialog mensajeAlerta = null;

	@Nullable
	@Override
	public View onCreateView ( LayoutInflater layoutInflater, ViewGroup contenedor, Bundle estadoInstanciaGuardada ){
		if ( vista == null ){

			return layoutInflater.inflate( R.layout.primera_seccion, null );
		}

		return vista;
	}

	@Override
	public void onViewCreated ( View view, Bundle savedInstanceState ){
		vista = view;
		vista.setTag( "s1" );
		// actualizaLeyenda();
		if ( !paginaConfigurada ){
			cargaPaginaSaes();
			paginaConfigurada = true;
		}
	}

	/*
	@Override
	public void onActivityCreated( Bundle savedInstanceState ){
		super.onActivityCreated( savedInstanceState );
		actualizaLeyenda();
	}
	*/

	public void controlaPaginaSaes (){
		if ( !paginaCargada ){
			paginaCargada = true;
			pagina.reload();
		}
	}

	public void cargaPaginaSaes (){

		configuraControlPagina();
		cargaPreferencias();
		// configuraControlFlotante();

		String paginaCargar = getPaginaCargarPreferencia();
		configuraConexionJs();

		paginaCargada = false;
		pagina.loadUrl( paginaCargar );
		pestanias.fijaPlantelSeleccionado();
		// Log.i( "Info", "Seguimiento - cargaPaginaSaes", new Exception() );
		// ( ( MainActivity ) pestanias.getActivity() ).setCargandoSaes();
	}

	private void configuraControlPagina (){
		if ( pagina == null ){
			//WebView.setWebContentsDebuggingEnabled( true );
			pagina = (WebView) vista.findViewById(R.id.nav_web);
			pagina.getSettings().setDomStorageEnabled( true );
			pagina.getSettings().setBuiltInZoomControls( true );
			pagina.getSettings().setJavaScriptEnabled( true );
			pagina.getSettings().setPluginState( WebSettings.PluginState.ON );
			pagina.getSettings().setDatabasePath( context.getDir( "database", Context.MODE_PRIVATE ).getPath() );
			// pagina.getSettings().setSaveFormData(false);
			pagina.getSettings().setDatabaseEnabled( true );
			pagina.getSettings().setSupportZoom( true );
			//pagina.clearFormData();
			pagina.setWebViewClient( getComportamientoPagina() );

			// configuraTeclado();
			configuraZoom();
			// configuraRedimensionamiento();
		}
	}

	private void configuraConexionJs (){
		javaJs.setPrimeraSeccion( (PrimeraSeccion) this );
		javaJs.setPagina( pagina );
		pagina.addJavascriptInterface( javaJs, "androidJs" );
	}

	// private void configuraRedimensionamiento (){
		/*scaleGestureDetector = new ScaleGestureDetector( pagina.getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){
			@Override
			public boolean onScale ( ScaleGestureDetector detector ){
				Log.i( marcaLog, "zoom : "+String.valueOf( detector.getScaleFactor() ) );
				return super.onScale( detector );
				//return true;
			}
		});*/

		/*pagina.setOnTouchListener( new View.OnTouchListener(){
			/*@Override
			public boolean onTouch( View view, MotionEvent motionEvent ){
				scaleGestureDetector.onTouchEvent( motionEvent );

				View.OnTouchListener escucha = new View.OnTouchListener();

				return true;
			}* /
		});*/
	// }

	// private void configuraControlFlotante (){
		// botonFlotante = (FloatingActionButton) vista.findViewById( R.id.fab );
		// botonFlotante.setImageDrawable( ContextCompat.getDrawable( getContext(), R.drawable.stat_notify_sync_noanim ) );
		// botonFlotante.setOnClickListener( new View.OnClickListener() {
		// 	@Override
		// 	public void onClick( View view ){

		// 		if ( opcionNavegacion == 0 ){
		// 			pagina.reload();
		// 		}

		// 		if ( opcionNavegacion == 1 ){
		// 			pagina.goBack();
		// 		}

		// 		// Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
		// 			//	.setAction("Action", null).show();
		// 	}
		// });
	// }

	private WebViewClient getComportamientoPagina (){

		return new WebViewClient() {

			@Override
			public void onPageFinished ( WebView webview, String url ){
				super.onPageFinished( webview, url );
				paginaCargada = true;
				// ( ( MainActivity ) pestanias.getActivity() ).setSaesCargado();
				controlaPaginaCargada( webview, url );
				// Log.i( "Info", "SAES { url : '"+ url +"' }" );
			}

			@Override
			public void onScaleChanged ( WebView vista, float viejaEscala, float nuevaEscala ){
				super.onScaleChanged( vista, viejaEscala, nuevaEscala );
				controlaEscalamientoPagina( vista, viejaEscala, nuevaEscala );
			}

			@Override
			public void onReceivedError ( WebView view, int errorCode, String description, String failingUrl ){
				controlaErrorPagina( view, errorCode, description, failingUrl );
			}

		};
	}

	private void controlaPaginaCargada ( WebView webview, String url ){
		if ( paginaCargada && paginaEsVisible() ){
			muestraPagina();
		}
		controlaZoomPagina();
		controlaContenidoPagina( webview, url );
		detectaSeccionSaes( url );
		//guardarNavegacion( url );
	}

	// private void controlaContenidoPagina (){
	// 	if ( url.contains( "/PDF/" ) ){
	// 		// setOpcionNavegacion( 1 );
	// 	} else {
	// 		// setOpcionNavegacion( 0 );

	// 		inyectaJs( webview );
	// 		javaJs.getContenido();
	// 		CookieSyncManager.getInstance().sync();
	// 	}
	// }

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

	private boolean paginaEsVisible (){
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

	private boolean esAndroidSuperiorKitkat (){
		return android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT_WATCH;
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

	private void controlaErrorPagina ( WebView view, int errorCode, String description, String failingUrl ){
		paginaCargada = false;
		( ( MainActivity ) pestanias.getActivity() ).resetearListadoCargado( "error ps" );
		ocultaControlPagina( view );
		if ( noHayAlertasActivas() ){
			muestraMensajeErrorPagina( errorCode );
			indicarMostrandoAlerta();
		}
	}

	private boolean noHayAlertasActivas (){
		return !( ( MainActivity ) pestanias.getActivity() ).getMostrandoAlerta();
	}

	private void indicarMostrandoAlerta(){
		( ( MainActivity ) pestanias.getActivity() ).setMostrandoAlerta( true );
	}

	private void muestraMensajeErrorPagina ( int codigoError ){
		String mensajeError = getMensajeErrorPagina( codigoError );
		// agregaMensajeError( mensajeError );
		// muestraMensajesPagina();
		presentaMensajeError( mensajeError );
	}

	// private void agregaMensajeError ( String mensajeError ){
	// 	if ( mensajesPagina == null ){
	// 		mensajesPagina = (TextView) vista.findViewById( R.id.mensaje_pagina );
	// 	}

	// 	mensajesPagina.setText( mensajeError );
	// }

	// private void muestraMensajesPagina (){
	// 	mensajesPagina.setVisibility( View.VISIBLE );
	// }

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

	private void ocultaControlPagina (){
		pagina.setVisibility( View.GONE );
	}

	private void ocultaControlPagina ( WebView pagina ){
		pagina.setVisibility( View.GONE );
	}

	private void controlaEscalamientoPagina ( WebView vista, float viejaEscala, float nuevaEscala ){
		// Log.i( marcaLog, "zoom escala : " + viejaEscala +" -> "+ nuevaEscala );
		if ( viejaEscala != nuevaEscala ){
			viejaEscala = nuevaEscala;
		}
	}

	private void configuraZoom (){
		pagina.getSettings().setLoadWithOverviewMode( true );
		pagina.getSettings().setUseWideViewPort( true );
	}

	public void setControlPlanteles ( Spinner controlListaPlanteles ){
		this.controlListaPlanteles = controlListaPlanteles;
	}

	public void setControlAccesos ( Spinner controlListaAccesos ){
		this.controlListaAccesos = controlListaAccesos;
	}

	/*
	public void actualizaLeyenda (){
		Button textView = (Button) vista.findViewById( R.id.mensaje_s1 );
		String plantel = controlListaPlanteles.getSelectedItem().toString();
		textView.setText( plantel );
	}
	*/

	public void setContador ( Integer contador ){
		this.contador = contador;
	}

	public void setContext( Context context ){
		this.context = context;
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

	private void cargaPreferencias() {
		preferencias = PreferenceManager.getDefaultSharedPreferences( context );
	}

	public void redirigePlantel (){
		if ( pagina != null ){

			if ( paginaCargando() ){
				detenerCargado();
			}

			String paginaCargar = getPaginaCargar();

			pagina.loadUrl( paginaCargar );
			paginaCargada = false;
			// Log.i( "Info", "Seguimiento - redirigePlantel", new Exception() );
		}
	}

	private boolean paginaCargando (){
		return !paginaCargada;
	}

	private void detenerCargado (){
		pagina.stopLoading();
	}

	public void redirigeAcceso ( Spinner listaAccesos ){
		String paginaCargar = getPaginaAcceso( listaAccesos );
		if ( !paginaCargar.equals( getPaginaCargar() ) && 
				!ultimaPagina.equals( paginaCargar )){

			ultimaPagina = paginaCargar;
			Log.i( marcaLog, " url-pagina : guardada " );
			pagina.loadUrl( paginaCargar );
			// Log.i( "Info", "Seguimiento - redirigeAcceso", new Exception() );
		}
	}

	private String getPaginaAcceso ( Spinner listaAccesos ){
		String acceso = listaAccesos.getSelectedItem().toString();

		return String.format( 
			"%s%s"
			, getPaginaCargar()
			, OpcionesAccesos.getDireccionAcceso( acceso )
		);
	}

	public String getPaginaAcceso ( String acceso ){
		return String.format( 
			"%s%s"
			, getPaginaCargar()
			, OpcionesAccesos.getDireccionAcceso( acceso )
		);
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
			// cadena.append( "var contenidoPagina=document.getElementsByTagName('html')[0].innerHTML;" );
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

	// public void setOpcionNavegacion ( int opcion ){
	// 	if ( opcionNavegacion != opcion ){

	// 		switch ( opcion ){
	// 			case 0:
	// 				presentaIconoRecargar();
	// 				break;
	// 			case 1:
	// 				presentaIconoRegresar();
	// 				break;
	// 		}

	// 		opcionNavegacion = opcion;

	// 	}
	// }

	public int getOpcionNavegacion (){
		return opcionNavegacion;
	}

	// private void presentaIconoRecargar (){
	// 	botonFlotante = (FloatingActionButton) vista.findViewById( R.id.fab );
	// 	botonFlotante.setImageDrawable( ContextCompat.getDrawable( getContext(), R.drawable.stat_notify_sync_noanim ) );
	// }

	// private void presentaIconoRegresar (){
	// 	botonFlotante = (FloatingActionButton) vista.findViewById( R.id.fab );
	// 	botonFlotante.setImageDrawable( ContextCompat.getDrawable( getContext(), R.drawable.ic_media_rew ) );
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

	// private void guardarNavegacion ( String paginaCargada ){
	// 	if ( !paginaCargada.equals( getPaginaCargar() ) && 
	// 			!ultimaPagina.equals( paginaCargada ) ){

	// 		ultimaPagina = paginaCargada;
	// 		Log.i( marcaLog, " url-pagina : guardada " );
	// 		//pagina.loadUrl( paginaCargada );
	// 	}
	// }

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

	public void setPestanias ( PestaniasFragment pestanias ){
		this.pestanias = pestanias;
		javaJs.setPestanias( pestanias );
	}

	public WebView getControlSaes (){
		return pagina;
	}

	public boolean esPaginaCargada (){
		return paginaCargada;
	}

}
