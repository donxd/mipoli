package com.example.pruebas.interfaz2;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pruebas on 3/09/16.
 */
public class AnoterFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View x = inflater.inflate( R.layout.anoter_layout, null );

		return x;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onViewCreated ( View view, Bundle savedInstanceState ){
		TextView mensajeProyecto = (TextView) view.findViewById( R.id.enlace_pagina_proyecto );
		mensajeProyecto.setClickable( true );
		mensajeProyecto.setMovementMethod( LinkMovementMethod.getInstance() );
		String enlaceProyecto = "<a href='https://es-la.facebook.com/ComplementoSaesChrome/'>Proyecto</a>";

		if ( Build.VERSION.SDK_INT > Build.VERSION_CODES.M ) {
			mensajeProyecto.setText(Html.fromHtml( enlaceProyecto, Html.FROM_HTML_MODE_LEGACY ) );
		} else {
			mensajeProyecto.setText(Html.fromHtml( enlaceProyecto ) );
		}
	}

}
