package vlados.dudos.domain.utils

import vlados.dudos.domain.model.Participant

object ModelsTransformUtil {
    fun listParticipantsToString(list: List<Participant>) : String {
        var resultString = ""
        for ((index, participant) in list.withIndex()){
            resultString += participant.name
            if (index+1 == list.size) resultString += ", "
        }
        return resultString
    }
}