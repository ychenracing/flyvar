# flyvar#

FlyVar is a free integrative platform that addresses the increasing need of next generation sequencing data analysis in the Drosophila research community. It is composed of three parts.

1. a database that contains 5.94 million DNA polymorphisms found in Drosophila melanogaster derived from whole genome shotgun sequencing of 561 genomes of D. melanogaster. It shows an average of one variant per 20 bases across the genome.
2. a GUI interface has been implemented to allow easy and flexible query of the database.
3. a set of interactive online tools enables filtering and annotation of genomic sequence obtained from individual D. melanogaster strains to identify candidate mutations.

FlyVar permits the analysis of next generation sequencing data without the need of extensive computational training or resources.  For more information please see [http://www.iipl.fudan.edu.cn/FlyVar](http://www.iipl.fudan.edu.cn/FlyVar).

## Reference##

\[1\] [Wang F, Jiang L, Chen Y, et al. FlyVar: a database for genetic variation in Drosophila melanogaster[J]. Database, 2015, 2015: bav079.](http://database.oxfordjournals.org/content/2015/bav079.long)

## Requirements

- MySQL 5.x
- Java 8+
- Tomcat 8.x+
- Perl 5.x+
- R 3.x+
- Works on Linux, Windows, Mac OSX

## Build##

### Maven###

You need maven to import this project and its dependencies. Import and use `maven install`, then it import its dependent libraries.

### Redis###

flyvar use redis as cache service. Since flyvar database contains huge data for variations, I suggest at least 1GB memory size for redis.

### MongoDB###

flyvar use MongoDB to store visit log and visit time.

### MySQL###

flyvar variation data SQL files will be provided after you send me an email to ask and I argee to offer.

### Tomcat###

Export `flyvar` to `flyvar.war`. Put `flyvar.war` in the folder `webapps`.

## Attention

- This is a **refactor** version of old [FlyVar](http://www.iipl.fudan.edu.cn/FlyVar).
- compared to old [FlyVar](http://www.iipl.fudan.edu.cn/FlyVar), this version:
  - improve code readability and stablitity
  - clearer project hierarchy
  - better experience and faster using cache
  - cross platforms, run annovar program on any Windows or Unix computer, adapt to all size devices

## Contact##

1. message me at [Yong Chen](https://www.facebook.com/yong.chen.5623293)
2. send an email to ychenracing@gmail.com

## Copyright##

Copyright©fudan.edu, bcm.edu.