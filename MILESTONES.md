## Milestone 5

### Progress Summary
This week the team conducted a second round of user studies and also finished the implementation of the DSL. New 
features from which we added include: implementing looping logic to schedule an entire group, implementing if/else 
logic for conditional scheduling, a XOR set operator, and the ability to add custom user defined functions. Upon 
receiving additional feedback from our TA, we also added a small feature which allows users to add descriptions 
to their events.

### Planned Timeline for Remaining Days
The team plans to complete the video presentation on Friday, October 15th. Over the weekend, we will continue to 
refactor our code for readability and complete additional testing in preparation for the final project submission. We
will create a README file which will describe how to use our DSL and prepare several example inputs demonstrating our
range of features.  

### Results of Final User Studies
There was a big improvement in usability from the first user study, because we pared the grammar down from many simple,
unintuitive rules to a few useful but complex rules. They thought it was cool that it actually outputted a file that 
could be used in actual calendar apps and that made it seem much more useful.  

Both users were confused by what we called the bitwise operator, which was used to move the start of a shift by a 
number of days. The confusion was both due to the name, as the only connection to the bitwise operator is the symbol 
<< and also due to the unintuitiveness of the grammar, which didn’t make it clear how the shift was shifted and what 
unit of time. As a result, we changed the name of the operator and added to the grammar the ability to move shifts by 
NUM TIMEUNITS, (ex. 2 days, 6 hours, etc). This will help with both useability and flexibility of the language.  

User 3 mentioned that having some sort of conditional would be useful, especially in real world use cases. It would also 
reduce repetition and the number of shift groups he had to make, by offering more specificity for scheduling shifts and 
groups. One of the stretch goals we had discussed was an if/else. As a result of this feedback, we put it at the top of 
the list and we were able to implement if/else in the DSL.  

Another issue User 3 had was shifts being linked to a specific day. He thought there were many more real world use cases 
for shifts that were only a block of time or linked to a day of the week. This is something we would consider changing 
if we had more time, but linking shifts to a specific day makes the grammar much more simple, because users don’t have 
to input dates repetitively and handling day of the week and date also caused confusion and an increase in formatting 
issues with the first user group. We chose simplicity over expressibility in this case.  

User 4 wanted to do merges on entity groups as well as shift groups, which is a logical extension of our grammar and 
would be a good next feature to implement if we continued with this project. She also wanted the ability to create and 
assign entities to a shift group on the same line and/or create multiple entities on the same line. Both of these would
be good features to implement if we continued with this project.  

### User Studies

**User Study 3**    
**Task:**
Use the above DSL to create a schedule that does the following:

* Create six employees, two who are cooks, two who are waiters, 1 manager, and 1 maitred
* Creates three shifts (12-4, 4-8, 8-12) on october 1 and two shifts (4-8, 8-12) on Oct 2.  
Schedule:  
  * 1 waiter for the 4-8 and 8-12 shifts,
  * 2 cooks for the 12-4 and 4-8 shifts
  * 1 manager for all of the shifts.
  * Repeat the shifts every week for a month. Waiters should all get similar numbers of shifts

**Summary:**  
User 3 is a project manager at a software company. He has programming experience and manages a small team.

User 3 was able to pick up and use the grammar quicker than users in the first study, due to a shift in focus to fewer 
commands but more complexity in the commands. Formatting was generally fine, although he tried to create an Entity with
white space in the name. He relied heavily on copying and editing from examples.   

He found the use of terms like ‘logical operator’ and ‘bitwise operator’ was not consistent with normal use of those 
terms and was confusing, especially ‘bitwise operator.’ Like the other user study found, it was not easy to understand 
what the ‘bitwise operator’ would do and referencing shifting bits made it worse. Renaming it to ‘shift by’ DAYS or 
‘shift by’ HOURS could improve this. An easy change would be to make it more clear that any time Entity is used,
Entity Group can also be used.  

His main opinion was that shifts should not be linked to a specific day. Being able to assign a group of people a
specific shift and repeat that was very useful. For example, assign all cooks to a shift from 0800-1200 every Monday 
or assigning waiters to a shift from 1700-200 for the next three days was a useful real world case, but having to link 
that to a specific date was frustrating.  
	
He also mentioned that having an if statement would be really useful, which is something we had discussed as a possible
feature. Being able to include a shift or an entity based on a condition would help and seemed like a common use case.

**Comments from User 3:**
* Dates should be standard ISO format, why aren’t they ISO, etc?
* At least times are in 24hr clock, that’s good
* I want to apply logical operators to entities, for example Apply shift1 to cook1 AND cook2
* Can I just use Entity Groups for Entity, so I can apply a shift group to a entity group
* Does looping apply all shifts to each entity or 1 shift from group to each entity in group?
* No way to say I want multiple shifts to apply to a group and ALSO repeat and/or shift by days. This would help a lot.

**User Study 4**  
**Task:**
Use the above DSL to create a schedule that does the following:

* Create five employees, two of whom are waiters, two are cooks, and 1 is a manager
* Creates two shifts (8-12, 12-4) on october 1 and two shifts (8-12, 12-4, 4-8) on Oct 2.
* Schedule 1 employees to fill the above shifts and repeat every 7 days for different employees for 1 month
* Schedule 2 cooks for the 8-12 and 12-4 shifts
* Schedule 1 waiter for 12-4 and 4-8 shifts
* Schedule 1 manager on every shift

**Summary:**  
User 4 is a junior software dev. She also has prior experience scheduling employees in retail settings.

Overall, User 4  found the grammar easier to decipher and use than in previous iterations, but there was confusion about
how transformations works. The grammar is more straightforward now and users don’t have to distinguish between many
different rules, but figuring out what the output from a merge is and where to use it was not clear. Being able to
create multiple entities on the same line would improve it and that is one change we could make. Similarly, the ability
to assign entities to shift groups when they are created as well as add entities on a line would be a good improvement.

User 4 thought that the most straightforward way to make a schedule was to brute force it and assign every shift 
individually, but that was tedious, so the transformations seemed more promising. She originally wanted to create 
repeating shifts through loops and store it in a shift group which isn’t possible currently but makes sense as a
feature. Loop was the transformation that was the most useful and saved work, by taking out repetition. She also wanted 
to be able to do the merge action on entity groups which could be a good extension of our DSL. 
	
She also wanted a default shift group that referred to all shifts. 

Formatting issues were much less common than the previous user studies. Partly this is due to the examples being easier
to copy and alter, partly due to less rules and removing days of the week.  

Getting a file that is an actual calendar (in google calendar or other programs) at the end was cool and seemed useful.

**Comments from User 4**:
* What’s the date format? Which is month/day?
* Being able to create multiple entities on one line would be much nicer, esp. If we could assign groups at the same time that would be nice
* We previously had roles that could be assigned when creating an entity, is that easy to re-add?
* Creating entities on the same line seems like a possible easy addition
* Entity Amy, Bob, Clara;
* Do I have to create a shift group for all the shifts? It would be useful to be able to refer to ALL shifts automatically with a keyword
* How do I know if this will work?
* UI detail, not implementation, but documentation for users should include what happens if input is invalid
* What are merges for??? Can I use merge transformations on employee groups?
* Loop syntax would make more sense if it used more natural language


## Milestone 4

### Progress Summary
This week the team redefined the scope of our project in order to concentrate more on DSL aspects and less on the hard
problem of scheduling. Rather than focusing on user defined constraints, our scheduler will provide users with a variety
of ways to define schedules for entities and entity groups. Our language will allow users to define schedules, modify a
schedule or group of schedules with operators, and apply the schedules entities in interesting ways. These changes
involved redesigning some aspects of our grammar and creating new examples. We also continued with AST and Visitor
pattern implementation and backend validation.

### Updated Grammar
program: HEADER ENTITY+ ENTITY_GROUP* SHIFT+ SHIFT_GROUP* TRANSFORMATION* ';';  
header: ‘Title:’ TEXT ';  
entity: ‘Entity’ NAME ';';  
entity_group: ‘Make a group called’ NAME ‘composed of entities’ NAME+ ';';  

shift: ‘Shift:’ SHIFT_NAME IS DATE TIME '-' DATE TIME;  
shift_group: 'Shift Group' SHIFT_GROUP_NAME ':' SHIFT_NAME (COMMA SHIFT_NAME)* ';';  

logical_operator: 'AND' | 'OR' | 'XOR' ';' ;  
bitwise_operator: >> NUM | << NUM ';' ;  

transformations: apply | merge | loop ';' ;  
apply: 'Apply' (SHIFT_GROUP_NAME | MERGE_GROUP_NAME) 'to' NAME (bitwise_operator NUM)?;  
merge: 'Merge' MERGE_GROUP_NAME SHIFT_GROUP_NAME logical_operator (SHIFT_GROUP_NAME | '(' merge ')') ;  
loop: 'Loop' SHIFT_GROUP_NAME 'over' ENTITY_GROUP_NAME bitwise_operator NUM 'each person' ('and repeat' NUM 'times')?;  

TEXT: [a-zA-Z]+;  
NAME: TEXT;  
SHIFT_NAME: TEXT;  
SHIFT_GROUP_NAME: TEXT;  
COMMA: ',';  

TIME: ([01]?[0-9]|2[0-3]):[0-5][0-9];  

DATE: [0-2][0-9]\/[0-3][0-9]\/[0-9]{2}(?:[0-9]{2})?  
NUM: [0-9]+;  

### Example Program
Title: ExampleSchedule

Entity Person1;  
Entity Person2;  
Entity Person3;  
Entity Person4;  
Entity Person5;  
Entity Person6;  
Entity Person7;  

EntityGroup Make entity group called GROUPA composed of entities Person4 Person5 Person6;

Shifts:  
Shift S1 is 10/01/2021 TIME - 10/01/2021 TIME;  
Shift S2 is 11/01/2021 TIME - 11/01/2021 TIME;  
Shift S3 is 12/01/2021 TIME - 2/01/2021 TIME;  

Shift S4 is 15/02/2021 TIME - 15/02/2021 TIME;  
Shift S5 is 16/02/2021 TIME - 16/02/2021 TIME;  

Shift groups:  
Shift Group SG1: S1 S2 S3;  
Shift Group SG2: S4 S5;  

Apply SG1 to Person1;  
Apply SG2 to Person2;  

Merge SG1 AND SG2 to Person3;  
Merge SG1 XOR SG2 to Person7;  

Loop SG1 over GROUPA >> 2 each person and repeat 3 times;  

### Plan For Final User Study
Meg will conduct the final two or three user studies at the beginning of next week. At this point we should have enough
of the project implemented so that participants can try running their programs. This should leave us with 5-6 days to
incorporate any feedback we receive.

### Status of Implementation
Our Lexer, Parser, ParseTreeToAST, and OutputGenerator classes are complete. What remains is our SchedulerEvaluator
class. The implementation is underway and should be finished by the start of next week. At this point we’ll begin
incorporating user study feedback and implementing our stretch goals.

### Further Steps
Mikayla  
In the coming two weeks, I plan on continuing to implement the project and participate in code reviews. 
Specifically, I plan to contribute to the implementation of the SchedulerEvaluator and the team's stretch goals. 
Additionally, I will assist in creating the final video submission for the project.

Ben  
Next week I plan to implement the remainder of the evaluation logic in our SchedulerEvaluator class. Each visit method
will run some basic validation for a given node. Transformation nodes will have an additional evaluation step and take
up the bulk of the work. I’ll also be assisting with code review.

Andre  
For the remaining weeks, I plan to help with work on the evaluator, particularly implementing the maps for identifiers.
I also plan on doing more testing, especially once we are able to generate some output. If time permits, I’ll also help
with implementing any extra features to our DSL (ie. control flow).

Mohammad  
Plan for the next week till the deadline is to help out with building the evaluator, to make sure we get the desired
output. Making maybe a map to keep track of all our shifts,shift groups, entities, entity groups. Then helping with the
testing, user study, video and if we decide to add any extra features. Will work on those as well.

Meg  
I will design test cases for the new grammar. I will use the test cases to identify and fix bugs and user studies.
I am planning on conducting two to three user studies with a working version of the DSL by next Tuesday and I will
write up the results to share with the group. I will also help implement the evaluator, find and fix bugs,
and assist with the video.


## Milestone 3

### Progress Summary
This week, the team prepared a [User Study Guide](https://docs.google.com/document/d/1YBh7u4OPTQatA1pct4yJMZs1ZBvj0DWZOhsqIVHoBBE/edit?usp=sharing)
which introduced our DSL, grammar, presented several examples, and described the user study task and conducted several user
studies. Based on the user study feedback, we made changes to our grammar to improve consistency, flexibility,
readability, and to include some additional features. The team also began implementing the lexer, parser, and AST
nodes for our DSL.


### User Studies

#### User Study 1
User 1 is a BCS student in her final year, so she has programming experience as well as work experience in finance.

**Summary**   
Deciphering the draft grammar and how it worked was the main source of confusion. There’s a lot of information and it
wasn’t clear what parts were important. She missed the line in the summary about single quotes as literal text that 
can be used, so her first attempt just mimicked the language in the examples. The other big issue was entities,
entity groups, and roles. User1 treated roles and groups the same and was confused about specifying scheduling or
availability for roles or groups. The grammar as written seems to only apply rules to names, but the examples include
rules applying to a role. Example 4 also includes a role that is a group name and that’s confusing. There were a few 
formatting issues. A more thorough grammar explanation or changing the formatting to match what users intuitively use 
(if other users make similar mistakes) could help this. 
* Range of days uses ‘to’ but a range for hours uses a ‘-’
* ‘Weekend’ used as a specific term but isn’t defined in grammar
* ‘At most’/’at least’ used instead of ‘maximum’/’minimum’

**Comments from User 1:**
* Does the order of rules matter?
* Monday - Sunday is the same as all days, should be able to say all days
* What is OPERATING_RULE for and how is RULE related?
* Separating rules by employee type is very helpful, would make sense if they had to be grouped together

**After Task:**  
* What happens if/when I do something wrong? How are errors handled?
* What if the rules conflict and there’s an impossible schedule?
* Could I get multiple possible schedules to choose between?
* Examples don’t match up with grammar that well,
* Having a more standard input would help a lot (literally says that she would prefer a form…)
* Separating rules by employee type is very helpful, would make sense if they had to be grouped together
* Do rules automatically apply to all entities or have to be specific to role?
* It would be easier to use this to sort out a schedule for employees with conflicting needs/times than doing it by hand


#### User Study 2
User 2 is a BCS student in his final year. He has experience as a project manager overseeing a large team with multiple 
different groups.

**Summary**  
After the first user study, I adapted my tactics and spent more time explaining how the draft grammar was set up. 
User 2 was able to figure out the grammar and use it, but as a result, found some gaps and issues with the setup.
The draft grammar doesn’t allow users to make rules for ENTITY_ROLEs or ENTITY_GROUPs, but I believe that was our
plan (and it makes sense) so I told User 2 that he could use a role or group any place NAME was used. Example 4 uses
ALL to schedule all entities but it’s not defined in the grammar. User 2 said this was definitely a concept he wanted
to use. I also noticed that creating entities is very repetitive and we could either allow users to list names with the
same roles or set up some sort of loop here.  

The biggest issue was figuring out how to ensure there was a manager from 10-4 every day. If we can make rules specific
to roles or groups, I think it would be reasonable for the backend to choose between managers, and so a rule
“Managers must be scheduled from 10-4” would do this, but right now, the grammar doesn’t allow that. I’m not sure if
there’s a different way. In general, it was difficult to figure out which of these two rules should be used for a
specific situation and what format for time is relevant. I think (hope?) we can rewrite and possibly combine these?
```
FREQUENCY: NAME ‘cannot be scheduled (more than’ (NUM ‘days in a row’ | FUNCTION)) | (‘on’ DAY+ ‘days’);
NAME ‘must be scheduled’ (‘a minimum of’ FUNCTION ‘hours per’ TIMEUNIT ‘,’)? (‘a maximum of’ FUNCTION ‘hours per’ TIMEUNIT ‘,’)? (‘an average of’ FUNCTION ‘hours per’ TIMEUNIT)?
```

**Comments from User 2:**

* Does the order of rules matter? Can you put rules before title or operating hours?
* What’s the difference between an entity group and a role?
* WTF re: sin function. No manager would ever use that to schedule people.
* Scheduling managers from 10-4
  * Can you use an ‘at least’ for managers? This would ensure there is at least 1 manager between 10-4 but allow you to have managers working at other times  if they are available
* Used a ‘cheat’ to ensure only one manager at a time by saying ‘M1 cannot be scheduled with M2’ but this wouldn’t work with more than two managers.
  * Ratio rule didn’t occur to him and it’s complicated to figure out how that would work with the 10-4 restriction.
* He wanted to use an OR operator
* He would like to use a rule like this
  * Cashiers | Managers hours < 40 week
  * I think it’s partly that it’s not easy to understand what rules do generally, so finding the right rule to insert the right values is hard
* Availability rule is confusing. It would be convenient and very useful to be able to state that a specific employee is not available on Tuesdays in July or an employee is no available on Tuesdays between 12-3 or something like that but the current grammar limits it to specific dates.
* Generally, he thought it was pretty reasonable and straightforward, but it was missing ways to create rules for common situations.

### Planned Changes After User Studies
* Allow users to specify rules using days, eg. 'Monday', 'Tuesday' or dd/mm/yyyy. This would be closer to natural
language and also more flexible.
* Refactor the grammar to be more internally consistent, eg. use the same patterns and keywords.
* Collapse unnecessary complexity, eg. ENTITY_ROLE and ENTITY_GROUP can be represented by just the GROUP concept
* Add useful default constants such as ALL_DAYS (Monday - Sunday)

### Next Steps
* Make the grammar changes suggested by the user studies
* Implement DSL
  * AST nodes
  * Scheduling algorithm
  * Input/Output


## Milestone 2

### Progress Summary
This week, the team began drafting the grammar for our Scheduler DSL. The user inputs we defined are schedule length, 
schedule start, operating hours, list of entities (eg. people, courses) to be scheduled, restrictions on when entities 
can be scheduled, and restrictions on how they can interact. These restrictions form the different Rules that will be 
implemented by our DSL. We created several example programs based on our draft grammar. The team is still considering 
potential outputs for our DSL such as plain text describing when entities are scheduled, a calendar, or a table. 
We also prepared a roadmap to outline the remaining tasks for the project and assign responsibilities to team members.
Based on the TA’s feedback we might add more features after our user study and initial implementation to get more richness in the language.  

### Draft Grammar
// default mode  
PROGRAM: HEADER OPERATING_HOURS RANGE ENTITY+ ENTITY_GROUP* RULES?;
 
HEADER: ‘Title:’ TEXT;

OPERATING_HOURS: ‘Operating hours:’ BEGINNING, END;

BEGINNING: TIME;

END: TIME;

RANGE: ‘Schedule’ ((NUM ‘days starting’ DATE?) | (DATE ‘to’ DATE));

ENTITY: ‘Entity’ NAME ENTITY_ROLE?;

ENTITY_ROLE: TEXT;

ENTITY_GROUP: ‘Make a group called’ NAME ‘composed of entities’ NAME+;
 
NAME: TEXT;

NUM: [0-9]+;

DATE: [0-2][0-9]\/[0-3][0-9]\/[0-9]{2}(?:[0-9]{2})?;

TIME: ([01]?[0-9]|2[0-3]):[0-5][0-9] ‘-’ ([01]?[0-9]|2[0-3]):[0-5][0-9];

TIMEUNIT: ‘day’ | ‘week’ | ‘month’ | ‘year’;

DAY: ‘Monday’ | ‘Tuesday’ | ‘Wednesday’ | ‘Thursday’ | ‘Friday’ | ‘Saturday’ | ‘Sunday’;

// rules   
RULES: 'Rules:' RULE+;

RULE: SCHEDULE | AVAILABILITY | FREQUENCY | OVERLAP | RATIO;

SCHEDULE: ‘Schedule’ NAME (‘at’ DATE BEGINNING ‘to’ END | ‘on’ DAY+ ‘from’ BEGINNING ‘to’ END (‘repeat:’ NUM ‘times’)?);

AVAILABILITY: NAME ‘is unavailable’ DATE BEGINNING ‘to’ END;

FREQUENCY: NAME ‘cannot be scheduled (more than’ (NUM ‘days in a row’ | FUNCTION)) | (‘on’ DAY+ ‘days’);

NAME ‘must be scheduled’ (‘a minimum of’ FUNCTION ‘hours per’ TIMEUNIT ‘,’)? (‘a maximum of’ FUNCTION ‘hours per’ TIMEUNIT ‘,’)? 
(‘an average of’ FUNCTION ‘hours per’ TIMEUNIT)?;
 
OVERLAP: NAME ‘cannot be scheduled with’ NAME;

RATIO: NUM ENTITY_ROLE ‘to’ NUM ENTITY_ROLE;

FUNCTION: VAR ‘=’, MATH_OPERATIONS;

MATH_OPERATIONS: EXP ([+,-,/,*,sin,cos,tan,log,ln,^]+  MATH_OPERATIONS)?;

EXP: VAR | NUM?;

VAR: ‘t’;
FUNCTION: ‘h(t)=’ MATH_OPERATIONS+;
 
// text mode  
TEXT: [a-zA-Z ]+;

### Example Programs
1. Title: MySchedule;  
   Operating Hours: 09:00 - 17:00;  
   Schedule: 22/09/2021 to 30/12/2021;  
   Entity PersonA employee;  
   Entity PersonB employee;  
   Entity PersonC supervisor;
     
   Rules:  
   PersonA is unavailable 23/09/2021 11:00 to 12:00;  
   PersonB cannot be scheduled more than 5 days in a row;  
   PersonA cannot be scheduled with PersonB;  
   1 employee to 1 supervisor;  
   PersonA hours = 4sin(pi/2*hours) + 8;  

2. Title: SimplestSchedule;  
   Operating Hours: 09:00 - 17:00;  
   Schedule: 22/09/2021 to 23/09/2021;  
   Entity PersonA employee;  
   
3. Title: EmptySchedule;  
   Operating Hours: 09:00 - 09:00;  
   Schedule: 22/09/2021 to 22/09/2021;  
   Entity PersonA employee;
   
4. Title: CourseSchedule;  
   Operating Hours: 09:00 - 22:00;  
   Schedule: 08/09/2021 to 02/12/2021;  
   Entity ClassA CPSC;  
   Entity ClassB ECON;  
   Entity ClassC CPSC;  
   Make a group called CPSC composed of ClassA ClassB;
     
   Rules:  
   ClassA must be scheduled a minimum of 3 hours per week, a maximum of 3 hours per week;  
   CPSC must be scheduled an average of 3 hours per week;  
   ALL cannot be scheduled more than 1 days in a row;  
   ClassA cannot be scheduled with ClassB;  

### Roadmap and Responsibilities
[Spreadsheet](https://docs.google.com/spreadsheets/d/1LiARy015J-S_C487LCFtOOMhoyJ-KrihcjczxoUqaws/edit?usp=sharing)
outlining DSL implementation timeline and team member responsibilities.

### Next Steps
* Mock up language design
* Conduct first user study
* Analysis user study results
* Begin DSL implementation
 



## Milestone 1

### Description
* We plan to build a DSL that allows an individual to generate a schedule for multiple people based on a number of 
user-imposed restrictions. Making multiple individual schedules fit according to each person’s unique restrictions is 
both tedious and difficult when done by hand. Our DSL would allow a user to simply define entities (i.e. people) and 
the rules that govern their availability, leaving the difficult part of building a valid schedule to our program.

* Example use case: An employer who wants to generate a schedule for their workplace involving multiple employees with 
restrictions based on their availability and workplace needs. The employer would specify complicated rules for when 
each employee can work, how many hours they can work, who they can work with, as well as a desired schedule that 
must be filled (e.g. business must be staffed from 9-5 by at least 2 cashiers and 1 manager). Our program would then 
generate one—or possibly multiple—valid schedules based on their restrictions. 

### Motivation
* Our original idea was to create a DSL which would generate timesheets for the BC Wildlife Service—two of our group 
members had experienced firsthand how this task was a tedious and time consuming part of the job. After several 
iterations, we expanded our idea to create a flexible multi-person scheduler. Our motivation was that this tool would 
help users reduce manual input and errors when creating schedules. We envisioned many potential use cases for the DSL 
including scheduling employee work hours, patients to appointment times, course scheduling at a university, and so on.

### Changes from feedback
* Originally we pitched a DSL that would simplify filling out timesheets for a single use case, the BC Wildfire Service. 

* TA Yanze suggested we expand to include more complexity by moving beyond a simple form-based approach and 
an individual use case. He suggested we instead let users create their own rules for multiple use cases.

* We took his suggestion and proposed a timesheet that wasn’t bound to a single use case—it would instead allow a user 
to define rules specific for their use case. We also expanded the scope of the rules we would allow users to define. 
Instead of simply allowing them to define numerical fields like hours worked in terms of integers we would also allow 
them to define hours worked in terms of functions. A user could define the number of hours worked as a function of time 
so that their schedule could change as time goes on in interesting ways. Hours worked per day, h, could change 
according to the number of days from day 0 on the schedule, t. For example, instead of defining that a user would work 
8 hours per day (h(t) = 8) their hours worked could oscillate according to a sinusoidal function like 
h(t) = 4sin(pi/2\*t) + 8. This would correspond to 8 hours worked on day 0, 12 on day 1, 8 on day 2, 4 on day 3, and 
so on. We envisioned a DSL that would allow users to stack additional rules on top of this function to create even more 
interesting schedules (e.g. h(t) = 4sin(pi/2\*t) + 8 unless the day is a Saturday/Sunday, in which case h(t) = 0). 
Functions like this would allow an employer to evenly assign expected overtime hours to employees or plan for high 
volume weeks based on regular events, like product deliveries or biweekly code sprints.

* We found this second iteration was still too narrow so reframed the project from a timesheet generator to a scheduler. 
This scheduler would, in a sense, allow multiple timesheets to be coordinated together to produce a richer and more 
complicated schedule. 

* Professor Summers suggested we incorporate rich conflict resolution capabilities, which we’ve also added to our 
roadmap of features. If a user inputs conflicting rules our program could alert them and give them a rich set of 
options to solve the conflict.

### List of Features (in order of priority)
1. Allow user to schedule a single individual based on restrictions they give.
    * Restrictions/rules may include:
        * How many hours an individual could work (as either an integer or function of time)
        * When they could work (as either an integer or function of time)(user can use their own cool formulas like 
        maybe the fibonacci sequence to do overtime)
        
2. Schedule multiple users based on a number of conflicting rules that will be adhered to in order or priority. 
Restrictions on:
    * When each individual can work (which hours of the day, which days of the week), or if preferred a function that 
    defines how an individual’s availability changes throughout the week
    * Maximum number of regular/overtime hours an individual could work
    * Which individuals can work with which
    * The ratio of different employee roles (e.g. labourers to supervisors)
    
3. Allow user to define scope of rules—whether they apply to all, a single individual, or a specific subclass.
4. Resolve conflicting schedules the user inputs.
5. Repeating schedules (possibly mathematically so users can be creative and create any schedule they like).
6. Generate not just one but multiple solutions that the user could select between for rulesets that have larger.
solution sets.
7. Output information beyond the schedules, such as the cost in labour based on hourly rates provided for each employee.

### Next Steps
* Define the grammar of our DSL and include examples
* Define the specific rules/restrictions we want our DSL implement
* Define what the output of our DSL will be (e.g. calendar, text, Excel sheet, etc.)
* Planned division of responsibilities for next steps and timeline
* Summary update
