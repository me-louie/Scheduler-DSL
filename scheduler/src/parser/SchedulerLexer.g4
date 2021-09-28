lexer grammar SchedulerLexer;
/// Default mode

HEADER_START: 'Title:' WS* -> mode(TEXT_MODE);
HEADER_END: 'End Title'; // or just ";"?
OPERATING_HOURS_START: 'Operating hours:' WS*;
OPERATING_RULE_START: 'Operating rule:' WS*;
OR1: 'All operating hours must be scheduled';
OR2:  'Entities can only be scheduled during operating hours, but not all scheduled hours must be used';
ENTITY_START: 'Entity' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_START: 'Make a group called' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_MID: 'composed of entities' WS* -> mode(TEXT_MODE);

RANGE_START: 'Range of schedule:' WS*;
RANGE_NUM_DAYS_MID :'days starting' WS* -> mode(DATE_MODE);
RANGE_DATE_DATE_MID: 'to' WS* -> mode(DATE_MODE);

BEGINNING: 'Start Time WS*' -> mode(TIME_MODE);
END: 'End Time WS*' -> mode(TIME_MODE);

DAY: 'day';
WEEK: 'week';
MONTH: 'month';
YEAR: 'year';

MONDAY: 'Monday';
TUESDAY: 'Tuesday';
WEDNESDAY: 'Wednesday';
THURSDAY: 'Thursday';
FRIDAY: 'Friday';
SATURDAY: 'Saturday';
SUNDAY: 'Sunday';

RULES_START: 'Rules:' WS*;
SCHEDULE_START: 'Schedule' WS* -> mode(TEXT_MODE);
FREQUENCY_START: 'Frequency' WS* -> mode(TEXT_MODE);
FREQUENCY_CANNOT_BE_SCHEDULED: 'cannot be scheduled' WS*;
FREQUENCY_MORE_THAN: 'more than' WS* -> mode(MATH_MODE);
FREQUENCY_DAYS_IN_ROW: 'days in a row' WS*;
AVAILABILITY_START: 'Unavailable' WS* -> mode(TEXT_MODE);
OVERLAP_START: 'Cannot schedule together' WS* -> mode(TEXT_MODE);
RATIO_START: 'Ratio' WS* -> mode(NUM_MODE);
MANDATORY_START: 'Mandatory to schedule' WS* -> mode(TEXT_MODE);
MANDATORY_MAX: 'a maximum of' WS* -> mode(NUM_MODE);
MANDATORY_AVG:'an average of' WS* -> mode(NUM_MODE);
MANDATORY_MIN: 'a minimum of' WS* -> mode(NUM_MODE);

AT: 'at' WS* -> mode(DATE_MODE);
TO: 'to' WS* -> mode(DATE_MODE);
ON: 'on' WS* -> mode(DATE_MODE);
FROM: 'from' WS* -> mode(DATE_MODE);
REPEAT_AT: 'repeat' WS*;
TIMES: 'times' WS*;

ENDLINE: ';';
MIN_MAX_PREFIX: 'must be scheduled' WS*;

TIMEUNIT_TEXT:'hours per' WS*;

COMMA: ',';
// todo: create consistency in function definition across grammar/lexer/parser
FUNCTION_PREFIX: 'h(t)=' WS* -> mode(MATH_MODE);


// Line breaks are ignored during tokenization (note that this rule only applies in DEFAULT_MODE, not IDENT_MODE)
WS : [\r\n\t ] -> channel(HIDDEN);

// Mode specifically for tokenizing the arbitrary text inside the title and in table cells
mode TEXT_MODE;
TEXT: ~[[|\]\r\n;]+ -> mode(DEFAULT_MODE);

mode TIME_MODE;
TIME:  ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) '-' ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) -> mode(DEFAULT_MODE);

mode DATE_MODE;
DATE: ('0'[1-9]|'1'[012])[- /.]('0'[1-9]|[12][0-9]|'3'[01])[- /.]('19'|'20')[0-9][0-9]-> mode(DEFAULT_MODE);

mode NUM_MODE;
NUM: [0-9]+ -> mode(DEFAULT_MODE);

mode MATH_MODE;
MATH: ('+'|'-'|'/'|'*'|'sin'|'cos'|'tan'|'log'|'ln'|'^'|NUM) -> mode(DEFAULT_MODE);