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
 * Direction enum.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public enum Direction {
    /**
     * Move Up
     */
    UP(0,"Up"), 
    
    /**
     * Move Right
     */
    RIGHT(1,"Right"), 
    
    /**
     * Move Down
     */
    DOWN(2,"Down"), 
    
    /**
     * Move Left
     */
    LEFT(3,"Left");
    
    
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
    private Direction(final int code, final String description) {
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
    
    /**
     * Overloads the toString and returns the description of the move.
     * @return 
     */
    @Override
    public String toString() { 
        return description;
    }
}
