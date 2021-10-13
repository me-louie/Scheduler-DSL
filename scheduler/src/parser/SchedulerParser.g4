parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header entity+ entity_group* shift+ shift_group* transformation*;
header          : HEADER_START TEXT ENDLINE;
entity          : ENTITY_START name ENDLINE;
entity_group    : ENTITY_GROUP_START name COLON name (COMMA name)* ENDLINE;
name            : TEXT;

shift           : SHIFT_START name IS DATE TIME TIME_SEPERATOR DATE TIME ENDLINE;
shift_group     : SHIFT_GROUP_START name COLON name (COMMA name)* ENDLINE;

logical_operator: LOGICAL_AND | LOGICAL_OR | LOGICAL_XOR;
bitwise_operator: SHIFT_LEFT | SHIFT_RIGHT;

transformation : ((apply | merge | loop) ENDLINE) | ifthenelse;
cond_transformations: transformation*;

apply           : APPLY_START name TO name (bitwise_operator NUM)?;
merge           : MERGE_START name COLON name logical_operator name;
loop            : LOOP_START name LOOP_MID_1 name bitwise_operator NUM LOOP_MID_2 (LOOP_MID_3 NUM LOOP_END)?;

ifthenelse      : IF cond COND_OPEN_BRACE COND_NEWLINE
                        thenblock=cond_transformations
                     CLOSE_BRACE ELSE OPEN_BRACE COND_NEWLINE
                        elseblock=cond_transformations
                     CLOSE_BRACE;

cond            : OPEN_PAREN name logical_operator name CLOSE_PAREN;

// todo: In order of importance:
//          1. very simple functions for use in loop (e.g. h(t) = 2t, h(t) = 5/t)
//          2. other logical operators (e.g. NAND, NOR, NXOR)
//          3. stretch/compress shift_groups (e.g. stretch: 'Stretch' STRETCH_GROUP_NAME SHIFT_GROUP_NAME NUM would stretch
//             the shift group by a factor of NUM and make it into a new shift_group called STRETCH_GROUP_NAME)




