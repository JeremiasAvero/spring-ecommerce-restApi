package com.jeremiasAvero.app.order.domain;

public enum OrderStatus {
    NEW,                        // creada desde carrito, aún sin intentar pago
    PENDING_PAYMENT,            // redirigido a PSP
    PAID,                       // pago acreditado
    CANCELLED,                  // cancelada por usuario/sistema
    REFUNDED                    // reembolsada total
}
