parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header operating_hours operating_rule range entity+ entity_group* rules?;
header          : HEADER_START TEXT ENDLINE;
operating_hours : OPERATING_HOURS_START TIME TO TIME ENDLINE;
operating_rule  : OPERATING_RULE_START (OPERATING_RULE_1 | OPERATING_RULE_2) ENDLINE;
range           : RANGE_START ((NUM RANGE_NUM_DAYS_MID DATE) | (DATE RANGE_DATE_DATE_MID DATE)) ENDLINE;
entity          : ENTITY_START name ENDLINE;
entity_group    : ENTITY_GROUP_START name ENTITY_GROUP_MID name+ ENDLINE;
name            : TEXT;

timeunit        : (DAY | WEEK | MONTH | YEAR);
days_of_week    : (MON | TUES | WED | THUR | FRI | SAT | SUN);

rules           : RULES_START schedule_rule+ ENDLINE;
schedule_rule   : (schedule | availability | frequency | overlap | ratio) ENDLINE;
schedule        : SCHEDULE_START name (specific_days | min_max_avg_days);
specific_days   : ON (specific_days_by_date | specific_days_by_days_of_week ) REPEAT NUM TIMES;
specific_days_by_date: DATE FROM TIME TO TIME;
specific_days_by_days_of_week: (days_of_week+ TERMINAL | ALL_DAYS) FROM TIME TO TIME;
min_max_avg_days: ((MANDATORY_MIN | MANDATORY_MAX | MANDATORY_AVG) (function | NUM) HOURS_PER timeunit)+;
availability    : AVAILABILITY_START name ON (specific_days_by_date | specific_days_by_days_of_week) REPEAT NUM TIMES;
frequency       : FREQUENCY_START name FREQUENCY_CANNOT_BE_SCHEDULED (FREQUENCY_MORE_THAN (function | NUM) FREQUENCY_DAYS_IN_ROW) | ON (days_of_week+ TERMINAL | ALL_DAYS);
overlap         : OVERLAP_START name name;
ratio           : RATIO_START (function | NUM) OF TEXT RATIO_OPERATOR (function | NUM) OF TEXT;

function        : FUNCTION_PREFIX math+;
math            : exp (MATH_OPERATOR+ math)?;
exp             : VAR | NUM;



// MATH_OPERATIONS: EXP ([+,-,/,*,sin,cos,tan,log,ln,^]+  MATH_OPERATIONS)?;

// EXP: VAR | NUM?;

// VAR: ‘t’;
// FUNCTION: ‘h(t)=’ MATH_OPERATIONS+;