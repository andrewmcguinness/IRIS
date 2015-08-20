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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class TestAggregateCommandController {

    private HashMap<String,InteractionCommand> map1;
    private HashMap<String,InteractionCommand> map2;
    private InteractionCommand cmds[];
    
    @Before
    public void setup() {
	map1 = new HashMap<String,InteractionCommand>();
	map2 = new HashMap<String,InteractionCommand>();
	cmds = new InteractionCommand[10];
	for ( int i=0 ; i < 10; ++i ) cmds[i] = mock(InteractionCommand.class);
	map1.put("Harley", cmds[0]);
	map1.put("Starsky", cmds[1]);
	map2.put("Tinka", cmds[2]);
	map2.put("Spot", cmds[3]);
    }

    @Test
    public void testTrivial() {
	NewCommandController simple = new NewCommandController(map1);
	AggregateCommandController controller = new AggregateCommandController(Collections.singletonList(simple));
	assertEquals(cmds[0], controller.fetchCommand("Harley"));
	assertEquals(cmds[1], controller.fetchCommand("Starsky"));
	assertNull(controller.fetchCommand("Spot"));
    }

    @Test
    public void testCombined() {
	ArrayList<NewCommandController> contents = new ArrayList<NewCommandController>();
	contents.add( new NewCommandController(map1) );
	contents.add( new NewCommandController(map2) );
	AggregateCommandController controller = new AggregateCommandController(contents);
	
	assertEquals(cmds[0], controller.fetchCommand("Harley"));
	assertEquals(cmds[1], controller.fetchCommand("Starsky"));
	assertEquals(cmds[2], controller.fetchCommand("Tinka"));
	assertEquals(cmds[3], controller.fetchCommand("Spot"));
	assertNull(controller.fetchCommand("Charlie"));
    }
}
