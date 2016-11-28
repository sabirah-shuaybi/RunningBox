import java.awt.*;
import objectdraw.*;
import java.util.Random;
import java.lang.*;

/**
 * Class RunningBox displays a pure blue, draggable box when program starts.
 * The "Running" box jumps to a new, randomly chosen location within the canvas every time user
 * clicks on it. If the user's click is outside of the box, the box's color becomes greener
 * until a certain point. Clicking or dragging the box results in the box reverting to it's
 * blue color.
 *
 * @author Sabirah Shuaybi
 * @version 9/27/16
 */

public class RunningBox extends WindowController
{
    private FilledRect box;

    //Box is a square of size 50 - constant
    private static final int BOX_SIZE = 50;

    //Define the pure blue color of box
    private static final Color BLUE = new Color(0, 0, 255);

    //To store lastPoint of mouse to enable dragging of box
    private Location lastPoint;

    //Boolean flag for whether or not the box is pressed on
    private boolean boxGrabbed = false;

    /* Displays a blue box centered on canvas */
    public void begin() {

        //Compute center of canvas to position box in center when program starts
        double canvasCenterX = canvas.getWidth()/2;
        double canvasCenterY = canvas.getHeight()/2;

        //Compute center of box relative to canvas center
        double boxCenteredX = canvasCenterX - (BOX_SIZE/2);
        double boxCenteredY = canvasCenterY - (BOX_SIZE/2);

        //Construct box
        box = new FilledRect(boxCenteredX, boxCenteredY, BOX_SIZE, BOX_SIZE, canvas);

        //Set its color to pure blue
        box.setColor(BLUE);

    }
    /* Decreases blue component of box by 20 and increases green component by 20 */
    private void changeColor() {

        //Debugging
        System.out.println("Entered changeColor");

        //Get box's current color
        Color boxColor = box.getColor();
        //Get green component of box's current color
        int currentGreen = boxColor.getGreen();
        //Get blue component of box's current color
        int currentBlue = boxColor.getBlue();

        //Add 20 to current green component and assign it to a local var
        int newGreen = currentGreen + 20;
        //Subtract 20 from current blue component and assign it to a local var
        int newBlue = currentBlue - 20;

        //Prevent green component from exceeding 255 which is the max for RGB scale
        if (newGreen > 255) {
            newGreen = 255;
        }

        //Prevent blue component from declining into the negatives (Min 0)
        if (newBlue < 0) {
            newBlue = 0;
        }

        //Create a new Color with these green and blue color alterations
        Color newBoxColor = new Color (0, newGreen, newBlue);
        //Set the color of box to this new color
        box.setColor(newBoxColor);

    }
    /* Returns a random location */
    private Location getRandomLocation() {

        Random r = new Random();

        //Generate two random values double values for x and y coordinate
        //Ensure that the random value is within the canvas, not outside
        //Ensure that the entire box will always be visible in outline of canvas
        double randomLocationX = r.nextDouble() * (canvas.getWidth() - box.getWidth());
        double randomLocationY = r.nextDouble() * (canvas.getHeight() - box.getHeight());

        //Return this new random location
        return new Location(randomLocationX, randomLocationY);

    }

    public void onMouseClick(Location point) {

        System.out.println("Entered onMouseClick");

        //If box is clicked, move box to a random location and set its color back to blue
        if (box.contains(point)) {
            box.moveTo(getRandomLocation());
            box.setColor(BLUE);
        }
        //Else, if click is outside of box, change box's color
        else if (!(box.contains(point))) {
            changeColor();
        }
    }

    public void onMousePress(Location point) {

        //If box is pressed on, set boxGrabbed to true and reset box color to blue
        if (box.contains(point)) {
            boxGrabbed = true;
            box.setColor(BLUE);
        }
        //Capture the value of point into instance variable lastPoint for dragging
        lastPoint = point;
    }

    public void onMouseDrag(Location point) {

        System.out.println("Entered onMouseDrag");

        //Distance x that box is dragged by user
        double distanceX = point.getX() - lastPoint.getX();
        //Distance y that box is dragged by user
        double distanceY = point.getY() - lastPoint.getY();

        //If box is grabbed, move box the distance dragged
        if (boxGrabbed) {
            box.move(distanceX, distanceY);
        }

        //Store point into lastPoint to remember the extent of drag
        lastPoint = point;
    }

    public void onMouseRelease(Location point) {

        //Set boxGrabbed back to false on onMouseRelease
        //To ensure no dragging of box without the mouse press contained within it
        boxGrabbed = false;
    }
}
