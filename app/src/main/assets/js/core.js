function cambiaTitulo (){
	document.querySelector( 'h2' ).innerHTML = 'aksjdlfkasdf';
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
	var usuario = document.querySelector( SELECTOR_USUARIO_IDENTIFICACION );
	var pass    = document.querySelector( SELECTOR_PASS_IDENTIFICACION );

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
	document.querySelector( 'h2' ).innerHTML += mensaje;
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
		var controlDato = document.querySelector( selectorControl );

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

//androidJs.guardaContenido( 'perro!!' );

cambiaTitulo();
detectaPantalla();