package com.temenos.interaction.core.command.naming;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2015 Temenos Holdings N.V.
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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.InteractionContext;
import com.temenos.interaction.core.command.naming.cmds.*;

public class TestNamedCommand {
    @Test
    public void testUnnamed() {
	InteractionCommand cmd = new VanillaCommand();
	NamedCommand nc = new NamedCommand( cmd );
	assertEquals( "Vanilla", nc.getName() );
    }

    @Test
    public void testStatic() {
	InteractionCommand cmd = new StaticNamedCommand();
	NamedCommand nc = new NamedCommand( cmd );
	assertEquals( "Aardvark", nc.getName() );
    }

    @Test
    public void testDynamic() {
	InteractionCommand c1 = new DynamicNamedCommand("Charlie");
	InteractionCommand c2 = new DynamicNamedCommand("Tuscany");

	NamedCommand n1 = new NamedCommand(c1);
	NamedCommand n2 = new NamedCommand(c2);

	assertEquals("Charlie", n1.getName());
	assertEquals("Tuscany", n2.getName());
    }

    @Test
    public void testUnnamedX() {
	InteractionCommand cmd = new VanillaCommand();
	NamedCommand nc = new NamedCommand( "Jester", cmd );
	assertEquals( "Jester", nc.getName() );
    }

    @Test
    public void testStaticX() {
	InteractionCommand cmd = new StaticNamedCommand();
	NamedCommand nc = new NamedCommand( "Spot", cmd );
	assertEquals( "Spot", nc.getName() );
    }

    @Test
    public void testDynamicX() {
	InteractionCommand c1 = new DynamicNamedCommand("Charlie");
	InteractionCommand c2 = new DynamicNamedCommand("Tuscany");

	NamedCommand n1 = new NamedCommand("Harley", c1);
	NamedCommand n2 = new NamedCommand("Starsky", c2);

	assertEquals("Harley", n1.getName());
	assertEquals("Starsky", n2.getName());
    }
}
