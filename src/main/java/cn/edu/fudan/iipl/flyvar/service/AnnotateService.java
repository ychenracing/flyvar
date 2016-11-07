package cn.edu.fudan.iipl.flyvar.service;

import java.nio.file.Path;
import java.util.Collection;

import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;

/**
 * annotate service
 * 
 * @author yorkchen
 * @since 2016-11-08 18:00:00
 */
public interface AnnotateService {

    /**
     * Annotate variation file in vcf format. The variation file to annotate must be put in the
     * directory of annovar's annotationPath.
     * 
     * @param vcfFormatVariationPath
     *            path to variation file in vcf format
     * @return vcfFormatVariationPath. If vcfFormatVariationPath is "/usr/local/variation.vcf", then
     *         we can achieve result files below through variationPath:
     *         <ul>
     *         <li>/usr/local/variation.vcf.avo_input.txt</li>
     *         <li>/usr/local/variation.vcf.avo_input.txt.invalid_input <b>if it exists</b></li>
     *         <li>/usr/local/variation.vcf.avo_input.txt.exonic_variant_function</li>
     *         <li>/usr/local/variation.vcf.avo_input.txt.log</li>
     *         <li>/usr/local/variation.vcf.avo_input.txt.variant_function</li>
     *         <li>/usr/local/variation.vcf_out</li>
     *         </ul>
     */
    public Path annotateVcfFormatVariation(Path vcfFormatVariationPath);

    /**
     * async annotate and merge annotate result, and send results via email to user. see {@link cn.edu.fudan.iipl.flyvar.service.AnnotateService#annotateVcfFormatVariation(Path) annotateVcfFormatVariation(Path)}
     * @param vcfFormatVariationPath
     * @param receiver receiver email address
     */
    public void asyncAnnotateVcfFormatVariation(Path vcfFormatVariationPath, String receiver);

    /**
     * Convert queryResultVariations to vcf file to prepare for annotating. This function will
     * generate a vcf file in annovar's annotationPath folder.
     * 
     * @param resultVariations
     * @return
     */
    public Path convertQueryResultVariationsToVcfFile(Collection<QueryResultVariation> resultVariations);

    /**
     * Convert variations to vcf file to prepare for annotating. This function will generate a vcf
     * file in annovar's annotationPath folder.
     * 
     * @param resultVariations
     * @return vcfFilePath. It is absolute path to the vcf file in annotationPath folder.
     */
    public Path convertVariationsToVcfFile(Collection<Variation> variations);

    /**
     * merge whole region annotation file and exon annotation file.
     * 
     * @param annovarInputFileName
     *            annovar input file name, it must be put in annovar's annotationPath folder.
     * @return
     */
    public Path mergeAnnotateResult(String annovarInputFileName);

}
