# Reaction-Tester
This small Java Swing application measures your response time to certain tasks.
There are 4 different situations:

* Visual Reaction Test
Tests your reaction to a simple visual change.
A square turns green in random intervals measuring the time in milliseconds until a button is pressed after the change.
* Audio Reaction Test
Same as above but sans the visual cue and an audio cue instead.
* Choice Reaction Test
4 letters are presented on the screen with a red background.
1 of them turns green in random intervals. Measures the time in milliseconds until the correct button is pressed.
Wrong input skips one pass.
* Stimuli Reaction Test
The screen flashed is different random colors. At the beginning one color is presented. If this color reappears press a button.

All Tests run 15 times and calculate the average reaction time.
The best and worst response is also stored.

The program does currently not filter outliers (e.g. you were distracted and clicked after 10 seconds)

Results can be extracted as a .txt file in the results tab if necessary.
