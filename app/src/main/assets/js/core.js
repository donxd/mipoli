NodeList.prototype.forEach = Array.prototype.forEach;

function cambiaTitulo (){
	getElementos( 'h2' ).forEach( function ( elemento ){
		elemento.innerHTML = 'aksjdlfkasdf';
	});

	console.log( 'lasjfdkalsjfd' );
}

function ajustarDisenio (){
	ajustaEstructuraPagina();
	ajustaMenu();
	ajustaEnlaces();
	ajustaElementos();
	ajustaColorFondo();
}

function ajustaEstructuraPagina (){
	ajustaAnchoContenedorPrincipal();
	mueveSeccionesPagina();
	agregaEstilosSecciones();
}

function ajustaAnchoContenedorPrincipal (){
	getElementos( '#wrapper' ).forEach( function ( elemento ){
		elemento.style.width   = '';
		elemento.style.display = 'table';
	});
}

function getElemento ( selector ){
	return document.querySelector( selector );
}

function mueveSeccionesPagina (){
	mueveElemento( SELECTOR_COLUMNA_IZQUIERDA, SELECTOR_CONTENIDO_PRINCIPAL, ANTES_DE );
	mueveElemento( '#footer', SELECTOR_CONTENEDOR_CENTRAL, DENTRO_DE );
	mueveElemento( SELECTOR_CONTENEDOR_NAVEGACION, SELECTOR_CONTENEDOR, ANTES_DE );
}

function mueveElemento ( selectorElemento, selectorDestino, posicion ){
	var elemento = copiarElemento( selectorElemento );
	eliminaElemento( selectorElemento );
	pegarElemento( elemento, selectorDestino, posicion );
}

function copiarElemento ( selectorElemento ){
	var elemento = getElemento( selectorElemento );
	if ( elemento != null ){
		return elemento.cloneNode( true );
	}

	return null;
}

function eliminaElemento ( selectorElemento ){
	var elemento = getElemento( selectorElemento );
	if ( elemento != null ){
		elemento.parentNode.removeChild( elemento );
	}
}

function pegarElemento ( elemento, selectorDestino, posicion ){
	switch ( posicion ){
		case DENTRO_DE:
			pegaElementoDentro( elemento, selectorDestino );
			break;
		case ANTES_DE:
			pegaElementoAntes( elemento, selectorDestino );
			break;
		case DESPUES_DE:
			pegaElementoDespues( elemento, selectorDestino );
			break;
	}
}

function pegaElementoDentro ( elemento, selectorDestino ){
	var padre = getElemento( selectorDestino );
	if ( padre != null ){
		padre.appendChild( elemento );
	}
}

function pegaElementoAntes ( elemento, selectorDestino ){
	var elementoHermano = getElemento( selectorDestino );
	if ( elementoHermano != null ){
		elementoHermano.parentNode.insertBefore( elemento, elementoHermano );
	}
}

function pegaElementoDespues ( elemento, selectorDestino ){
	var elementoHermano = getElemento( selectorDestino );
	if ( elementoHermano != null ){

		if ( elementoHermano.nextSibling ){
			elementoHermano.parentNode.insertBefore( elemento, elementoHermano.nextSibling );
		} else {
			elementoHermano.parentNode.appendChild( elemento );
		}

	}
}

function agregaEstilosSecciones (){

	getElementos( SELECTOR_COLUMNA_IZQUIERDA ).forEach( function ( elemento ){
		elemento.style.display = 'table-cell';
		elemento.style.verticalAlign = 'top';
	});

	getElementos( SELECTOR_CONTENIDO_PRINCIPAL ).forEach( function ( elemento ){
		elemento.setAttribute( 'style', getEstiloContenidoPrincipal() );
	});

	getElementos( SELECTOR_CONTENEDOR_CONTENIDO_PRINCIPAL ).forEach( function ( elemento ){
		elemento.style.marginLeft = '0px';
	});

	getElementos( SELECTOR_ACCESOS_RAPIDOS ).forEach( function ( elemento ){
		elemento.style.verticalAlign = 'top';
	});
	
	getElementos( SELECTOR_CONTENEDOR ).forEach( function ( elemento ){
		elemento.removeAttribute('style');
	});

	getElementos( SELECTOR_ELEMENTOS_MENU_SECCIONES ).forEach( function ( elemento ){
		elemento.style.width = '';
	});

	getElementos( SELECTOR_CONTENEDOR_MENU_SECCIONES ).forEach( function ( elemento ){
		elemento.setAttribute( 'style', ' width : initial; ');
	});

	getElementos( SELECTOR_CONTENEDOR_CENTRAL ).forEach( function ( elemento ){
		elemento.setAttribute('style', getEstiloColumnaCentral() );
	});

	getElementos( SELECTOR_CONTENEDOR_NAVEGACION ).forEach( function ( elemento ){
		elemento.setAttribute( 'style', getEstiloContenedorCentral() );
	});

	getElementos( SELECTOR_OPCIONES_SECCIONES ).forEach( function ( elemento ){
		elemento.style.width = '100%';
	});

	getElementos( SELECTOR_CONTENEDOR_DESCENTRALIZADO ).forEach( function ( elemento ){
		if ( elemento.children[ 0 ].nodeName == 'TABLE' ){
			elemento.children[ 0 ].setAttribute( 'style', 'background-color: #FFF; width: 100%;' );
		}
	});

	setTimeout( ajustaEspacioNavegacion, 500 );
}

function getEstiloContenidoPrincipal (){
	return ' display : table-cell; float : none; border-left: 1px solid #FFF; border-right: 1px solid #FFF; min-width: 648px;';
}

function getEstiloColumnaCentral (){
	return 'float : initial; width : initial; margin-left : initial; padding-top : 0px; ';
}

function getEstiloContenedorCentral (){
	return ' position : initial; top : initial; left : initial; font-size : 0.9em; padding : 10px 20px;';
}

function ajustaEspacioNavegacion (){
	var menuSecciones = getElemento( SELECTOR_COLUMNA_IZQUIERDA );
	var navegacion = getElemento( SELECTOR_NAVEGACION );
	if ( navegacion != null && menuSecciones != null ){
		navegacion.setAttribute( 'style', getEstiloNavegacion( menuSecciones ) );
	}
}

function getEstiloNavegacion ( menuSecciones ){
	return 'padding: 0 '
			+ menuSecciones.clientWidth 
			+'px; border-bottom: 1px solid #FFF; border-top: 1px solid #990000; background-color: #F2F2F2;';
}

function ajustaMenu (){
	getElementos( SELECTOR_SUBMENU ).forEach( function ( subMenu ){

		subMenu.style.width = 'auto';

		getElementos( '.item.ctl00_subMenu_4' ).forEach( function ( elemento ){
			if ( elemento.children.length == 2 ){
				elemento.children[ 1 ].style.paddingLeft = '20px';
			}
		});
		
		getElementos( '#ctl00_subMenu br' ).forEach( function ( elemento ){
			elemento.parentNode.removeChild( elemento );
		});

		getElementos( '#ctl00_subMenu img' ).forEach( function ( elemento ){
			elemento.parentNode.removeChild( elemento );
		});

		getElemento( SELECTOR_SUBNAVEGACION ).insertBefore(
			  getElementoEstilosMenu()
			, getElemento( SELECTOR_SUBMENU ).nextSibling
		);

	});
}

function ajustaEnlaces (){
	getElementos( 'a:not([href=""])' ).forEach( function ( elemento ){
		procesaEnlace( elemento );
	});
}

function procesaEnlace ( enlace ){
	switch ( enlace.getAttribute( 'href' ) ){
		case PAGINA_ETS_PRINCIPAL:
			enlace.setAttribute( 'href', '#' );
			break;
		case PAGINA_REGLAMENTO:
			enlace.setAttribute( 'href', ENLACE_REGLAMENTO  );
			enlace.setAttribute( 'target', '_blank' );
			break;
		case PAGINA_SPA_PRINCIPAL:
			enlace.text = 'SPA';
			break;
		case PAGINA_SPA_INSCRIPCION:
			enlace.text = 'Inscribir SPA';
			break;
		case PAGINA_PROFESORES_PRINCIPAL:
			enlace.text = 'Profesores';
			break;
		case PAGINA_ALUMNOS_GENERAL:
			enlace.text = 'General';
			break;
		case PAGINA_ALUMNOS_MEDICOS:
			enlace.text = 'Médicos';
			break;
		case PAGINA_ALUMNOS_DEPORTIVOS:
			enlace.text = 'Deportivos';
			break;
	}
}

function ajustaElementos (){
	getElemento( SELECTOR_CONTENEDOR_CONTENIDO_PRINCIPAL ).style.display = 'table';
	getElementos( SELECTOR_ACCESOS_RAPIDOS ).forEach( function ( elemento ){
		elemento.style.display = 'table-cell';
		elemento.style.float   = 'none';
	});
}

function ajustaColorFondo (){
	var elemento = getElemento( 'body' );
	elemento.setAttribute(
		  'style'
		, 'text-align: center; background-image : none !important; background-repeat: initial !important; background-color: #802434 !important; font-family: Verdana, Geneva, Arial, Helvetica, sans-serif; font-size: 0.78em; '
	);
}

function getElementos ( selector ){
	return document.querySelectorAll( selector );
}

function getElementoEstilosMenu (){
	var disenio = document.createElement( 'style' );

	disenio.setAttribute( 'type','text/css' );
	disenio.innerHTML =  '#subnav .item { padding : 0px 7px;	border : none; display : block; }';
	disenio.innerHTML += '#subnav .item a {	width : 100%; display : block; }';
	disenio.innerHTML += '#subnav .selected:hover { background-color: #FF9900; color: #990000; }';
	disenio.innerHTML += '#subnav .hover { background-color: initial; color: #990000; }';
	disenio.innerHTML += '#subnav { margin-bottom: 0px; } ';
	disenio.innerHTML += '.sidebarcontainer { margin : 0px auto } ';

	return disenio;
}

function detectaPantalla (){

	switch (location.pathname){
		case '/':
		case '/default.aspx':
		case '/Default.aspx':
			detectaIdentificacion();
			break;
	}
	/*
	case '/alumnos/default.aspx':
			pantalla_alumnos_inicio();
			break;
		case '/Academica/Equivalencias.aspx':
			pantalla_equivalencias();
			break;
		case '/Alumnos/Evaluacion_docente/califica_profe.aspx':
		case '/Alumnos/Evaluacion_Docente/Califica_Profe.aspx':
			pantalla_califica_profesor();
			break;
		case '/Alumnos/Evaluacion_docente/evaluacion_profesor.aspx':
		case '/Alumnos/Evaluacion_Docente/evaluacion_profesor.aspx':
			pantalla_evalua_profesor();
			break;
		case '/Academica/mapa_curricular.aspx':
			// informacionPlanes();
			break;
		case '/Academica/Ocupabilidad_grupos.aspx':
			pantalla_ocupabilidad();
			break;
		case '/Academica/horarios.aspx':
			pantalla_horarios();
			break;
		case '/Academica/Calendario.aspx':
			pantalla_calendario();
			break;
		case '/Alumnos/Reinscripciones/Comprobante_Horario.aspx':
			horarioDirecto();
			break;
		case '/Alumnos/Informacion_semestral/Horario_Alumno.aspx':
			pantalla_horario_alumno();
			break;
		case '/Alumnos/Reinscripciones/reinscribir.aspx':
			pantalla_reinscribir();
			break;
		case '/Alumnos/boleta/kardex.aspx':
			pantalla_kardex();
			break;
		case '/Alumnos/tutores/Evaluacion_Tutores.aspx':
			pantalla_evaluacion_tutores();
			break;
		case '/Alumnos/Reinscripciones/fichas_reinscripcion.aspx':
			pantalla_ficha_reinscripcion();
			break;
		case '/Alumnos/info_alumnos/DatosAlumnosMedicos.aspx':
			pantalla_datos_medicos();
			break;
		case '/Alumnos/info_alumnos/DatosAlumnosDeportivos.aspx':
			pantalla_datos_deportivos();
			break;
		case '/Alumnos/Informacion_semestral/calificaciones_sem.aspx':
			pantalla_calificaciones();
			break;
		case '/Alumnos/Reinscripciones/Reporte_Horario.aspx':
			pantalla_reporte_horario();
			break;
		case '/Alumnos/Saberes/Inscripcion_Saberes.aspx':
			pantalla_incripcion_spa();
			break;
		case '/Alumnos/Saberes/calificaciones_saberes.aspx':
			pantalla_calificaciones_spa();
			break;
		case '/Alumnos/tutores/comentarios.aspx':
			pantalla_tutores_comentarios();
			break;
		case '/Academica/agenda_escolar.aspx':
			pantalla_agenda_escolar();
			break;
		case '/Academica/Calendario_ets.aspx':
			pantalla_calendario_ets();
			break;
	*/

}

function detectaIdentificacion (){
	var usuario = getElemento( SELECTOR_USUARIO_IDENTIFICACION );
	var pass    = getElemento( SELECTOR_PASS_IDENTIFICACION );

	if ( usuario != null && pass != null ){
		if ( almacenamientoDatos() ){
			muestraMensajeAlmacenamiento();
			agregaComportamientoControlesIdentificacion( usuario, pass );
			recuperaDatosIdentificacion();
		} else {
			muestraMensajeNoAlmacenamiento();
		}
	}
}

function almacenamientoDatos (){
	return window.localStorage != undefined && window.localStorage != null;
}

function muestraMensajeAlmacenamiento (){
	muestraMensaje( '<br/> Se puede Almacenar' );
}

function muestraMensajeNoAlmacenamiento (){
	muestraMensaje( '<br/> No se puede almacenar' );
}

function muestraMensaje ( mensaje ){
	getElementos( 'h2' ).forEach( function ( elemento ){
		elemento.innerHTML += mensaje;
	});
}

function agregaComportamientoControlesIdentificacion ( usuario, pass ){
	usuario.addEventListener( 'blur', guardaUsuarioIdentificacion, true );
	pass.addEventListener( 'blur', guardaPasswordIdentificacion, true );
}

function guardaUsuarioIdentificacion (){
	guardaInformacionIdentificacion( this.value, IDENTIFICACION_USUARIO );
}

function guardaInformacionIdentificacion ( valor, identificadorAlmacenamiento ){
	if ( valor != null && valor.length > 0 ){
		localStorage.setItem( identificadorAlmacenamiento, valor );
	}
}

function guardaPasswordIdentificacion (){
	guardaInformacionIdentificacion( this.value, IDENTIFICACION_PASSWORD );
}

function recuperaDatosIdentificacion (){
	recuperaUsuarioIdentificacion();
	recuperaPassIdentificacion();
}

function recuperaUsuarioIdentificacion (){
	recuperaDatoIdentificacion( IDENTIFICACION_USUARIO, SELECTOR_USUARIO_IDENTIFICACION );
}

function recuperaDatoIdentificacion ( identificadorAlmacenamiento, selectorControl ){
	var dato  = recuperaDatoAlmacenado( identificadorAlmacenamiento );
	colocaDatoIdentificacion( dato, selectorControl );
}

function colocaDatoIdentificacion ( dato, selectorControl ){
	if ( dato.length > 0 ){
		var controlDato = getElemento( selectorControl );

		if ( controlDato != null ){
			controlDato.value = dato;
		}
	}
}

function recuperaPassIdentificacion (){
	recuperaDatoIdentificacion( IDENTIFICACION_PASSWORD, SELECTOR_PASS_IDENTIFICACION );
}

function recuperaDatoAlmacenado ( identificadorAlmacenamiento ){
	var dato = localStorage.getItem( identificadorAlmacenamiento );
	if ( dato == null ){
		dato = '';
	}

	return dato;
}

var IDENTIFICACION_USUARIO  = 'usuario';
var IDENTIFICACION_PASSWORD = 'password';

var SELECTOR_USUARIO_IDENTIFICACION = '#ctl00_leftColumn_LoginUser_UserName';
var SELECTOR_PASS_IDENTIFICACION    = '#ctl00_leftColumn_LoginUser_Password';

var SELECTOR_SUBMENU       = '#ctl00_subMenu';
var SELECTOR_CONTENEDOR    = '.container';
var SELECTOR_NAVEGACION    = '#mainnav';
var SELECTOR_SUBNAVEGACION = '#subnav';

var SELECTOR_COLUMNA_IZQUIERDA = '#leftcolumn';
var SELECTOR_ACCESOS_RAPIDOS   = '#rightcolumn';

var SELECTOR_CONTENIDO_PRINCIPAL            = '#floatwrapper';
var SELECTOR_CONTENEDOR_CONTENIDO_PRINCIPAL = '#contentwrapper';

var SELECTOR_ELEMENTOS_MENU_SECCIONES  = '#leftcolumn .item';
var SELECTOR_CONTENEDOR_MENU_SECCIONES = '#leftcolcontainer';
var SELECTOR_CONTENEDOR_CENTRAL        = '#centercolumn';
var SELECTOR_CONTENEDOR_NAVEGACION     = '#breadcrumbs';

var SELECTOR_OPCIONES_SECCIONES = '#subnav > table > tbody > tr > td > table';
var SELECTOR_CONTENEDOR_DESCENTRALIZADO = '#copy';

var DENTRO_DE  = 0;
var ANTES_DE   = 1;
var DESPUES_DE = 2;

var ENLACE_REGLAMENTO = 'http://www.contenido.ccs.ipn.mx/G-866-2011-E.pdf';

var PAGINA_ETS_PRINCIPAL        = '/Alumnos/ETS/default.aspx';
var PAGINA_REGLAMENTO           = '/Reglamento/Default.aspx';
var PAGINA_SPA_PRINCIPAL        = '/Alumnos/Saberes/DEFAULT.ASPX';
var PAGINA_SPA_INSCRIPCION      = '/Alumnos/Saberes/Inscripcion_Saberes.aspx';
var PAGINA_PROFESORES_PRINCIPAL = '/Alumnos/Evaluacion_Docente/Default.aspx';
var PAGINA_ALUMNOS_GENERAL      = '/Alumnos/info_alumnos/Datos_Alumno.aspx';
var PAGINA_ALUMNOS_MEDICOS      = '/Alumnos/info_alumnos/DatosAlumnosMedicos.aspx';
var PAGINA_ALUMNOS_DEPORTIVOS   = '/Alumnos/info_alumnos/DatosAlumnosDeportivos.aspx';

androidJs.guardaContenido( 'perro!!' );

cambiaTitulo();
ajustarDisenio();
detectaPantalla();