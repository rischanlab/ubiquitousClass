You can find the APK in:
/TouchnToy/bin

The code can be built using:
Eclipse, Android SDK

Note on collecting data:
- The folder /%root_dir%/_TouchnToy_Single/user_0 is overwritten for each new person. 
Thus you need to rename and copy it to a safe place before starting collecting for a new person.
- The size of the keys in keyboard is hard-coded. You can change those parameters in /TouchnToy/res/layout/keyboard_main.xml

The format of one record is (15 values):
 1st : timestamps (in milliseconds)
 2nd : device ID (6)
 3rd : source (4098)
 4th : raw X- position of touch point
 5th : raw Y- position of touch point
 6th : size of touch point (0 <= size <= 1)
 7th : tool major
 8th : tool minor
 9th : touch major
10th : touch minor
11th : orientation
12th : touch pressure (0 <= pressure <= 1)
13th : touch action by value (0, 1, 2)
14th : touch action by string (TOUCH_DOWN, TOUCH_UP, TOUCH_MOVE)
15th : pressed key (A-Z, a-Z, 0-9, symbols, BACK(backspace), ENTER, SYM(character<->symbol), CHANGE(shift) )

For more reference, please refer to the description of class MotionEvent of Android SDK.
http://developer.android.com/reference/android/view/MotionEvent.html

The input strings can be found at the end of /TouchnToy/src/iat/touchntoy/MainActivity.java

