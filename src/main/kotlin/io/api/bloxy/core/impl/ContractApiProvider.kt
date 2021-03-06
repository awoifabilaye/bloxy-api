package io.api.bloxy.core.impl

import io.api.bloxy.error.ParamException
import io.api.bloxy.executor.IHttpClient
import io.api.bloxy.model.dto.contract.ContractDetail
import io.api.bloxy.model.dto.contract.Event
import io.api.bloxy.model.dto.contract.Method
import io.api.bloxy.model.dto.contract.SignatureDetail


/**
 * API for smart contracts analytics and parsing
 * More information - https://bloxy.info/api_methods#smart_contract
 *
 * @see io.api.bloxy.core.IContractApi
 * @see io.api.bloxy.core.impl.BasicProvider
 *
 * @author GoodforGod
 * @since 02.12.2018
 */
class ContractApiProvider internal constructor(client: IHttpClient, key: String) : BasicProvider(client, "smart_contract", key) {

    companion object {
        private val errors = listOf(
            "^Smart contract".toRegex(),
            "^Event not found".toRegex(),
            "^Function not found".toRegex()
        )
    }

    private fun checkSigHashRequired(hashes: List<String>): List<String> {
        if (hashes.isNullOrEmpty()) throw ParamException("Hashes are null or empty")
        hashes.forEach { hash -> if (hash.isBlank()) throw ParamException("Hash is not empty or blank: $hash") }
        return hashes
    }

    private fun sigHashesAsParam(hashes: List<String>, prefix: String = ""): String {
        return asParam(checkSigHashRequired(hashes), "${prefix}signature_hash[]=", "&signature_hash[]=")
    }

    private fun sigNamesAsParam(hashes: List<String>, prefix: String = ""): String {
        return asParam(checkNonBlank(hashes), "${prefix}name[]=", "&name[]=")
    }

    /**
     * @see io.api.bloxy.core.IContractApi.signaturesByHash
     */
    fun signaturesByHash(
        hashes: List<String>,
        limit: Int = 100,
        offset: Int = 0
    ): List<SignatureDetail> {
        val params = "resolve_signature?${sigHashesAsParam(hashes)}"
        return getOffset(params, limit, offset, 1000, skipErrors = errors)
    }

    /**
     * @see io.api.bloxy.core.IContractApi.signaturesByName
     */
    fun signaturesByName(
        names: List<String>,
        limit: Int = 100,
        offset: Int = 0
    ): List<SignatureDetail> {
        val params = "find_signature?${sigNamesAsParam(names)}"
        return getOffset(params, limit, offset, 1000, skipErrors = errors)
    }

    /**
     * @see io.api.bloxy.core.IContractApi.withMethod
     */
    fun withMethod(
        signatureHash: String,
        limit: Int = 100,
        offset: Int = 0
    ): List<ContractDetail> {
        val params = "method_in_contracts?signature_hash=${checkNonBlank(signatureHash)}"
        return getOffset(params, limit, offset, 1000, skipErrors = errors)
    }

    /**
     * @see io.api.bloxy.core.IContractApi.withEvent
     */
    fun withEvent(
        signatureHash: String,
        limit: Int = 100,
        offset: Int = 0
    ): List<ContractDetail> {
        val params = "event_in_contracts?signature_hash=${checkNonBlank(signatureHash)}"
        return getOffset(params, limit, offset, 1000, skipErrors = errors)
    }

    /**
     * @see io.api.bloxy.core.IContractApi.methods
     */
    fun methods(
        contract: String
    ): List<Method> {
        val params = "smart_contract_methods?smart_contract_address=${checkAddressRequired(contract)}"
        return get(params, errors)
    }

    /**
     * @see io.api.bloxy.core.IContractApi.events
     */
    fun events(
        contract: String
    ): List<Event> {
        val params = "smart_contract_events?smart_contract_address=${checkAddressRequired(contract)}"
        return get(params, errors)
    }
}