package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class PaymentMethod(
    @SerializedName("_id") val id: String,
    val code: String? = "VNPAY",
    var paymentMethod: PaymentMethodCode? = PaymentMethodCode.VNPAY
) {
    fun setPaymentMethodCode() {
        paymentMethod = when (code) {
            "COD" -> PaymentMethodCode.COD
            "VNPAY" -> PaymentMethodCode.VNPAY
            else -> PaymentMethodCode.VNPAY
        }
    }
}
