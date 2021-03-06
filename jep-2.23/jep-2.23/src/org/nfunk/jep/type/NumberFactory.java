/*****************************************************************************

JEP - Java Expression Parser
    JEP is a Java package for parsing and evaluating mathematical
	expressions. It currently supports user defined variables,
	constant, and functions. A number of common mathematical
	functions and constants are included.

Author: Nathan Funk
Copyright (C) 2000 Nathan Funk

    JEP is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    JEP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JEP; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*****************************************************************************/

package org.nfunk.jep.type;

/**
 * This interface can be implemented to create numbers of any object type.
 * By implementing this interface and calling the setNumberFactory() method of
 * the JEP class, the constants in an expression will be created with that
 * class.
 */
public interface NumberFactory {
	
	/**
	 * Creates a number object and initializes its value.
	 * @param value The initial value of the number.
	 */
	public Object createNumber(double value);
}