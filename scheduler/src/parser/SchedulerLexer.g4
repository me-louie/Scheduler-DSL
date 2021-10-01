lexer grammar SchedulerLexer;
/// Default mode

HEADER_START: 'Title:' WS* -> mode(TEXT_MODE);
HEADER_END: 'End Title'; // or just ";"?
OPERATING_HOURS_START: 'Operating hours:' WS* -> mode(TIME_MODE);
OPERATING_RULE_START: 'Operating rule:' WS*;
OPERATING_RULE_1: 'All operating hours must be scheduled';
OPERATING_RULE_2:  'Entities can only be scheduled during operating hours, but not all scheduled hours must be used';
ENTITY_START: 'Entity' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_START: 'Make a group called' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_MID: 'composed of entities' WS* -> mode(TEXT_MODE);

RANGE_START: 'Range of schedule:' WS* -> mode(DATE_MODE);
RANGE_NUM_DAYS_MID :'days starting' WS* -> mode(DATE_MODE);

// added " date" to differentiate from lexer TO rule, make this sound nicer
RANGE_DATE_DATE_MID: 'to date' WS* -> mode(DATE_MODE);

DAY: 'day';
WEEK: 'week';
MONTH: 'month';
YEAR: 'year';

HOURS_PER: 'hours per';

RULES_START: 'Rules:' WS*;
SCHEDULE_START: 'Schedule' WS* -> mode(TEXT_MODE);
FREQUENCY_START: 'Frequency' WS* -> mode(TEXT_MODE);
FREQUENCY_CANNOT_BE_SCHEDULED: 'cannot be scheduled' WS*;
FREQUENCY_MORE_THAN: 'more than' WS* -> mode(NUM_MODE);
FREQUENCY_DAYS_IN_ROW: 'days in a row' WS*;
AVAILABILITY_START: 'Unavailable' WS* -> mode(TEXT_MODE);
OVERLAP_START: 'Cannot schedule together' WS* -> mode(TEXT_MODE);
RATIO_START: 'Ratio' WS* -> mode(NUM_MODE);
RATIO_OPERATOR: ':' WS* -> mode(NUM_MODE);
MANDATORY_START: 'Mandatory to schedule' WS* -> mode(TEXT_MODE);
MANDATORY_MAX: 'a maximum of' WS* -> mode(NUM_MODE);
MANDATORY_AVG:'an average of' WS* -> mode(NUM_MODE);
MANDATORY_MIN: 'a minimum of' WS* -> mode(NUM_MODE);

TO: 'to' WS* -> mode(TIME_MODE); // only using for times right now, what about dates?
ON: 'on' WS* -> mode(DATE_MODE);
FROM: 'from' WS* -> mode(TIME_MODE);
OF: 'of' WS* -> mode(TEXT_MODE);
COMMA: ',' WS* -> mode(TEXT_MODE);
REPEAT: 'repeat' WS*;
TIMES: 'times' WS*;

ENDLINE: ';';
FUNCTION_PREFIX: 'h(t)=' WS*;

// Line breaks are ignored during tokenization (note that this rule only applies in DEFAULT_MODE, not IDENT_MODE)
WS : [\r\n\t ]+ -> channel(HIDDEN);

mode TEXT_MODE;
TEXT: [a-zA-Z]+ -> mode(DEFAULT_MODE);

mode TIME_MODE;
TIME:  ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) -> mode(DEFAULT_MODE);

mode DATE_MODE;
// mm-dd-yyyy or mm/dd/yyyy
DATE: ('0'[1-9]|'1'[012])[- /.]('0'[1-9]|[12][0-9]|'3'[01])[- /.]('19'|'20')[0-9][0-9]-> mode(DEFAULT_MODE);
ALL_DAYS: 'Monday Tuesday Wednesday Thursday Friday Saturday Sunday' -> mode(DEFAULT_MODE);
MON: 'Monday';
TUES: 'Tuesday';
WED: 'Wednesday';
THUR: 'Thursday';
FRI: 'Friday';
SAT: 'Saturday';
SUN: 'Sunday';
TERMINAL: 'exclusive' -> mode(DEFAULT_MODE);

mode NUM_MODE;
NUM: [0-9]+ -> mode(DEFAULT_MODE);
// TODO: I think we need to define these operators individually as tokens. Same for var.
MATH_OPERATOR: ('+'|'-'|'/'|'*'|'sin'|'cos'|'tan'|'log'|'ln'|'^') -> mode(DEFAULT_MODE);

VAR: ('(t)'|'t') -> mode(DEFAULT_MODE);

// FUNCTION: VAR ‘=’, MATH_OPERATIONS;

// MATH_OPERATIONS: EXP ([+,-,/,*,sin,cos,tan,log,ln,^]+  MATH_OPERATIONS)?;

// EXP: VAR | NUM?;

// VAR: ‘t’;
// FUNCTION: ‘h(t)=’ MATH_OPERATIONS+;