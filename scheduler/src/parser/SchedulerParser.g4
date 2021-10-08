parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header entity+ entity_group* shift+ shift_group* transformations*;
header          : HEADER_START TEXT ENDLINE;
entity          : ENTITY_START name ENDLINE;
entity_group    : ENTITY_GROUP_START name ENTITY_GROUP_MID name (COMMA name)* ENDLINE;
name            : TEXT;

shift           : SHIFT_START name IS DATE TIME TIME_SEPERATOR DATE TIME ENDLINE;
shift_group     : SHIFT_GROUP_START name COLON name (COMMA name)* ENDLINE;

logical_operator: LOGICAL_AND | LOGICAL_OR | LOGICAL_XOR;
bitwise_operator: SHIFT_LEFT | SHIFT_RIGHT;

transformations : (apply | merge | loop) ENDLINE ;

apply           : APPLY_START name TO name (bitwise_operator NUM)?;
merge           : MERGE_START name name logical_operator (name | LEFT_BRACE merge RIGHT_BRACE);
loop            : LOOP_START name LOOP_MID_1 name bitwise_operator NUM LOOP_MID_2 (LOOP_MID_3 NUM LOOP_END)?;

// todo: In order of importance:
//          1. ifthenelse logic (e.g. if (condition) { apply sg to e >> 4 } else { apply sg2 to e << 1 }
//          2. very simple functions for use in loop (e.g. h(t) = 2t, h(t) = 5/t)
//          3. other logical operators (e.g. NAND, NOR, NXOR)
//          4. stretch/compress shift_groups (e.g. stretch: 'Stretch' STRETCH_GROUP_NAME SHIFT_GROUP_NAME NUM would stretch
//             the shift group by a factor of NUM and make it into a new shift_group called STRETCH_GROUP_NAME)




