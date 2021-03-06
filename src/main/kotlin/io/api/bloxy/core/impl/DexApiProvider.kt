package io.api.bloxy.core.impl

import io.api.bloxy.executor.IHttpClient
import io.api.bloxy.model.dto.dex.*
import org.jetbrains.annotations.NotNull


/**
 * API for Analysis of decentralised exchanges (DEX)
 * More information - https://bloxy.info/api_methods#dex
 *
 * @see io.api.bloxy.core.IDexApi
 * @see io.api.bloxy.core.impl.BasicProvider
 *
 * @author GoodforGod
 * @since 16.11.2018
 */
class DexApiProvider internal constructor(client: IHttpClient, key: String) : BasicProvider(client, "dex", key) {

    companion object {
        private val errors = listOf(
            "^Protocols not found".toRegex(),
            "^Protocol can not be".toRegex(),
            "^Not found any DEXes".toRegex()
        )
    }

    private fun protocolAsParam(values: List<String>): String {
        return asParam(values, "&protocol[]=", "protocol[]=")
    }

    private fun contractAsParam(values: List<String>): String {
        return asParam(checkAddress(values), "&smart_contract_address[]=", "smart_contract_address[]=")
    }

    /**
     * @see io.api.bloxy.core.IDexApi.protocols
     */
    @NotNull
    fun protocols(): List<DexProtocol> {
        return get("protocols?")
    }

    /**
     * @see io.api.bloxy.core.IDexApi.contracts
     */
    @NotNull
    @JvmOverloads
    fun contracts(
        protocols: List<String> = emptyList(),
        limit: Int = 100,
        offset: Int = 0,
        timeSpanDays: Int = 30
    ): List<DexContract> {
        val params = "smart_contracts?days=${toDays(timeSpanDays)}${protocolAsParam(protocols)}"
        return getOffset(params, limit, offset)
    }

    /**
     * @see io.api.bloxy.core.IDexApi.trades
     */
    @NotNull
    @JvmOverloads
    fun trades(
        protocols: List<String> = emptyList(),
        dexContracts: List<String> = emptyList(),
        tokenAddresses: List<String> = emptyList(),
        limit: Int = 100,
        offset: Int = 0,
        timeSpanDays: Int = 5
    ): List<DexTrade> {
        val paramsAddrs = "${contractAsParam(dexContracts)}${tokenAsParam(tokenAddresses)}"
        val params = "trades?days=${toDays(timeSpanDays, 30)}${protocolAsParam(protocols)}$paramsAddrs"
        return getOffset(params, limit, offset, skipErrors = errors)
    }

    /**
     * @see io.api.bloxy.core.IDexApi.pendingTxs
     */
    @NotNull
    @JvmOverloads
    fun pendingTxs(
        protocols: List<String> = emptyList(),
        dexContracts: List<String> = emptyList()
    ): List<DexTxPending> {
        val paramDex = contractAsParam(dexContracts)
        val formattedDex = if (protocols.isEmpty()) paramDex.replace("&", "") else paramDex
        val params = "pending?${protocolAsParam(protocols)}$formattedDex"
        return get(params, errors)
    }

    /**
     * @see io.api.bloxy.core.IDexApi.tradesActive
     */
    @NotNull
    @JvmOverloads
    fun tradesActive(
        protocols: List<String> = emptyList(),
        dexContracts: List<String> = emptyList(),
        limit: Int = 100,
        offset: Int = 0,
        timeSpanDays: Int = 30
    ): List<DexTradeActive> {
        val paramsAddrs = "${protocolAsParam(protocols)}${contractAsParam(dexContracts)}"
        val params = "traders?days=${toDays(timeSpanDays, 720)}$paramsAddrs"
        return getOffset(params, limit, offset, skipErrors = errors)
    }
}