package com.vandenbreemen.sim_assistant.api.message

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class ApplicationError(message: String?) : Exception(message) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(other !is ApplicationError)return false

        return this.message == other.message
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}