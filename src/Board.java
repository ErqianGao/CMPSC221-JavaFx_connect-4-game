/**
 * Board.java
 * this class will create a connect 4 game with javaFX
 *
 * @author Erqian Gao
 */
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class Board extends Application
{
    //a two dimensional array that contains the rectangles needed for the board
    Rectangle[][] spots = new Rectangle[7][6];
    GridPane board;
    Label[] rows  = new Label[7];
    //a column of rectangles that will indicate the current row
    Rectangle[] indicators = new Rectangle[7];
    Button moveLeft;
    Button moveRight;
    Button set;
    Button reset;
    Button back;
    //contains the current location of the disc that is just been set
    int currentRow = 0;
    int currentColumn = 0;
    //contain the location of last disc for the back method
    int lastRow = 0;
    int lastColumn = 0;
    Color currentColor = Color.RED;
    Color lastColor = Color.YELLOW;
    boolean finished = false;
    String winner = "";

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        board = new GridPane();

        for (int i = 0; i < 7; i++)
        {
            indicators[i] = new Rectangle();
            indicators[i].setWidth( 80 );
            indicators[i].setHeight( 40 );
            indicators[i].setFill( Color.WHITE );
            board.add( indicators[i], i , 0 );

            rows[i] = new Label( " rows " + new Integer( i ).toString() );
            rows[i].setTextAlignment( TextAlignment.CENTER );
            rows[i].setTextFill( Color.BLUE );
            rows[i].setMinSize( 80, 40 );
            board.add( rows[i], i , 1 );
        }
        indicators[currentRow].setFill( currentColor );

        //this will set the board to initial value
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                spots[i][j] = new Rectangle();
                spots[i][j].setHeight( 80 );
                spots[i][j].setWidth( 80 );
                board.add( spots[i][j], i  , j + 2 );
                spots[i][j].setFill( Color.WHITE );
            }
        }
        moveLeft = new Button("move left");
        moveLeft.setOnAction( new moveLeftHandler() );

        moveRight = new Button( "move right");
        moveRight.setOnAction( new moveRightHandler() );

        set = new Button( "set the disc" );
        set.setOnAction( new setHandler() );

        reset = new Button( "RESET" );
        reset.setOnAction( new resetHandler() );

        back = new Button( "Back" );
        back.setOnAction( new backHandler() );

        board.setGridLinesVisible( true );

        HBox moveButtons = new HBox( 80, moveLeft, moveRight );
        moveButtons.setPadding( new Insets( 10 ) );
        moveButtons.setAlignment( Pos.CENTER );

        HBox setButton = new HBox( set );
        setButton.setAlignment( Pos.CENTER );

        HBox backButton = new HBox( back );
        backButton.setAlignment( Pos.CENTER );
        backButton.setPadding( new Insets( 10 ) );

        HBox resetButton = new HBox( reset );
        resetButton.setAlignment( Pos.CENTER );
        resetButton.setPadding( new Insets( 10 ) );

        board.setPadding( new Insets( 20 ) );

        VBox total = new VBox( 10, moveButtons, setButton, backButton, board, resetButton );


        Scene scene = new Scene( total );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "Connect 4 Game" );
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch( args );
    }

    /**
     * check if any side wins with check method in four direction and checkFull method
     * will print the winner or tie
     */
    public void checkWin()
    { ;
        if( checkFull() && !checkHorizontal() && !checkVertical() && !checkLeftUp() && !checkRightUp() )
        {
            set.setOnAction( null );

            System.out.println( "Tie!" );
            System.out.println();
        }
        if( checkHorizontal() || checkVertical() || checkLeftUp() || checkRightUp() )
        {
            if( lastColor.equals( Color.RED ) )
            {
                winner = "RED";
            }
            else
            {
                winner = "YELLOW";
            }
            set.setOnAction( null );

            System.out.println( "The winner is " + winner );
            System.out.println();
        }
    }

    /**
     * this will return the result of check horizontally
     * if there is four or more rectangle with same color in one column that means the color wins
     * @return will return true if the color wins
     */
    public boolean checkHorizontal()
    {
        int i = 0;
        int count = 0;
        while( currentRow - i > -1 )
        {
            if( !spots[currentRow-i][currentColumn].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        /**
         * this i is set to one because the rectangle at current location is already
         * counted once and I don't want it to be down twice
         */
        i = 1;

        while( currentRow + i < 7 )
        {
            if( !spots[currentRow+i][currentColumn].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        if( count >= 4 )
        {
            return true;
        }

        return false;
    }

    /**
     * this will return the result of check vertically
     * if there is four or more rectangle with same color in one row that means the color wins
     * @return will return true if the color wins
     */
    public boolean checkVertical()
    {
        int i = 0;
        int count = 0;
        while( currentColumn - i > -1 )
        {
            if( !spots[currentRow][currentColumn-i].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        /**
         * this i is set to one because the rectangle at current location is already
         * counted once and I don't want it to be down twice
         */
        i = 1;

        while( currentColumn + i < 6 )
        {
            if( !spots[currentRow][currentColumn+i].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        if( count >= 4 )
        {
            return true;
        }
        return false;
    }

    /**
     * this will return the result of check diagonally, in left up and right down direction
     * @return will return true if the color wins
     */
    public boolean checkLeftUp()
    {
        int i = 0;
        int count = 0;
        while( currentRow - i > -1 && currentColumn - i > -1 )
        {
            if( !spots[currentRow-i][currentColumn-i].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        /**
         * this i is set to one because the rectangle at current location is already
         * counted once and I don't want it to be down twice
         */
        i = 1;

        while( currentRow + i < 7 && currentColumn + i < 6 )
        {
            if( !spots[currentRow+i][currentColumn+i].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        if( count >= 4 )
        {
            return true;
        }
        return false;
    }

    /**
     * this will return the result of check diagonally, in left down and right up direction
     * @return will return true if the color wins
     */
    public boolean checkRightUp()
    {
        int i = 0;
        int count = 0;
        while( currentRow - i > -1 && currentColumn + i < 6 )
        {
            if( !spots[currentRow-i][currentColumn+i].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        /**
         * this i is set to one because the rectangle at current location is already
         * counted once and I don't want it to be down twice
         */
        i = 1;

        while( currentRow + i < 7 && currentColumn - i > -1 )
        {
            if( !spots[currentRow+i][currentColumn-i].getFill().equals( lastColor ) )
            {
                break;
            }
            count++;
            i++;
        }

        if( count >= 4 )
        {
            return true;
        }
        return false;
    }

    /**
     * check the spots one by one, if none of them is white, that shows the spot is full
     * @return will return true if the spots are full
     */
    public boolean checkFull()
    {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                if( spots[i][j].getFill().equals( Color.WHITE ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * will move the indicator and the currentRow to left for one spot
     */
    class moveLeftHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            if( currentRow > 0 )
            {
                indicators[currentRow].setFill( Color.WHITE );
                indicators[currentRow - 1].setFill( currentColor );
                currentRow--;
            }
        }
    }

    /**
     * will move the indicator and the currentRow to right for one spot
     */
    class moveRightHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            if( currentRow < 6 )
            {
                indicators[currentRow].setFill( Color.WHITE );
                indicators[currentRow + 1].setFill( currentColor );
                currentRow++;
            }
        }
    }

    /**
     * will set a disc in currentColor at the lowest spot in that row
     * will activate checkWin method every time it is clicked
     */
    class setHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            boolean set = false;
            lastRow = currentRow;
            for (int i = 5; i > -1; i--)
            {
                if ( spots[currentRow][i].getFill().equals(Color.WHITE) )
                {
                    spots[currentRow][i].setFill(currentColor);
                    set = true;
                    currentColumn = i;
                    break;
                }
            }

            lastColumn = currentColumn;

            if (set == true)
            {
                if (currentColor.equals(Color.RED))
                {
                    lastColor = currentColor;
                    currentColor = Color.YELLOW;
                }
                else
                {
                    lastColor = currentColor;
                    currentColor = Color.RED;
                }
                indicators[currentRow].setFill(currentColor);
            }
            checkWin();
        }
    }

    /**
     * will clear all the data, set the game to initial state
     */
    class resetHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            for (int i = 0; i < 7; i++)
            {
                for (int j = 0; j < 6; j++)
                {
                    spots[i][j].setFill( Color.WHITE );
                }
            }

            indicators[currentRow].setFill( Color.WHITE );
            currentColor = Color.RED;
            lastColor = Color.YELLOW;
            currentRow = 0;
            currentColumn = 0;
            set.setOnAction( new setHandler() );

            indicators[currentRow].setFill( currentColor );
        }
    }

    /**
     * this will reset the last step
     * I came up with this idea when I tried to play the game with my friend
     * we discovered that it would be very inconvenient if we need to reset
     * the whole game if we mistakenly clicked
     *
     * there will also be a popup window with a gif of Thanos using time stone
     * unfortunately, I can neither control the stop of gif nor close the window
     * after a specific time, so the window need to be closed manually.
     * but I think this is really cool anyway.
     */
    class backHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            if( !spots[lastRow][lastColumn].getFill().equals( Color.WHITE ) )
            {
                Stage stage = new Stage();
                Image image = new Image( "file:LittleGiantAxisdeer-size_restricted.gif" );
                ImageView imageView = new ImageView( image );
                HBox hBox = new HBox( imageView );

                stage.setTitle( "time goes backwards" );
                stage.setScene( new Scene( hBox ) );
                stage.show();

                spots[lastRow][lastColumn].setFill(Color.WHITE);

                if( currentColor.equals( Color.RED ) )
                {
                    lastColor = currentColor;
                    currentColor = Color.YELLOW;
                }
                else
                {
                    lastColor = currentColor;
                    currentColor = Color.RED;
                }
                indicators[currentRow].setFill(currentColor);
            }
        }
    }
}
