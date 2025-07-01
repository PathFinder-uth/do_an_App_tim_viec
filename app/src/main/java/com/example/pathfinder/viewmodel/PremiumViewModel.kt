package com.example.pathfinder.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PremiumViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState

    fun purchasePremium() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _purchaseState.value = PurchaseState.Error("Bạn cần đăng nhập để thực hiện.")
                return@launch
            }

            _purchaseState.value = PurchaseState.Loading

            // Mô phỏng quá trình thanh toán thành công
            db.collection("users").document(userId)
                .update("isPremium", true)
                .addOnSuccessListener {
                    _purchaseState.value = PurchaseState.Success
                    Log.d("PremiumPurchase", "User $userId upgraded to premium.")
                }
                .addOnFailureListener { e ->
                    _purchaseState.value = PurchaseState.Error(e.localizedMessage ?: "Nâng cấp thất bại.")
                }
        }
    }

    fun resetState() {
        _purchaseState.value = PurchaseState.Idle
    }
}

sealed class PurchaseState {
    object Idle : PurchaseState()
    object Loading : PurchaseState()
    object Success : PurchaseState()
    data class Error(val message: String) : PurchaseState()
}
