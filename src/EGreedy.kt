
abstract class EGreedy(memAprend: MemoriaAprend) : SelAccao(memAprend) {
    abstract val actions: List<Accao>
    abstract val epsilon: Double

    abstract override fun selecionarAccao(s: Estado): Accao
    abstract override fun maxAccao(s: Estado): Accao
    abstract fun explorar(): Accao
    abstract fun aproveitar(s: Estado): Accao
}

fun <T : Comparable<T>> Iterable<T>.argmax(): Int? {
    return withIndex().maxByOrNull { it.value }?.index
}

class EGreedyImpl(
    memAprend: MemoriaAprend,
    override val actions: MutableList<Accao>,
    override val epsilon: Double
) : EGreedy(memAprend) {

    override fun selecionarAccao(s: Estado): Accao {
        return if (Math.random() < epsilon) {
            explorar()
        } else {
            aproveitar(s)
        }
    }

    override fun maxAccao(s: Estado): Accao {
        val tempAction = actions.shuffled()
        val maxIndex = tempAction.map { memAprend.q(s, it) }.argmax()
        // Return the argmax action from actions (in python: return np.argmax(actions, lambda (a): memAprend.q(s, a)))

        // Return the action at that index or throw an exception if no action is found
        return if (maxIndex != null) {
            tempAction[maxIndex]
        } else {
            throw IllegalStateException("No actions available to select from.")
        }

    }

    override fun explorar(): Accao {
        // return random choice from actions (in python: return random.choice(actions))
        return actions.random()
    }

    override fun aproveitar(s: Estado): Accao {
        return maxAccao(s)
    }

}



