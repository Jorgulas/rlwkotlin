class Sarsa(
    override val memAprend: MemoriaAprend,
    override val selAccao: SelAccao,
    override val alfa: Double,
    override val gama: Double
) : AprendRef() {

    override fun aprender(s: Estado, a: Accao, r: Double, sn: Estado, an: Accao?) {
        val qsa = memAprend.q(s, a)
        val qsanan = memAprend.q(sn, an)
        val q = qsa + alfa * (r + gama * qsanan - qsa)
        memAprend.atualizar(s, a, q)
    }
}