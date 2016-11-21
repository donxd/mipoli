package com.example.pruebas.interfaz2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
		// implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout drawer;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private Spinner controlPlanteles;
	private Spinner controlAccesos;
	private SharedPreferences preferencias;
	private OpcionesPlantel opcionesPlantel;

	private PestaniasFragment pestaniasFragment = new PestaniasFragment();
	private AnoterFragment anoterFragment = new AnoterFragment();

	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		ajustaBarraHerramientas();

		drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
		NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );

		controlPlanteles = (Spinner) findViewById( R.id.lista_herramientas );
		controlAccesos   = (Spinner) findViewById( R.id.lista_accesos );
		// inicializaControlPlanteles();

		fragmentManager = getSupportFragmentManager();
		pestaniasFragment.setContext( this );
		pestaniasFragment.configuraBarraHerramientas( controlPlanteles );
		pestaniasFragment.configuraBarraAccesos( controlAccesos );
		pestaniasFragment.aplicaPreferencias();
		pestaniasFragment.setManejador( fragmentManager );

		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace( R.id.contenido_layout, pestaniasFragment );
		fragmentTransaction.commit();

		navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
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

				return false;
			}
		});

		Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
		//setSupportActionBar(toolbar);

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

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

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
		if ( android.os.Build.VERSION.SDK_INT > 17 ){

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

}
