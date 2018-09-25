# OM 2.0 Release Notes

According to [Versioning of test suits](https://github.com/opengeospatial/cite/wiki/ETS-Names-and-Versioning#versioning-of-test-suites), the version of the OM2.0 should be revised to 0.x to indicate its beta status.

## ~~1.0~~ 0.1 
  * First release - all the conformance classes.
  * [#1](https://github.com/opengeospatial/ets-om20/issues/1) Cannot validate schema-aware schematron files
    * Due to SAXON license problem, we can't use SAXON to validate schema-aware schematrons. Instead of using the schema-element function in schematron files, we wrote our own schema-element feature to make it work.

## ~~1.1~~ 0.1
  * [#2](https://github.com/opengeospatial/ets-om20/issues/2) Release notes are missing on project website
  * Add conformance class configuration in config.xml
  * [#3](https://github.com/opengeospatial/ets-om20/issues/3) Improve name of test suite.
  * [#4](https://github.com/opengeospatial/ets-om20/issues/4) Rename ctl artifact created during built

## ~~1.2~~ 0.2
  * [#6](https://github.com/opengeospatial/ets-om20/issues/6) Add conformance class configuration into the ets-om20 test

## 0.3
 * Create Dockerfile
 * [#5](https://github.com/opengeospatial/ets-om20/issues/5) Review minimum conformance classes are present and documented
 * [#7](https://github.com/opengeospatial/ets-om20/issues/7) Wrong versioning of test in beta