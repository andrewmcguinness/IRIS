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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.junit.Test;

public class TestNewCommandController {

	@Test
	public void testDefaultConstructorNotNullNotRegistered() {
		NewCommandController cc = new NewCommandController();
		InteractionCommand command = cc.fetchCommand("dostuff");
		assertNull(command);
	}

	@Test
	public void testFetchCommandNoCommandsSetNotFound() {
		NewCommandController cc = new NewCommandController(new HashMap<String, InteractionCommand>());
		InteractionCommand command = cc.fetchCommand("dostuff");
		assertNull(command);
	}

	@Test
	public void testCommandRegistered() {
		NewCommandController cc = new NewCommandController();
		InteractionCommand command = mock(InteractionCommand.class);
		cc.addCommand("DO", command);
		assertEquals(command, cc.fetchCommand("DO"));
	}

	@Test
	public void testIsValidCommandCommandRegistered() {
		NewCommandController cc = new NewCommandController();
		InteractionCommand command = mock(InteractionCommand.class);
		cc.addCommand("DO", command);
		assertTrue(cc.isValidCommand("DO"));
		assertFalse(cc.isValidCommand("NOTHING"));
	}

}
