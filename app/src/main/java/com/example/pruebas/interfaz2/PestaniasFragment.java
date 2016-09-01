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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PestaniasFragment extends Fragment {

	public static TabLayout tabLayout;
	public static ViewPager viewPager;
	public static int numero_secciones = 3;

	private PrimeraSeccion seccion1 = new PrimeraSeccion();
	private SegundaSeccion seccion2 = new SegundaSeccion();
	private TerceraSeccion seccion3 = new TerceraSeccion();

	private Context context;
	private Spinner listaPlanteles;
	private AdaptadorPestanias adaptadorPestanias;
	private FragmentManager manejador;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View x = inflater.inflate( R.layout.pestanias_layout, null );
		tabLayout = (TabLayout) x.findViewById( R.id.tabs );
		viewPager = (ViewPager) x.findViewById( R.id.viewPager );

		adaptadorPestanias = new AdaptadorPestanias( getChildFragmentManager() );

		viewPager.setAdapter( adaptadorPestanias );
		viewPager.addOnPageChangeListener( getControladorPestanias() );

		tabLayout.post(new Runnable() {
			@Override
			public void run() {
				tabLayout.setupWithViewPager( viewPager );
			}
		});

		return x;
	}

	public void setContext( Context context ){
		this.context = context;
	}

	public void configuraBarraHerramientas ( Spinner listaHerramientas ){
		listaPlanteles = listaHerramientas;
		seccion1.setControlPlanteles( listaPlanteles );
		//Spinner listaHerramientas = (Spinner) vistaPrincipal.findViewById( R.id.lista_herramientas );
		listaHerramientas.setAdapter( getAdaptadorDatos() );
		// listaHerramientas.setOnItemSelectedListener( getEventoSeleccionListaHerramientas() );

	}

	private ArrayAdapter<String> getAdaptadorDatos (){
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>( context, R.layout.lista_presentacion, getListaDatos() );

		/*
		vista,
		// R.layout.support_simple_spinner_dropdown_item,
		R.layout.lista_presentacion,
		getListaDatos()
		*/

		adaptador.setDropDownViewResource( R.layout.lista_seleccion );

		return adaptador;
	}

	private List<String> getListaDatos (){
		List<String> listaDatos = new ArrayList<>();
		listaDatos.add( "UPIICSA" );
		listaDatos.add( "ESCOM" );

		return listaDatos;
	}

	private ViewPager.OnPageChangeListener getControladorPestanias (){
		return new ViewPager.OnPageChangeListener(){

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				switch ( position ){
					case 0:
						controlaPrimeraSeccion();
						muestraPlanteles();
						break;
					case 1: ocultaPlanteles(); break;
					case 2: ocultaPlanteles(); break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		};
	}

	private void ocultaPlanteles (){
		listaPlanteles.setVisibility( View.GONE );
	}

	private void muestraPlanteles (){
		listaPlanteles.setVisibility( View.VISIBLE );
	}

	private void controlaPrimeraSeccion (){
		// FragmentTransaction fragmentTransaction = manejador.beginTransaction();
		//seccion1.actualizaLeyenda();
		seccion1.cargaPaginaSaes();
		// fragmentTransaction.detach( manejador );
		// fragmentTransaction.commit();
	}

	public void setManejador ( FragmentManager manejador ) {
		this.manejador = manejador;
	}

	class AdaptadorPestanias extends FragmentPagerAdapter {

		public AdaptadorPestanias ( FragmentManager fragmentManager ){
			super( fragmentManager );
		}

		@Override
		public Fragment getItem ( int position ){
			switch ( position ){
				case 0 : return seccion1;
				case 1 : return seccion2;
				case 2 : return seccion3;
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
