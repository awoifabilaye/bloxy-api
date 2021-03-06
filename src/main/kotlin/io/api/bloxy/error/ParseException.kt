package io.api.bloxy.error


/**
 * Server response parse exception
 *
 * @author GoodforGod
 * @since 17.11.2018
 */
class ParseException(message: String?, cause: Throwable?) : BloxyException(message, cause)