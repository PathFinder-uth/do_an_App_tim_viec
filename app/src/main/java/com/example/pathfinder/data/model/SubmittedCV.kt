package com.example.pathfinder.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class SubmittedCV(
    @DocumentId val id: String = "",

    @get:PropertyName("jobId") @set:PropertyName("jobId")
    var jobId: String = "",

    @get:PropertyName("applicantId") @set:PropertyName("applicantId")
    var applicantId: String = "",

    @get:PropertyName("cvUrl") @set:PropertyName("cvUrl")
    var cvUrl: String = "",

    @get:PropertyName("applicantName") @set:PropertyName("applicantName")
    var applicantName: String = "",

    @get:PropertyName("jobTitle") @set:PropertyName("jobTitle")
    var jobTitle: String = "",

    @get:PropertyName("isReviewed") @set:PropertyName("isReviewed")
    var isReviewed: Boolean = false
)
