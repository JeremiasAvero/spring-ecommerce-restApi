package com.jeremiasAvero.app.order.infraestructure.in.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressDto {
    @NotBlank @Size(max=120) private String street;
    @NotBlank @Size(max=80)  private String city;
    @Size(max=80)            private String state;
    @Size(max=20)            private String zip;
    @NotBlank @Size(max=80)  private String country;

    public @NotBlank @Size(max = 120) String getStreet() {
        return street;
    }

    public void setStreet(@NotBlank @Size(max = 120) String street) {
        this.street = street;
    }

    public @NotBlank @Size(max = 80) String getCity() {
        return city;
    }

    public void setCity(@NotBlank @Size(max = 80) String city) {
        this.city = city;
    }

    public @Size(max = 80) String getState() {
        return state;
    }

    public void setState(@Size(max = 80) String state) {
        this.state = state;
    }

    public @Size(max = 20) String getZip() {
        return zip;
    }

    public void setZip(@Size(max = 20) String zip) {
        this.zip = zip;
    }

    public @NotBlank @Size(max = 80) String getCountry() {
        return country;
    }

    public void setCountry(@NotBlank @Size(max = 80) String country) {
        this.country = country;
    }
}