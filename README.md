# Blocks
A Tetris-inspired game for Sidephone SP-01.

## Tetris Fall Speed
Level	sec/line
00 		0.8s
05 		0.46s
10 		0.16s
19 		0.03s
29	 	0.016s

## Tetris Score System

1 line: 40 * (1 + level)
2 lines: 100 * (1 + level)
3 lines: 300 * (1 + level)
4 lines: 1200 * (1 + level)

10 lines = next level

## Colors
Cyan I
Yellow O
Purple or magenta T
Green S
Red Z
Blue J
Orange L

## Tetromino start locations

The I and O spawn in the middle columns
The rest spawn in the left-middle columns
The tetriminoes spawn horizontally with J, L and T spawning flat-side first.
Spawn above playfield, row 21 for I, and 21/22 for all other tetriminoes.
Immediately drop one space if no existing Block is in its path

Rotation: https://tetris.fandom.com/wiki/Super_Rotation_System

## Controls
A rotates counterclockwise,
B rotates 90 degrees clockwise.
 may be significant visual discrepancies between the emulator and a real device. Always test your game on a real device before publishing it._