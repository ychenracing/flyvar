package cn.edu.fudan.iipl.flyvar.service;

import java.nio.file.Path;

/**
 * annotate service
 * @author yorkchen
 * @since 2016-11-08 18:00:00
 */
public interface AnnotateService {

    /**
     * annotate variation file in vcf format.
     * @param variationPath path to variation file in vcf format
     * @return variationPath. If variationPath is "/usr/local/variation.vcf", 
     * then we can achieve result files below through variationPath:
     * <ul>
     * <li>/usr/local/variation.vcf.avo_input.txt</li>
     * <li>/usr/local/variation.vcf.avo_input.txt.invalid_input <b>if it exists</b></li>
     * <li>/usr/local/variation.vcf.avo_input.txt.exonic_variant_function</li>
     * <li>/usr/local/variation.vcf.avo_input.txt.log</li>
     * <li>/usr/local/variation.vcf.avo_input.txt.variant_function</li>
     * <li>/usr/local/variation.vcf_out</li>
     * </ul>
     */
    public Path annotateVariationVcfFormat(Path variationPath);

}
