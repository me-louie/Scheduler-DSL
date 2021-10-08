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
