package com.pootentially.pootential.utility.Models

import com.pootentially.pootential.utility.firebase.models.Restroom

/**
 * Created by nick on 2/10/18.
 */
data class ChangedRestroom(
    var status: Enumerations.ChangedStatus = Enumerations.ChangedStatus.ADDED,
    var restroom: Restroom? = null
)