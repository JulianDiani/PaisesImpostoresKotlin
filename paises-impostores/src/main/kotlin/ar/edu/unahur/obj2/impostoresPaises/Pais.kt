package ar.edu.unahur.obj2.impostoresPaises

class Pais(
    val nombre: String, val codigoISO3: String, val poblacion:Int, val superficie: Double,
    val continente: String,
    val bloquesRegionales: List<String>, val idiomas: List<String>,val paisesLimitrofes:MutableList<Pais>) {
    fun esPlurinacional()= idiomas.size>1
    fun esUnaIsla()= paisesLimitrofes.isEmpty()
    fun densidadPoblacional()= poblacion/superficie
    fun vecinoMasPoblado() =(paisesLimitrofes+this).maxByOrNull { it.poblacion }!!
    fun esLimitrofeCon(pais: Pais)=paisesLimitrofes.map { it.nombre }.contains(pais.nombre)
    fun necesitaTraduccion(pais: Pais) = cantidadDeIdiomasEnComun(pais) == 0
    fun cantidadDeIdiomasEnComun(pais: Pais) = this.idiomas.intersect(pais.idiomas).size
    fun esPotencialAliadoDe(pais: Pais) = !this.necesitaTraduccion(pais) && this.comparteAlMenosUnBloqueRegionalCon(pais)
    fun cantidadDeBloquesRegionalesEnComun(pais: Pais) = this.bloquesRegionales.intersect(pais.bloquesRegionales).size
    fun comparteAlMenosUnBloqueRegionalCon(pais: Pais) = this.cantidadDeBloquesRegionalesEnComun(pais) >= 1

}

object Observatorio {
    var api:RestCountriesAPI=RestCountriesAPI()
    val adapter=PaisAdapter

    fun todosLosPaises()= api.todosLosPaises().map {adapter.adaptarPaisSinLimitrofes(it) }
    fun todosLosPaisesConLimitrofes()=api.todosLosPaises().map{adapter.adaptarPais(it)}
    fun buscarPaisDeNombre(nombrePais:String)= adapter.adaptarPais(api.buscarPaisesPorNombre(nombrePais).first())
    fun sonLimitrofes(nombrePais1:String, nombrePais2:String):Boolean {
        val pais1=buscarPaisDeNombre(nombrePais1)
        val pais2=buscarPaisDeNombre(nombrePais2)

        return pais1.esLimitrofeCon(pais2)
    }
    fun necesitanTraduccion(nombrePais1: String,nombrePais2: String)= buscarPaisDeNombre(nombrePais1).necesitaTraduccion(buscarPaisDeNombre(nombrePais2))
    fun sonPotencialesAliados(nombrePais1: String,nombrePais2: String)= buscarPaisDeNombre(nombrePais1).esPotencialAliadoDe(buscarPaisDeNombre(nombrePais2))
    fun paisesOrdenadosPorDensidad()= todosLosPaises().sortedByDescending { it.densidadPoblacional() }
    fun top5paisesMasDensos()=paisesOrdenadosPorDensidad().take(5)
    fun codigoIso5PaisesMasDensos()=top5paisesMasDensos().map{it.codigoISO3}
    fun paisesDeContinente(continente:String)= todosLosPaises().filter{it.continente==continente}
    fun cantidadDePlurinacionalesDeContinente(continente: String)=paisesDeContinente(continente).count {it.esPlurinacional() }
    fun todosLosContinentes()= todosLosPaises().map{it.continente}.toSet()
    fun continenteConMasPlurinacionales()= todosLosContinentes().maxByOrNull { continente-> cantidadDePlurinacionalesDeContinente(continente)}!!
    fun paisesIsla()= todosLosPaisesConLimitrofes().filter{it.esUnaIsla()}
    fun promedioDeDensidadDePaisesIsla()= paisesIsla().map { it.densidadPoblacional() }.average()
}
object PaisAdapter{
    fun crearPais(nombre:String,codigo:String,poblacion:Int,superficie: Double,continente:String,bloquesRegionales: List<String>,idiomas: List<String>,paisesLimitrofes:MutableList<Pais>) = Pais(nombre,codigo,poblacion,superficie,continente,bloquesRegionales,idiomas,paisesLimitrofes)
    fun adaptarPais(country:Country):Pais{
        val bloquesRegionales=adaptarBloquesRegionales(country)
        val idiomas=adaptarIdiomas(country)
        val poblacion=adaptarPoblacion(country)
        val pais= crearPais(country.name,country.alpha3Code,poblacion,country.area ?: 1.0,country.region,bloquesRegionales,idiomas,adaptarLimitrofes(country).toMutableList())

        return pais
    }
    fun adaptarBloquesRegionales(country:Country)=country.regionalBlocs.map{it.name}
    fun adaptarIdiomas(country: Country)=country.languages.map{it.name}
    fun adaptarPaisSinLimitrofes(country: Country):Pais{
        val bloquesRegionales=adaptarBloquesRegionales(country)
        val idiomas=adaptarIdiomas(country)
        val poblacion=adaptarPoblacion(country)
        val pais=crearPais(country.name,country.alpha3Code,poblacion,
            country.area ?: 1.0,country.region,
            bloquesRegionales,idiomas, mutableListOf())
        return pais
    }

    fun adaptarPoblacion(country: Country)=country.population.toInt()
    fun adaptarLimitrofes(country: Country):List<Pais>{
        val limitrofes=country.borders.map{codigo->Observatorio.api.paisConCodigo(codigo)}
        return limitrofes.map{pais->adaptarPaisSinLimitrofes(pais)}
    }
}