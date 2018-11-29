package io.api.bloxy.core.dex

import io.api.bloxy.core.Tester
import io.api.bloxy.error.ParamException
import org.junit.Test


/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 21.11.2018
 */
class TradesActiveTests : Tester() {

    @Test
    fun `valid empty params`() {
        val list = api.dex.tradesActive(emptyList())
        assertNotNull(list)
        assertFalse(list.isEmpty())
        assertFalse(list[0].isEmpty())
        assertTrue(list[0].haveFromTime())
        assertTrue(list[0].haveTillTime())
        assertNotNull(list[0].address)
        assertNotNull(list[0].addressAnnotation)
        assertNotNull(list[0].contractType)
        assertNotNull(list[0].currencies)
        assertNotNull(list[0].dexes)
        assertNotNull(list[0].fromTime)
        assertNotNull(list[0].fromTimeAsString)
        assertNotNull(list[0].makerTrades)
        assertNotNull(list[0].takerTrades)
        assertNotNull(list[0].tillTime)
        assertNotNull(list[0].tillTimeAsString)
        assertNotNull(list[0].typeAsString)
    }

    @Test
    fun `valid with protocol`() {
        val protocols = listOf("IDEX")
        val list = api.dex.tradesActive(protocols)
        assertNotNull(list)
        assertFalse(list.isEmpty())
        assertFalse(list[0].isEmpty())
    }

    @Test
    fun `valid with contracts`() {
        val contracts = listOf("0x2a0c0dbecc7e4d658f48e01e3fa353f44050c208")
        val list = api.dex.tradesActive(dexContracts = contracts, timeSpanDays = 800)
        assertNotNull(list)
        assertFalse(list.isEmpty())
        assertFalse(list[0].isEmpty())
    }

    @Test(expected = ParamException::class)
    fun `invalid dex contract`() {
        val contracts = listOf("0xa0c0dbecc7e4d658f48e01e3fa353f44050c208")
        val list = api.dex.tradesActive(dexContracts = contracts)
        assertNotNull(list)
        assertTrue(list.isEmpty())
    }

    @Test
    fun `empty dex contract not exist`() {
        val contracts = listOf("0x1a0c0dbecc7e4d658f48e01e3fa353f44050c208")
        val list = api.dex.tradesActive(dexContracts = contracts)
        assertNotNull(list)
        assertTrue(list.isEmpty())
    }

    @Test
    fun `dex protocol not exist`() {
        val protocols = listOf("IDEXIA")
        val list = api.dex.tradesActive(protocols)
        assertNotNull(list)
        assertTrue(list.isEmpty())
    }

    @Test
    fun `valid with protocol and contract`() {
        val contracts = listOf("0x2a0c0dbecc7e4d658f48e01e3fa353f44050c208")
        val protocols = listOf("IDEX")
        val list = api.dex.tradesActive(protocols, contracts)
        assertNotNull(list)
        assertFalse(list.isEmpty())
        assertFalse(list[0].isEmpty())
    }
}