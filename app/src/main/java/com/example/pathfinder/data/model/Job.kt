package com.example.pathfinder.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties // Bỏ qua các trường lạ, tránh crash
data class Job(
     val id: String = "",

    // Thêm các annotation @PropertyName và chuyển sang `var`
    // để đảm bảo Firestore có thể đọc và ghi dữ liệu một cách chính xác.
    @get:PropertyName("recruiterId") @set:PropertyName("recruiterId")
    var recruiterId: String = "",

    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("requirements") @set:PropertyName("requirements")
    var requirements: String = "",

    @get:PropertyName("salary") @set:PropertyName("salary")
    var salary: String = "",

    @get:PropertyName("location") @set:PropertyName("location")
    var location: String = "",

    @get:PropertyName("type") @set:PropertyName("type")
    var type: String = "",

    @get:PropertyName("companyName") @set:PropertyName("companyName")
    var companyName: String = "",

    @get:PropertyName("logoUrl") @set:PropertyName("logoUrl")
    var logoUrl: String = "",

    @get:PropertyName("createdAt") @set:PropertyName("createdAt")
    var createdAt: Long = 0L,

    @get:PropertyName("deadline") @set:PropertyName("deadline")
    var deadline: Long = 0L,

    @get:PropertyName("isCvSubmitted") @set:PropertyName("isCvSubmitted")
    var isCvSubmitted: Boolean = false,

    @get:PropertyName("isFilled") @set:PropertyName("isFilled")
    var isFilled: Boolean = false,

    @get:PropertyName("jobType") @set:PropertyName("jobType")
    var jobType: String = "free",

     @get:PropertyName("category") @set:PropertyName("category")
     var category: String = ""
)
