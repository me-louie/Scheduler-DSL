lexer grammar SchedulerLexer;
/// Default mode
HEADER_START: 'Title:' WS* -> mode(TEXT_MODE);

ENTITY_START: 'Entity' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_START: 'Make a group called' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_MID: 'composed of entities' WS* -> mode(TEXT_MODE);

SHIFTS: 'Shifts:' WS*;
SHIFT_START: 'Shift' WS*;
SHIFT_GROUP_START: 'Shift group' WS*;

LOGICAL_AND: 'AND' WS* -> mode(TEXT_MODE);
LOGICAL_OR: 'OR' WS* -> mode(TEXT_MODE);
LOGICAL_XOR: 'XOR' WS* -> mode(TEXT_MODE);

SHIFT_RIGHT: '>>' WS* -> mode(NUM_MODE);
SHIFT_LEFT: '<<' WS* -> mode(NUM_MODE);

APPLY_START: 'Apply' WS* -> mode(TEXT_MODE);
MERGE_START: 'Merge' WS* -> mode(TEXT_MODE);
LOOP_START: 'Loop' WS* -> mode(TEXT_MODE);
LOOP_MID_1: 'over' WS* -> mode(TEXT_MODE);
LOOP_MID_2: 'each person' WS*;
LOOP_MID_3: 'and repeat' WS* -> mode(NUM_MODE);
LOOP_END: 'times' WS*;


TO: 'to' WS* -> mode(TEXT_MODE);
IS: 'is' WS* -> mode(DATE_MODE);
TIME_SEPERATOR: '-' WS* -> mode(TIME_MODE);
COMMA: ',' WS* -> mode(TEXT_MODE);
COLON: ':' WS* -> mode(TEXT_MODE);
ENDLINE: ';';


// Line breaks are ignored during tokenization (note that this rule only applies in DEFAULT_MODE, not IDENT_MODE)
WS : [\r\n\t ]+ -> channel(HIDDEN);

mode TEXT_MODE;
TEXT: [a-zA-Z]+ -> mode(DEFAULT_MODE);

mode TIME_MODE;
TIME:  ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) -> mode(DEFAULT_MODE);

mode DATE_MODE;
// mm-dd-yyyy or mm/dd/yyyy
DATE: ('0'[1-9]|'1'[012])[- /.]('0'[1-9]|[12][0-9]|'3'[01])[- /.]('19'|'20')[0-9][0-9]-> mode(TIME_MODE);


mode NUM_MODE;
NUM: [0-9]+ -> mode(DEFAULT_MODE);