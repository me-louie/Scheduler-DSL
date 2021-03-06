lexer grammar SchedulerLexer;
// Default mode
ENTITY_START: 'Entity' WS* -> mode(TEXT_MODE);
ENTITY_GROUP_START: 'EntityGroup' WS* -> mode(TEXT_MODE);

SHIFT_START: 'Shift' WS* -> mode(TEXT_MODE);
SHIFT_GROUP_START: 'Shift Group' WS* -> mode(TEXT_MODE);

SET_AND: 'AND' WS* -> mode(TEXT_MODE);
SET_OR: 'OR' WS* -> mode(TEXT_MODE);
SET_XOR: 'XOR' WS* -> mode(TEXT_MODE);
SET_EXCEPT: 'EXCEPT' WS* -> mode(TEXT_MODE);

HOURS_SHIFT: 'HOURS';
DAYS_SHIFT: 'DAYS';
WEEKS_SHIFT: 'WEEKS';
MONTHS_SHIFT: 'MONTHS';
YEARS_SHIFT: 'YEARS';

SHIFT_RIGHT: '>>' WS* -> mode(EXP_MODE);
SHIFT_LEFT: '<<' WS* -> mode(EXP_MODE);

APPLY_START: 'Apply' WS* -> mode(TEXT_MODE);
MERGE_START: 'Merge' WS* -> mode(TEXT_MODE);
LOOP_START: 'Loop' WS* -> mode(TEXT_MODE);
OFFSET: 'Offset:' WS*;
REPEAT: 'Repeat:' WS* -> mode(NUM_MODE);

IS: 'is' WS* -> mode(DATE_MODE);
TIME_SEPERATOR: '-' WS* -> mode(DATE_MODE);
COMMA: ',' WS* -> mode(TEXT_MODE);
COLON: ':' WS* -> mode(TEXT_MODE);
ENDLINE: ';' WS*;

IF: 'if' WS*;
ELSE: 'else' WS*;
OPEN_PAREN: '(' -> mode(TEXT_MODE);
CLOSE_PAREN: ')' -> mode(COND_MODE);
OPEN_BRACE: '{' -> mode(COND_MODE);
CLOSE_BRACE: '}';
OPEN_QUOTE: '"' -> mode(TEXT_BLOCK_MODE);
LOOP_SEPERATOR: '|' WS*;

VAR_START: 'Var' WS* -> mode(TEXT_MODE);
EXPRESSION_START: 'Expression' WS* -> mode(TEXT_MODE);
EQUALSIGN: '=' WS* -> mode(EXP_MODE);

MATH: ('+'|'-'|'/'|'*'|'^') WS*-> mode(EXP_MODE);
// Line breaks are ignored during tokenization (note that this rule only applies in DEFAULT_MODE)
WS : [\r\n\t ]+ -> channel(HIDDEN);

mode TEXT_MODE;
TEXT: [a-zA-Z0-9]+ -> mode(DEFAULT_MODE);

mode TEXT_BLOCK_MODE;
DESCRIPTION: ~["]+;
CLOSE_QUOTE: '"' -> mode(DEFAULT_MODE);

mode TIME_MODE;
TIME:  ([01]?[0-9]|'2'[0-3])(':'[0-5][0-9]) WS*-> mode(DEFAULT_MODE);

mode DATE_MODE; // mm-dd-yyyy or mm/dd/yyyy
DATE: ('0'[1-9]|'1'[012])[- /.]('0'[1-9]|[12][0-9]|'3'[01])[- /.]('19'|'20')[0-9][0-9] WS*-> mode(TIME_MODE);

mode NUM_MODE;
NUM: [0-9]+ -> mode(DEFAULT_MODE);

mode EXP_MODE;
VARORNUM: '-'?[a-zA-Z0-9]* -> mode(DEFAULT_MODE);

mode COND_MODE;
COND_WS: [\t ]+ -> channel(HIDDEN);
COND_OPEN_BRACE: '{';
COND_NEWLINE : [\r\n]+ -> mode(DEFAULT_MODE);