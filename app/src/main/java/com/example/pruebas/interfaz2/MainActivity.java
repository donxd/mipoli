package com.example.pruebas.interfaz2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
		// implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout drawer;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	//private DrawerLayout contenidoPublicidad;
	//private RelativeLayout contenidoPublicidad;

	private Spinner controlPlanteles;
	private Spinner controlAccesos;
	private SharedPreferences preferencias;
	private ImageButton controlRecargar;

	private PestaniasFragment pestaniasFragment = new PestaniasFragment();
	private AnoterFragment anoterFragment = new AnoterFragment();
	private static final int ANCHO_LISTA_SECCIONES = 80;
	private static final int ANCHO_LISTA_PLANTELES = 90;

	private boolean sesionCompartida = false;
	private Context contexto;
	private AdView adView;
	private InterstitialAd mInterstitialAd;
	private boolean anuncioCargado = false;

	private static final int PESTANIA_SAES        = 0;
	private static final int PESTANIA_REFERENCIAS = 1;
	private static final int PESTANIA_LUGARES     = 2;

	private List<Integer> listaPaginaCargando = new ArrayList<Integer>();
	private List<Integer> listaPaginaCargada = new ArrayList<Integer>();
	private static final long TIEMPO_DEMORA_CARGADO = 6000;
	private boolean mostrandoAlerta = false;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		this.contexto = this;

		ajustaBarraHerramientas();

		//contenidoPublicidad = (RelativeLayout) findViewById( R.id.contenedor_publicidad );
		//contenidoPublicidad = (DrawerLayout) findViewById( R.id.contenedor_publicidad );
		drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
		NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );

		controlPlanteles = (Spinner) findViewById( R.id.lista_herramientas );
		controlAccesos   = (Spinner) findViewById( R.id.lista_accesos );

		// inicializaControlPlanteles();
		adView = newAdView();
		// cargaAdview();

		mInterstitialAd = newInterstitialAd();
		//cargaInterstitialAd();

		fragmentManager = getSupportFragmentManager();
		configuraContenedorPestanias();

		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace( R.id.contenido_layout, pestaniasFragment );
		fragmentTransaction.commit();

		navigationView.setNavigationItemSelectedListener( getConfiguracionNavegacion() );

		/*
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});
		*/

		ajustaTextoListaSecciones();
		ajustaTextoListaPlanteles();

		agregaComportamientoBotonRecargar();

		ActionBarDrawerToggle toggle = getPanelLateral( drawer );
		drawer.setDrawerListener( toggle );
		toggle.syncState();

	}

	private void configuraContenedorPestanias (){
		pestaniasFragment.setContext( this );
		pestaniasFragment.configuraBarraHerramientas( controlPlanteles );
		pestaniasFragment.configuraBarraAccesos( controlAccesos );
		pestaniasFragment.aplicaPreferencias();
		pestaniasFragment.setManejador( fragmentManager );
	}

	private ActionBarDrawerToggle getPanelLateral ( DrawerLayout drawer ){
		Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
		//setSupportActionBar(toolbar);

		return new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
	}

	private NavigationView.OnNavigationItemSelectedListener getConfiguracionNavegacion(){
		return new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {

				drawer.closeDrawers();

				int id = item.getItemId();

				// if (id == R.id.nav_camera) {

				// } else if (id == R.id.nav_gallery) {

				// } else if (id == R.id.nav_slideshow) {

				// } else if (id == R.id.nav_manage) {

				// } else if (id == R.id.nav_share) {

				// } else if (id == R.id.nav_send) {

				// }

				switch ( id ){
					case R.id.nav_camera:
						fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace( R.id.contenido_layout, pestaniasFragment );
						fragmentTransaction.commit();
						break;
					case R.id.nav_gallery:
						// FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.replace( R.id.contenido_layout, anoterFragment );
						fragmentTransaction.commit();
						break;
					case R.id.nav_slideshow:
						break;
					case R.id.nav_manage:
						break;
					case R.id.nav_share:
						break;
					case R.id.nav_send:
						break;
				}

				return true;
			}
		};
	}

	private void cargaAdview (){
		if ( adView == null ){
			adView = newAdView();
		}

		if ( !anuncioCargado ){
			AdRequest adRequest = new AdRequest.Builder().setRequestAgent( "android_studio:ad_template" ).build();
			adView.loadAd(adRequest);
		}

		muestraAdView();
	}

	private AdView newAdView (){
		AdView adView = new AdView( contexto );

		int medida = (int) getResources().getDimension( R.dimen.tamanio_ads );
		if ( medida != 1 ){
			adView.setAdSize( AdSize.MEDIUM_RECTANGLE );
		} else {
			adView.setAdSize( AdSize.LARGE_BANNER );
		}

		adView.setAdUnitId( getResources().getString( R.string.banner_ad_unit_id ) );
		adView.setAdListener( getControladorAd() );

		//contenidoPublicidad.addView( adView );
		getWindow().addContentView( adView, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
		//drawer.addc
		//contenidoPublicidad.addView( adView );

		return adView;
	}

	private InterstitialAd newInterstitialAd() {
		InterstitialAd interstitialAd = new InterstitialAd( contexto );
		interstitialAd.setAdUnitId( getString( R.string.interstitial_ad_unit_id ) );
		interstitialAd.setAdListener( getControladorInterstitialAd() );

		return interstitialAd;
	}


	private AdListener getControladorInterstitialAd (){
		return new AdListener() {
			@Override
			public void onAdLoaded() {
				mInterstitialAd.show();
			}
		};
	}

	private void cargaInterstitialAd (){
		if ( mInterstitialAd == null ){
			nuevoInterstitialAd();
		}

		if ( !mInterstitialAd.isLoaded() ){
			AdRequest adRequest = new AdRequest.Builder().setRequestAgent( "android_studio:ad_template" ).build();
			mInterstitialAd.loadAd( adRequest );
		}

		//mInterstitialAd.show();
	}

	private AdListener getControladorAd (){
		return new AdListener(){
			@Override
			public void onAdOpened (){
				if ( adView != null ){
					adView.setVisibility( View.GONE );
				}
				// Toast.makeText( contexto, "onAdOpened !!!",Toast.LENGTH_LONG ).show();
				// Log.i( "anunciossss", "onAdOpened !!!" );
			}

			// @Override
			// public void onAdClosed (){
			// 	// Toast.makeText( contexto, "onAdClosed !!!",Toast.LENGTH_LONG ).show();
			// 	// Log.i( "anunciossss", "onAdClosed !!!" );
			// }

			// @Override
			// public void onAdLeftApplication (){
			// 	// Toast.makeText( contexto, "onAdLeftApplication !!!",Toast.LENGTH_LONG ).show();
			// 	// Log.i( "anunciossss", "onAdLeftApplication !!!" );
			// }

			@Override
			public void onAdFailedToLoad ( int errorCode ){
				anuncioCargado = false;
			}

			@Override
			public void onAdLoaded (){
				anuncioCargado = true;
			}

		};
	}

	/*private AdSize getMedidaAd (){
		int medida = (int) getResources().getDimension( R.dimen.tamanio_ads );

		if ( medida == 2 ){
			return AdSize.FULL_BANNER;
		}

		return AdSize.LARGE_BANNER;
	}*/

	private void agregaComportamientoBotonRecargar (){
		setControlRecargar();
		controlRecargar.setOnClickListener( new View.OnClickListener(){
			public void onClick( View vista ){
				WebView navegador = getControlPagina();

				if ( navegador != null ){
					recargaNavegador( navegador );
					mostrarMensajeRecargando();
				}
			}
		});
	}

	private void recargaNavegador ( WebView navegador ){
		if ( pestaniasFragment.getViewPager().getCurrentItem() != PESTANIA_REFERENCIAS && getPaginaCargada() ){
			inyectaScript( navegador, getScriptRecargar() );
		} else {
			navegador.reload();
			listaPaginaCargando.clear();
		}
	}

	private void inyectaScript ( WebView navegador, String script ){
		if ( script != null && !script.isEmpty() ){
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
				navegador.evaluateJavascript( script, null );
			} else {
				navegador.loadUrl( "javascript:" + script );
			}
		}
	}

	private String getScriptRecargar (){
		return "recargarPantalla()";
	}

	private void mostrarMensajeRecargando (){
		Toast.makeText( contexto, "Recargando...", Toast.LENGTH_LONG ).show();
	}

	private void setControlRecargar (){
		controlRecargar = (ImageButton) findViewById( R.id.controlRecargar );
	}

	private void nuevoInterstitialAd (){
		mInterstitialAd = newInterstitialAd();
	}

	// private void inicializaControlPlanteles (){
	// 	controlPlanteles.setOnItemSelectedListener();
	// }

	/*
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {

			// FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace( R.id.contenido_layout, anoterFragment );
			fragmentTransaction.commit();

		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}*/

	public void ocultaAccesos (){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//controlAccesos.setVisibility( View.GONE );
				controlAccesos.setEnabled( false );
			}
		});
	}

	public void muestraAccesos (){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				//controlAccesos.setVisibility( View.VISIBLE );
				controlAccesos.setEnabled( true );
			}
		});
	}

	private void ajustaBarraHerramientas (){
		if ( android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 ){

			int tamanioBarraHerramientas = getTamanioBarraHerramientas();

			CoordinatorLayout contenedorBarraHerramientas = (CoordinatorLayout) findViewById( R.id.contenedorBarraHerramientas );
			//contenedorBarraHerramientas.setMinimumHeight( tamanioBarraHerramientas );
			ViewGroup.LayoutParams parametros = contenedorBarraHerramientas.getLayoutParams();
			parametros.height = tamanioBarraHerramientas;

			contenedorBarraHerramientas.setLayoutParams( parametros );

			Log.i( "Info",  "Actualización tamanio barra de herramientas : " + tamanioBarraHerramientas );
		}

	}

	private int getTamanioBarraHerramientas (){

		int tamanioBarraHerramientas = getDimenBarraHerramientas();

		/*switch ( tamanioBarraHerramientas ){
			case
		}*/

		return tamanioBarraHerramientas;

	}

	private int getDimenBarraHerramientas (){
		return (int) getResources().getDimension(R.dimen.barra_herramientas);
	}

	public void muestraReferencias (){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pestaniasFragment.cambiaPestania( 1 );
			}
		});
	}

	public void cargaEnlaceReferencia ( final WebView webReferencias, final String pagina ){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				webReferencias.loadUrl( pagina );
			}
		});
	}

	@Override
	public void onConfigurationChanged ( Configuration newConfig ){
		super.onConfigurationChanged( newConfig );

		ajustaTextoListaSecciones();
		ajustaTextoListaPlanteles();

	// 	String ajusteJs = null;
	// 	if ( newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ){
	// 		Log.i( "Info", "estado horizontal" );
	// 		ajusteJs = "presentacionHorizontal()";
	// 	}

	// 	if ( newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ){
	// 		Log.i( "Info", "estado vertical" );
	// 		ajusteJs = "presentacionVertical()";
	// 	}

	// 	if ( ajusteJs != null ){
	// 		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){
	// 			pestaniasFragment.getPrimeraSeccion().getControlSaes().evaluateJavascript( ajusteJs, null );
	// 		} else {
	// 			pestaniasFragment.getPrimeraSeccion().getControlSaes().loadUrl( "javascript:" + ajusteJs );
	// 		}
	// 	}

	}

	public int getOrientacion (){
		return getResources().getConfiguration().orientation;
	}

	public boolean esOrientacionVertical (){
		return getOrientacion() == Configuration.ORIENTATION_PORTRAIT;
	}

	private void ajustaTextoListaSecciones (){
		if ( esOrientacionVertical() ){
			controlAccesos.getLayoutParams().width = ANCHO_LISTA_SECCIONES;
		} else {
			controlAccesos.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
	}

	private void ajustaTextoListaPlanteles (){
		if ( esOrientacionVertical() ){
			controlPlanteles.getLayoutParams().width = ANCHO_LISTA_PLANTELES;
		} else {
			controlPlanteles.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
	}

	@Override
	public void onBackPressed (){

		WebView navegador = getControlPagina();

		if ( navegador != null && navegador.canGoBack() ){
			navegador.goBack();
		}
	}

	public WebView getControlPagina (){

		if ( pestaniasFragment == null || pestaniasFragment.getViewPager() == null ) return null;

		switch ( pestaniasFragment.getViewPager().getCurrentItem() ){
			case PESTANIA_SAES        : return pestaniasFragment.getPrimeraSeccion().getControlSaes();
			case PESTANIA_REFERENCIAS : return pestaniasFragment.getSegundaSeccion().getControlReferencias();
			case PESTANIA_LUGARES     : return pestaniasFragment.getTerceraSeccion().getControlLugares();
			default: return null;
		}
	}

	public boolean getPaginaCargada (){
		if ( pestaniasFragment == null || pestaniasFragment.getViewPager() == null ) return false;

		switch ( pestaniasFragment.getViewPager().getCurrentItem() ){
			case PESTANIA_SAES    : return pestaniasFragment.getPrimeraSeccion().esPaginaCargada();
			case PESTANIA_LUGARES : return pestaniasFragment.getTerceraSeccion().esPaginaCargada();
			default: return false;
		}
	}

	public void comparteSesion (){
		if ( pestaniasFragment != null && !sesionCompartida ){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					String paginaOcupabilidad = pestaniasFragment.getPrimeraSeccion().getPaginaAcceso( OpcionesAccesos.OPCION_OCUPABILIDAD );
					WebView paginaLugares =  pestaniasFragment.getTerceraSeccion().getControlLugares();

					if ( paginaLugares != null && paginaOcupabilidad != null ){
						paginaLugares.loadUrl( paginaOcupabilidad );

						if ( pestaniasFragment.getViewPager().getCurrentItem() != 0 ){
							pestaniasFragment.getPrimeraSeccion().getControlSaes().reload();
						}

					} else {

						if ( paginaLugares == null ) Log.i( "Info", "Control - Pagina no existe" );
						if ( paginaOcupabilidad == null ) Log.i( "Info", "Control - Destino no existe" );

					}
				}
			});

			sesionCompartida = true;
			pestaniasFragment.getTerceraSeccion().setEstadoSesionCompartida( sesionCompartida );
		}
	}

	public void inactivaTransferenciaSesion (){
		sesionCompartida = false;
		pestaniasFragment.getTerceraSeccion().setEstadoSesionCompartida( sesionCompartida );
	}

	public void muestraPublicidad (){
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				cargaAdview();
				muestraAdView();
			}
		});
	}

	private void muestraAdView (){
		adView.setVisibility( View.VISIBLE );
	}

	public void muestraPublicidadInterstitial (){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cargaInterstitialAd();
			}
		});
	}

	public void setCargandoSaes (){
		resetearListadoCargado( "a" );
		listaPaginaCargando.add( PESTANIA_SAES );
		Log.i( "Info", "Cargando .... SAES " );
		creaTemporizadorPaginaCargando();
	}

	public void setCargandoLugares (){
		resetearListadoCargado( "b" );
		listaPaginaCargando.add( PESTANIA_LUGARES );
		Log.i( "Info", "Cargando .... LUGARES " );
		creaTemporizadorPaginaCargando();
	}

	private void creaTemporizadorPaginaCargando (){
		new android.os.Handler().postDelayed( getComportamientoCargadoLento(), TIEMPO_DEMORA_CARGADO );
		Log.i( "Info", "Temporizador inicializado\n" );
	}

	private Runnable getComportamientoCargadoLento (){
		return new Runnable (){

			public void run (){
				Log.i( "Info", getMensajeCargando() );
				if ( paginasEnEsperaCargando() ){
					Toast.makeText( contexto, "La página esta tardando demasiado en responder.", Toast.LENGTH_LONG ).show();
					creaTemporizadorPaginaCargando();
				}
			}
		};
	}

	private String getMensajeCargando (){
		StringBuilder mensaje = new StringBuilder();
		mensaje
			.append( "Cargando [ " )
			.append( listaPaginaCargando.size() )
			.append( " ]" );

		return mensaje.toString();
	}

	private boolean paginasEnEsperaCargando (){
		return !listaPaginaCargando.isEmpty();
	}

	public void setSaesCargado (){
		// listaPaginaCargada.add( PESTANIA_SAES );
		Log.i( "Info", "Saes - Cargado \n" );
		resetearListadoCargado( "c" );
	}

	public void setLugaresCargado (){
		// listaPaginaCargada.add( PESTANIA_LUGARES );
		Log.i( "Info", "Lugares - Cargado \n" );
		resetearListadoCargado( "d" );
	}

	public void resetearListadoCargado (){
		listaPaginaCargando.clear();
		Log.i( "Info", "Cargado = 0 \n" );
	}

	public void resetearListadoCargado ( String marca ){
		listaPaginaCargando.clear();
		Log.i( "Info", "Cargado = 0 : "+ marca +"\n" );
	}

	public boolean getMostrandoAlerta (){
		return mostrandoAlerta;
	}

	public boolean setMostrandoAlerta ( boolean estadoAlertas ){
		return mostrandoAlerta = estadoAlertas;
	}

}
