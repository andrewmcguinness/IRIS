package com.temenos.interaction.core.command;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2016 Temenos Holdings N.V.
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

import java.util.Collections;
import java.util.Properties;

import com.temenos.interaction.core.MultivaluedMapImpl;
import com.temenos.interaction.core.entity.Metadata;
import com.temenos.interaction.core.hypermedia.Action;
import com.temenos.interaction.core.hypermedia.ResourceState;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestMatchCommand {
	private InteractionCommand cmd;
	private InteractionContext ctx;
	private Properties commandProperties;
	
	protected void setExpression(String expression) {
		commandProperties.setProperty("Expression", expression);
	}
	
	@Before
	public void setup() {
		MultivaluedMapImpl pParams = new MultivaluedMapImpl();
		MultivaluedMapImpl qParams = new MultivaluedMapImpl();

		pParams.add("Variable", "Value");
		qParams.add("Query", "Apple");

		cmd = new MatchCommand();
		commandProperties = new Properties();
		Action getAction = new Action("GET", Action.TYPE.VIEW, commandProperties);
		ResourceState currentState = new ResourceState("Entity", "match", Collections.singletonList(getAction), "/");
		ctx = new InteractionContext(null,
									 null,
									 pParams,
									 qParams,
									 currentState,
									 new Metadata("Test"));
	}
	
	@Test
	public void testEquals() throws InteractionException {
		setExpression("{Variable}='Value'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test
	public void testEqualsFail() throws InteractionException {
		setExpression("{Variable}='Vaalue'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.FAILURE, result);
	}

	@Test
	public void testQuoting() throws InteractionException {
		setExpression("Thing='Thing'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test
	public void testQuotingDeref() throws InteractionException {
		setExpression("'{Variable}'='Value'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.FAILURE, result);
	}

	@Test
	public void testQuotingBrackets() throws InteractionException {
		setExpression("'{Variable}'='{Variable}'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test
	public void testQueryParam() throws InteractionException {
		setExpression("{Query}='Apple'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	/* A variable that does not exist resolves to its name */
	@Test
	public void testMissingVar() throws InteractionException {
		setExpression("{Undefined}='Undefined'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test 
	public void testSpaces() throws InteractionException {
		setExpression("{Variable} = 'Value'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	/* != is broken, because it comes after = in the list, and there is
     * no break in the loop */
	@Test 
	public void testNotEquals() throws InteractionException {
		setExpression("{Variable}!='Wrong'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test
	public void testNotEqualsFail() throws InteractionException {
		setExpression("{Variable}!='Value'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.FAILURE, result);
	}

	@Test
	public void testStartsWith() throws InteractionException {
		setExpression("{Variable}startsWith'Val'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test 
	public void testStartsWithLiteral() throws InteractionException {
		setExpression("'Value' startsWith 'Val'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test 
	public void testStartsWithSpace() throws InteractionException {
		setExpression("{Variable} startsWith 'Val'");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.SUCCESS, result);
	}

	@Test
	public void testError() throws InteractionException {
		setExpression("Invalid expression");
		InteractionCommand.Result result = cmd.execute(ctx);
		assertEquals(InteractionCommand.Result.FAILURE, result);
	}
}
