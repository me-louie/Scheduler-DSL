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
