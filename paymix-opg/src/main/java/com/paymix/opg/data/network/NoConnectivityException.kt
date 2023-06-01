package com.paymix.opg.data.network

import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String?
        get() = "No Internet Connection !" //TODO Language

}