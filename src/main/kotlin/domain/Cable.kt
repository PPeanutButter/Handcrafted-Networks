package domain

import domain.model.Port

abstract class Cable(
    open val endA: Port,
    open val endB: Port,
) {
    open fun disconnect() {}
}