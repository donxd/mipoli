package com.example.pruebas.interfaz2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

}
