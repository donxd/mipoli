package com.example.pruebas.interfaz2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PestaniasFragment extends Fragment {

	public static TabLayout tabLayout;
	public static ViewPager viewPager;
	public static int numero_secciones = 3;

	private final PrimeraSeccion seccion1 = new PrimeraSeccion();
	private final SegundaSeccion seccion2 = new SegundaSeccion();
	private final TerceraSeccion seccion3 = new TerceraSeccion();
	private OpcionesPlantel opcionesPlantel = new OpcionesPlantel();


	private Spinner listaPlanteles;
	private AdaptadorPestanias adaptadorPestanias;
	private FragmentManager manejador;

	private SharedPreferences preferencias;
	private Context context;

	private boolean seccionesCreadas = false;
	private static Integer contador = 0;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View x = inflater.inflate( R.layout.pestanias_layout, null );
		tabLayout = (TabLayout) x.findViewById( R.id.tabs );
		viewPager = (ViewPager) x.findViewById( R.id.viewPager );

		seccion1.setContador( contador );
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

	public void setContext ( Context context ){
		this.context = context;

		cargaPreferencias();
		seccion1.setContext( context );
	}

	private void cargaPreferencias () {
		preferencias = PreferenceManager.getDefaultSharedPreferences( this.context );
	}

	public void aplicaPreferencias () {
		aplicaPreferenciaPlantel();
	}

	private void aplicaPreferenciaPlantel () {
		String plantel = preferencias.getString( "plantel", "" );
		if ( plantel.length() > 0 ) {
			listaPlanteles.setSelection( opcionesPlantel.getIndiceOpcionPlantel( plantel ) );
		}
	}

	public void configuraBarraHerramientas ( Spinner listaHerramientas ){
		listaPlanteles = listaHerramientas;
		seccion1.setControlPlanteles( listaPlanteles );
		//Spinner listaHerramientas = (Spinner) vistaPrincipal.findViewById( R.id.lista_herramientas );
		listaHerramientas.setAdapter( getAdaptadorDatos() );
		listaHerramientas.setOnItemSelectedListener( getEventoSeleccionPlantel() );
	}

	private ArrayAdapter<String> getAdaptadorDatos (){
		List<String> listaPlanteles = opcionesPlantel.getListaOpcionesPlanteles();
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>( context, R.layout.lista_presentacion, listaPlanteles );

		/*
		vista,
		// R.layout.support_simple_spinner_dropdown_item,
		R.layout.lista_presentacion,
		getListaDatos()
		*/

		adaptador.setDropDownViewResource( R.layout.lista_seleccion );

		return adaptador;
	}

	private AdapterView.OnItemSelectedListener getEventoSeleccionPlantel (){
		return new AdapterView.OnItemSelectedListener (){
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
				guardaPreferenciaPlantel();
				//verificaContexto();
				redirigePlantelPreferencia();
			}

			@Override
			public void onNothingSelected( AdapterView<?> adapterView ){
			}
		};
	}

	private void guardaPreferenciaPlantel (){
		SharedPreferences.Editor editorPreferencias = getEditorPreferencias();
		editorPreferencias.putString( "plantel", listaPlanteles.getSelectedItem().toString() );
		editorPreferencias.commit();
	}

	private void redirigePlantelPreferencia (){
		seccion1.redirigePlantel();
	}

	private SharedPreferences.Editor getEditorPreferencias () {
		return preferencias.edit();
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
					case 1:
						ocultaPlanteles();
						seccion2.revisaConexionInternet();
						break;
					case 2:
						ocultaPlanteles();
						break;
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
		//getChildFragmentManager().beginTransaction().
		// FragmentTransaction fragmentTransaction = manejador.beginTransaction();
		//seccion1.actualizaLeyenda();
		//seccion1.cargaPaginaSaes();
		// fragmentTransaction.detach( manejador );
		// fragmentTransaction.commit();
		seccion1.controlaPaginaSaes();
	}

	public void setManejador ( FragmentManager manejador ) {
		this.manejador = manejador;
	}

	/*private void verificaContexto (){
		if ( preferencias == null ){
			cargaPreferencias();
		}
	}*/

	class AdaptadorPestanias extends FragmentPagerAdapter {

		public AdaptadorPestanias ( FragmentManager fragmentManager ){
			super( fragmentManager );
		}

		@Override
		public Fragment getItem ( int position ){

			/*
			Fragment seccion = null;
			FragmentManager fragmentManager = getChildFragmentManager();

			String mostrar = "";
			String [] ocultar = new String [ 2 ];

			switch ( position ){
				case 0 :
					seccion = seccion1;
					mostrar = "s1";
					ocultar[ 0 ] = "s2";
					ocultar[ 0 ] = "s3";
					break;
				case 1 :
					seccion = seccion2;
					mostrar = "s2";
					ocultar[ 0 ] = "s1";
					ocultar[ 0 ] = "s3";
					break;
				case 2 :
					seccion = seccion3;
					mostrar = "s3";
					ocultar[ 0 ] = "s1";
					ocultar[ 0 ] = "s2";
					break;
			}

			if ( seccion != null ){
				fragmentManager.beginTransaction().show( fragmentManager.findFragmentByTag( mostrar ) );
				fragmentManager.beginTransaction().show( fragmentManager.findFragmentByTag( ocultar[ 0 ] ) );
				fragmentManager.beginTransaction().show( fragmentManager.findFragmentByTag( ocultar[ 1 ] ) );
			}


			return seccion;
			*/

			switch ( position ){
				case 0  : return seccion1;
				case 1  : return seccion2;
				case 2  : return seccion3;
				default : return  null;
			}

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
