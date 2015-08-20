package com.temenos.interaction.core.command;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2013 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregateCommandController implements CommandController {
    private final Logger logger = LoggerFactory.getLogger(AggregateCommandController.class);
    private final ArrayList<CommandController> elements = new ArrayList<CommandController>(4);
    
    public AggregateCommandController(Iterable<? extends CommandController> elements, boolean allowDups) {
	for ( CommandController cc : elements ) {
	    if ( !allowDups) {
		for ( String cmd : cc ) {
		    if ( isValidCommand( cmd ) )
			throw new IllegalArgumentException( "Duplicate command " + cmd + " in element number " + (this.elements.size()+1) );
		}
	    }
	    this.elements.add( cc );
	}
    }

    public AggregateCommandController(Iterable<? extends CommandController> elements) {
	this(elements, false);
    }

    public InteractionCommand fetchCommand(String name) {
	for ( CommandController cc : elements ) {
	    InteractionCommand cmd = cc.fetchCommand(name);
	    if (cmd!=null) return cmd;
	}
	return null;
    }

    public boolean isValidCommand(String name) {
	for ( CommandController cc : elements ) {
	    if ( cc.isValidCommand(name) ) return true;
	}
	return false;
    }

    private class CmdIterator implements Iterator<String> {
	Iterator<CommandController> i1;
	Iterator<String> i2;
	
	CmdIterator() {
	    i1 = elements.iterator();
	    i2 = null;
	}

	public boolean hasNext() {
	    while ((i2 == null) || ( ! i2.hasNext() )) {
		if ( ! i1.hasNext() ) return false;
		i2 = i1.next().iterator();
	    }
	    return true;
	}

	public String next() {
	    while ((i2 == null) || ( ! i2.hasNext() )) {
		if (!i1.hasNext()) throw new NoSuchElementException("no more controllers");
		i2 = i1.next().iterator();
	    }
	    return i2.next();
	}

	public void remove() {
	    throw new UnsupportedOperationException("Cannot delete commands");
	}
    }
    
    public Iterator<String> iterator() {
	return new CmdIterator();
    }
}
