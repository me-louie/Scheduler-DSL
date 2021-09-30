parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header operating_hours operating_rule range entity+ entity_group* rules? ENDLINE;
header          : HEADER_START TEXT ENDLINE;
operating_hours : OPERATING_HOURS_START TIME TO TIME ENDLINE;
operating_rule  : OPERATING_RULE_START (OPERATING_RULE_1 | OPERATING_RULE_2) ENDLINE;
range           : RANGE_START ((NUM RANGE_NUM_DAYS_MID DATE) | (DATE RANGE_DATE_DATE_MID DATE)) ENDLINE;
entity          : ENTITY_START name entity_role? ENDLINE;
entity_group    : ENTITY_GROUP_START name ENTITY_GROUP_MID name+ ENDLINE;
name            : TEXT;
entity_role     : TEXT;


rules           : RULES_START rule2+ ENDLINE;
rule2            : (schedule | availability | frequency | overlap | ratio | mandatory) ENDLINE;
schedule        : SCHEDULE_START name ON ((DATE FROM TIME TO TIME) | (DAY+ FROM TIME TO TIME REPEAT NUM TIMES));
availability    : AVAILABILITY_START name FROM DATE TIME TO DATE TIME;
frequency       : FREQUENCY_START name FREQUENCY_CANNOT_BE_SCHEDULED ((FREQUENCY_MORE_THAN (function | NUM) FREQUENCY_DAYS_IN_ROW) | (ON DAY+));
mandatory       : MANDATORY_START name (MANDATORY_MIN (function | NUM) HOURS_PER TIMEUNIT)? (MANDATORY_MAX (function | NUM) HOURS_PER TIMEUNIT)? (MANDATORY_AVG (function | NUM) HOURS_PER TIMEUNIT)?;
overlap         : OVERLAP_START name name;
ratio           : RATIO_START (function | NUM) entity_role RATIO_OPERATOR entity_role;


function        : FUNCTION_PREFIX math+;
math            : exp (MATH_OPERATOR+ math)?;
exp             : VAR | NUM;



// MATH_OPERATIONS: EXP ([+,-,/,*,sin,cos,tan,log,ln,^]+  MATH_OPERATIONS)?;

// EXP: VAR | NUM?;

// VAR: ‘t’;
// FUNCTION: ‘h(t)=’ MATH_OPERATIONS+;
