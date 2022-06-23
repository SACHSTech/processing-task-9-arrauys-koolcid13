import processing.core.PApplet;
import processing.core.PImage;

/**
* A program Sketch.java that demonstrates snowfall and player needs to get to the finish point without touching snow
* @author: Avin A.
*
*/

public class Sketch extends PApplet {
	
	PImage snowflake1;
  PImage snowflake2;
  PImage snowman;

  float [] fltCircleLeft = new float[25];
  float [] fltCircleUp = new float[25];
  float [] fltCircleRight = new float[25];
  float [] fltCircleDown = new float[25];
  boolean [] ballHideStatus = new boolean[25];
  int [] intSnowflakeFig = new int[25];

  float fltManX = 10;
  float fltManY = 320;

  double dblFallSpeed = 1;

  int intMistakes = 0, intLoopCnt = 0, intLastTouch = -120;
  boolean aPressed = false, wPressed = false, sPressed = false, dPressed = false;
 
  /**
   * Initial settings happens in it
   *
   */
  public void settings() {
    size(800, 400);
  }

  /**
   * Is called once and to set the initial environment code is to happen in
   *
   */
  public void setup() {
    background(122, 177, 240);
    
    for (int i = 0; i < 25; i ++) {
      // randomly chooses snowflake1 or snowflake2 for each index
      float fltImgChooser = random (2);
      if (fltImgChooser < 1) {
        intSnowflakeFig[i] = 1;
      }
      else {
        intSnowflakeFig[i] = 2;
      }

      // some x-values are randomly generated while others are not in order to have a playable setting
      if (i % 3 == 0) {
        fltCircleLeft[i] = random (width);
      }
      else {
        fltCircleLeft[i] = (i + 1) * (width / 26);
      }
      fltCircleUp[i] = random (height);
      fltCircleRight[i] = fltCircleLeft[i] + 30;
      fltCircleDown[i] = fltCircleUp[i] + 30;
      ballHideStatus[i] = false;
    }

    snowflake1 = loadImage ("snowflake1.png");
    snowflake1.resize(30, 30);
    snowflake2 = loadImage ("snowflake2.png");
    snowflake2.resize(30, 30);
    snowman = loadImage ("snowman.png");
    snowman.resize (30, 73);
  }
  
  /**
   * Is called continuously and executes the codes within it infinite times
   *
   */
  public void draw() {
    intLoopCnt ++;
    // if user reaches destination
    if (fltManY < 30 && fltManX > 730) {
      intMistakes = -1;
    }

    // if user still has lives
    if (intMistakes < 3 && intMistakes > -1) {
      background(122, 177, 240);
      fill (54, 7, 87);
      stroke(54, 7, 87);
      textSize(14);
      text ("get here", 730, 20);

      for (int i = 0; i < 25; i ++) {
        if (ballHideStatus[i] == false) {
          if (intSnowflakeFig[i] == 1) {
            fltCircleUp[i] = (fltCircleUp[i] + (float) dblFallSpeed) % height;
            fltCircleDown[i] = fltCircleUp[i] + 30;
            image (snowflake1, fltCircleLeft[i], fltCircleUp[i]);
          }
          else {
            fltCircleUp[i] = (fltCircleUp[i] + (float) dblFallSpeed) % height;
            fltCircleDown[i] = fltCircleUp[i] + 30;
            image (snowflake2, fltCircleLeft[i], fltCircleUp[i]);
          }

          // new collisions are checked every 2 seconds to avoid counting collisions more than once
          if  (intLoopCnt - intLastTouch > 120 && fltManX + 30 > fltCircleLeft[i] && fltManX < fltCircleRight[i] && fltManY + 73 > fltCircleUp[i] && fltManY < fltCircleDown[i]) {
            intMistakes ++;
            intLastTouch = intLoopCnt;
          }
        }
      }

      if (aPressed == true) {
        fltManX --;
      }
      if (wPressed == true) {
        fltManY --;
      }
      if (sPressed == true) {
        fltManY ++;
      }
      if (dPressed == true) {
        fltManX ++;
      }
      image (snowman, fltManX, fltManY);
      
      displayLives (intMistakes);
    }

    // if user loses
    else if (intMistakes >= 3) {
      background(255);
      fill (0);
      stroke (0);
      textSize(20);
      text ("you lost :)", 350, 200);
    }

    // if user wins
    else {
      background(255);
      fill (0);
      stroke (0);
      textSize(20);
      text ("you won :)", 350, 200);
    }
    
  }

  /**
   * Is called every time a key is pressed and the key that was pressed is stored in the 'key' variable
   *
   */
  public void keyPressed () {
    // up and down indicate snowfall speed
    if (keyCode == UP) {
      dblFallSpeed -= 0.45;
    }
    else if (keyCode == DOWN) {
      dblFallSpeed += 0.45;
    }
    // shows if wasd keys are being used
    else if (key == 'a') {
      aPressed = true;
    }
    else if (key == 'w') {
      wPressed = true;
    }
    else if (key == 's') {
      sPressed = true;
    }
    else if (key == 'd') {
      dPressed = true;
    }
  }

  /**
   * Is called every time a key is released and the key that was released is stored in the 'key' variable
   *
   */
  public void keyReleased () {
    // shows if wasd keys are being released
    if (key == 'a') {
      aPressed = false;
    }
    else if (key == 'w') {
      wPressed = false;
    }
    else if (key == 's') {
      sPressed = false;
    }
    else if (key == 'd') {
      dPressed = false;
    }
  }

  /**
   * Is called every time a mouse button is pressed and released
   *
   */
  public void mouseClicked () {
    for (int i = 0; i < 25; i ++) {
      // if any snowflake is clicked on
      if (mouseX > fltCircleLeft[i] && mouseX < fltCircleRight[i] && mouseY > fltCircleUp[i] && mouseY < fltCircleDown[i]) {
        ballHideStatus[i] = true;
      }
    }
  }
  
  /**
   * Based on the number of mistakes, draws green and red squares to show remaining lives
   *
   * @param lives  number of lives lost
   *
   */
  private void displayLives (int lives) {
    for (int i = 3; i > 0; i --) {
      if (i <= intMistakes) {
        fill (232, 80, 70);
        stroke (232, 80, 70);
      }
      else {
        fill (97, 217, 67);
        stroke (97, 217, 67);
      }
      rect ((i * 15), 10, 10, 10);
    }
  }
  
}