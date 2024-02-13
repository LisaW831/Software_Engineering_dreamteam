# USE CASE: 4 Produce a Report on the number of populated cities based on a certain criteria
## CHARACTERISTIC INFORMATION
### Goal in Context
As an *locations advisor* I want *to produce a report on the top N populated cities in the world, continent, region, country or district where N is provided by the user* so that *I can support geographical reporting of the organisation.*
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
1. Employee requests population information from the Locations Advisor
2. Location Advisor obtains the specific continent, region country or district they want the information for
3. Locations Advisor extracts the population information from the database
4. Locations advisor provides report to the user
## EXTENSIONS
3. **Role does not exist**:
    1. Location advisor informs the employee that no role exists.
## SUB-VARIATIONS
None.
## SCHEDULE
**DUE DATE**: Release 2.0