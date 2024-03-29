package com.example.pruebas.interfaz2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class PestaniasFragment extends Fragment {

	public static TabLayout tabLayout;
	public static ViewPager viewPager;

	private final PrimeraSeccion seccion1 = new PrimeraSeccion();
	private final SegundaSeccion seccion2 = new SegundaSeccion();
	private final TerceraSeccion seccion3 = new TerceraSeccion();
	private OpcionesPlantel opcionesPlantel = new OpcionesPlantel();
	private OpcionesAccesos opcionesAccesos = new OpcionesAccesos();

	private Spinner listaPlanteles;
	private Spinner listaAccesos;
	private AdaptadorPestanias adaptadorPestanias;
	private FragmentManager manejador;

	private SharedPreferences preferencias;
	private Context context;

	private boolean seccionesCreadas = false;
	private static Integer contador = 0;
	public static final int NUMERO_SECCIONES = 3;

	private static final int PESTANIA_SAES        = 0;
	private static final int PESTANIA_REFERENCIAS = 1;
	private static final int PESTANIA_LUGARES     = 2;

	private String plantelSeleccionado = null;
	private String plantelActivo = null;

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

		seccion1.setPestanias( this );
		seccion2.setPestanias( this );
		seccion3.setPestanias( this );

		return x;
	}

	public void setContext ( Context context ){
		this.context = context;

		cargaPreferencias();
		seccion1.setContext( context );
		seccion2.setContext( context );
		seccion3.setContext( context );
	}

	private void cargaPreferencias () {
		preferencias = PreferenceManager.getDefaultSharedPreferences( this.context );
	}

	public void aplicaPreferencias () {
		aplicaPreferenciaPlantel();
	}

	private void aplicaPreferenciaPlantel () {
		String plantel = getPreferenciaPlantel();
		if ( plantel.length() > 0 ) {
			seleccionaPreferenciaPlantel( plantel );
		}
		fijaPlantelSeleccionado();
	}

	private String getPreferenciaPlantel (){
		return preferencias.getString( "plantel", "" );
	}

	private void seleccionaPreferenciaPlantel ( String plantel ){
		listaPlanteles.setSelection( opcionesPlantel.getIndiceOpcionPlantel( plantel ) );
	}

	public void configuraBarraHerramientas ( Spinner listaHerramientas ){
		listaPlanteles = listaHerramientas;
		seccion1.setControlPlanteles( listaPlanteles );
		seccion2.setControlPlanteles( listaPlanteles );
		seccion3.setControlPlanteles( listaPlanteles );
		//Spinner listaHerramientas = (Spinner) vistaPrincipal.findViewById( R.id.lista_herramientas );
		listaHerramientas.setAdapter( getAdaptadorDatos() );
		listaHerramientas.setOnItemSelectedListener( getEventoSeleccionPlantel() );
	}

	public void configuraBarraAccesos ( Spinner controlAccesos ){
		listaAccesos = controlAccesos;
		seccion1.setControlAccesos( listaAccesos );
		seccion3.setControlAccesos( listaAccesos );
		//Spinner controlAccesos = (Spinner) vistaPrincipal.findViewById( R.id.lista_herramientas );
		controlAccesos.setAdapter( getAdaptadorAccesos() );
		controlAccesos.setOnItemSelectedListener( getEventoSeleccionAcceso() );
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

	private ArrayAdapter<String> getAdaptadorAccesos (){
		List<String> listaAccesos = opcionesAccesos.getListaOpcionesAccesos();
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>( context, R.layout.lista_presentacion, listaAccesos );
		adaptador.setDropDownViewResource( R.layout.lista_seleccion );

		return adaptador;
	}

	private AdapterView.OnItemSelectedListener getEventoSeleccionPlantel (){
		return new AdapterView.OnItemSelectedListener (){
			@Override
			public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ){

				if ( plantelActivo != getPlantelSeleccionado() ){
					//verificaContexto();
					// ( ( MainActivity ) getActivity() ).resetearListadoCargado( "Redireccion" );
					plantelActivo = getPlantelSeleccionado();

					plantelSeleccionado = plantelActivo;
					guardaPreferenciaPlantel();

					redirigePlantelPreferencia();
				}

			}

			@Override
			public void onNothingSelected( AdapterView<?> adapterView ){
			}
		};
	}

	private String getPlantelPreferencia (){
		String plantel = preferencias.getString( "plantel", "" );
		if ( plantel.length() == 0 ) {
			plantel = getPlantelInicial();
		}

		return plantel;
	}

	private void guardaPreferenciaPlantel (){
		SharedPreferences.Editor editorPreferencias = getEditorPreferencias();
		editorPreferencias.putString( "plantel", plantelSeleccionado );
		editorPreferencias.commit();
	}

	public String getPlantelSeleccionado (){
		return listaPlanteles.getSelectedItem().toString();
	}

	private String getPlantelInicial (){
		return getPlantelSeleccionado();
	}

	private void redirigePlantelPreferencia (){
		seccion1.redirigePlantel();
		seccion2.redirigePlantelReferencias();
		seccion3.redirigePlantel();
	}

	private AdapterView.OnItemSelectedListener getEventoSeleccionAcceso (){
		return new AdapterView.OnItemSelectedListener (){
			@Override
			public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ){
				redirigeSaesAcceso();
			}

			@Override
			public void onNothingSelected( AdapterView<?> adapterView ){
			}
		};
	}

	private void redirigeSaesAcceso (){
		seccion1.redirigeAcceso( listaAccesos );
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
					case PESTANIA_SAES :
						muestraPlanteles();
						muestraAccesos();
						controlaPrimeraSeccion();
						break;
					case PESTANIA_REFERENCIAS :
						muestraPlanteles();
						ocultaAccesos();
						// seccion2.revisaConexionInternet();
						break;
					case PESTANIA_LUGARES :
						muestraPlanteles();
						ocultaAccesos();
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

	public void ocultaAccesos (){
		listaAccesos.setVisibility( View.GONE );
	}

	private void muestraPlanteles (){
		listaPlanteles.setVisibility( View.VISIBLE );
	}

	public void muestraAccesos (){
		listaAccesos.setVisibility( View.VISIBLE );
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

	private void controlaTerceraSeccion (){
		seccion3.controlaPaginaLugares();
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
				case PESTANIA_SAES        : return seccion1;
				case PESTANIA_REFERENCIAS : return seccion2;
				case PESTANIA_LUGARES     : return seccion3;
				default : return null;
			}

		}

		@Override
		public int getCount (){
			return NUMERO_SECCIONES;
		}

		@Override
		public CharSequence getPageTitle ( int position ){
			switch ( position ){
				case PESTANIA_SAES        : return "SAES";
				case PESTANIA_REFERENCIAS : return "Referencias";
				case PESTANIA_LUGARES     : return "Lugares";
			}

			return null;
		}

	}

	public ViewPager getViewPager (){
		return viewPager;
	}

	public AdaptadorPestanias getAdaptadorPestanias (){
		return adaptadorPestanias;
	}

	public void cambiaPestania ( int posicion ){
		viewPager.setCurrentItem( posicion, false );
	}

	public PrimeraSeccion getPrimeraSeccion (){
		return seccion1;
	}

	public SegundaSeccion getSegundaSeccion (){
		return seccion2;
	}

	public TerceraSeccion getTerceraSeccion (){ 
		return seccion3; 
	}

	public OpcionesPlantel getOpcionesPlantel (){ 
		return opcionesPlantel;
	}

	public OpcionesAccesos getOpcionesAccesos (){ 
		return opcionesAccesos;
	}

	/*
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}
	*/

	public void fijaPlantelSeleccionado (){
		plantelActivo = getPlantelSeleccionado();
	}

	/*
	public Spinner getControlAccesos (){
		return listaAccesos;
	}
	*/
}
