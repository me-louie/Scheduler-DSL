parser grammar SchedulerParser;
options { tokenVocab = SchedulerLexer; }

program         : header entity+ entity_group* shift+ shift_group transformations*;
header          : HEADER_START TEXT ENDLINE;
entity          : ENTITY_START name ENDLINE;
entity_group    : ENTITY_GROUP_START name ENTITY_GROUP_MID name (COMMA name)* ENDLINE;
name            : TEXT;

shift           : SHIFT_START name IS DATE TIME TIME_SEPERATOR TIME ENDLINE;
shift_group     : SHIFT_GROUP_START name COLON name (COMMA name)* ENDLINE;

logical_operator: LOGICAL_AND | LOGICAL_OR | LOGICAL_XOR;
bitwise_operator: SHIFT_LEFT | SHIFT_RIGHT;

transformations: (apply | merge | loop) ENDLINE ;
// TODO: Change apply/merge to handle (name|merge) in place of name.
// In our brainstorming sess we wanted to be able to accept (name|merge) after APPLY_START we switch to text_mode
// so we won't match MERGE. Same for merge, after logical_operator we switch to text_mode so don't match MERGE.
apply: APPLY_START name TO name (bitwise_operator NUM)?;
merge: MERGE_START name logical_operator name;
loop: LOOP_START name LOOP_MID_1 name bitwise_operator NUM LOOP_MID_2 (LOOP_MID_3 NUM LOOP_END)?;




