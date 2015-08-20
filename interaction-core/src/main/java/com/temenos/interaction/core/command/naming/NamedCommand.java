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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.temenos.interaction.core.command.InteractionCommand;

/* A command with a name to use with it in a controller. The name
 * can be specified, or left to default.
 *
 * RULES FOR DEFAULT COMMAND NAMES:
 *  If the command has the Command annotation with a "name" argument, use that.
 *  If the command has a method with the CommandName annotation, invoke that to
 *  get the name.
 *  Otherwise, use the class name (without package name)
 *  If the class name ends with "Command" strip it off
 *  (unless the name is just "Command", in which case use that)
 *
 *  This is all intended as a convenient default for one-off commands. If the commands are composed
 *  with constructor arguments, or reused across projects, it will probably save confusion in the long
 *  run to add them to the controller explicitly with names (i.e. use the Map constructor)
 */
public class NamedCommand {
    private String name;
    private InteractionCommand command;
    
    public NamedCommand( String name, InteractionCommand command ) {
		this.name = name;
		this.command = command;
    }

    public NamedCommand( InteractionCommand cmd ) {
		this( defaultName( cmd ), cmd );
    }

    public String getName() { return name; }
    public InteractionCommand getCommand() { return command; }

    public static String defaultName( InteractionCommand cmd ) {
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

}

