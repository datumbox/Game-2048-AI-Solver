/* 
 * Copyright (C) 2014 Vasilis Vryniotis <bbriniotis at datumbox.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.datumbox.opensource.game;

import com.datumbox.opensource.dataobjects.ActionStatus;
import com.datumbox.opensource.dataobjects.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The main class of the Game 2048.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public class Board implements Cloneable {
    /**
     * The size of the board
     */
    public static final int BOARD_SIZE = 4;
    
    /**
     * The maximum combination in which the game terminates
     */
    public static final int TARGET_POINTS = 2048;
    
    /**
     * The theoretical minimum win score until the target point is reached
     */
    public static final int MINIMUM_WIN_SCORE = 18432;
    
    /**
     * The score so far
     */
    private int score=0;
    
    /**
     * The board values
     */
    private int[][] boardArray;
    
    /**
     * Random Generator which is used in the creation of random cells
     */
    private final Random randomGenerator;
    
    /**
     * It caches the number of empty cells
     */
    private Integer cache_emptyCells=null;
    
    /**
     * Constructor without arguments. It initializes randomly the Board
     */
    public Board() {
        boardArray = new int[BOARD_SIZE][BOARD_SIZE];
        randomGenerator = new Random(System.currentTimeMillis());
        
        addRandomCell();
        addRandomCell();
        
    }
    
    /**
     * Deep clone
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Board copy = (Board)super.clone();
        copy.boardArray = clone2dArray(boardArray);
        return copy;
    }
    
    /**
     * Getter for score attribute
     * 
     * @return 
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Getter for BoardArray
     * @return 
     */
    public int[][] getBoardArray() {
        return clone2dArray(boardArray);
    }
    
    /**
     * Getter for RandomGenerator field
     * 
     * @return 
     */
    public Random getRandomGenerator() {
        return randomGenerator;
    }
    
    /**
     * Performs one move (up, down, left or right).
     * 
     * @param direction
     * @return 
     */
    public int move(Direction direction) {    
        int points = 0;
        
        //rotate the board to make simplify the merging algorithm
        if(direction==Direction.UP) {
            rotateLeft();
        }
        else if(direction==Direction.RIGHT) {
            rotateLeft();
            rotateLeft();
        }
        else if(direction==Direction.DOWN) {
            rotateRight();
        }
        
        for(int i=0;i<BOARD_SIZE;++i) {
            int lastMergePosition=0;
            for(int j=1;j<BOARD_SIZE;++j) {
                if(boardArray[i][j]==0) {
                    continue; //skip moving zeros
                }
                
                int previousPosition = j-1;
                while(previousPosition>lastMergePosition && boardArray[i][previousPosition]==0) { //skip all the zeros
                    --previousPosition;
                }
                
                if(previousPosition==j) {
                    //we can't move this at all
                }
                else if(boardArray[i][previousPosition]==0) {
                    //move to empty value
                    boardArray[i][previousPosition]=boardArray[i][j];
                    boardArray[i][j]=0;
                }
                else if(boardArray[i][previousPosition]==boardArray[i][j]){
                    //merge with matching value
                    boardArray[i][previousPosition]*=2;
                    boardArray[i][j]=0;
                    points+=boardArray[i][previousPosition];
                    lastMergePosition=previousPosition+1;
                    
                }
                else if(boardArray[i][previousPosition]!=boardArray[i][j] && previousPosition+1!=j){
                    boardArray[i][previousPosition+1]=boardArray[i][j];
                    boardArray[i][j]=0;
                }
            }
        }
        
        
        score+=points;
        
        //reverse back the board to the original orientation
        if(direction==Direction.UP) {
            rotateRight();
        }
        else if(direction==Direction.RIGHT) {
            rotateRight();
            rotateRight();
        }
        else if(direction==Direction.DOWN) {
            rotateLeft();
        }
        
        return points;
    }
    
    /**
     * Returns the Ids of the empty cells. The cells are numbered by row.
     * 
     * @return 
     */
    public List<Integer> getEmptyCellIds() {
        List<Integer> cellList = new ArrayList<>();
        
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                if(boardArray[i][j]==0) {
                    cellList.add(BOARD_SIZE*i+j);
                }
            }
        }
        
        return cellList;
    }
    
    /**
     * Counts the number of empty cells
     * 
     * @return 
     */
    public int getNumberOfEmptyCells() {
        if(cache_emptyCells==null) {
            cache_emptyCells = getEmptyCellIds().size();
        }
        return cache_emptyCells;
    }
    
    /**
     * Checks if any of the cells in the board has value equal or larger than the
     * target.
     * 
     * @return 
     */
    public boolean hasWon() {
        if(score<MINIMUM_WIN_SCORE) { //speed optimization
            return false;
        }
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                if(boardArray[i][j]>=TARGET_POINTS) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks whether the game is terminated
     * 
     * @return 
     * @throws java.lang.CloneNotSupportedException 
     */
    public boolean isGameTerminated() throws CloneNotSupportedException {
        boolean terminated=false;
        
        if(hasWon()==true) {
            terminated=true;
        }
        else {
            if(getNumberOfEmptyCells()==0) { //if no more available cells
                Board copyBoard = (Board) this.clone();
                                
                if(copyBoard.move(Direction.UP)==0 
                   && copyBoard.move(Direction.RIGHT)==0 
                   && copyBoard.move(Direction.DOWN)==0 
                   && copyBoard.move(Direction.LEFT)==0) {
                    terminated=true;
                }
                
                //copyBoard=null;
            }
        }
        
        return terminated;
    }
    
    /**
     * Performs an Up, Right, Down or Left move
     * 
     * @param direction
     * @return 
     * @throws java.lang.CloneNotSupportedException 
     */
    public ActionStatus action(Direction direction) throws CloneNotSupportedException {
        ActionStatus result = ActionStatus.CONTINUE;
        int newPoints = move(direction);
        
        //add random cell
        boolean newCellAdded = addRandomCell();
        
        if(newPoints==0 && newCellAdded==false) {
            if(isGameTerminated()) {
                result = ActionStatus.NO_MORE_MOVES;
            }
            else {
                result = ActionStatus.INVALID_MOVE;
            }
        }
        else {
            if(newPoints>=TARGET_POINTS) {
                result = ActionStatus.WIN;
            }
            else {
                if(isGameTerminated()) {
                    result = ActionStatus.NO_MORE_MOVES;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Sets the value to an empty cell. 
     * 
     * @param i
     * @param j
     * @param value 
     */
    public void setEmptyCell(int i, int j, int value) {
        if(boardArray[i][j]==0) {
            boardArray[i][j]=value;
            cache_emptyCells=null;
        }
    }
    
    /**
     * Rotates the board on the left
     */
    private void rotateLeft() {
        int[][] rotatedBoard = new int[BOARD_SIZE][BOARD_SIZE];
        
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                rotatedBoard[BOARD_SIZE-j-1][i] = boardArray[i][j];
            }
        }
        
        boardArray=rotatedBoard;
    }
    
    /**
     * Rotates the board on the right
     */
    private void rotateRight() {
        int[][] rotatedBoard = new int[BOARD_SIZE][BOARD_SIZE];
        
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                rotatedBoard[i][j]=boardArray[BOARD_SIZE-j-1][i];
            }
        }
        
        boardArray=rotatedBoard;
    }
    
    /**
     * Creates a new Random Cell
     */
    private boolean addRandomCell() {
        List<Integer> emptyCells = getEmptyCellIds();
        
        int listSize=emptyCells.size();
        
        if(listSize==0) {
            return false;
        }
        
        int randomCellId=emptyCells.get(randomGenerator.nextInt(listSize));
        int randomValue=(randomGenerator.nextDouble()< 0.9)?2:4;
        
        int i = randomCellId/BOARD_SIZE;
        int j = randomCellId%BOARD_SIZE;
        
        setEmptyCell(i, j, randomValue);
        
        return true;
    }
    
    /**
     * Clones a 2D array
     * 
     * @param original
     * @return 
     */
    private int[][] clone2dArray(int[][] original) { 
        int[][] copy = new int[original.length][];
        for(int i=0;i<original.length;++i) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
