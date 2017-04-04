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

## Dependencies

```xml
<dependency>
	<groupId>commons-io</groupId>
	<artifactId>commons-io</artifactId>
	<version>2.5</version>
</dependency>
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>3.4</version>
</dependency>
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-dbcp2</artifactId>
	<version>2.1.1</version>
</dependency>
<dependency>
	<groupId>commons-logging</groupId>
	<artifactId>commons-logging</artifactId>
	<version>1.1.3</version>
</dependency>
<dependency>
	<groupId>javax.mail</groupId>
	<artifactId>mail</artifactId>
	<version>${javamail.version}</version>
</dependency>
<dependency>
	<groupId>javax.mail</groupId>
	<artifactId>javax.mail-api</artifactId>
	<version>${javamail-api.version}</version>
</dependency>
<dependency>
	<groupId>javax.validation</groupId>
	<artifactId>validation-api</artifactId>
	<version>${javax.validation.version}</version>
</dependency>
<dependency>
	<groupId>org.freemarker</groupId>
	<artifactId>freemarker</artifactId>
	<version>${freemarker.version}</version>
</dependency>
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-validator</artifactId>
	<version>${hibernate-validator.version}</version>
</dependency>
<dependency>
	<groupId>joda-time</groupId>
	<artifactId>joda-time</artifactId>
	<version>${joda-time.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-mongodb</artifactId>
	<version>${springdatamongo.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-redis</artifactId>
	<version>${springdataredis.version}</version>
</dependency>
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
	<version>${jedis.version}</version>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>${mysqlconnector.version}</version>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-core</artifactId>
	<version>${jackson.version}</version>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-databind</artifactId>
	<version>${jackson.version}</version>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-annotations</artifactId>
	<version>${jackson.version}</version>
</dependency>
<dependency>
	<groupId>com.google.guava</groupId>
	<artifactId>guava</artifactId>
	<version>${guava.version}</version>
</dependency>
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>${druid.version}</version>
</dependency>
<dependency>
	<groupId>org.aspectj</groupId>
	<artifactId>aspectjweaver</artifactId>
	<version>${aspectj.version}</version>
</dependency>
<dependency>
	<groupId>org.aspectj</groupId>
	<artifactId>aspectjrt</artifactId>
	<version>${aspectj.version}</version>
</dependency>
<dependency>
	<groupId>net.sf.ehcache</groupId>
	<artifactId>ehcache</artifactId>
	<version>${ehcache.version}</version>
</dependency>
<dependency>
	<groupId>taglibs</groupId>
	<artifactId>standard</artifactId>
	<version>${taglibs.version}</version>
</dependency>
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>jstl</artifactId>
	<version>${jstl.version}</version>
</dependency>
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>${junit.version}</version>
	<scope>test</scope>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webmvc</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-config</artifactId>
	<version>${springsecurity.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-web</artifactId>
	<version>${springsecurity.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-core</artifactId>
	<version>${springsecurity.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.security</groupId>
	<artifactId>spring-security-taglibs</artifactId>
	<version>${springsecurity.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context-support</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-test</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>aopalliance</groupId>
	<artifactId>aopalliance</artifactId>
	<version>${aopalliance.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-aop</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-beans</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-jdbc</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-expression</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-web</artifactId>
	<version>${springframework.version}</version>
</dependency>
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis-spring</artifactId>
	<version>${springmybatis.version}</version>
</dependency>
<dependency>
	<groupId>log4j</groupId>
	<artifactId>log4j</artifactId>
	<version>${log4j.version}</version>
</dependency>
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>${slf4j.version}</version>
</dependency>
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-log4j12</artifactId>
	<version>${slf4j.version}</version>
</dependency>
```

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
