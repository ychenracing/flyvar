package cn.edu.fudan.iipl.flyvar.service;

import java.util.Collection;
import java.util.List;

import cn.edu.fudan.iipl.flyvar.model.Variation;
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
     * filter variations exist in dispensable genes.
     * @param variations
     * @return
     */
    public List<Variation> filterDispensableGeneVariations(Collection<Variation> variations);

}
