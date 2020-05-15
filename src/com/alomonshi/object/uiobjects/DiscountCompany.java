package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.Services;

import java.util.List;

public class DiscountCompany{

    private Company company;
    private List<Services> discountServices;

    public DiscountCompany(Company company, List<Services> discountServices) {
        this.company = company;
        this.discountServices = discountServices;
    }

    public Company getCompany() {
        return company;
    }
    
    public List<Services> getDiscountServices() {
        return discountServices;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setDiscountServices(List<Services> discountServices) {
        this.discountServices = discountServices;
    }
}
