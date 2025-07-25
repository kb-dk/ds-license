# Changelog
All notable changes to ds-license will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Added new /audit/ path in API 
- Added API GET request /audit/auditEntryById returning a single AuditLog by id
- Added API GET request /audit/auditEntriesByObjectId returning a list of all AuditLogs related to a specific object by object id.
- Added Audit Logging to all methods changing the configurations through UI or API
- Added and Changed  columns to the table AUDITLOG to accommodate the new API calls (**Remember to create new delta migrations for OPS**)

### Changed
- Deleted API request to delete multiple restricted IDs
- Moved storage method that is only used by unit tests to a storage subclass used by unittest. The methods are very destructive such as clearing all tables.

### Fixed



## [3.0.0](https://github.com/kb-dk/ds-license/releases/tag/ds-license-3.0.0) 2025-06-12
### Added
- Added client method for the APi endpoint: `rights/calculate`

### Changed
- Deletion of rights restrictions now returns the amount of records deleted instead of nothing.
### Fixed

## [1.5.3](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.5.3) 2025-03-05
### Added

- Removed auto generated DsLicenseClient class that was a blocker for better exception handling. All DsLicenseClient methods now only throws ServiceException mapped to HTTP status in same way calling the API directly.
- Bumped kb-util to v1.6.9 for service2service oauth support.
- Added injection of Oauth token on all service methods when using DsLicenseClient.
- Added DTO which contains all needed values for calculating rights.
- Added ddl scripts and storage methods for restricted IDs

### Changed
- Bumped SwaggerUI dependency to v5.18.2
- Bumped multiple OpenAPI dependency versions

### Fixed
- Fixed /api-docs wrongly showing petstore example API spec


## [1.5.2](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.5.2) 2025-01-07
### Changed
- Upgraded dependency cxf-rt-transports-http to v.3.6.4 (fix memory leak)

## [1.5.1](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.5.1) 2024-12-02
### Changed
- make all loggers static
- changed spammy log.info to log.debug and added information about user data.

## [1.5.0](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.5.0) 2024-09-10
### Changed
- Bumped kb-util version to improve YAML logging and remove double logging.


### Removed
- Removed non-resolvable git.tag from build.properties

### Added
- Enabled OAuth2 on module. Much is copy-paste from ds-image to see it working in two different modules.  Plans are to refactor common functionality out into kb-util/template projects.
No methods are defined to require OAuth yet!


## [1.4.2](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.4.2) 2024-06-11
- Added YAML property to disable all GUI: gui.adminGuiEnabled=true
- Added YAML property for cache refresh time : cache.reloadInSeconds=300
- Audit log will now show name and email from AD loaded by KeyCloak.
- Access to admin GUI will also required login via KeyCloak. AD realm is configured in the KeyCloak server. direct URL and clientSecret must be known my both KeyCloak and LicenseModule.

## [1.4.1](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.4.1) 2024-05-13
### Added
- Added git information to the status endpoint. It now delivers, deployed branch name, commit hash, time of latest commit and closest tag
- Bumb sb-parent to v.25
- Added 'integration' tag to some unittests.
- Support for dynamically updating values in OpenAPI spec through internal JIRA issue [DRA-139](https://kb-dk.atlassian.net/browse/DRA-139).
- Added sample config files and documentation to distribution tar archive. [DRA-415](https://kb-dk.atlassian.net/browse/DRA-415)
- Added POM profiles to control testing


### Changed 
- Changed parent POM and thereby nexus repository

### Fixed 
- Correct resolving of maven build time in project properties. [DRA-415](https://kb-dk.atlassian.net/browse/DRA-415)
- Switch to nested properties camelCase [DRA-431](https://kb-dk.atlassian.net/browse/DRA-431)


## [1.4.0](https://github.com/kb-dk/ds-license/releases/tag/ds-license-1.4.0) 2024-01-11
### Changed
- logback template changes

## [1.3.1]((https://github.com/kb-dk/ds-license/releases/tag/v1.0.0)) 2024-01-11
- Update exception throwing and catching throughout the service.
- Client jar not longer has logback.xml included. Using maven jar-plugin instead that can handle exclusions.
- Added service endpoints to webapp, which makes the service endpoints reachable.


## [1.3.0]((https://github.com/kb-dk/ds-license/releases/tag/v1.0.0)) 2023-12-05
### Changed
- General style of YAML configuration files, by removing the first level of indentation.


## [1.2.0] - 2023-11-30
### Added
- Ds-license client
- Option for caching to prevent DOS from image zooming calls.

### Changed
- Solr calls changed from GET to POST 


## [1.0.0] - YYYY-MM-DD
### Added

- Initial release of <project>


[Unreleased](https://github.com/kb-dk/ds-license/compare/v1.0.0...HEAD)
[1.0.0](https://github.com/kb-dk/ds-license/releases/tag/v1.0.0)
