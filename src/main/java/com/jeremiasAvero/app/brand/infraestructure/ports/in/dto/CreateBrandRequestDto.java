package com.jeremiasAvero.app.brand.infraestructure.ports.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CreateBrandRequestDto {
    @NotBlank @Size(max = 200)
    private String name;

    public @NotBlank @Size(max = 200) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 200) String name) {
        this.name = name;
    }
}
