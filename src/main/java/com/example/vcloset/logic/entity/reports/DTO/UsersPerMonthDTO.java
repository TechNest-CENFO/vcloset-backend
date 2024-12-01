package com.example.vcloset.logic.entity.reports.DTO;

public class UsersPerMonthDTO {
    private Integer year;
    private String month;
    private Long cant;

    public UsersPerMonthDTO(Integer year, String month, Long cant) {
        this.year = year;
        this.month = month;
        this.cant = cant;
    }

    // Getters and Setters
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCant() {
        return cant;
    }

    public void setCant(Long cant) {
        this.cant = cant;
    }
}
