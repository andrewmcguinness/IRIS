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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.temenos.interaction.core.command.naming.NamedCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewCommandController implements CommandController {
	private final Logger logger = LoggerFactory.getLogger(NewCommandController.class);

	private Map<String, InteractionCommand> commands = new HashMap<String, InteractionCommand>();

	/**
	 * Create an empty command controller.
	 */
	public NewCommandController() {
	}

	/**
	 * Create a command controller and add the supplied commands to the resource path
	 * @precondition commands not null
	 * @postcondition all supplied commands can be retrieved with {@link fetchCommand}
	 * @param resourcePath
	 * @param Map<String, InteractionCommand> commands
	 */
	public NewCommandController(Map<String, InteractionCommand> commands) {
		assert(commands != null);
		for(String name : commands.keySet()) {
			addCommand(name, commands.get(name));
		}
	}

	/** Add commands from a list.
	 */
	public NewCommandController(Iterable<NamedCommand> commandList) {
	    for ( NamedCommand cmd : commandList ) {
			add( cmd );
		}
	}

	/**
	 * Add a command to transition a resources state.
	 * @precondition name not null
	 * @precondition {@link InteractionCommand} not null
	 */
	public void addCommand(String name, InteractionCommand c) {
		assert(name != null);
		assert(c != null);
		if ( commands.containsKey(name) )
			throw new IllegalArgumentException( "Duplicate command name " + name + " (Controller has " + commands.size() + " commands)" );
		commands.put(name, c);
	}

	public void add(NamedCommand cmd ) {
		addCommand(cmd.getName(), cmd.getCommand());
	}

	/*
	 * Returns the {@link InteractionCommand} bound to this name.
	 *
	 * @precondition String name is non null
	 * @postcondition InteractionCommand class previously registered by #addCommand
	 * @invariant commands is not null and number of commands
	 */
	public InteractionCommand fetchCommand(String name) {
		logger.debug("Looking up interaction command by name [" + name + "]");
		InteractionCommand command = commands.get(name);
		if (command == null) {
			logger.error("No command bound to [" + name + "]");
		}
		return command;
	}

	public boolean isValidCommand(String name) {
		return (commands.get(name) != null);
	}

	/** read-only iterator for commands
	 */
	public Iterator<String> iterator() {
		return Collections.unmodifiableSet( commands.keySet() ).iterator();
	}
}
