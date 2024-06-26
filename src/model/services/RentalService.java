package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

import java.time.Duration;

public class RentalService {
    private Double pricePerDay;
    private Double pricePerHour;
    private BrazilTaxService taxService;

    public RentalService(Double pricePerDay, Double pricePerHour, BrazilTaxService taxService) {
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
        this.taxService = taxService;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public BrazilTaxService getTaxService() {
        return taxService;
    }

    public void setTaxService(BrazilTaxService taxService) {
        this.taxService = taxService;
    }

    public void processInvoice(CarRental carRental) {
        //finds the difference in minutes between two dates
        double minutes = Duration.between(carRental.getStart(), carRental.getFinish()).toMinutes();
        //divide it by 60 to get the amount of hours in those minutes
        double hours = minutes / 60.0;
        //if <12h charge by hours and if >12h charge by days
        double basicPayment;
        if (hours <= 12.0) {
            //round up the number of hours ex: 4h10m becomes 5h
            basicPayment = pricePerHour * Math.ceil(hours);
        } else {
            basicPayment = pricePerDay * Math.ceil(hours / 24);
        }

        double tax = taxService.tax(basicPayment);
        carRental.setInvoice(new Invoice(basicPayment, tax));
    }
}
