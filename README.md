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
program         : entity+ entity_group* shift+ transformation*;
entity          : 'Entity' ENTITY_NAME ';'
entity_group    : 'Entity Group' ENTITY_GROUP_NAME ':' ENTITY_NAME (',' ENTITY_NAME)* ';';
name            : TEXT;

shift           : 'Shift' SHIFT_NAME 'is' DATE TIME '-' DATE TIME ('"' DESCRIPTION '"')? ';';
shift_group     : 'Shift Group' SHIFT_GROUP_NAME ':' SHIFT_NAME (',' SHIFT_NAME)* ;

set_operator    : 'AND' | 'OR' | 'XOR' | 'EXCEPT';
offset_operator : '<<' | '>>';

variable        : 'Var' VARIABLE_NAME ('=' (VARIABLE_NAME | NUM))? ';'?;
expression      : 'Expression' EXPRESSION_NAME '=' (VARIABLE_NAME | EXPRESSION_NAME | NUM) MATH (VARIABLE_NAME | EXPRESSION_NAME | NUM) ';'?;

transformation  : ((shift_group | apply | merge | loop | expression | variable) ';') |  ifthenelse;
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

The order of the program matters:
We need to write the entity first then entity groups then shifts and then transformations otherwise you might get errors due to the Parser. 

**Apply:** Applies a shift/shift group to an entity/entity group
  * Apply has optional OFFSET/REPEAT.
  * If OFFSET/REPEAT is specified, Apply applies the shift/shift group to an entity/entity group. Then, it will offset the shifts by the specified timeunit and apply them to the entities again REPEAT_TIMES.

**Merge:**// Merges shift_groups/other merges by set operations like union/intersection/XORS/compliments(the logical not) to other shift_groups or merges. 
    * So a shift group or a merge is combination of shifts
    * Based on the logical operator provided, we combine the two combinations of shifts. (Like for 
            instance Merge merge1: shiftgroup1 AND shiftgroup2 would result in an new shift group linked to merge1 that is an intersection between the two shiftgroups)
    * We can do recursion in merge that is line by line:
        So we can have :
                        Merge merge3: merge1 AND merge2;
                        Merge merge1: SG1 OR SG2;
                        Merge merge2: SG1 AND SG2;
                        Merge merge4: merge2 AND merge3;
        From the above example one you can see that it doesn't matter whether you define the merge you are going to use in the new merge before or after that new merge is defined. 
    * See ExampleMerge for a working example of various cases. 

**Variables/Expressions:**// User can only type in Integers. These are basically for normal math functions. So that you can insert a variable or expression as an offset instead of a direct number.
    * So Variables can be defined as numbers or other variables that were defined previously and the same variable can't be defined again like in Java:
        *So we can do Var x = 10;
            or we can do: Var y = 10; Var x = y;
            or we can do: Var x;(this sets var x to zero)
        *But we can't do Var x =10; Var y = 20; Var x=y;
    * The expressions are just so we can do mathematical operations(plus,minus,multiply,divide,power) between variables or integers or other expressions.
        *So we can do: Var x = 3; Var y = 10; Expression f1 = x + y; 
            or we can do: Expression f1 = 3 + y; or Expression f1 = x + 10;
            or we can : Expression f1 = 3 + 10;
        We can also do something like:    
                Var y = 10;            
                Expression f1 = 3 + f2;
                Expression f3 = f1 * f2;
                Expression f2 = y / 5;
    * Then we can put expression names or variable names or integers as offsets in apply or loop transformation: 
                        Apply SG1: Person4 | Offset: >> f1 MONTHS;
                        Apply SG2: Person5 | Offset: >> x days;
    See ExampleMath for use cases. 

**Loop:** Loop iterates through the entity group and applies shifts with offset by the specified timeunit.
  * Each person in the entity group will receive different shifts by an offset. 
  * Eg. First entity gets all the shifts, as-is. Second entity gets all `shifts+offset`. Third entity gets all `shifts+2*offset`, etc.
  * If REPEAT is specified, once Loop iterates through the entire entity group, it will go back to the first entity and continue applying the offset shifts REPEAT_TIMES
