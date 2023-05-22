package ar.edu.unahur.obj2.impostoresPaises

// Acá encapsulamos el manejo de la consola real, desacoplandolo del programa en sí
object Consola {
  fun leerLinea() = readLine()
  fun escribirLinea(contenido: String) {
    println(contenido)
  }
}

object Programa {
  var entradaSalida = Consola
  var api = Observatorio.api
  fun mostrarMenuPrincipal(){
    entradaSalida.escribirLinea("Hola, Bienvenido al sistema de informacion de paises, ingresa alguna de las siguientes opciones!")
    entradaSalida.escribirLinea("1.Saber informacion sobre un pais")
    entradaSalida.escribirLinea("2.Saber informacion sobre 2 paises en conjunto.")
    entradaSalida.escribirLinea("3.Saber los codigo ISO3 de los paises con mayor densidad poblacional.")
    entradaSalida.escribirLinea("4.Saber el continente con mas paises plurinacionales(con mas de un lenguaje)")
    entradaSalida.escribirLinea("5.Conocer el promedio de densidad de los paises isla")
    entradaSalida.escribirLinea("6.Salir")
  }
  fun registrarUnPais():Country{
    entradaSalida.escribirLinea("Ingresa el nombre de un pais.")
    val pais= entradaSalida.leerLinea()
    checkNotNull(pais)
    var paisesEncontrados:List<Country>
    paisesEncontrados= api.buscarPaisesPorNombre(pais)
    while(paisesEncontrados.isEmpty()){
      entradaSalida.escribirLinea("Ese pais no existe,reingresar.")
      entradaSalida.escribirLinea("Ingresa el nombre de un pais.")
      val pais= entradaSalida.leerLinea()
      checkNotNull(pais)
       paisesEncontrados= api.buscarPaisesPorNombre(pais)
    }
    return paisesEncontrados.first()
  }
  fun mostrarSubMenuParaUnPais(){
    entradaSalida.escribirLinea("Queres saber mas info del pais? Elegi una de las siguientes opciones.")
    entradaSalida.escribirLinea("1.Saber si es plurinacional.")
    entradaSalida.escribirLinea("2.Saber si es una isla")
    entradaSalida.escribirLinea("3.Saber su densidad poblacional")
    entradaSalida.escribirLinea("4.Saber su vecino mas poblado")
    entradaSalida.escribirLinea("5.Salir")
  }
  fun mostrarSubMenuParaDosPaises(){
    entradaSalida.escribirLinea("Elegi una de las siguientes opciones!")
    entradaSalida.escribirLinea("1.Saber si los paises son limitrofes")
    entradaSalida.escribirLinea("2.Saber si necesitan traduccion para poder dialogar.")
    entradaSalida.escribirLinea("3.Saber si son potenciales aliados.")
    entradaSalida.escribirLinea("4.Salir.")
  }
  fun ejecutarAccionParaMenuPrincipal(){
    val respuesta = entradaSalida.leerLinea()
    val accion = when {
      respuesta == "1" -> {
        val pais= registrarUnPais()
        entradaSalida.escribirLinea(
          "${pais.name} (${pais.alpha3Code}) es un país de ${pais.region}, con una población de ${pais.population} habitantes."
        )
        mostrarSubMenuParaUnPais()
        ejecutarAccionPara1Pais(pais.name)
      }
      respuesta=="2"->{
        val pais1=registrarUnPais().name
        entradaSalida.escribirLinea("Ahora ingresá el nombre del segundo país:")
        val pais2= registrarUnPais().name
        mostrarSubMenuParaDosPaises()
        ejecutarAccionPara2Paises(pais1,pais2)
      }
      respuesta=="3"-> {mostrarIsoDeLos5PaisesMasDensos();mostrarMenuDeRegresoOSalida()}
      respuesta=="4"-> {mostrarContinenteConMasPlurinacionales();mostrarMenuDeRegresoOSalida()}
      respuesta=="5"-> {mostrarPromedioDeDensidadDePaisesIsla();mostrarMenuDeRegresoOSalida()}
      else-> mostrarMensajeDeSalida()
    }
  }
  fun mostrarMenuDeRegresoOSalida(){
    entradaSalida.escribirLinea("Desea seguir consultando?")
    entradaSalida.escribirLinea("1.Ir al menu principal.")
    entradaSalida.escribirLinea("2.Salir")
    val resp= entradaSalida.leerLinea()
    if(resp=="1"){
      mostrarMenuPrincipal()
      ejecutarAccionParaMenuPrincipal()
    }else{
      mostrarMensajeDeSalida()
    }
  }
  fun mostrarMensajeDeSalida()=entradaSalida.escribirLinea("Saliendo del sistema, hasta luego! :D")
  fun mostrarPromedioDeDensidadDePaisesIsla()=entradaSalida.escribirLinea("el promedio de densidad de paises isla es de:${Observatorio.promedioDeDensidadDePaisesIsla()}") // Esta consulta tarda MIL años, pero anda
  fun mostrarContinenteConMasPlurinacionales()=entradaSalida.escribirLinea("el continente con mas paises plurinacionales es:${Observatorio.continenteConMasPlurinacionales()} con ${Observatorio.cantidadDePlurinacionalesDeContinente(Observatorio.continenteConMasPlurinacionales())} paises plurinacionales")
  fun mostrarIsoDeLos5PaisesMasDensos()=entradaSalida.escribirLinea("los codigo ISO3 de los paises con mayor densidad poblacional son: ${Observatorio.codigoIso5PaisesMasDensos()}")
  fun mostrarSi2PaisesSonLimitrofes(pais1: String,pais2: String)=entradaSalida.escribirLinea("${Observatorio.sonLimitrofes(pais1,pais2)}")
  fun mostrarSi2PaisesNecesitanTraduccion(pais1: String,pais2: String)= entradaSalida.escribirLinea("${Observatorio.necesitanTraduccion(pais1,pais2)}")
  fun mostrarSi2PaisesSonPotencialesAliados(pais1: String,pais2: String)= entradaSalida.escribirLinea("${Observatorio.sonPotencialesAliados(pais1,pais2)}")
  fun mostrarSiEsPlurinacional(pais:String)=entradaSalida.escribirLinea("${Observatorio.buscarPaisDeNombre(pais).esPlurinacional()}")
  fun mostrarSiEsUnaIsla(pais: String)=entradaSalida.escribirLinea("${Observatorio.buscarPaisDeNombre(pais).esUnaIsla()}")
  fun mostrarDensidadPoblacionalDe(pais: String)=entradaSalida.escribirLinea(
    "La densidad poblacional de ${pais} de ${
      Observatorio.buscarPaisDeNombre(
        pais
      ).densidadPoblacional()
    }"
  )
  fun mostrarVecinoMasPobladoDe(pais: String)=entradaSalida.escribirLinea(
    "El vecino mas poblado de ${pais} es ${
      Observatorio.buscarPaisDeNombre(
        pais
      ).vecinoMasPoblado().nombre
    } con ${Observatorio.buscarPaisDeNombre(pais).vecinoMasPoblado().poblacion} habitantes"
  )
  fun ejecutarAccionPara2Paises(pais1:String,pais2:String){
    val opcion= entradaSalida.leerLinea()
    val accion=when{
      opcion=="1"->{ mostrarSi2PaisesSonLimitrofes(pais1,pais2);mostrarMenuDeRegresoOSalida() }
      opcion=="2"->{mostrarSi2PaisesNecesitanTraduccion(pais1,pais2);mostrarMenuDeRegresoOSalida()}
      opcion=="3"->{mostrarSi2PaisesSonPotencialesAliados(pais1,pais2);mostrarMenuDeRegresoOSalida()}
      else-> mostrarMensajeDeSalida()
    }
  }
  fun ejecutarAccionPara1Pais(pais:String){
    val opcion = entradaSalida.leerLinea()
    val respuesta = when { // acá se van agregando las opciones
      opcion == "1" -> {mostrarSiEsPlurinacional(pais);mostrarMenuDeRegresoOSalida()}
      opcion == "2" -> {mostrarSiEsUnaIsla(pais);mostrarMenuDeRegresoOSalida()}
      opcion == "3" -> {mostrarDensidadPoblacionalDe(pais);mostrarMenuDeRegresoOSalida()}
      opcion == "4" -> {mostrarVecinoMasPobladoDe(pais);mostrarMenuDeRegresoOSalida()}
      else -> mostrarMensajeDeSalida()
    }
  }

  fun iniciar() {

    mostrarMenuPrincipal()
    ejecutarAccionParaMenuPrincipal()

  }
}