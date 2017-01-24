package com.example.pruebas.interfaz2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pruebas.interfaz2.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SegundaSeccion extends Fragment {

	private View vista = null;
	private static boolean paginaConfigurada = false;
	private boolean paginaCargada = false;
	private boolean paginaError = false;
	private JavaJs javaJs = JavaJs.getInstancia();

	private Context context;
	private WebView pagina;
	private SharedPreferences preferencias;
	private Spinner controlListaPlanteles;

	private PestaniasFragment pestanias;

	private TextView mensajesPagina = null;
	private AlertDialog.Builder constructorAlertas = null;
	private AlertDialog mensajeAlerta = null;

	@Nullable
	@Override
	public View onCreateView (LayoutInflater layoutInflater, ViewGroup contenedor, Bundle estadoInstanciaGuardada ){
		return layoutInflater.inflate( R.layout.segunda_seccion, null );
	}

	@Override
	public void onViewCreated ( View view, Bundle savedInstanceState ){
		vista = view;
		vista.setTag( "s2" );

		if ( !paginaConfigurada ){
			paginaError = false;
			cargaPaginaReferencias();
			paginaConfigurada = true;
			configuraBotonReintentar();
		}

		if ( !paginaCargada ){
			paginaCargada = true;
		}
	}

	public void cargaPaginaReferencias (){

		configuraControlPagina();
		cargaPreferencias();

		String paginaCargar = getPaginaCargar();

		// javaJs.setPrimeraSeccion( (PrimeraSeccion) this );
		// javaJs.setPagina( pagina );
		javaJs.setWebReferencias( pagina );
		// pagina.addJavascriptInterface( javaJs, "androidJs" );

		paginaCargada = false;
		paginaError = false;
		pagina.loadUrl( paginaCargar );
	}

	private void configuraControlPagina (){
		if ( pagina == null ){

			pagina = (WebView) vista.findViewById( R.id.webReferencias );
			pagina.getSettings().setBuiltInZoomControls( true );
			pagina.getSettings().setSupportZoom( true );
			pagina.getSettings().setJavaScriptEnabled( true );
			pagina.setWebViewClient( getComportamientoPagina() );
		}
	}

	private void cargaPreferencias () {
		preferencias = PreferenceManager.getDefaultSharedPreferences( this.context );
	}

	/*
	private void cargaPreferencias () {
		if ( context == null ){
			context = getActivity();
		}

		preferencias = PreferenceManager.getDefaultSharedPreferences( context );
	}
	*/

	private String getPaginaCargar (){
		String plantel = preferencias.getString( "plantel", "" );
		if ( plantel.length() == 0 ) {
			plantel = getControlPlantelSeleccion();
		}

		return String.format( "http://diccionariodemaestros.com/%s/", plantel.toLowerCase() );
	}

	private String getControlPlantelSeleccion (){
		return controlListaPlanteles.getSelectedItem().toString();
	}

	/*
	private String getControlPlantelSeleccion (){
		if ( controlListaPlanteles == null ){
			Log.i( "Info", "controlListaPlanteles -> null" );

			if ( pestanias == null ) Log.i( "Info", "pestanias? -> null" );

			MainActivity principal = (MainActivity) getActivity();

			if ( principal == null ) Log.i( "Info", "main activity -> null" );

			Spinner controlPlanteles = principal.inicializaControlListaPlanteles();
			PestaniasFragment pestaniasPrincipal = principal.getPestanias();

			if ( pestaniasPrincipal == null ) Log.i( "Info", "PestaniasFragment -> null" );
			else {
				pestanias = pestaniasPrincipal;
			}

			if ( controlPlanteles == null ) Log.i( "Info", "controlPlanteles -> null" );
			else {
				return controlPlanteles .getItemAtPosition( 0 ).toString();
			}

			if ( controlListaPlanteles == null ) Log.i( "Info", "controlListaPlanteles2 -> null" );
			else {
				return controlListaPlanteles.getItemAtPosition( 0 ).toString();
			}

		}

		return controlListaPlanteles.getSelectedItem().toString();
	}
	*/

	public void setContext( Context context ){
		this.context = context;
	}

	public void setControlPlanteles ( Spinner controlListaPlanteles ){
		this.controlListaPlanteles = controlListaPlanteles;
	}

	// public void revisaConexionInternet(){
	// 	new AccesoInternet().execute();
	// }

	// private class AccesoInternet extends AsyncTask<String, Void, String> {

	// 	@Override
	// 	protected String doInBackground( String... parametros ){
	// 		InetAddress inetAddress = null;

	// 		try {
	// 			inetAddress = InetAddress.getByName( "www.google.com.mx" );

	// 			return inetAddress.getHostAddress();

	// 		} catch ( UnknownHostException error ){
	// 		}

	// 		return null;
	// 	}

	// 	@Override
	// 	protected void onPostExecute ( String respuesta ){

	// 		TextView textView = (TextView) vista.findViewById( R.id.textView2 );
	// 		String salida = "No hay acceso";

	// 		if ( respuesta != null ){
	// 			salida = "Si hay acceso";
	// 		}

	// 		textView.setText( salida );
	// 	}

	// }

	public void redirigePlantelReferencias (){
		pagina.stopLoading();
		String paginaCargar = getPaginaCargar();
		paginaError = false;
		pagina.loadUrl( paginaCargar );
	}

	public WebView getControlReferencias (){
		return pagina;
	}

	private WebViewClient getComportamientoPagina (){

		return new WebViewClient() {

			@Override
			public void onPageFinished ( WebView webview, String url ){
				super.onPageFinished( webview, url );
				paginaCargada = true;
				if ( paginaError ){
					( ( MainActivity ) pestanias.getActivity() ).ocultaPaginaPresentada();
					paginaError = false;
				} else {
					// ( ( MainActivity ) pestanias.getActivity() ).setSaesCargado();
					controlaPaginaCargada( webview, url );
					// Log.i( "Info", "SAES { url : '"+ url +"' }" );
				}
			}

			@Override
			public void onReceivedError ( WebView view, int errorCode, String description, String failingUrl ){
				controlaErrorPagina( view, errorCode, description, failingUrl );
			}

		};
	}

	private void controlaPaginaCargada ( WebView webview, String url ){
		if ( paginaCargada && paginaNoEsVisible() ){
			muestraPagina();
		}
	}

	private void muestraPagina (){
		pagina.setVisibility( View.VISIBLE );
	}

	private boolean paginaNoEsVisible (){
		return pagina.getVisibility() == View.GONE;
	}

	private void controlaErrorPagina ( WebView view, int errorCode, String description, String failingUrl ){
		paginaCargada = false;
		paginaError = true;
		// ( ( MainActivity ) pestanias.getActivity() ).ocultaPaginaPresentada();
		// ( ( MainActivity ) pestanias.getActivity() ).resetearListadoCargado( "error ps" );
		// ocultaControlPagina( view );
		// ocultaControlPagina();
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

	private String getMensajeErrorPagina ( int codigoError ){
		switch ( codigoError ){
			case WebViewClient.ERROR_CONNECT        : return "Hubo un error en la conexión de la página.";
			case WebViewClient.ERROR_TIMEOUT        : return "La página del plantel está tardando mucho en responder.";
			case WebViewClient.ERROR_FILE_NOT_FOUND : return "La página solicitada no ha sido encontrada [404].";
			default                                 : return "Hubo un error al conectar con la página.";
		}
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

	public void setPestanias ( PestaniasFragment pestanias ){
		this.pestanias = pestanias;
	}

	private void configuraBotonReintentar (){
		Button reintentar = (Button) vista.findViewById( R.id.boton_reintentar_2 );
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
