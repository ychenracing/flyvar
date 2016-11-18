package cn.edu.fudan.iipl.flyvar.model.constants;

public class Constants {

    /** sample name列表缓存key */
    public static final String CACHE_SAMPLE_NAME_LIST                     = "CACHE_SAMPLE_NAME_LIST_";

    /** sample name Set缓存key */
    public static final String CACHE_SAMPLE_NAME_SET                      = "CACHE_SAMPLE_NAME_SET_";

    /** 数据库中是否有该variation的缓存key */
    public static final String CACHE_VARIATION_EXIST_IN_DB                = "CACHE_VARIATION_EXIST_IN_DB_";

    /** Dispensable gene数据库中是否有该variation的缓存key */
    public static final String CACHE_VARIATION_EXIST_IN_DISPENSABLE_GENE  = "CACHE_VARIATION_EXIST_IN_DISPENSABLE_GENE_";

    /** 多少sample name中有该有该variation的缓存key */
    public static final String CACHE_COUNT_SAMPLE_NAME_CONTAINS_VARIATION = "CACHE_QUERY_COUNT_SAMPLE_NAME_CONTAINS_VARIATION_";

    /** region对应的那些variations的缓存key */
    public static final String CACHE_REGION_VARIATIONS                    = "CACHE_QUERY_REGION_VARIATIONS_";

    /** whole region的gene name对应的那些variations的缓存key */
    public static final String CACHE_GENE_NAME_WHOLE_VARIATION_REGIONS    = "CACHE_GENE_NAME_WHOLE_REGION_VARIATION_REGIONS_";

    /** exon region的gene name对应的那些variations的缓存key */
    public static final String CACHE_GENE_NAME_EXON_VARIATION_REGIONS     = "CACHE_GENE_NAME_EXON_REGION_VARIATION_REGIONS_";

    /** sample文件内容对应的缓存key */
    public static final String CACHE_SAMPLE_VARIATIONS                    = "CACHE_SAMPLE_VARIATIONS_";

    /** sample文件对应的annotate缓存结果的缓存key */
    public static final String CACHE_SAMPLE_ANNOTATE_RESULT               = "CACHE_SAMPLE_ANNOTATE_RESULT_";

}
