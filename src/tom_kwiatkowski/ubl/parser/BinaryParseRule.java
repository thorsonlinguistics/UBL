/***************************  LICENSE  *******************************
* This file is part of UBL.
* 
* UBL is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as 
* published by the Free Software Foundation, either version 3 of the 
* License, or (at your option) any later version.
* 
* UBL is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public 
* License along with UBL.  If not, see <http://www.gnu.org/licenses/>.
***********************************************************************/



package tom_kwiatkowski.ubl.parser;

import java.util.*;

public interface BinaryParseRule {

    /* 
       takes two cell, left and right, as input.  assumes
       these cells are adjacent.  adds any new cells it can
       produce to the result list.
    */
    abstract public void newCellsFrom(Cell left, Cell right, List result);


    

}
