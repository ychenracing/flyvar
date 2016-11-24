package cn.edu.fudan.iipl.flyvar.service;

import java.util.Collection;
import java.util.List;

import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.constants.RemoveDispensableType;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;

public interface FilterService {

    /**
     * filter variations not exist in dbType variation database.
     * @param variations
     * @param dbType
     * @return filtered variations all exist in database.
     */
    public List<Variation> filterVariations(Collection<Variation> variations,
                                            VariationDataBaseType dbType);

    /**
     * async filter variations not exist in dbType variation database. 
     * If removeDispensable is yes, then filter dispensable gene variations. 
     * At last, result variations will write to a file and send a email to user to download.
     * 
     * @param variations
     * @param dbType
     * @param removeDispensable
     * @param receiver
     */
    public void asyncFilterAndSendEmail(Collection<Variation> variations,
                                        VariationDataBaseType dbType,
                                        RemoveDispensableType removeDispensable, String receiver);

    /**
     * async filter variations not exist in dbType variation database and annotate them. 
     * If removeDispensable is yes, then filter dispensable gene variations. 
     * At last, result variations will write to a file and send a email to user to download.
     * 
     * @param variations
     * @param dbType
     * @param removeDispensable
     * @param receiver
     */
    public void asyncFilterAnnotateAndSendEmail(Collection<Variation> variations,
                                                VariationDataBaseType dbType,
                                                RemoveDispensableType removeDispensable,
                                                String receiver);

    /**
     * filter variations exist in dispensable genes.
     * @param variations
     * @return
     */
    public List<Variation> filterDispensableGeneVariations(Collection<Variation> variations);

}
