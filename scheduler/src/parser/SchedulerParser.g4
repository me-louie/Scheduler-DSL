parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header operating_hours operating_rule range entity+ entity_group* rules? ENDLINE;
header          : HEADER_START TEXT ENDLINE;
operating_hours : OPERATING_HOURS_START TIME TO TIME ENDLINE;
operating_rule  : OPERATING_RULE_START (OPERATING_RULE_1 | OPERATING_RULE_2) ENDLINE;
range           : RANGE_START ((NUM RANGE_NUM_DAYS_MID DATE) | (DATE RANGE_DATE_DATE_MID DATE)) ENDLINE;
entity          : ENTITY_START TEXT ENDLINE;
entity_group    : ENTITY_GROUP_START TEXT ENTITY_GROUP_MID TEXT+ ENTITY_GROUP_END ENDLINE;

rules           : RULES_START rule+ ENDLINE;
rule            : (schedule | availability | frequency | overlap | ratio ) ENDLINE;
schedule        : SCHEDULE_START TEXT (specific_days | min_max_avg_days);
specific_days   : ON (DATE FROM TIME TO TIME | (DAY+ | ALL_DAYS FROM TIME TO TIME (REPEAT NUM TIMES)?));
min_max_avg_days: (MANDATORY_MIN (FUNCTION | NUM) HOURS_PER TIMEUNIT)? (MANDATORY_MAX (FUNCTION | NUM) HOURS_PER TIMEUNIT)? (MANDATORY_AVG (FUNCTION | NUM) HOURS_PER TIMEUNIT)?;
availability    : AVAILABILITY_START TEXT ON (DATE FROM TIME TO TIME | (DAY+ | ALL_DAYS) FROM TIME TO TIME REPEAT NUM TIMES);
frequency       : FREQUENCY_START TEXT FREQUENCY_CANNOT_BE_SCHEDULED ((FREQUENCY_MORE_THAN (FUNCTION | NUM) FREQUENCY_DAYS_IN_ROW) | (ON DAY+ | ALL_DAYS));
overlap         : OVERLAP_START TEXT TEXT;
ratio           : RATIO_START (FUNCTION | NUM) TEXT RATIO_OPERATOR TEXT;

function        : FUNCTION_PREFIX (MATH* (NUM | VAR)+ MATH*)+ ENDLINE;