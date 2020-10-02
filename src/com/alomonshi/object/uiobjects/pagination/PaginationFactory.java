package com.alomonshi.object.uiobjects.pagination;

import com.alomonshi.configuration.ConfigurationParameter;

/**
 * Hendling pagination functions
 */
public class PaginationFactory {

    private Pagination pagination;

    /**
     * Cunstructor
     * @param pagination to be injected
     */
    public PaginationFactory(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Getting offset to fetched from database
     * @return offset
     */
    public int getOffset() {
        if (pagination != null) {
            if (pagination.getPageNumber() > 0 && pagination.getPageSize() > 0)
                return pagination.getPageNumber()* pagination.getPageSize();
            else return ConfigurationParameter.defaultPaginationOffset;
        }
        else
            return ConfigurationParameter.defaultPaginationOffset;
    }

    /**
     * Data size to be fetched from database
     * @return data size
     */
    public int getDataSize() {
        if (pagination != null) {
            if (pagination.getPageSize() > 0)
                return pagination.getPageSize();
            else
                return ConfigurationParameter.defaultPaginationPageSize;
        }else
            return ConfigurationParameter.defaultPaginationPageSize;
    }


    /**
     * Getting total page count
     * @return total page count
     */
    public int getTotalPageCount () {
        if (pagination.getPageSize() > 0) {
            float totalPageCount = (float) pagination.getTotalData()/pagination.getPageSize();
            return (int)Math.ceil(totalPageCount);
        }else
            return 0;
    }


}
