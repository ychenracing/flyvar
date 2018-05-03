# flyvar

FlyVar is a free integrative platform that addresses the increasing need of next generation sequencing data analysis in the Drosophila research community. It is composed of three parts.

1. a database that contains 5.94 million DNA polymorphisms found in Drosophila melanogaster derived from whole genome shotgun sequencing of 561 genomes of D. melanogaster. It shows an average of one variant per 20 bases across the genome.
2. a GUI interface has been implemented to allow easy and flexible query of the database.
3. a set of interactive online tools enables filtering and annotation of genomic sequence obtained from individual D. melanogaster strains to identify candidate mutations.

FlyVar permits the analysis of next generation sequencing data without the need of extensive computational training or resources.  For more information please see [http://www.iipl.fudan.edu.cn/FlyVar](http://www.iipl.fudan.edu.cn/FlyVar).

## Requirements

- **Java 8+:** flyvar was written by Java8 with the most popular Java EE framework-Spring & Spring MVC. Since great features like IOC, AOP, Spring Security, Spring Task and so on, Spring can apparently improve the efficiency of J2EE project development. 
- **MySQL 5.x:** flyvar use MySQL to store vatiation data. All data tables were built indices, and transformed to MyISAM engine to provide faster query service.
- **Tomcat 8.x+:** flyvar use Tomcat as HTTP server, with default 150 occurrences.
- **Perl 5.x+:** annovar program was written by Perl. So flyvar rely on perl to provide annovar running environment. 
- **R 3.x+:** flyvar use R to combine annovar results. Exonic variant functions will be combined into variant function results.
- **Linux or Windows or Mac OSX:** flyvar is platform-independent. It still works fine when you put it on Linux, Windows or Mac OSX or Sierra.

## Build

### Maven

Use maven to import dependencies and build the project.

### Redis

flyvar use redis as cache service. Since flyvar database contains huge data for variations, I suggest at least 1GB memory size for redis.

### MongoDB

flyvar use MongoDB to store visit log and visit time.

### MySQL

To accelerate data processing, flyvar use MyISAM engine for all data tables. Variation data SQL files will be provided after you send me an email to ask and I argee to offer.

### Tomcat

flyvar use tomcat as http server. Export `flyvar` to `flyvar.war`. Put `flyvar.war` in the folder `webapps`.


## Attention

- This is a **refactor** version of old [FlyVar](http://www.iipl.fudan.edu.cn/FlyVar).
- compared to old [FlyVar](http://www.iipl.fudan.edu.cn/FlyVar), this version:
  - improve code readability and stablitity
  - clearer project hierarchy
  - better experience and faster data processing using cache
  - more safety
  - cross platforms, run annovar program on any Windows or Unix computer, adapt to all size devices

## Contact

1. message me at [Yong Chen](https://www.facebook.com/yong.chen.5623293)
2. send an email to ychenracing@gmail.com

## Reference

\[1\] [Wang F, Jiang L, Chen Y, et al. FlyVar: a database for genetic variation in Drosophila melanogaster[J]. Database, 2015, 2015: bav079.](https://academic.oup.com/database/article/2433212/FlyVar-a-database-for-genetic-variation-in)

## Copyright

Copyright©[fudan.edu](http://www.fudan.edu.cn), [bcm.edu](https://www.bcm.edu/).
