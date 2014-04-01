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
package com.datumbox.opensource;

import com.datumbox.opensource.ai.AIsolver;
import com.datumbox.opensource.dataobjects.ActionStatus;
import com.datumbox.opensource.game.Board;
import com.datumbox.opensource.dataobjects.Direction;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * The main class of the Game.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public class ConsoleGame {

    /**
     * Main function of the game.
     * 
     * @param args
     * @throws CloneNotSupportedException 
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        
        System.out.println("The 2048 Game in JAVA!");
        System.out.println("======================");
        System.out.println();
        System.out.println("The Game is developed by Vasilis Vryniotis, visit Datumbox.com for more information. This implementation is based on the ideas and projects of Gabriele Cirulli and Matt Overlan.");
        while(true) {
            printMenu();
            int choice;
            try {
                Scanner sc = new Scanner (System.in);     
                choice = sc.nextInt();
                switch (choice) {
                    case 1:  playGame();
                             break;
                    case 2:  calculateAccuracy();
                             break;
                    case 3:  help();
                             break;
                    case 4:  return;
                    default: throw new Exception();
                }
            }
            catch(Exception e) {
                System.out.println("Wrong choice");
            }
        }
    }
    
    /**
     * Prints Help menu
     */
    public static void help() {
        System.out.println("Seriously?!?!?");
    }
    
    /**
     * Prints main menu
     */
    public static void printMenu() {
        System.out.println();
        System.out.println("Choices:");
        System.out.println("1. Play the 2048 Game");
        System.out.println("2. Estimate the Accuracy of AI Solver");
        System.out.println("3. Help");
        System.out.println("4. Quit");
        System.out.println();
        System.out.println("Enter a number from 1-4:");
    }
    
    /**
     * Estimates the accuracy of the AI solver by running multiple games.
     * 
     * @throws CloneNotSupportedException 
     */
    public static void calculateAccuracy() throws CloneNotSupportedException {
        int wins=0;
        int total=10;
        System.out.println("Running "+total+" games to estimate the accuracy:");
        
        for(int i=0;i<total;++i) {
            int hintDepth = 7;
            Board theGame = new Board();
            Direction hint = AIsolver.findBestMove(theGame, hintDepth);
            ActionStatus result=ActionStatus.CONTINUE;
            while(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE) {
                result=theGame.action(hint);

                if(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE ) {
                    hint = AIsolver.findBestMove(theGame, hintDepth);
                }
            }

            if(result == ActionStatus.WIN) {
                ++wins;
                System.out.println("Game "+(i+1)+" - won");
            }
            else {
                System.out.println("Game "+(i+1)+" - lost");
            }
        }
        
        System.out.println(wins+" wins out of "+total+" games.");
    }
    
    /**
     * Method which allows playing the game.
     * 
     * @throws CloneNotSupportedException 
     */
    public static void playGame() throws CloneNotSupportedException {
        System.out.println("Play the 2048 Game!"); 
        System.out.println("Use 8 for UP, 6 for RIGHT, 2 for DOWN and 4 for LEFT. Type a to play automatically and q to exit. Press enter to submit your choice.");
        
        int hintDepth = 7;
        Board theGame = new Board();
        Direction hint = AIsolver.findBestMove(theGame, hintDepth);
        printBoard(theGame.getBoardArray(), theGame.getScore(), hint);
        
        try {
            InputStreamReader unbuffered = new InputStreamReader(System.in, "UTF8");
            char inputChar;
            
            ActionStatus result=ActionStatus.CONTINUE;
            while(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE) {
                inputChar = (char)unbuffered.read();
                //inputChar = 'a';
                if(inputChar=='\n' || inputChar=='\r') {
                    continue;
                }
                else if(inputChar=='8') {
                    result=theGame.action(Direction.UP);
                }
                else if(inputChar=='6') {
                    result=theGame.action(Direction.RIGHT);
                }
                else if(inputChar=='2') {
                    result=theGame.action(Direction.DOWN);
                }
                else if(inputChar=='4') {
                    result=theGame.action(Direction.LEFT);
                }
                else if(inputChar=='a') {
                    result=theGame.action(hint);
                }
                else if(inputChar=='q') {
                    System.out.println("Game ended, user quit.");
                    break;
                }
                else {
                    System.out.println("Invalid key! Use 8 for UP, 6 for RIGHT, 2 for DOWN and 4 for LEFT. Type a to play automatically and q to exit. Press enter to submit your choice.");
                    continue;
                }
                
                if(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE ) {
                    hint = AIsolver.findBestMove(theGame, hintDepth);
                }
                else {
                    hint = null;
                }
                printBoard(theGame.getBoardArray(), theGame.getScore(), hint);
                
                if(result!=ActionStatus.CONTINUE) {
                    System.out.println(result.getDescription());
                }
            }
        } 
        catch (IOException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Prints the Board
     * 
     * @param boardArray
     * @param score 
     * @param hint 
     */
    public static void printBoard(int[][] boardArray, int score, Direction hint) {
        System.out.println("-------------------------");
        System.out.println("Score:\t" + String.valueOf(score));
        System.out.println();
        System.out.println("Hint:\t" + hint);
        System.out.println();
        
        for(int i=0;i<boardArray.length;++i) {
            for(int j=0;j<boardArray[i].length;++j) {
                System.out.print(boardArray[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }
}
