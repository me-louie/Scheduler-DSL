# Scheduler DSL
This DSL provides the user a flexible and powerful way of defining schedules for entities and entity groups. The language allows users to define schedules, modify a schedule
or group of schedule with operators, and apply schedules to entities in interesting ways. Our DSL supports conditional control flow, looping constructs, and the ability to
create user defined time functions. The DSL input is simply a .txt file. The DSL outputs an .ics file which can be imported into any major calendar software.

Example inputs are located: // TODO add path
## Grammar
// TODO Add grammar once it is finalized.

## Getting Started
Dependency are located within the scheduler/lib folder.  
Write your scheduling program in the Input.txt file.  
Run the program to generate the .ics file.  

### Importing .ics to [Google Calendar](https://calendar.google.com/) 
0. (Optional) Create a new Google Calendar by pressing the **+ > Create new calendar** on the left-hand panel.
1. Open the Settings Menu.
2. In the left-hand tool bar select "Import & export"
3. Import the generated .ics file.
4. Select the calendar you wish to import your events to.
5. Select Import.
6. Return to the calendar view to view your imported events. Note, it may take a few moments for the imported events to appear.
