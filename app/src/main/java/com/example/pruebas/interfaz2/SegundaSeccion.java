package com.example.pruebas.interfaz2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pruebas.interfaz2.R;

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
}
