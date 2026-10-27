package com.igav.igav_project.Model.Entity.Inventory.SockLot;


import com.igav.igav_project.Model.Shared.ValueObjects.Moneda;
import java.math.RoundingMode;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public record StockBalance(
    int quantity,
    @Embedded
    Moneda averageCost,
    @Embedded
    Moneda totalValue
) {

    public StockBalance applyEntry(int inQty, Moneda inUnitCost) {
        int newQty = this.quantity + inQty;

        // Calculamos el valor agregado multiplicando la moneda de entrada por la cantidad ingresada
        Moneda addedValue = inUnitCost.multiply(inQty);

        // Se suman ambos valores utilizando el método plus()
        Moneda newTotalValue = this.totalValue.plus(addedValue);

        // Se calcula el costo promedio dividiendo el nuevo valor total entre la nueva cantidad total
        Moneda newAvgCost = newTotalValue.divide(newQty, 4, RoundingMode.HALF_UP);

        return new StockBalance(newQty, newAvgCost, newTotalValue);
    }

    public ExitResult applyExit(int outQty) {
        // Se calcula el costo descargado multiplicando la moneda del costo promedio por la cantidad que sale
        Moneda costoDescargado = this.averageCost.multiply(outQty);

        int newQty = this.quantity - outQty;

        // Se calcula el nuevo valor total remanente
        Moneda newTotalValue = this.averageCost.multiply(newQty);

        StockBalance nuevo = new StockBalance(newQty, this.averageCost, newTotalValue);

        return new ExitResult(nuevo, costoDescargado);
    }

    // Record anidado auxiliar para representar la tupla de retorno (StockBalance, Moneda)
    public record ExitResult(
        StockBalance nuevo,
        Moneda costoDescargado
    ) {}
}