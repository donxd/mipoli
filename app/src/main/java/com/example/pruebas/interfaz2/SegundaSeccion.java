package com.example.pruebas.interfaz2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pruebas.interfaz2.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SegundaSeccion extends Fragment {

	private View vista = null;

	@Nullable
	@Override
	public View onCreateView (LayoutInflater layoutInflater, ViewGroup contenedor, Bundle estadoInstanciaGuardada ){
		return layoutInflater.inflate( R.layout.segunda_seccion, null );
	}

	@Override
	public void onViewCreated ( View view, Bundle savedInstanceState ){
		vista = view;
		vista.setTag( "s2" );
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


}
