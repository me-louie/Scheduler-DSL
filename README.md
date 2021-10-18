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
### Grammar Documentation
#### Entity
An entity to be scheduled (e.g. employee, person, course). Entities may be referenced by their given name anywhere that ENTITY_NAME is present in other non-terminals.
```
Entity e1;  // defines an entity named e1
Entity e2;  // defines an entity named e2
```
#### Entity Group
A group of one or more entities.
```
Entity Group eg1: e1;      // defines an entity group named eg1 with 1 member: e1
Entity Group eg2: e1, e2;  // defines an entity group named eg2 with 2 members: e1 and e2
```
#### Shift
A date/time range for which an entity may be scheduled. The start date/time must be before or the same as the end date/time. Shifts may optionally include a description after the end date.

```
Shift S1 is 10/11/2021 09:00 - 10/11/2021 17:00
Shift S2 is 10/12/2021 09:00 - 10/12/2021 17:00 "Second day of work.. less exciting";
```
#### Shift Group
A group of one or more shifts.

```
Shift Group SG1: S1;
Shift Group SG2: S1, S2, S3;
```
#### Variable
A shorthand reference for a constant integer value. Variables may be defined as constants or other variables. If no value is provided a default value of 0 will be assigned.

```
Var x = 1;
Var y = x;  // y = 1
Var z;      // z = 0
```
#### Expression
A shorthand reference for a simple mathematical expression Expressions take two numerical arguments and one operator. These numerical arguments may be integers, variables, or other expressions. The operator argument is one of '+' for addition, '-' for subtraction, '/' for division, and '*' for multiplication. It is the user's responsibility to watch out for self-recursive expressions, these will cause the program to crash (e.g. `Expression e1 = e1 + 2`).

```
Var x = 1;
Expression e1 = 1 + x    // e1 = 2
Expression e2 = e1 * 2   // e2 = 4
Then we can put expression names or variable names or integers as offsets in apply or loop transformation:
Apply SG1: Person4 | Offset: >> f1 MONTHS;
Apply SG2: Person5 | Offset: >> x days;
See ExampleMath.txt for use cases. 
```
#### Set Operators
Operators used to perform set operations between two shift groups.
* AND - takes the intersection of two shift groups
* OR  - takes the union of two shift groups
* EXCEPT - takes the difference between the first and second shift groups
* XOR - takes the difference between the first and second shift groups as well as the difference between the second and first

```
Shift Group SG1: S1, S4;
Shift Group SG2: S1, S2, S3;

SG1 AND SG2      // S1
SG1 OR SG2       // S1, S2, S3, S4
SG1 EXCEPT SG2   // S1, S4
SG1 XOR SG2      // S2, S3, S4
```
#### Offset Operators
Operators used to offset shifts by some amount of time. A time unit and offset amount must always be specified with this operator.
* << - offset backwards in time
* \>\> - offset forwards in time

```
Shift S1 is 10/11/2021 09:00 - 10/11/2021 17:00

S1 << 1 DAYS  // returns a shift from 10/10/2021 09:00 - 10/10/2021 17:00
S1 >> 3 HOURS // returns a shift from 10/11/2021 12:00 - 10/11/2021 20:00
```
#### Apply
The basic operation for scheduling entities/entity groups. First argument can either be an entity or entity group. Second argument can either be a shift or shift group. Any combination of these 4 types is valid. An Offset and Repeat argument can optionally be specified. If specified the shift/shift group will be applied as is, then offset and re-applied NUM times. NUM must be a positive integer. The offset amount may be any integer, variable, or expression.
```
Entity e1;
Entity e2;
Entity Group eg1: e1, e2;

Shift S1 is 10/11/2021 09:00 - 10/11/2021 17:00
Shift S2 is 10/12/2021 09:00 - 10/12/2021 17:00
Shift Group SG1: S1, S2;

Apply S1: e1                                    // schedules e1 for S1
Apply S1: eg1                                   // schedules e1, e2 for S1
Apply SG1: e1                                   // schedules e1 for S1, S2
Apply SG1: eg1                                  // schedules e1, e2 for S1, S2
Apply SG1: eg1 | Offset: << 2 DAYS | Repeat: 3; // schedules e1, e2 for S1, S2, S1 << 2 Days, S2 << 2 days, S1 << 4 days, S2 << 4 days
```

#### Merge
Operator for creating a new shift group by performing set operations on two existing shift groups. Assigns the newly created shift group to the name provided in MERGE_NAME, which can then be referenced in any operation that takes a shift group, including other merges.

```
Shift S1 is 10/11/2021 09:00 - 10/11/2021 17:00
Shift S2 is 10/12/2021 09:00 - 10/12/2021 17:00
Shift S3 is 10/13/2021 09:00 - 10/13/2021 17:00

Shift Group SG1: S1, S2;
Shift Group SG2: S2, S3;

Merge m1: SG1 AND SG2      // creates a new shift group named m1 composed of S2
Merge m2: SG1 OR SG2       // creates a new shift group named m2 composed of S1, S2, S3
Merge m3: SG1 EXCEPT SG2   // creates a new shift group named m3 composed of S1
Merge m4: m1 XOR SG2       // creates a new shift group named m4 composed of S3
Merge m5: m2 EXCEPT m4     // creates a new shift group named m5 composed of S1, S2

See ExampleMerge.txt for a working example of various cases. 
```

#### Loop
Operator for scheduling members of entity groups using a loop. Takes in as first argument an entity group, individual entities are invalid. Takes in as second argument either a shift or shift group. Takes as third argument an Offset followed by an optional Repeat argument. Iterates through the entity group and applies shifts with offset. Each person in the entity group will receive the same shifts with an increasing offset (i.e. first entity gets all the shifts with no offset, second entity gets all shifts + offset, third entity gets all shifts + (2*offset), etc). If the Repeat argument is provided once the entire entity group has been iterated through we loop back to the first entity and continue applying the offset shifts. The entire entity group list will be iterated through NUM times. NUM must be a positive integer.

```
Entity e1;
Entity e2;
Entity Group eg1: e1, e2;

Shift S1 is 10/11/2021 09:00 - 10/11/2021 17:00
Shift S2 is 10/12/2021 09:00 - 10/12/2021 17:00
Shift Group SG1: S1, S2;

Loop SG1: eg1 | Offset: >> 1 DAYS;             // schedules e1 for S1, S2
                                               // schedules e2 for S1 >> 1 day, S2 >> 1 day

Loop S1: eg1 | Offset: >> 1 DAYS | Repeat: 2;  // schedules e1 for S1
                                               // schedules e2 for S1 >> 1 day
                                               // schedules e1 for S1 >> 2 days
                                               // schedules e2 for S1 >> 3 days
```
#### IfThenElse
Conditional logic that can be used to wrap transformations. Will lead to either the 'if' or 'else' block of transformations being executed depending on the provided cond argument. The provided cond must be a set operation between two existing shift groups (including those created as the result of a merge). The cond with evaluate to true if the result of the set operation is a non-empty set, false otherwise. Both if and else blocks may be empty.

```
Entity e1;
Entity e2;
Entity Group eg1: e1, e2;

Shift S1 is 10/11/2021 09:00 - 10/11/2021 17:00
Shift S2 is 10/12/2021 09:00 - 10/12/2021 17:00
Shift S2 is 10/13/2021 09:00 - 10/13/2021 17:00
Shift Group SG1: S1, S2;
Shift Group SG2: S2, S3

if (SG1 AND SG2) {   // SG1 AND SG2 = S2, evaluates to true
    Apply S1: e1;    // will be executed
} else {
    Apply S2: e1;    // will not be executed
}
```