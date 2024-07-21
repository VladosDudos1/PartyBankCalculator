package vlados.dudos.domain.utils

import android.content.Context
import vlados.dudos.domain.model.Event
import vlados.dudos.domain.model.Participant
import vlados.dudos.domain.system.SharedManager
import vlados.dudos.domain.utils.ListOperationsSupport.getMaxId

object ModelsTransformUtil {
    fun listParticipantsToString(list: List<Participant>) : String {
        var resultString = ""
        for ((index, participant) in list.withIndex()){
            resultString += participant.name
            if (index+1 != list.size) resultString += ", "
        }
        return resultString
    }

    fun createNewEvent(listIds: List<Int>, name: String, listParticipant: List<Participant>, owner: Participant) : Event {
        return Event(
            getMaxId(listIds),
            name = name,
            listParticipant,
            0,
            owner,
            listOf()
        )
    }
}