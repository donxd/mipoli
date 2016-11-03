package com.example.pruebas.interfaz2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pruebas.interfaz2.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SegundaSeccion extends Fragment {

	private View vista = null;
	private static boolean paginaConfigurada = false;
	private JavaJs javaJs = JavaJs.getInstancia();

	private Context context;
	private WebView pagina;
	private SharedPreferences preferencias;
	private Spinner controlListaPlanteles;

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
			cargaPaginaReferencias();
			paginaConfigurada = true;
		}
	}

	public void cargaPaginaReferencias (){

		cargaPreferencias();

		String paginaCargar = getPaginaCargar();

		pagina = (WebView) vista.findViewById( R.id.webReferencias );
		
		pagina.getSettings().setBuiltInZoomControls( true );
		pagina.getSettings().setSupportZoom( true );
		pagina.getSettings().setJavaScriptEnabled( true );
		pagina.setWebViewClient( new WebViewClient() );

		// javaJs.setPrimeraSeccion( (PrimeraSeccion) this );
		// javaJs.setPagina( pagina );
		javaJs.setWebReferencias( pagina );
		// pagina.addJavascriptInterface( javaJs, "androidJs" );

		pagina.loadUrl( paginaCargar );
	}

	private void cargaPreferencias () {
		preferencias = PreferenceManager.getDefaultSharedPreferences( this.context );
	}

	private String getPaginaCargar (){
		String plantel = preferencias.getString( "plantel", "" );
		if ( plantel.length() == 0 ) {
			plantel = controlListaPlanteles.getSelectedItem().toString();
		}

		return String.format( "http://diccionariodemaestros.com/%s/", plantel.toLowerCase() );
	}

	public void setContext( Context context ){
		this.context = context;
	}

	public void setControlPlanteles ( Spinner controlListaPlanteles ){
		this.controlListaPlanteles = controlListaPlanteles;
	}

	public void revisaConexionInternet(){
		new AccesoInternet().execute();
	}

	private class AccesoInternet extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground( String... parametros ){
			InetAddress inetAddress = null;

			try {
				inetAddress = InetAddress.getByName( "www.google.com.mx" );

				return inetAddress.getHostAddress();

			} catch ( UnknownHostException error ){
			}

			return null;
		}

		@Override
		protected void onPostExecute ( String respuesta ){

			TextView textView = (TextView) vista.findViewById( R.id.textView2 );
			String salida = "No hay acceso";

			if ( respuesta != null ){
				salida = "Si hay acceso";
			}

			textView.setText( salida );
		}

	}

	public void redirigePlantelReferencias (){
		String paginaCargar = getPaginaCargar();
		pagina.loadUrl( paginaCargar );
	}


}
