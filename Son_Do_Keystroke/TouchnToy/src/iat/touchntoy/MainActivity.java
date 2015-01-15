package iat.touchntoy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity implements OnTouchListener, OnClickListener,
OnFocusChangeListener {
	
	private int ID_SYM = 1;
	private int ID_SHIFT = 2;
	private int ID_SPACE = 3;
	private int ID_BACK = 4;
	private int ID_ENTER = 5;
	private int ID_NORMALKEY = 6;
	private int ID_INVALIDKEY = 0;
	
	private float fTEXTSIZE = 20;
	private float fLEFTVOLUME = 0.01f;
	private float fRIGHTVOLUME = 0.01f;
	
	
	private final static int iMAX_SAMPLES = 4;
	private final static int iMAX_COUNT = 100;
	private String szSamples[] = new String[iMAX_SAMPLES+1];
	
	private final String szCorrect = "Correct!";
	private final String szIncorrect = "Incorrect! Please type the sample again!";
	
	private boolean bStart = false;
	private boolean bAppendTouch = false;
	final private String szOutDirName  = "_TouchnToy_Single";
	final private String szUserDirName  = "user_0";
	final private String szTouchOutFilePrefix = "touch_record";
	final private String szSamplePrefix = "_sample";
	final private String szCountPrefix = "_count";
	final private String szOutFileFormat = ".txt";
	final private String szDelimiter = " ";
	final private String szEndLine = "\n";
	
	private static boolean bRegister = false ;
	
	private static int iCount = 1 ;
	private static int iCountTmp[] = new int[iMAX_SAMPLES+1] ;
	
	private String szTouchOutputString = "";
	
	
	private Button buttonUp, buttonDown;
	private Button buttonPrev, buttonNext;
	
	
	private EditText editText; // Edit Text box
	private RelativeLayout layoutKey, layoutMyKey;
	private boolean isEdit = true ;
	private String szShiftTagLower = "";
	private String szShiftTagUpper = "";
	private String szShiftTagUpper1 = "";
	private String szCharSymTag = "";
	private String szSymCharTag = "";
	private int iW, iWindowWidth;
	private Button buttonB[]   = new Button[29];
	private Button buttonSpace, buttonEnter, buttonBack, buttonShift, buttonSym ;
	
	private MediaPlayer mediaBtn[] = new MediaPlayer[29];
	private MediaPlayer mediaSpace, mediaEnter, mediaBack, mediaShift, mediaSym;
	
	
	private String charLower[] = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
			"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
			"x", "y", "z", ",", ".", "'" };
	private String charUpper[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z", ",", ".", "'" };
	private String charSym1[] = {"!", ":", "/", "#", "3", "$", "&", "^", "8", "*",
			"(", ")", "?", ";", "9", "0", "1", "4", "@", "5", "7", "~", "2",
			"_", "6", "-", "<", ">", "\""};
	private String charSym2[] = {"↑", "\\", "´", "←", "÷", "→", "^^", "^_^", "}", "=.=",
			"T.T", "T_T", "¿", "¡", "[", "]", "+", "=", "↓", "%", "{", "|", "×",
			"`", "°", "♥", "■", "♦", "●"};
	
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        File szOutDir = new File(Environment.getExternalStorageDirectory()+File.separator+szOutDirName);
		if(!szOutDir.exists()) {
			szOutDir.mkdir(); //directory is created;
		}
        
		File szMyDir = new File(Environment.getExternalStorageDirectory()+File.separator+szOutDirName+File.separator+szUserDirName);
		if(szMyDir.exists()) {
		     szMyDir.delete();
		}
		szMyDir.mkdir(); //directory is created;
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        try {
			// adjusting key regarding window sizes
        	        	
            buttonPrev = (Button) findViewById(R.id.buttonprev);
            buttonNext = (Button) findViewById(R.id.buttonnext);
            
            buttonUp   = (Button) findViewById(R.id.buttoncountup);
            buttonDown = (Button) findViewById(R.id.buttoncountdown);
            
            buttonUp.setVisibility(View.INVISIBLE);
            buttonDown.setVisibility(View.INVISIBLE);
        	
        	init();
			setKeys();
			setSizeFirstRow();
			setSizeSecondRow();
			setSizeThirdRow();
			setSizeForthRow();
			
			
			editText = (EditText) findViewById(R.id.editText1);
			editText.setOnTouchListener(this);
			editText.setOnFocusChangeListener(this);
			editText.setOnClickListener(this);
			
			editText.setText(szSamples[iCount]+" ");

			layoutKey = (RelativeLayout) findViewById(R.id.idKeyLayout);
			layoutMyKey = (RelativeLayout) findViewById(R.id.idMyKeyboard);

		} catch (Exception e) {
			e.printStackTrace();
		}
        
        hideDefaultKeyboard();
        enableKeyboard();
        
        changeCapitalLetters1();
		changeCapitalTags1();
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	
        public PlaceholderFragment() {
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
 // enabling customized keyboard
 	private void enableKeyboard() {
 		
 		layoutKey.setVisibility(RelativeLayout.VISIBLE);
 		layoutMyKey.setVisibility(RelativeLayout.VISIBLE);

 	}
 	
 	
 	private void hideDefaultKeyboard() {
 		getWindow().setSoftInputMode(
 				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

 	}
 	
        
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    
    public void onButtonCheck (View view) {
    	
    	final Button buttonStart = (Button)findViewById(R.id.buttonstart);
    	
    	if (bRegister) {
    		szSamples[iCount] = editText.getText().toString() ;
    		iCountTmp[iCount] = 0 ;
    		bRegister = false ;
    		buttonPrev.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.VISIBLE);
            buttonStart.setText("Check");
            editText.setText("");
    	}
    	
    	if (checkShow()) {
    		buttonUp.setVisibility(View.VISIBLE);
            buttonDown.setVisibility(View.VISIBLE);
    	} else if (checkHide()) {
    		buttonUp.setVisibility(View.INVISIBLE);
            buttonDown.setVisibility(View.INVISIBLE);
    	}
    	
    	if (checkRegister()) {
    		buttonPrev.setVisibility(View.INVISIBLE);
            buttonNext.setVisibility(View.INVISIBLE);
            bRegister = true ;
            buttonStart.setText("Done");
            editText.setText("");
            szSamples[iCount] = "" ;
            iCountTmp[iCount] = 0 ;
    	}
    	
    	
    	bAppendTouch = false;
		
		changeCapitalLetters1();
		changeCapitalTags1();
		buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
    	
		if (!bRegister) {
	    	if (iCount > 0) {
	    		String s = checkInputString();
	    		if (!s.equals(szIncorrect)) {
	    			iCountTmp[iCount]++;
	    		}
	    		
	    	}
		}
    	
    	if (!bStart) {
    		bStart = true;
    	}
    	
    	if (!bRegister) {
    		editText.setText(szSamples[iCount]+" ");
    	}
    	
    	if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
    		
    	}
    	final TextView textViewCount = (TextView)findViewById(R.id.textView_count);
    	textViewCount.setText("Sample: "+iCount);
    	final TextView textViewCountTmp = (TextView)findViewById(R.id.textView_counttmp);
    	textViewCountTmp.setText("Count: "+iCountTmp[iCount]);
    	
    	szTouchOutputString = "" ;
    	    	
    }
    
    private boolean checkShow () {
    	boolean bRet = true;
    	
    	String src = "showButtonCount";
    	String dst = editText.getText().toString();
    	
    	bRet = dst.equals(src) ? true : false;
    	
    	return bRet;
    }
    
    private boolean checkHide () {
    	boolean bRet = true;
    	
    	String src = "hideButtonCount";
    	String dst = editText.getText().toString();
    	
    	bRet = dst.equals(src) ? true : false;
    	
    	return bRet;
    }
    
    private boolean checkRegister () {
    	boolean bRet = true;
    	
    	String src = "registerSample";
    	String dst = editText.getText().toString();
    	
    	bRet = dst.equals(src) ? true : false;
    	
    	return bRet;
    }
    
    public void onButtonPrev (View view) {
    	
		bStart = false;
		bAppendTouch = false;
		
		changeCapitalLetters1();
		changeCapitalTags1();
		buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
		
		if (iCount > 1) {
			iCount--;
		}
		
		
		editText.setText(szSamples[iCount]+" ");
	
		final TextView textViewCount = (TextView)findViewById(R.id.textView_count);
    	textViewCount.setText("Sample: "+iCount);
    	final TextView textViewCountTmp = (TextView)findViewById(R.id.textView_counttmp);
    	textViewCountTmp.setText("Count: "+iCountTmp[iCount]);
    	
    	final Button buttonStart = (Button)findViewById(R.id.buttonstart);
    	buttonStart.setText("Check");
    	
    }
    
    public void onButtonNext (View view) {
    	
		bStart = false;
		bAppendTouch = false;
		
		changeCapitalLetters1();
		changeCapitalTags1();
		buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
		
		if (iCount < iMAX_SAMPLES) {
			iCount++;
		}
		
		
		editText.setText(szSamples[iCount]+" ");
	
		final TextView textViewCount = (TextView)findViewById(R.id.textView_count);
    	textViewCount.setText("Sample: "+iCount);
    	final TextView textViewCountTmp = (TextView)findViewById(R.id.textView_counttmp);
    	textViewCountTmp.setText("Count: "+iCountTmp[iCount]);
    	
    	final Button buttonStart = (Button)findViewById(R.id.buttonstart);
    	buttonStart.setText("Check");
    	
    }
    
    public void onButtonUp (View view) {
    	
		bStart = false;
		bAppendTouch = false;
		
		changeCapitalLetters1();
		changeCapitalTags1();
		buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
		
		if (iCountTmp[iCount] < iMAX_COUNT) {
			iCountTmp[iCount]++;
		}
		
		
		editText.setText(szSamples[iCount]+" ");
	
		final TextView textViewCount = (TextView)findViewById(R.id.textView_count);
    	textViewCount.setText("Sample: "+iCount);
    	final TextView textViewCountTmp = (TextView)findViewById(R.id.textView_counttmp);
    	textViewCountTmp.setText("Count: "+iCountTmp[iCount]);
    	
    	final Button buttonStart = (Button)findViewById(R.id.buttonstart);
    	buttonStart.setText("Check");
    	
    }
    
    public void onButtonDown (View view) {
    	
		bStart = false;
		bAppendTouch = false;
		
		changeCapitalLetters1();
		changeCapitalTags1();
		buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
		
		if (iCountTmp[iCount] > 0) {
			iCountTmp[iCount]--;
		}
		
		
		editText.setText(szSamples[iCount]+" ");
	
		final TextView textViewCount = (TextView)findViewById(R.id.textView_count);
    	textViewCount.setText("Sample: "+iCount);
    	final TextView textViewCountTmp = (TextView)findViewById(R.id.textView_counttmp);
    	textViewCountTmp.setText("Count: "+iCountTmp[iCount]);
    	
    	final Button buttonStart = (Button)findViewById(R.id.buttonstart);
    	buttonStart.setText("Check");
    	
    }
    
    private String checkInputString () {
    	if (iCount > 0) {
    		String srcStr = szSamples[iCount];
    		String dstStr = editText.getText().toString();
    		boolean bEqual = true;
    		if (srcStr.length() != dstStr.length()) {
    			bEqual = false;
    		} else {
	    		for (int i = 0 ; i < srcStr.length(); i++) {
    				int srcCode = (int) (srcStr.charAt(i));
    				int dstCode = (int) (dstStr.charAt(i));
    				
    				if (srcCode == 160) {
    					srcCode = 32;
    				}
    				if (dstCode == 160) {
    					dstCode = 32;
    				}
    				
	    			if (srcCode != dstCode) {
	    				bEqual = false;
	    				break;
	    			}
	    		}
    		}
    		if (bEqual) {
    			return szCorrect;
    		}
    		else {
    			return szIncorrect;
    		}
    	} else {
    		return "";
    	}
    }
    
            
	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		
		if (view == editText && hasFocus == true) {
			isEdit = true ;
		}
		
	}
	

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		String s = checkTouch(view);
		
		if (!"".equals(s)) {
			
			onClickAnimation(view,event);
			
			final long liMiliSeconds = System.currentTimeMillis();
	    	final int iDeviceID      = event.getDeviceId();
	    	final int iSource        = event.getSource();
			final float fRawXCoo     = event.getRawX();
			final float fRawYCoo     = event.getRawY();
			final float fSize        = event.getSize();
			final float fToolMajor   = event.getToolMajor();
			final float fToolMinor   = event.getToolMinor();
			final float fTouchMajor  = event.getTouchMajor();
			final float fTouchMinor  = event.getTouchMinor();
			final float fOrientation = event.getOrientation();
			final float fPressure    = event.getPressure() ;
			final int iAction        = MotionEventCompat.getActionMasked(event);
	    				
			final String szActionString = getActionString(iAction);
			
			szTouchOutputString = liMiliSeconds + szDelimiter + iDeviceID + szDelimiter + iSource + szDelimiter + fRawXCoo + szDelimiter + fRawYCoo + szDelimiter + fSize + szDelimiter + fToolMajor + szDelimiter + fToolMinor + szDelimiter + fTouchMajor + szDelimiter + fTouchMinor + szDelimiter + fOrientation + szDelimiter + fPressure + szDelimiter + iAction + szDelimiter + szActionString + szDelimiter + s + szEndLine ;
			try {
	    		//File szMyDir = new File(Environment.getExternalStorageDirectory()+File.separator+szOutDirName+szUserDirName);
	    		if (!bAppendTouch) {
	    			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
		    				Environment.getExternalStorageDirectory()+File.separator+szOutDirName+File.separator+szUserDirName+File.separator+szTouchOutFilePrefix + szSamplePrefix + iCount + szCountPrefix + iCountTmp[iCount] + szOutFileFormat));
		    		bufferedWriter.write(szTouchOutputString);
		    		bufferedWriter.close();
		    		bAppendTouch = true;
	    		} else {
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
		    				Environment.getExternalStorageDirectory()+File.separator+szOutDirName+File.separator+szUserDirName+File.separator+szTouchOutFilePrefix + szSamplePrefix + iCount + szCountPrefix + iCountTmp[iCount] + szOutFileFormat,
		    				true));
		    		bufferedWriter.append(szTouchOutputString);
		    		bufferedWriter.close();
	    		}
	    	}
	    	catch (Exception e) {
	    		e.printStackTrace();
	    	}
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				//
			}
			
			if (event.getAction() == MotionEvent.ACTION_UP) {
				onClick(view);
			}
			
			
			
		}
		
		return true ;
	}
	
	private String checkTouch (View view) {
		String s = "";
		
		if (view == buttonEnter) {
			s = "Enter";
		} else if (view == buttonSpace) {
			s = "Space" ;
		} else if (view == buttonBack) {
			s = "Back";
		} else if (view == buttonSym) {
			s = "Sym";
		} else if (view == buttonShift) {
			s = "Change";
		} else {
			
			for (int i = 0 ; i < buttonB.length; i++) {
				if (view == buttonB[i]) {
					s = (String) buttonB[i].getTag();
				}
			}
		}
		
		return s;
	} 
	
	@SuppressLint("NewApi") private void onClickAnimation (View view, MotionEvent event) {
		
		final Button b = (Button) (view);
		
		if (view == buttonEnter) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				FillCustomGradient(b,true);
				mediaEnter.setLooping(true);
				mediaEnter.start();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				FillCustomGradient(b,false);
				mediaEnter.pause();
			}
		} else if (view == buttonSpace) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				FillCustomGradient(b,true);
				mediaSpace.setLooping(true);
				mediaSpace.start();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				FillCustomGradient(b,false);
				mediaSpace.pause();
			}
		} else if (view == buttonBack) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				FillCustomGradient(b,true);
				mediaBack.setLooping(true);
				mediaBack.start();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				FillCustomGradient(b,false);
				mediaBack.pause();
			}
		} else if (view == buttonShift) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (buttonSym.getTag().equals(szCharSymTag)) {
					if (buttonShift.getTag().equals(szShiftTagUpper1)) {
						FillCustomGradient(b,true);
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					} else if (buttonShift.getTag().equals(szShiftTagUpper)) {
						FillCustomGradient(b,true);
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					} else {
						FillCustomGradient(b,true);
						buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
					}
				} else {
					FillCustomGradient(b,true);
					buttonShift.setTextColor(getResources().getColor(R.color.white));
				}
				mediaShift.setLooping(true);
				mediaShift.start();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (buttonSym.getTag().equals(szCharSymTag)) {
					if (buttonShift.getTag().equals(szShiftTagUpper1)) {
						FillCustomGradient(b,true);
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					} else if (buttonShift.getTag().equals(szShiftTagUpper)) {
						FillCustomGradient(b,false);
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					} else {
						FillCustomGradient(b,false);
						buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
					}
				} else {
					FillCustomGradient(b,false);
					buttonShift.setTextColor(getResources().getColor(R.color.white));
				}
				mediaShift.pause();
			}
		} else if (view == buttonSym) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				FillCustomGradient(b,true);
				mediaSym.setLooping(true);
				mediaSym.start();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				FillCustomGradient(b,false);
				mediaSym.pause();
			}
		} else {
			
			for (int i = 0 ; i < buttonB.length; i++) {
				if (view == buttonB[i]) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						FillCustomGradient(b,true);
						mediaBtn[i].setLooping(true);
						mediaBtn[i].start();
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						FillCustomGradient(b,false);
						mediaBtn[i].pause();
					}
				}
			}
		}
	} 
	
	private String getActionString(int iAction) {
		String s = null;
		switch(iAction){
			case MotionEvent.ACTION_DOWN:
				s = "TOUCH_DOWN";
				break;
			case MotionEvent.ACTION_UP:
				s = "TOUCH_UP";
				break;
			case MotionEvent.ACTION_MOVE:
				s = "TOUCH_MOVE";
				break;
			default:
				s = "TOUCH_UNKNOWN";
		}
		
		return s;
	}
	
	@SuppressLint("NewApi") private void FillCustomGradient(View view, final boolean isPressed) {
        
		GradientDrawable gradientDrawable;
		if (!isPressed) {
			gradientDrawable = new GradientDrawable(
		            GradientDrawable.Orientation.TOP_BOTTOM,
		            new int[] {getResources().getColor(R.color.GradientBeginNormal),getResources().getColor(R.color.GradientEndNormal)});
			gradientDrawable.setStroke(3, getResources().getColor(R.color.gray_dark));
		} else {
			gradientDrawable = new GradientDrawable(
		            GradientDrawable.Orientation.TOP_BOTTOM,
		            new int[] {getResources().getColor(R.color.GradientBeginClick),getResources().getColor(R.color.GradientEndClick)});
			gradientDrawable.setStroke(3, getResources().getColor(R.color.orange_medium));
		}
	    gradientDrawable.setCornerRadius(8);
	    
	    
	    view.setBackground(gradientDrawable);
		
    }
	
	@Override
	public void onClick(View view) {
		int idView = checkKeyPress(view);
		if (idView != ID_INVALIDKEY) {
						
			if (idView != ID_SYM && idView != ID_SHIFT) {
				String szEditText = editText.getText().toString();
				if (szCorrect.equals(szEditText) || szIncorrect.equals(szEditText)) {
					editText.setText("");
				}
				if (szEditText.equals(szSamples[iCount]+" ")) {
					editText.setText("");
				}
			}
			
			if (view == buttonShift) {
				
				if (buttonShift.getTag().equals(szShiftTagUpper)) {
					
					if (buttonSym.getTag().equals(szCharSymTag)) {
						changeSmallLetters();
						changeSmallTags();
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					} else {
						changeSyNuLetters1();
						changeSyNuTags1();
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					}
										
				} else if (buttonShift.getTag().equals(szShiftTagUpper1)) {
					if (buttonSym.getTag().equals(szCharSymTag)) {
						changeCapitalLetters();
						changeCapitalTags();
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					} else {
						changeSyNuLetters1();
						changeSyNuTags1();
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					}
				} else if (buttonShift.getTag().equals(szShiftTagLower)) {
					
					if (buttonSym.getTag().equals(szCharSymTag)) {
						changeCapitalLetters1();
						changeCapitalTags1();
						buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
					} else {
						changeSyNuLetters2();
						changeSyNuTags2();
						buttonShift.setTextColor(getResources().getColor(R.color.white));
					}
					
				}
				
			} else if (view != buttonBack && view != buttonSym) {
				String sc = editText.getText().toString();
				addText(view);
				
				if (buttonSym.getTag().equals(szCharSymTag) && buttonShift.getTag().equals(szShiftTagUpper1)) {
					changeSmallLetters();
					changeSmallTags();
					buttonShift.setTextColor(getResources().getColor(R.color.white));
				}
				if ((view == buttonEnter && buttonSym.getTag().equals(szCharSymTag)) || (view == buttonSpace && sc != null && sc.length() > 0 && (".".equals(sc.substring(sc.length()-1)) || "!".equals(sc.substring(sc.length()-1)) || "?".equals(sc.substring(sc.length()-1))))) {
					changeCapitalLetters1();
					changeCapitalTags1();
					buttonShift.setTextColor(getResources().getColor(R.color.green_medium));
				}
				
			
			} else if (view == buttonBack) {
				isBack(view);
				
				if (buttonSym.getTag().equals(szCharSymTag) && buttonShift.getTag().equals(szShiftTagUpper1)) {
					changeSmallLetters();
					changeSmallTags();
					buttonShift.setTextColor(getResources().getColor(R.color.white));
				}
				
			} else if (view == buttonSym) {
				String nTag = (String) buttonSym.getTag();
				if (nTag.equals(szCharSymTag)) {
					changeSyNuLetters1();
					changeSyNuTags1();
					buttonShift.setTextColor(getResources().getColor(R.color.white));
										
				}
				if (nTag.equals(szSymCharTag)) {
					changeSmallLetters();
					changeSmallTags();
					buttonShift.setTextColor(getResources().getColor(R.color.white));
				}
				
			}
		}
	}
	
	private int checkKeyPress (View view) {
		if (view == buttonEnter) {
			return ID_ENTER;
		} else if (view == buttonSpace) {
			return ID_SPACE;
		} else if (view == buttonBack) {
			return ID_BACK;
		} else if (view == buttonSym) {
			return ID_SYM;
		} else if (view == buttonShift) {
			return ID_SHIFT;
		} else {
			for (int i = 0 ; i < buttonB.length; i++) {
				if (view == buttonB[i]) {
					return ID_NORMALKEY;
				}
			}
		}
		
		return ID_INVALIDKEY;
	} 
	
	private void addText(View view) {
		if (isEdit == true) {
			String s = null;
			if (view == buttonEnter) {
				s = "\n";
			} else if (view == buttonSpace) {
				s = " ";
			} else {
				s = (String) view.getTag();
			}
			if (s != null) {
				// adding text in Edittext
				editText.append(s);
				
			}
			
			
			// Check for errors, text color change here
			String Str = editText.getText().toString();
			
			int m = szSamples[iCount].length() < Str.length() ? szSamples[iCount].length() : Str.length();
			
			int pos = -1;
			for (int i = 0 ; i < m ; i++) {
				if ((int)szSamples[iCount].charAt(i) != (int)Str.charAt(i)) {
					pos = i;
					break;
				}
			}
			
			if (pos >= 0 && pos < Str.length()) {
				Spannable modifiedText = new SpannableString(Str);
				modifiedText.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.yellow_medium)), pos, Str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				editText.setText(modifiedText);
				editText.setSelection(Str.length());
			} else if (m < Str.length()) {
				pos = m ;
				Spannable modifiedText = new SpannableString(Str);
				modifiedText.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.yellow_medium)), pos, Str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				editText.setText(modifiedText);
				editText.setSelection(Str.length());
			}
			
			
		}

	}

	private void isBack(View view) {
		if (isEdit == true) {
			CharSequence cc = editText.getText();
			if (cc != null && cc.length() > 0) {
				{
					editText.setText("");
					editText.append(cc.subSequence(0, cc.length() - 1));
				}

			}
		}

		
	}
	private void changeSmallLetters() {
		buttonShift.setVisibility(Button.VISIBLE);
		for (int i = 0; i < charLower.length; i++)
			buttonB[i].setText(charLower[i]);
		buttonSym.setText(szCharSymTag);
		
		buttonB[6].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[6].setPadding(0, 0, 0, 0);
		
		buttonB[7].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[7].setPadding(0, 0, 0, 0);
		
		buttonB[9].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[9].setPadding(0, 0, 0, 0);
		
		buttonB[10].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[10].setPadding(0, 0, 0, 0);
		
		buttonB[11].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[11].setPadding(0, 0, 0, 0);
	}
	private void changeSmallTags() {
		for (int i = 0; i < charLower.length; i++)
			buttonB[i].setTag(charLower[i]);
		buttonShift.setTag(szShiftTagLower);
		buttonSym.setTag(szCharSymTag);
	}
	
	private void changeCapitalLetters1() {
		buttonShift.setVisibility(Button.VISIBLE);
		for (int i = 0; i < charUpper.length; i++)
			buttonB[i].setText(charUpper[i]);
		buttonSym.setText(szCharSymTag);
		
		buttonB[6].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[6].setPadding(0, 0, 0, 0);
		
		buttonB[7].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[7].setPadding(0, 0, 0, 0);
		
		buttonB[9].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[9].setPadding(0, 0, 0, 0);
		
		buttonB[10].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[10].setPadding(0, 0, 0, 0);
		
		buttonB[11].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[11].setPadding(0, 0, 0, 0);
	}

	private void changeCapitalTags1() {
		for (int i = 0; i < charUpper.length; i++)
			buttonB[i].setTag(charUpper[i]);
		buttonShift.setTag(szShiftTagUpper1);
		buttonSym.setTag(szCharSymTag);
	}
	
	private void changeCapitalLetters() {
		buttonShift.setVisibility(Button.VISIBLE);
		for (int i = 0; i < charUpper.length; i++)
			buttonB[i].setText(charUpper[i]);
		buttonSym.setText(szCharSymTag);
		
		buttonB[6].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[6].setPadding(0, 0, 0, 0);
		
		buttonB[7].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[7].setPadding(0, 0, 0, 0);
		
		buttonB[9].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[9].setPadding(0, 0, 0, 0);
		
		buttonB[10].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[10].setPadding(0, 0, 0, 0);
		
		buttonB[11].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[11].setPadding(0, 0, 0, 0);
	}

	private void changeCapitalTags() {
		for (int i = 0; i < charUpper.length; i++)
			buttonB[i].setTag(charUpper[i]);
		buttonShift.setTag(szShiftTagUpper);
		buttonSym.setTag(szCharSymTag);
	}

	private void changeSyNuLetters1() {

		for (int i = 0; i < charSym1.length; i++)
			buttonB[i].setText(charSym1[i]);
		buttonSym.setText(szSymCharTag);
		
		buttonB[6].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[6].setPadding(0, 0, 0, 0);
		
		buttonB[7].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[7].setPadding(0, 0, 0, 0);
		
		buttonB[9].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[9].setPadding(0, 0, 0, 0);
		
		buttonB[10].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[10].setPadding(0, 0, 0, 0);
		
		buttonB[11].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		buttonB[11].setPadding(0, 0, 0, 0);
	}

	private void changeSyNuTags1() {
		for (int i = 0; i < charSym1.length; i++)
			buttonB[i].setTag(charSym1[i]);
		buttonShift.setTag(szShiftTagLower);
		buttonSym.setTag(szSymCharTag);
	}
	
	@SuppressLint("InlinedApi")
	private void changeSyNuLetters2() {

		for (int i = 0; i < charSym2.length; i++)
			buttonB[i].setText(charSym2[i]);
		buttonSym.setText(szSymCharTag);
		
		buttonB[6].setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		buttonB[6].setPadding(20, 0, 0, 0);
		
		buttonB[7].setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		buttonB[7].setPadding(10, 0, 0, 0);
		
		buttonB[9].setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		buttonB[9].setPadding(10, 0, 0, 0);
		
		buttonB[10].setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		buttonB[10].setPadding(8, 0, 0, 0);
		
		buttonB[11].setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		buttonB[11].setPadding(4, 0, 0, 0);
	}

	private void changeSyNuTags2() {
		for (int i = 0; i < charSym2.length; i++)
			buttonB[i].setTag(charSym2[i]);
		buttonShift.setTag(szShiftTagUpper);
		buttonSym.setTag(szSymCharTag);
	}
	
	
	private void setSizeFirstRow() {
		iW = (iWindowWidth / 10);
		//iW = iW - 15;
		buttonB[16].setWidth(iW);
		buttonB[22].setWidth(iW + 3);
		buttonB[4].setWidth(iW);
		buttonB[17].setWidth(iW);
		buttonB[19].setWidth(iW);
		buttonB[24].setWidth(iW);
		buttonB[20].setWidth(iW);
		buttonB[8].setWidth(iW);
		buttonB[14].setWidth(iW);
		buttonB[15].setWidth(iW);
		
		buttonB[16].setHeight(50);
		buttonB[22].setHeight(50);
		buttonB[4].setHeight(50);
		buttonB[17].setHeight(50);
		buttonB[19].setHeight(50);
		buttonB[24].setHeight(50);
		buttonB[20].setHeight(50);
		buttonB[8].setHeight(50);
		buttonB[14].setHeight(50);
		buttonB[15].setHeight(50);

	}

	private void setSizeSecondRow() {
		iW = (iWindowWidth / 10);
		buttonB[0].setWidth(iW);
		buttonB[18].setWidth(iW);
		buttonB[3].setWidth(iW);
		buttonB[5].setWidth(iW);
		buttonB[6].setWidth(iW);
		buttonB[7].setWidth(iW);
		buttonB[26].setWidth(iW);
		buttonB[9].setWidth(iW);
		buttonB[10].setWidth(iW);
		buttonB[11].setWidth(iW);

		buttonB[0].setHeight(50);
		buttonB[18].setHeight(50);
		buttonB[3].setHeight(50);
		buttonB[5].setHeight(50);
		buttonB[6].setHeight(50);
		buttonB[7].setHeight(50);
		buttonB[9].setHeight(50);
		buttonB[10].setHeight(50);
		buttonB[11].setHeight(50);
	}

	private void setSizeThirdRow() {
		iW = (iWindowWidth / 12);
		buttonB[25].setWidth(iW);
		buttonB[23].setWidth(iW);
		buttonB[2].setWidth(iW);
		buttonB[21].setWidth(iW);
		buttonB[1].setWidth(iW);
		buttonB[13].setWidth(iW);
		buttonB[12].setWidth(iW);
		buttonBack.setWidth(iW);

		buttonB[25].setHeight(50);
		buttonB[23].setHeight(50);
		buttonB[2].setHeight(50);
		buttonB[21].setHeight(50);
		buttonB[1].setHeight(50);
		buttonB[13].setHeight(50);
		buttonB[12].setHeight(50);
		buttonBack.setHeight(50);

	}

	private void setSizeForthRow() {
		iW = (iWindowWidth / 10);
		buttonSpace.setWidth(iW * 4);
		buttonSpace.setHeight(50);
		buttonB[26].setWidth(iW);
		buttonB[26].setHeight(50);

		buttonB[27].setWidth(iW);
		buttonB[27].setHeight(50);

		buttonB[28].setHeight(50);
		buttonB[28].setWidth(iW);
		buttonEnter.setWidth(iW + (iW / 1));
		buttonEnter.setHeight(50);

	}

	private void setKeys() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		iWindowWidth = displaymetrics.widthPixels;
		
		buttonB[0]  = (Button) findViewById(R.id.idKeyChar_a);
		buttonB[1]  = (Button) findViewById(R.id.idKeyChar_b);
		buttonB[2]  = (Button) findViewById(R.id.idKeyChar_c);
		buttonB[3]  = (Button) findViewById(R.id.idKeyChar_d);
		buttonB[4]  = (Button) findViewById(R.id.idKeyChar_e);
		buttonB[5]  = (Button) findViewById(R.id.idKeyChar_f);
		buttonB[6]  = (Button) findViewById(R.id.idKeyChar_g);
		buttonB[7]  = (Button) findViewById(R.id.idKeyChar_h);
		buttonB[8]  = (Button) findViewById(R.id.idKeyChar_i);
		buttonB[9]  = (Button) findViewById(R.id.idKeyChar_j);
		buttonB[10] = (Button) findViewById(R.id.idKeyChar_k);
		buttonB[11] = (Button) findViewById(R.id.idKeyChar_l);
		buttonB[12] = (Button) findViewById(R.id.idKeyChar_m);
		buttonB[13] = (Button) findViewById(R.id.idKeyChar_n);
		buttonB[14] = (Button) findViewById(R.id.idKeyChar_o);
		buttonB[15] = (Button) findViewById(R.id.idKeyChar_p);
		buttonB[16] = (Button) findViewById(R.id.idKeyChar_q);
		buttonB[17] = (Button) findViewById(R.id.idKeyChar_r);
		buttonB[18] = (Button) findViewById(R.id.idKeyChar_s);
		buttonB[19] = (Button) findViewById(R.id.idKeyChar_t);
		buttonB[20] = (Button) findViewById(R.id.idKeyChar_u);
		buttonB[21] = (Button) findViewById(R.id.idKeyChar_v);
		buttonB[22] = (Button) findViewById(R.id.idKeyChar_w);
		buttonB[23] = (Button) findViewById(R.id.idKeyChar_x);
		buttonB[24] = (Button) findViewById(R.id.idKeyChar_y);
		buttonB[25] = (Button) findViewById(R.id.idKeyChar_z);
		buttonB[26] = (Button) findViewById(R.id.idKeyChar_sym1);
		buttonB[27] = (Button) findViewById(R.id.idKeyChar_sym2);
		buttonB[28] = (Button) findViewById(R.id.idKeyChar_sym3);
		buttonSpace = (Button) findViewById(R.id.idKeySpace);
		buttonEnter = (Button) findViewById(R.id.idKeyEnter);
		buttonShift = (Button) findViewById(R.id.idKeyShift);
		buttonBack = (Button) findViewById(R.id.idKeyBack);
		buttonSym = (Button) findViewById(R.id.idKeyChange);
		for (int i = 0; i < buttonB.length; i++) {
			buttonB[i].setOnTouchListener(this);
			buttonB[i].setTextSize(fTEXTSIZE);
			FillCustomGradient(buttonB[i],false);
			mediaBtn[i] = MediaPlayer.create(getBaseContext(), R.raw.btn_type);
			mediaBtn[i].setVolume(fLEFTVOLUME, fRIGHTVOLUME);
		}
		buttonSpace.setOnTouchListener(this);
		FillCustomGradient(buttonSpace,false);
		mediaSpace = MediaPlayer.create(getBaseContext(), R.raw.btn_type);
		mediaSpace.setVolume(fLEFTVOLUME, fRIGHTVOLUME);
		
		buttonEnter.setOnTouchListener(this);
		mediaEnter = MediaPlayer.create(getBaseContext(), R.raw.btn_type);
		mediaEnter.setVolume(fLEFTVOLUME, fRIGHTVOLUME);
		
		buttonBack.setOnTouchListener(this);
		mediaBack = MediaPlayer.create(getBaseContext(), R.raw.btn_type);
		mediaBack.setVolume(fLEFTVOLUME, fRIGHTVOLUME);
		
		buttonShift.setOnTouchListener(this);
		mediaShift = MediaPlayer.create(getBaseContext(), R.raw.btn_type);
		mediaShift.setVolume(fLEFTVOLUME, fRIGHTVOLUME);
		
		buttonSym.setOnTouchListener(this);
		FillCustomGradient(buttonSym,false);
		mediaSym = MediaPlayer.create(getBaseContext(), R.raw.btn_type);
		mediaSym.setVolume(fLEFTVOLUME, fRIGHTVOLUME);

	}
	
	private void init () {
		
		szShiftTagLower  = "lower" ;
		szShiftTagUpper  = "UPPER" ;
		szShiftTagUpper1 = "UPPER1" ;
    	szSymCharTag     = "abc" ;
    	szCharSymTag     = "12#" ;
		
		for (int i = 0; i < iCountTmp.length; i++) {
        	iCountTmp[i] = 0;
        }
		final TextView textViewCount = (TextView)findViewById(R.id.textView_count);
    	textViewCount.setText("Sample: "+iCount);
    	final TextView textViewCountTmp = (TextView)findViewById(R.id.textView_counttmp);
    	textViewCountTmp.setText("Count: "+iCountTmp[iCount]);
		
    	
    	szSamples[0] = "";
		szSamples[1] = "258649137";
		szSamples[2] = "Live life to the fullest because you only get to live once.";
		szSamples[3] = "bioinformatics";
		szSamples[4] = "We are the world, we are the children\nWe are the ones who make a brighter day\nSo let's start giving";
    	
	}
	
		
}
