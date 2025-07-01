package com.example.pathfinder.viewmodel.state

import android.net.Uri

// ✔✔ State dành cho RecruiterInfoViewModel, bao gồm dữ liệu nhập, đường dẫn logo, và trạng thái form

data class RecruiterUiState(
    val logoUrl: String = "",               // Link ảnh logo trên Cloudinary (nếu có)
    val logoUri: Uri? = null,                // Uri logo trên thiết bị (trước khi upload)

    val companyName: String = "",
    val companyDescription: String = "",
    val companyAddress: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val website: String = "",
    val industry: String = "",
    val companySize: String = "",

    // ✖ Thay vì boolean, dùng String? cho error message (null = không có lỗi)
    val companyNameError: String? = null,
    val companyDescriptionError: String? = null,
    val companyAddressError: String? = null,
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val websiteError: String? = null,
    val industryError: String? = null,
    val companySizeError: String? = null,

    val isSubmitting: Boolean = false,      // Dùng cho hiện loading
    val isSubmitted: Boolean = false        // Đã submit thành công hay chưa
)
