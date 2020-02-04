package com.alomonshi.restwebservices.clientservices;
import com.alomonshi.datalayer.dataaccess.TableCompanyCategory;
import com.alomonshi.object.tableobjects.CompanyCategories;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/companyCategories")
public class CompanyCategoriesWebService{
    /**
     * Getting list of available categories
     * @return list of categories in a specified category type
     */
    @GET
    @Path("/getList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CompanyCategories> getCompanyCategoryList() {
        try{
            return TableCompanyCategory.getCompanyCategoryList(2);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}
