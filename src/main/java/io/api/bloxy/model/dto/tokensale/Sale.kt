package io.api.bloxy.model.dto.tokensale


/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 18.11.2018
 */
data class Sale(
    val token_address: String = "",
    val symbol: String = "",
    val token_type: String = "",
    val transactions: Long = 0,
    val eth_amount: Number = .0,
    val token_amount: Number = .0,
    val token_buyers: Long = 0
) {
}