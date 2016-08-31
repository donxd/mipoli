package com.example.pruebas.interfaz2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PestaniasFragment extends Fragment {

	public static TabLayout tabLayout;
	public static ViewPager viewPager;
	public static int numero_secciones = 3;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View x = inflater.inflate( R.layout.pestanias_layout, null );
		tabLayout = (TabLayout) x.findViewById( R.id.tabs );
		viewPager = (ViewPager) x.findViewById( R.id.viewPager );

		viewPager.setAdapter( new AdaptadorPestanias( getChildFragmentManager() ) );

		tabLayout.post(new Runnable() {
			@Override
			public void run() {
				tabLayout.setupWithViewPager( viewPager );
			}
		});

		return x;
	}

	class AdaptadorPestanias extends FragmentPagerAdapter {

		public AdaptadorPestanias ( FragmentManager fragmentManager ){
			super( fragmentManager );
		}

		@Override
		public Fragment getItem ( int position ){
			switch ( position ){
				case 0 : return new PrimeraSeccion();
				case 1 : return new SegundaSeccion();
				case 2 : return new TerceraSeccion();
			}

			return null;
		}

		@Override
		public int getCount (){
			return numero_secciones;
		}

		@Override
		public CharSequence getPageTitle ( int position ){
			switch ( position ){
				case 0: return "Primera";
				case 1: return "Segunda";
				case 2: return "Tercera";
			}

			return null;
		}

	}


}
