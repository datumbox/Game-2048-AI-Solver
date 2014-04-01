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
package com.datumbox.opensource.dataobjects;

/**
 * Action Status enum.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public enum ActionStatus {
    /**
     * Successful move, the game continues.
     */
    CONTINUE(0, "Successful move, the game continues."),
    
    /**
     * Game completed successfully.
     */
    WIN(1, "You won, the game ended!"),
    
    /**
     * No more moves, end of game.
     */
    NO_MORE_MOVES(2,"No more moves, the game ended!"),
    
    /**
     * Invalid move, move can't be performed.
     */
    INVALID_MOVE(3,"Invalid move!");
    
    /**
     * The numeric code of the status
     */
    private final int code;
    
    /**
     * The description of the status
     */
    private final String description;
    
    /**
     * Constructor
     * 
     * @param code
     * @param description 
     */
    private ActionStatus(final int code, final String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * Getter for code.
     * 
     * @return 
     */
    public int getCode() {
        return code;
    }
 
    /**
     * Getter for description.
     * 
     * @return 
     */
    public String getDescription() {
        return description;
    }
}
