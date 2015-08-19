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

	/** Add commands without explicit names:
	 *  If the command has the Command annotation with a "name" argument, use that
	 *  Otherwise, use the class name (without package name)
     *  If the class name ends with "Command" strip it off
	 *  (unless the name is just "Command", in which case use that)
	 *  This is all intended as a convenient default for one-off commands. If the commands are composed
	 *  with constructor arguments, or reused across projects, it will probably save confusion in the long
	 *  run to add them to the controller explicitly with names (i.e. use the Map constructor)
	 */
	public NewCommandController(Iterable<InteractionCommand> commandList) {
	    for ( InteractionCommand cmd : commandList ) {
			addCommand( defaultCommandName(cmd), cmd );
		}
	}

	public static String defaultCommandName( InteractionCommand cmd ) {
		String name = null;

		Class<?> clazz = cmd.getClass();
		// Check for a @CommandName annotation
		for ( Method m : clazz.getMethods() ) {
			try {			
				if ( m.isAnnotationPresent(CommandName.class) ) {
					if (m.getParameterTypes().length > 0)
						throw new IllegalArgumentException( "ClassName annotation invalid: " + clazz.getName() + "." + m.getName() +
															"() method should have zero parameters" );
					name = m.invoke(cmd).toString();
					break;
				}
			}
			catch ( IllegalAccessException e ) {
				throw new IllegalArgumentException( "Cannot invoke @ClassName method " + clazz.getName() + "." + m.getName(), e );
			}
			catch ( InvocationTargetException e ) {
				throw new IllegalArgumentException( "Cannot invoke @ClassName method " + clazz.getName() + "." + m.getName(), e );
			}
		}

		// Check for a @Command(name=...) annotation
		if ( clazz.isAnnotationPresent(Command.class) )
			name = clazz.getAnnotation(Command.class).name();
				
		// Fall back to using the class name
		if ((name == null) || name.isEmpty() ) {
			String className = clazz.getSimpleName();
			if ((className.length() > 7) && className.endsWith("Command" ))
				name = className.substring(0, className.length() - 7);
			else
				name = className;
		}
		return name;
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
