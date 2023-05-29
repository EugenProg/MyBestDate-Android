package com.bestDate.data.utils.subscription

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.bestDate.data.extension.toLongServerDate
import com.bestDate.data.preferences.Preferences
import com.bestDate.data.preferences.PreferencesUtils
import com.bestDate.data.utils.Logger
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class SubscriptionManager(context: Context) {
    private val client: BillingClient
    private var productList: MutableList<ProductDetails> = mutableListOf()

    @Inject
    lateinit var preferencesUtils: PreferencesUtils

    var updateSubscriptionData: ((start: String, end: String) -> Unit)? = null

    init {
        client = BillingClient.newBuilder(context)
            .setListener { result, purchasesList ->
                purchaseUpdated(result, purchasesList)
            }
            .enablePendingPurchases()
            .build()

        startConnection()
    }

    private fun purchaseUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
            val isActive = purchases.first().purchaseState == Purchase.PurchaseState.PURCHASED
            preferencesUtils.saveBoolean(Preferences.HAS_A_ACTIVE_SUBSCRIPTION, isActive)
        }
    }

    fun startConnection() {
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingResponseCode.OK) {
                    getSubscriptionDetails()
                    getPurchases()
                    getPurchaseHistory()
                }
            }

            override fun onBillingServiceDisconnected() {
                Logger.print("Billing server disconnected")
            }
        })
    }

    private fun getPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.SUBS)
        client.queryPurchasesAsync(params.build()) { result, purchases ->
            if (result.responseCode == BillingResponseCode.OK) {
                val hasActiveSubscription =
                    purchases.lastOrNull()?.purchaseState == Purchase.PurchaseState.PURCHASED
                preferencesUtils.saveBoolean(
                    Preferences.HAS_A_ACTIVE_SUBSCRIPTION,
                    hasActiveSubscription
                )
            }
        }
    }

    private fun getPurchaseHistory() {
        val params = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(ProductType.SUBS)

        client.queryPurchaseHistoryAsync(params.build()) { result, purchases ->
            if (result.responseCode == BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
                val purchase = purchases.first()
                val startTime = Date(purchase.purchaseTime)
                val endDate = getEndDate(startTime, getPeriod(purchase.products))
                updateSubscriptionData?.invoke(
                    startTime.toLongServerDate(),
                    endDate.toLongServerDate()
                )
            }
        }
    }

    private fun getSubscriptionDetails() {
        val queryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    getProductById("monthly_plan"),
                    getProductById("three_months_plan"),
                    getProductById("six_months_plan")
                )
            )
            .build()

        client.queryProductDetailsAsync(queryParams) { result, detailsList ->
            if (result.responseCode == BillingResponseCode.OK) {
                productList.clear()
                detailsList.forEach {
                    //it.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
                    productList.add(it)
                }
            }
        }
    }

    private fun getProductById(id: String): Product {
        return Product.newBuilder()
            .setProductId(id)
            .setProductType(ProductType.SUBS)
            .build()
    }

    private fun getPeriod(products: List<String>?): Int {
        return when {
            products?.contains("monthly_plan") == true -> 1
            products?.contains("three_months_plan") == true -> 3
            products?.contains("six_months_plan") == true -> 6
            else -> 1
        }
    }

    private fun getEndDate(startDate: Date, period: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.MONTH, period)
        return calendar.time
    }

    fun launchBilling(activity: Activity, product: ProductDetails) {
        val params = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product)
            .setOfferToken(product.subscriptionOfferDetails?.firstOrNull()?.offerToken.orEmpty())
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(params))
            .build()

        client.launchBillingFlow(activity, flowParams)
    }

    fun getProductDetailsById(id: String): ProductDetails? {
        return productList.firstOrNull { it.productId == id }
    }
}