package com.example.pruebas.interfaz2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PrimeraSeccion extends Fragment {

	private View vista = null;

	private WebView pagina;
	private Context context;
	private Spinner controlListaPlanteles;
	private SharedPreferences preferencias;

	private Integer contador;
	private String paginaCarga;

	private static boolean paginaConfigurada = false;
	private boolean paginaCargada = true;


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

		cargaPreferencias();

		String paginaCargar = getPaginaCargar();

		pagina = (WebView) vista.findViewById(R.id.nav_web);
		pagina.getSettings().setBuiltInZoomControls(true);
		pagina.getSettings().setJavaScriptEnabled(true);
		pagina.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView webview, String url) {
				super.onPageFinished(webview, url);

				if (paginaCargada && pagina.getVisibility() == View.GONE) {
					pagina.setVisibility(View.VISIBLE);
					CookieSyncManager.getInstance().sync();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				paginaCargada = false;
				TextView textView = (TextView) vista.findViewById(R.id.mensaje_pagina);
				textView.setText("No hay internet");
				textView.setVisibility(View.VISIBLE);

					/*
					WebView webView = (WebView) vista.findViewById( R.id.nav_web );
					webView.setVisibility( View.GONE );
					*/
				pagina.setVisibility(View.GONE);
			}

		});

		pagina.loadUrl( paginaCargar );
	}

	public void setControlPlanteles ( Spinner controlListaPlanteles ){
		this.controlListaPlanteles = controlListaPlanteles;
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

	private String getPaginaCargar (){
		String plantel = preferencias.getString("plantel", "");
		if ( plantel.length() == 0 ) {
			plantel = controlListaPlanteles.getSelectedItem().toString();
		}

		return String.format( "https://www.saes.%s.ipn.mx", plantel );
	}

	private void cargaPreferencias() {
		preferencias = PreferenceManager.getDefaultSharedPreferences( context );
	}

	public void redirigePlantel (){
		String paginaCargar = getPaginaCargar();
		pagina.loadUrl( paginaCargar );
	}

}
