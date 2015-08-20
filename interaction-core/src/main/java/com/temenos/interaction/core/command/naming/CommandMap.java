package com.temenos.interaction.core.command.naming;

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

import com.temenos.interaction.core.command.InteractionCommand;

/** This is a convenience for setting up sets of commands from configuration files 
 *  or via Spring. It takes a set of objects which can either be Commands (which will
 *  be given their default names) or NamedCommands with preset names. It just saves
 *  having to wrap a Command in a NamedCommand if the default name is what is wanted.
 *
 *  Programmatic setup of commands should use type-safe methods instead.
 */
public class CommandMap implements Iterable<NamedCommand> {
    private ArrayList<NamedCommand> contents = new ArrayList<NamedCommand>();

    public CommandMap(Iterable<Object> items) {
	for ( Object item : items ) {
	    if ( item instanceof InteractionCommand )
		add( (InteractionCommand) item );
	    else if ( item instanceof NamedCommand )
		add( (NamedCommand) item );
	    else
		throw new IllegalArgumentException( "Unexpected object " + item + " -- require InteractionCommand or NamedCommand" );
	}
    }

    public void add( InteractionCommand unnamed ) {
	add( new NamedCommand(unnamed) );
    }

    public void add( NamedCommand cmd ) {
	contents.add( cmd );
    }

    public class NCIterator implements Iterator<NamedCommand> {
	private Iterator<NamedCommand> rw;
	public NCIterator() {
	    rw = contents.iterator();
	}
	public boolean hasNext() {
	    return rw.hasNext();
	}
	public NamedCommand next() {
	    return rw.next();
	}
	@Override
	public void remove() {
	    throw new UnsupportedOperationException("Cannot delete from CommandMap");
	}
    }

    public Iterator<NamedCommand> iterator() {
	return new NCIterator();
    }
}
