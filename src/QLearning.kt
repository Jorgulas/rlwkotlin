open class QLearning(
    override val memAprend: MemoriaAprend,
    override val selAccao: SelAccao,
    override val alfa: Double,
    override val gama: Double
) : AprendRef() {
    override fun aprender(s: Estado, a: Accao, r: Double, sn: Estado, an: Accao?) {
        val maxAn = selAccao.maxAccao(sn)
        val qsa = memAprend.q(s, a)
        val qsanan = memAprend.q(sn, maxAn)
        val q = qsa + alfa * (r + gama * qsanan - qsa)
        memAprend.atualizar(s, a, q)

    }
}

data class Multi<out A, out B, out C, out D> (val first: A, val second: B, val third: C, val fourth: D)

class ModeloTR {
    private val T = mutableMapOf<Pair<Estado, Accao>, Estado>() // Estado, Accao -> prox Estado
    private val R = mutableMapOf<Pair<Estado, Accao>, Double>() // Estado, Accao -> Recompensa

    fun atualizar(s: Estado, a: Accao, r: Double, sn: Estado) {
        T[s to a] = sn
        R[s to a] = r
    }

    fun amostrar() : Multi<Estado, Accao, Double, Estado> {
        val pairSA : Pair<Estado, Accao> = T.keys.random()
        val sn = T[pairSA]
        val r = R[pairSA]
        if(sn == null || r == null) {
            throw IllegalStateException("No actions available to select from.")
        }
        return Multi(pairSA.first, pairSA.second, r, sn)
    }
}

class DynaQ(
    override val memAprend: MemoriaAprend,
    override val selAccao: SelAccao,
    override val alfa: Double,
    override val gama: Double,
    private val numSim: Int,
    private val modelo: ModeloTR
) : QLearning(memAprend, selAccao, alfa, gama) {

    override fun aprender(s: Estado, a: Accao, r: Double, sn: Estado, an: Accao?) {
        super.aprender(s, a, r, sn, an)
        modelo.atualizar(s, a, r, sn)
        simular()
    }

    fun simular() {
        repeat(numSim) {
            val (s, a, r, sn) = modelo.amostrar()
            super.aprender(s, a, r, sn, null) // TODO try "selAccao.maxAccao(sn)" instead of "null"
        }
    }

}