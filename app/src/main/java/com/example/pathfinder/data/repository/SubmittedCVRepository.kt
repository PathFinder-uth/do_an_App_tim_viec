package com.example.pathfinder.data.repository

import com.example.pathfinder.data.model.SubmittedCV
import com.google.firebase.firestore.FirebaseFirestore

class SubmittedCVRepository {

    fun saveCvToFirestore(
        jobId: String,
        applicantId: String,
        cvUrl: String,
        applicantName: String,
        jobTitle: String,

    ) {
        val db = FirebaseFirestore.getInstance()
        val cvData = hashMapOf(
            "applicantId" to applicantId,
            "applicantName" to applicantName,
            "jobId" to jobId,
            "jobTitle" to jobTitle,
            "cvUrl" to cvUrl
        )
        db.collection("submitted_cvs").add(cvData)
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }
}