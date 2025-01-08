# OMXML 2.0 Release Notes

According to [Versioning of test suits](https://github.com/opengeospatial/cite/wiki/ETS-Names-and-Versioning#versioning-of-test-suites), the version of the OMXML 2.0 should be revised to 0.x to indicate its beta status.

## ~~1.0~~ 0.1 
  * First release - all the conformance classes.
  * [#1](https://github.com/opengeospatial/ets-omxml20/issues/1) Cannot validate schema-aware schematron files
    * Due to SAXON license problem, we can't use SAXON to validate schema-aware schematrons. Instead of using the schema-element function in schematron files, we wrote our own schema-element feature to make it work.

## ~~1.1~~ 0.2
  * [#2](https://github.com/opengeospatial/ets-omxml20/issues/2) Release notes are missing on project website
  * Add conformance class configuration in config.xml
  * [#3](https://github.com/opengeospatial/ets-omxml20/issues/3) Improve name of test suite.
  * [#4](https://github.com/opengeospatial/ets-omxml20/issues/4) Rename ctl artifact created during built

## 0.3
 * Create Dockerfile
 * [#5](https://github.com/opengeospatial/ets-omxml20/issues/5) Review minimum conformance classes are present and documented
 * [#7](https://github.com/opengeospatial/ets-omxml20/issues/7) Wrong versioning of test in beta
 * [#6](https://github.com/opengeospatial/ets-omxml20/issues/6) Add conformance class configuration into the ets-omxml20 test

## 0.4
 * [#12](https://github.com/opengeospatial/ets-omxml20/issues/12) Web Browser Interface must be updated
 * [#13](https://github.com/opengeospatial/ets-omxml20/issues/13) Review and update conformance classes

## 0.5 (2020-11-24)
 * [#15](https://github.com/opengeospatial/ets-omxml20/issues/15) Update documentation to use "OMXML 2.0" instead of "OM 2.0"
 * [#14](https://github.com/opengeospatial/ets-omxml20/issues/14) Update version of TEAM Engine dependency used by Docker Image
 * [#20](https://github.com/opengeospatial/ets-omxml20/issues/20) Ensure that test fails against non O&M XML files
 * [#16](https://github.com/opengeospatial/ets-omxml20/issues/16) Create unit tests for OMXML 2.0

## 1.0 (2021-02-19)
 * [#27](https://github.com/opengeospatial/ets-omxml20/issues/27) Prepare release 1.0
 * [#26](https://github.com/opengeospatial/ets-omxml20/pull/26) Bump xercesImpl from 2.11.0 to 2.12.1
 * [#18](https://github.com/opengeospatial/ets-omxml20/issues/18) Cleanup dependencies

## 1.1 (2022-03-31)
 * [#24](https://github.com/opengeospatial/ets-omxml20/issues/24) Add template to get an XML/JSON response via rest endpoint
 * [#30](https://github.com/opengeospatial/ets-omxml20/pull/30) Bump xercesImpl from 2.12.1 to 2.12.2
 * [#28](https://github.com/opengeospatial/ets-omxml20/pull/28) Set Docker TEAM Engine version to 5.4.1

## 1.2 (2025-01-08)

Attention: Java 17 and Tomcat 10.1 are required.

 * [#31](https://github.com/opengeospatial/ets-omxml20/issues/31) Migrate test suite to TEAM Engine 6 (Java 17)

