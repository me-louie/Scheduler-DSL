parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : entity+ entity_group* shift+ shift_group* transformation*;
entity          : ENTITY_START name ENDLINE;
entity_group    : ENTITY_GROUP_START name COLON name (COMMA name)* ENDLINE;
name            : TEXT;

shift           : SHIFT_START name IS DATE TIME TIME_SEPERATOR DATE TIME ENDLINE;
shift_group     : SHIFT_GROUP_START name COLON name (COMMA name)* ENDLINE;

set_operator    : SET_AND | SET_OR | SET_XOR | SET_EXCEPT;
offset_operator : SHIFT_LEFT | SHIFT_RIGHT;

variable        : VAR_START name EQUALSIGN VARORNUM;
expression      : EXPRESSION_START name EQUALSIGN VARORNUM MATH VARORNUM;

transformation  : ((apply | merge | loop | expression | variable) ENDLINE) |  ifthenelse;
timeShiftUnits  : HOURS_SHIFT | DAYS_SHIFT | WEEKS_SHIFT | MONTHS_SHIFT | YEARS_SHIFT ;
cond_transformations: transformation*;
apply           : APPLY_START name TO name (offset_operator (VARORNUM) timeShiftUnits)?;
merge           : MERGE_START name COLON name set_operator name;
loop            : LOOP_START name LOOP_MID_1 name offset_operator VARORNUM timeShiftUnits LOOP_MID_2 (LOOP_MID_3 NUM LOOP_END)?;
ifthenelse      : IF cond COND_OPEN_BRACE COND_NEWLINE
                        thenblock=cond_transformations
                     CLOSE_BRACE ELSE OPEN_BRACE COND_NEWLINE
                        elseblock=cond_transformations
                     CLOSE_BRACE;
cond            : OPEN_PAREN name set_operator name CLOSE_PAREN;





