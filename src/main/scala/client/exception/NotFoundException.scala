package com.innowise.covid
package client.exception

class NotFoundException(private val message: String = "") extends RuntimeException(message) {
  
}
