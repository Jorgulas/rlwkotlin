

//------------- Classes que definem o ambiente -------------

class Accao(task : (Any?) -> Unit)
class Estado(val s: Any)

abstract class MemoriaAprend {
    abstract fun atualizar(s: Estado, a: Accao, r: Double)
    abstract fun q(s: Estado, a: Accao?): Double
}

abstract class SelAccao(val memAprend: MemoriaAprend) {
    abstract fun selecionarAccao(s: Estado): Accao
    abstract fun maxAccao(s: Estado): Accao
}

abstract class AprendRef {
    abstract val memAprend: MemoriaAprend
    abstract val selAccao: SelAccao
    abstract val alfa: Double
    abstract val gama: Double

    abstract fun aprender(s: Estado, a: Accao, r: Double, sn: Estado, an: Accao? = null)
}


//------------- Classe de memória de decisões -------------

class MemoriaEsparsa(private val valorOmissao: Double = 0.0) : MemoriaAprend() {
    private val memoria = mutableMapOf<Estado, MutableMap<Accao, Double>>()

    override fun atualizar(s: Estado, a: Accao, r: Double) {
        memoria[s] = memoria[s] ?: mutableMapOf()
        memoria[s]!![a] = r
    }

    override fun q(s: Estado, a: Accao?): Double {
        return memoria[s]?.get(a) ?: valorOmissao
    }
}