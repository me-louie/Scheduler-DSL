parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header operating_hours operating_rule range entity+ entity_group* rules? ENDLINE;
header          : HEADER_START TEXT ENDLINE;
operating_hours : OPERATING_HOURS_START TIME TO TIME ENDLINE;
operating_rule  : OPERATING_RULE_START (OPERATING_RULE_1 | OPERATING_RULE_2) ENDLINE;
range           : RANGE_START ((NUM RANGE_NUM_DAYS_MID DATE) | (DATE RANGE_DATE_DATE_MID DATE)) ENDLINE;
entity          : ENTITY_START TEXT ENDLINE;
entity_group    : ENTITY_GROUP_START TEXT ENTITY_GROUP_MID TEXT+ ENDLINE;

timeunit        : (DAY | WEEK | MONTH | YEAR);
days_of_week    : (MON | TUES | WED | THUR | FRI | SAT | SUN);

rules           : RULES_START schedule_rule+ ENDLINE;
schedule_rule   : (schedule | availability | frequency | overlap | ratio ) ENDLINE;
schedule        : SCHEDULE_START TEXT (specific_days | min_max_avg_days);
specific_days   : ON (specific_days_by_date | specific_days_by_days_of_week ) FROM TIME TO TIME REPEAT NUM TIMES;
specific_days_by_date: DATE FROM TIME TO TIME;
specific_days_by_days_of_week: days_of_week+ TERMINAL | ALL_DAYS;
min_max_avg_days: ((MANDATORY_MIN | MANDATORY_MAX | MANDATORY_AVG) (function | NUM) HOURS_PER timeunit)+;
availability    : AVAILABILITY_START TEXT ON (DATE FROM TIME TO TIME | (days_of_week+ TERMINAL | ALL_DAYS) FROM TIME TO TIME REPEAT NUM TIMES);
frequency       : FREQUENCY_START TEXT FREQUENCY_CANNOT_BE_SCHEDULED (FREQUENCY_MORE_THAN (function | NUM) FREQUENCY_DAYS_IN_ROW) | ON (days_of_week+ | ALL_DAYS);
overlap         : OVERLAP_START TEXT TEXT;
ratio           : RATIO_START (function | NUM) OF TEXT RATIO_OPERATOR (function | NUM) OF TEXT;
// TODO: fix function
function        : FUNCTION_PREFIX (MATH_OPERATOR* (NUM | VAR)+ MATH_OPERATOR*)+ ENDLINE;