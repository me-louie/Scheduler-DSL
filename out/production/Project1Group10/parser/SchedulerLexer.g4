lexer grammar SchedulerLexer;
/// Default mode

HEADER: 'Title:' WS* -> mode(TEXT_MODE);
OPERATING_HOURS: 'Operating hours:' WS*;
ENTITY: 'Entity' WS*;
MAKE_GROUP: 'Make a group called' WS* -> mode(TEXT_MODE);
COMPOSED_OF: 'composed of entities' WS* -> mode(TEXT_MODE);


RANGE_PREFIX: 'RANGE' WS*;
RANGE_TEXT :'days starting' WS* -> mode(DATE_MODE);

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

RULES: 'Rules:' WS*;
AT: 'at' WS* -> mode(DATE_MODE);
TO: 'to' WS* -> mode(DATE_MODE);
SCHEDULE: 'Schedule' WS* -> mode(TEXT_MODE);
ON: 'on' WS* -> mode(DATE_MODE);
FROM: 'from' WS* -> mode(DATE_MODE);
REPEAT_AT: 'repeat' WS*;
TIMES: 'times' WS*;

IS_UNAVAILABLE: 'is unavailable' WS* -> mode(DATE_MODE);
FREQ_SCHEDULER_PREFIX: 'cannot be scheduled more than' WS*;
SPECIFIC_DAY_PREFIX: 'cannot be scheduled on' WS*;
MIN_MAX_PREFIX: 'must be scheduled' WS*;
MIN_TEXT: 'a minimum of' WS*;
TIMEUNIT_TEXT:'hours per' WS*;
MAX_TEXT: 'a maximum of' WS*;
AVERAGE_TEXT:'an average of' WS*;
COMMA: ',';
OVERLAP_TEXT: 'cannot be scheduled with' WS*;
RATIO: 'to a ratio of' WS*;
FUNCTION_PREFIX: 'h(t)=' WS* -> mode(MATH_MODE);


// Line breaks are ignored during tokenization (note that this rule only applies in DEFAULT_MODE, not IDENT_MODE)
WS : [\r\n\t ] -> channel(HIDDEN);

// Mode specifically for tokenizing the arbitrary text inside the title and in table cells
mode TEXT_MODE;
TEXT: ~[[|\]\r\n]+ -> mode(DEFAULT_MODE);

mode TIME_MODE;
TIME:  ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) '-' ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) -> mode(DEFAULT_MODE);

mode DATE_MODE;
DATE: ('0'[1-9]|'1'[012])[- /.]('0'[1-9]|[12][0-9]|'3'[01])[- /.]('19'|'20')[0-9][0-9]-> mode(DEFAULT_MODE);

mode NUM_MODE;
NUM: [0-9]+ -> mode(DEFAULT_MODE);

mode MATH_MODE;
MATH: ('+'|'-'|'/'|'*'|'sin'|'cos'|'tan'|'log'|'ln'|'^') -> mode(DEFAULT_MODE);