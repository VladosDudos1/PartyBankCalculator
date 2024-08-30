package vlados.dudos.domain

import org.junit.Test
import vlados.dudos.domain.calculating.CalculateManager
import vlados.dudos.domain.model.DebtPair
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.model.Purchase

class CalculateManagerTest {

    private val listEvents = generateEvents()

    @Test
    fun debtCalculatingDuplicatesRemoving() {
        val result1 = CalculateManager().calculateDebts(event = listEvents[0])

        println(result1)
        assert(result1[0].listDebts.size == 2)
    }

    @Test
    fun debtCalculatingCorrectSum() {
        val result2 = CalculateManager().calculateDebts(event = listEvents[1])

        println(result2)
    }

    @Test
    fun debtCalculatingClean() {
        val result3 = CalculateManager().calculateDebts(event = listEvents[2])

        println(result3)
    }

    private fun generateEvents(): List<Event> {
        val participant1 = Participant(1, "Alice")
        val participant2 = Participant(2, "Bob")
        val participant3 = Participant(3, "Charlie")
        val participant4 = Participant(4, "David")
        val participant5 = Participant(5, "Eve")

        val purchase1 = Purchase(1, "Book", 100.0, participant1, listOf(participant1, participant2, participant3), mutableListOf(
            DebtPair(100.0, participant2), DebtPair(1000.0, participant1), DebtPair(429.0, participant3)
        ))
        val purchase2 = Purchase(2, "Pen", 50.0, participant2, listOf(participant1))
        val purchase3 = Purchase(3, "Notebook", 200.0, participant3, listOf(participant1, participant2, participant4))
        val purchase4 = Purchase(4, "Laptop", 1000.0, participant4, listOf(participant3, participant5))
        val purchase5 = Purchase(5, "Phone", 500.0, participant5, listOf(participant1, participant4))
        val purchase6 = Purchase(6, "Phone2", 3500.0, participant3, listOf(participant1, participant2))

        val event1 = Event(
            id = 1,
            name = "Event 1",
            participants = listOf(participant1, participant2, participant3),
            sum = 150,
            owner = participant1,
            listPurchases = mutableListOf(purchase1)
        )

        val event2 = Event(
            id = 2,
            name = "Event 2",
            participants = listOf(participant1, participant2, participant3, participant4, participant5),
            sum = 1700,
            owner = participant3,
            listPurchases = mutableListOf(purchase3, purchase4)
        )

        val event3 = Event(
            id = 3,
            name = "Event 3",
            participants = listOf(participant1, participant2, participant3, participant4, participant5),
            sum = 4300,
            owner = participant4,
            listPurchases = mutableListOf(purchase1, purchase3, purchase5, purchase6)
        )

        val event4 = Event(
            id = 4,
            name = "Event 4",
            participants = listOf(participant1, participant2, participant3, participant4, participant5),
            sum = 1550,
            owner = participant5,
            listPurchases = mutableListOf(purchase2, purchase4, purchase5)
        )

        val event5 = Event(
            id = 5,
            name = "Event 5",
            participants = listOf(participant1, participant2, participant3, participant4, participant5),
            sum = 800,
            owner = participant1,
            listPurchases = mutableListOf(purchase1, purchase3, purchase5)
        )

        return listOf(event1, event2, event3, event4, event5)
    }
}