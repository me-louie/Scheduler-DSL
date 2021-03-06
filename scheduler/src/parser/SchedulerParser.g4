parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : entity+ entity_group* shift+ transformation*;
entity          : ENTITY_START name ENDLINE;
entity_group    : ENTITY_GROUP_START name COLON name (COMMA name)* ENDLINE;
name            : TEXT;

shift           : SHIFT_START name IS DATE TIME TIME_SEPERATOR DATE TIME (OPEN_QUOTE DESCRIPTION CLOSE_QUOTE)? ENDLINE;
shift_group     : SHIFT_GROUP_START name COLON name (COMMA name)*;

set_operator    : SET_AND | SET_OR | SET_XOR | SET_EXCEPT;
offset_operator : SHIFT_LEFT | SHIFT_RIGHT;

variable        : VAR_START name (EQUALSIGN VARORNUM)?;
expression      : EXPRESSION_START name EQUALSIGN VARORNUM MATH VARORNUM;

transformation  : ((shift_group|apply | merge | loop | expression | variable) ENDLINE) |  ifthenelse;
timeShiftUnits  : HOURS_SHIFT | DAYS_SHIFT | WEEKS_SHIFT | MONTHS_SHIFT | YEARS_SHIFT;
cond_transformations: transformation*;
apply           : APPLY_START name COLON name (LOOP_SEPERATOR OFFSET offset_operator (VARORNUM) timeShiftUnits)? (LOOP_SEPERATOR REPEAT NUM)?;
merge           : MERGE_START name COLON name set_operator name;
loop            : LOOP_START name COLON name LOOP_SEPERATOR OFFSET offset_operator VARORNUM timeShiftUnits (LOOP_SEPERATOR REPEAT NUM)?;
ifthenelse      : IF cond COND_OPEN_BRACE COND_NEWLINE
                        thenblock=cond_transformations
                     CLOSE_BRACE ELSE OPEN_BRACE COND_NEWLINE
                        elseblock=cond_transformations
                     CLOSE_BRACE;
cond            : OPEN_PAREN name set_operator name CLOSE_PAREN;





