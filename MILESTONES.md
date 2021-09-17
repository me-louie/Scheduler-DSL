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
