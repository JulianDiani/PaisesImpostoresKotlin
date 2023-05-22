package ar.edu.unahur.obj2.impostoresPaises

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class ProgramaTest : DescribeSpec({
    describe("Programa") {

        val consolaMock = mockk<Consola>()
        val apiMockk=mockk<RestCountriesAPI>()
        Observatorio.api=apiMockk

        // Configuramos un mock para la entrada salida
        Programa.entradaSalida = consolaMock

        // Indicamos que los llamados a `escribirLinea` no hacen nada (just Runs)
        every { consolaMock.escribirLinea(any()) } just Runs
        every { apiMockk.buscarPaisesPorNombre("Argentina") } returns listOf(
            Country(
                "Argentina", "ARG", "Buenos Aires", "America", 40000000, 100000.0,
                listOf("PAR","BRA"), listOf(Language("Español"),Language("Guarani")), listOf(RegionalBloc("MER","Mercosur"))
            )
        )
        every { apiMockk.paisConCodigo("ARG") } returns Country(
            "Argentina", "ARG", "Buenos Aires", "America", 40000000, 100000.0,
            listOf("PAR","BRA"), listOf(Language("Español"),Language("Guarani")), listOf(RegionalBloc("MER","Mercosur"))
        )
        every {apiMockk.paisConCodigo("FRA")} returns Country(
            "Francia", "FRA", "Paris", "Europa", 40000000, 100000.0,
            listOf("RUS"), emptyList(), emptyList()
        )
        every { apiMockk.buscarPaisesPorNombre("Paraguay") } returns listOf(
            Country(
                "Paraguay", "PAR", "Asuncion", "America", 40000000, 100000.0,
                listOf("ARG","BRA"), listOf(Language("Español"),Language("Guarani")), listOf(RegionalBloc("MER","Mercosur"))
            )
        )
        every { apiMockk.paisConCodigo("PAR") } returns Country(
            "Paraguay", "PAR", "Asuncion", "America", 40000000, 100000.0,
            listOf("ARG","BRA"), listOf(Language("Español"),Language("Guarani")), listOf(RegionalBloc("MER","Mercosur"))
        )
        every { apiMockk.paisConCodigo("BRA") } returns Country(
            "Brazil", "BRA", "Brazilia", "America", 206135893, 100000.0,
            listOf("ARG","PAR"), listOf(Language("Portugues")), listOf(RegionalBloc("MER","Mercosur"))
        )
        every { apiMockk.buscarPaisesPorNombre("Brazil") } returns listOf(Country(
            "Brazil", "BRA", "Brazilia", "America", 206135893, 100000.0,
            listOf("ARG","PAR"), listOf(Language("Portugues")), listOf(RegionalBloc("MER","Mercosur"))
        ))
        every { apiMockk.buscarPaisesPorNombre("Russia") } returns listOf(
            Country(
                "Russia", "RUS", "Moscu", "Europa", 40000000, 100000.0,
                listOf("FRA"), listOf(Language("Ruso")), listOf(RegionalBloc("EUR","Euromer"))
            )
        )
        every { apiMockk.paisConCodigo("RUS") } returns Country(
            "Russia", "RUS", "Moscu", "Europa", 40000000, 100000.0,
            listOf("FRA"), listOf(Language("Ruso")), listOf(RegionalBloc("EUR","Euromer"))
        )
        every { apiMockk.buscarPaisesPorNombre("China") } returns listOf(
            Country(
                "China", "CHN", "Beijing", "Asia", 4000000000, 100000.0,
                listOf(), listOf(Language("Chino")), listOf(RegionalBloc("ASI","Asiamer"))
            )
        )
        every { apiMockk.paisConCodigo("CHN") } returns Country(
            "China", "CHN", "Beijing", "Asia", 4000000000, 100000.0,
            listOf(), listOf(Language("Chino")), listOf(RegionalBloc("ASI","Asiamer"))
        )
        every {apiMockk.buscarPaisesPorNombre("Argfsgdfs")} returns emptyList()

        it("Argentina es plurinacional") {

            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina","1","2") // ACA ES, OPCION DEL PRIMER MENU,PAIS,OPCION DEL SEG MENU

            // Iniciamos el programa
            Programa.iniciar()


            // Verificamos que se escribió "por pantalla" el resultado esperado
            verify {
                consolaMock.escribirLinea("true")
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }

        }
        it("Brazil NO es plurinacional") {

            every { consolaMock.leerLinea() } returnsMany listOf("1","Brazil","1","2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("false")
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }

        }

        it("Argentina NO es una isla") {


            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina","2","2")


            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("false")
            }

        }

        it("China ES una isla") /*(solo en este test, NO así en la api real)*/ {

            every { consolaMock.leerLinea() } returnsMany listOf("1","China","2","2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("true")
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }

        }

        it("Saber densidad poblacional de Argentina") {

            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina","3","2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("La densidad poblacional de Argentina de 400.0")
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }

        }

        it("Ingresar un pais que no existe"){
            every { consolaMock.leerLinea() } returnsMany listOf("1","Argfsgdfs","Argentina")
            Programa.iniciar()
            verify{consolaMock.escribirLinea("Ese pais no existe,reingresar.")
                consolaMock.escribirLinea("Argentina (ARG) es un país de America, con una población de 40000000 habitantes.")
            }
        }

        it("Saber el vecino mas poblado de Argentina") {

            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina","4","2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("El vecino mas poblado de Argentina es Brazil con 206135893 habitantes")
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }

        }

        it("Salir del sistema al ingresar una opcion que no existe") {

            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina","8")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }

        }

        it("Salir del sistema al ingresar la opcion de salir.") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina","5")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("Saliendo del sistema, hasta luego! :D")
            }
        }

        it("Argentina y paraguay paises son limitrofes") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina","Paraguay","1","Argentina","Paraguay","2")// repito argentina y paraguay xq al elegir la opcion vuelve a usar el observatorio para buscar paises.

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("true")
            }
        }

        it("Argentina y Russia no son limitrofes") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina","Russia","1","Argentina","Russia","2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("false")
            }
        }

        it("Argentina y Brazil necesitan traduccion para poder dialogar") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina", "Brazil", "2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("true")
            }
        }

        it("Argentina y Paraguay no necesitan traduccion para poder dialogar") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina", "Paraguay", "2")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("false")
            }
        }

        it("argentina y Brazil no son potenciales aliados") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina", "Brazil", "3")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("false")
            }
        }

        it("argentina y Paraguay son potenciales aliados") {

            every{consolaMock.escribirLinea(any())} just runs
            every { consolaMock.leerLinea() } returnsMany listOf("2","Argentina", "Paraguay", "3")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("true")
            }
        }

        it("Codigos ISO3 de los 5 paises con mayor densidad poblacional") {

            every{consolaMock.escribirLinea(any())} just runs
            every{apiMockk.todosLosPaises()}returns listOf(Country(
                "Paraguay", "PAR", "Asuncion", "America", 41000000, 100000.0,
                listOf("ARG"),listOf(Language("Guarani"),Language("Español")), emptyList()
            ), Country(
                "Argentina", "ARG", "Buenos Aires", "America", 402000000, 100000.0,
                listOf("PAR","BRA"), listOf(Language("Español"),Language("Guarani")), listOf(RegionalBloc("MER","Mercosur"))
            ),
                Country(
                    "Australia", "AUS", "Auckland", "Oceania", 40000000, 100000.0,
                    listOf(),listOf(Language("Ingles"),Language("Frances")), emptyList()
                ),Country(
                    "Francia", "FRA", "Paris", "Europa", 40000000, 100000.0,
                    listOf("RUS"), emptyList(), emptyList()
                ),Country(
                    "Brazil", "BRA", "Brazilia", "America", 206135893, 100000.0,
                    listOf("ARG","PAR"), listOf(Language("Portugues")), listOf(RegionalBloc("MER","Mercosur"))
                ),Country(
                    "Russia", "RUS", "Moscu", "Europa", 40000000000, 100000.0,
                    listOf("FRA"), listOf(Language("Ruso")), listOf(RegionalBloc("EUR","Euromer"))
                )
            )
            every { consolaMock.leerLinea() } returnsMany listOf("3")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("los codigo ISO3 de los paises con mayor densidad poblacional son: [RUS, ARG, BRA, PAR, AUS]")
            }
        }

        it("Saber el continente con mas paises plurinacionales") {

            every{consolaMock.escribirLinea(any())} just runs
            every{apiMockk.todosLosPaises()}returns listOf(Country(
                "Paraguay", "PAR", "Asuncion", "America", 41000000, 100000.0,
                listOf("ARG"),listOf(Language("Guarani"),Language("Español")), emptyList()
            ),Country(
                "Argentina", "ARG", "Buenos Aires", "America", 402000000, 100000.0,
                listOf("PAR","BRA"), listOf(Language("Español"),Language("Guarani")), listOf(RegionalBloc("MER","Mercosur"))
            ),
                Country(
                    "Australia", "AUS", "Auckland", "Oceania", 40000000, 100000.0,
                    listOf(),listOf(Language("Ingles"),Language("Frances")), emptyList()
                ),Country(
                    "Francia", "FRA", "Paris", "Europa", 40000000, 100000.0,
                    listOf("RUS"), emptyList(), emptyList()
                ),Country(
                    "Brazil", "BRA", "Brazilia", "America", 206135893, 100000.0,
                    listOf("ARG","PAR"), listOf(Language("Portugues")), listOf(RegionalBloc("MER","Mercosur"))
                ),Country(
                    "Russia", "RUS", "Moscu", "Europa", 40000000000, 100000.0,
                    listOf("FRA"), listOf(Language("Ruso")), listOf(RegionalBloc("EUR","Euromer"))
                )
            )
            every { consolaMock.leerLinea() } returnsMany listOf("4")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("el continente con mas paises plurinacionales es:America con 2 paises plurinacionales")
            }
        }

        it("Saber el promedio de densidad poblacional de los paises que son isla") {

            every{consolaMock.escribirLinea(any())} just runs
            every {apiMockk.todosLosPaises()} returns listOf(
            Country(
                "Australia", "AUS", "Auckland", "Oceania", 40000000, 100000.0,
                listOf(),listOf(Language("Ingles"),Language("Frances")), emptyList()
            ))
            every { consolaMock.leerLinea() } returnsMany listOf("5")

            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("el promedio de densidad de paises isla es de:400.0")
            }
        }

        it("Buscar pais por nombre") {

            every { consolaMock.leerLinea() } returnsMany listOf("1","Argentina")
            Programa.iniciar()

            verify {
                consolaMock.escribirLinea("Argentina (ARG) es un país de America, con una población de 40000000 habitantes.")
            }
        }
  }
})
