# USE CASE: 2 Produce a Report on the number of populated countries based on a certain criteria
## CHARACTERISTIC INFORMATION
### Goal in Context
As a *locations advisor* I want *to produce a report on the top N populated countries in the world, continent and region where N is provided by the user* so that *I can support geographical reporting of the organisation.*
### Scope
Company.
### Level
Primary task.
### Preconditions
We know the role.  Database contains current population data.
### Success End Condition
A report is available for the locations advisor to provide to the user.
### Failed End Condition
No report is produced.
### Primary Actor
Locations Advisor.
### Trigger
A request for location information is sent to the Locations Advisor.
## MAIN SUCCESS SCENARIO
1. The user initiates the request to generate a report with the top N populated countries.
2. The system prompts the user to specify the criteria for the report, including:
Whether to consider countries worldwide, within a specific continent, or within a particular region.
The value of N, representing the number of countries to include in the report.
3. The user provides the requested criteria, including the desired value of N and the geographical scope (worldwide, continent-specific, or region-specific).
4. The system retrieves population data from the database based on the specified criteria.
5. The system ranks the countries based on their population size.
6. The system selects the top N populated countries according to the user's specified value of N.
7. The system generates a report containing:
The names of the selected countries.
The population size of each country.
8. The system presents the report to the user through the designated interface.
## EXTENSIONS
None.
## SUB-VARIATIONS
None.
## SCHEDULE
12/02/2024