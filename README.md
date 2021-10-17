# Scheduler DSL
This DSL provides the user a flexible and powerful way of defining schedules for entities and entity groups. The language allows users to define schedules, modify a schedule
or group of schedule with operators, and apply schedules to entities in interesting ways. Our DSL supports conditional control flow, looping constructs, and the ability to
create user defined time functions. The DSL input is simply a .txt file. The DSL outputs an .ics file which can be imported into any major calendar software.

## Getting Started
External jar dependencies are located within the `scheduler/lib folder`.  

Example input files are located within the `scheduler/ExampleInputs` directory. You can load one of these example files or create your own. Run the program to generate the .ics file.

### Importing .ics to [Google Calendar](https://calendar.google.com/) 
0. (Optional) Create a new Google Calendar by pressing the **+ > Create new calendar** on the left-hand panel.
1. Open the Settings Menu.
2. In the left-hand tool bar select **"Import & export"**
3. Import the generated .ics file.
4. Select the calendar you wish to import your events to.
5. Select **Import**.
6. Return to the calendar view to view your imported events. Note, it may take a few moments for the imported events to appear.

## Grammar
```
program         : entity+ entity_group* shift+ shift_group* transformation*;
entity          : 'Entity' ENTITY_NAME ';'
entity_group    : 'Entity Group' ENTITY_GROUP_NAME ':' ENTITY_NAME (',' ENTITY_NAME)* ';';
name            : TEXT;

shift           : 'Shift' SHIFT_NAME 'is' DATE TIME '-' DATE TIME ('"' DESCRIPTION '"')? ';';
shift_group     : 'Shift Group' SHIFT_GROUP_NAME ':' SHIFT_NAME (',' SHIFT_NAME)* ';';

set_operator    : 'AND' | 'OR' | 'XOR' | 'EXCEPT';
offset_operator : '<<' | '>>';

variable        : 'Var' VARIABLE_NAME ('=' (VARIABLE_NAME | NUM))? ';'?;
expression      : 'Expression' EXPRESSION_NAME '=' (VARIABLE_NAME | EXPRESSION_NAME | NUM) MATH (VARIABLE_NAME | EXPRESSION_NAME | NUM) ';'?;

transformation  : ((apply | merge | loop | expression | variable) ';') |  ifthenelse;
timeShiftUnits  : 'HOURS' | 'DAYS' | 'WEEKS' | 'MONTHS' | 'YEARS';
cond_transformations: transformation*;
apply           : 'Apply' (SHIFT_NAME | SHIFT_GROUP_NAME | MERGE_GROUP_NAME) ':' (ENTITY_NAME | ENTITY_GROUP_NAME) ('| Offset:' offset_operator (VARIABLE_NAME | EXPRESSION_NAME | NUM) timeShiftUnits '| Repeat:' NUM)?;
merge           : 'Merge' MERGE_GROUP_NAME ':' (SHIFT_GROUP_NAME | MERGE_GROUP_NAME) set_operator (SHIFT_GROUP_NAME | MERGE_GROUP_NAME);
loop            : 'Loop' (SHIFT_NAME | SHIFT_GROUP_NAME | MERGE_GROUP_NAME) ':' ENTITY_GROUP_NAME '| Offset:' offset_operator (VARIABLE_NAME | EXPRESSION_NAME | NUM) timeShiftUnits ('| Repeat:' NUM)?;
ifthenelse      : 'if' cond '{'
                        cond_transformations
                   '} else {'
                        cond_transformations
                   '}';
cond            : '(' SHIFT_GROUP_NAME set_operator SHIFT_GROUP_NAME ')';

ENTITY_NAME: TEXT;
ENTITY_GROUP_NAME: TEXT;
SHIFT_NAME: TEXT;
SHIFT_GROUP_NAME: TEXT;
MERGE_GROUP_NAME: TEXT;
VARIABLE_NAME: TEXT;
EXPRESSION_NAME: TEXT;

NUM: [0-9]+;
DATE: ('0'[1-9]|'1'[012])[- /.]('0'[1-9]|[12][0-9]|'3'[01])[- /.]('19'|'20')[0-9][0-9];
TIME:  ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]);
DESCRIPTION: ~["]+;

```
// TODO add more docs for Merge, function, etc.  
## Documentation

**Apply:** Applies a shift/shift group to an entity/entity group
  * Apply has optional OFFSET/REPEAT.
  * If OFFSET/REPEAT is specified, Apply applies the shift/shift group to an entity/entity group. Then, it will offset the shifts by the specified timeunit and apply them to the entities again REPEAT_TIMES.

**Merge:**// TODO

**Loop:** Loop iterates through the entity group and applies shifts with offset by the specified timeunit.
  * Each person in the entity group will receive different shifts by an offset. 
  * Eg. First entity gets all the shifts, as-is. Second entity gets all `shifts+offset`. Third entity gets all `shifts+2*offset`, etc.
  * If REPEAT is specified, once Loop iterates through the entire entity group, it will go back to the first entity and continue applying the offset shifts REPEAT_TIMES
