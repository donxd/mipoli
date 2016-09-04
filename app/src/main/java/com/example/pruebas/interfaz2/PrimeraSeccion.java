package com.example.pruebas.interfaz2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	private Spinner controlListaPlanteles;
	private static boolean paginaConfigurada = false;
	private Integer contador;
	private boolean paginaCargada = true;
	private WebView pagina;

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
		pagina = (WebView) vista.findViewById( R.id.nav_web );
		pagina.getSettings().setBuiltInZoomControls( true );
		pagina.getSettings().setJavaScriptEnabled(true);
		pagina.setWebViewClient( new WebViewClient() {

			@Override
			public void onPageFinished( WebView webview, String url ){
				super.onPageFinished( webview, url );

				if ( paginaCargada && pagina.getVisibility() == View.GONE ){
					pagina.setVisibility( View.VISIBLE );
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
				paginaCargada = false;
				TextView textView = (TextView) vista.findViewById( R.id.mensaje_pagina );
				textView.setText( "No hay internet" );
				textView.setVisibility( View.VISIBLE );

				/*
				WebView webView = (WebView) vista.findViewById( R.id.nav_web );
				webView.setVisibility( View.GONE );
				*/
				pagina.setVisibility( View.GONE );
			}

		});

		pagina.loadUrl( "https://www.saes.upiicsa.ipn.mx" );
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

}
