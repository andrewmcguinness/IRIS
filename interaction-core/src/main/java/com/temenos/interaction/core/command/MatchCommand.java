package com.temenos.interaction.core.command;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2014 Temenos Holdings N.V.
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


import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple matcher 
 * The Expression must be passed in the property as follow :
 * 	 view: MatchCommand {
 *   	properties [ Expression="{entity}='verCustomer_Input'" ]
 *	 }
 * Only simple expression are valid (=, &gt;, &lt;, &lt;=, &gt;=, !=, startsWith, endsWith, contains)
 * The comparison is ALWAYS made on a String basis, so a '&lt;' will in fact be a 
 * left.compareTo(right) < 0
 * 
 * The &quot; and " are removed from the values prior to comparison.
 * The Values are trimmed prior to comparison
 * Example : "  hello" = 'hello  ' return true.
 * (Note therefore "onetwo" contains " " returns true like "one two" contains " ")
 * 
 * @author taubert
 * 
 */
public class MatchCommand implements InteractionCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(MatchCommand.class);

	/*
	 * important : the longer (in chars) first
	 */
	private static final String[] supportedComparators = new String[]{"startsWith", "endsWith", "contains", "<=", ">=", "!=", "<", ">", "="};

	/* The comparators to be used by the parser */
	protected String[] comparators() {
		return supportedComparators;
	}
	
	/* Look up a parameter in the context.
	 * Override this to change lookup behaviour
	 * Returns first value in Path multimap, and if that fails
	 * then first value in Query multimap
	 */
	protected String lookup(InteractionContext ctx, String variable) {
		String value = ctx.getPathParameters().getFirst(variable);
		if (value == null) {
			value = ctx.getQueryParameters().getFirst(variable);
		}
		return value;
	}

	/* Evaluation of a simple expression, after parsing */
	protected boolean simpleExpression(String op, String left, String right) {
		boolean bResult = false;
		if ("=".equals(op)){
			bResult = left.equals(right);
		}else if (">".equals(op)){
			bResult = left.compareTo(right) > 0;
		}else if ("<".equals(op)){
			bResult = left.compareTo(right) < 0;
		}else if (">=".equals(op)){
			bResult = left.compareTo(right) >= 0;
		}else if ("<=".equals(op)){
			bResult = left.compareTo(right) <= 0;
		}else if ("!=".equals(op)){
			bResult = !left.equals(right);
		}else if ("startsWith".equals(op)){
			bResult = left.startsWith(right);
		}else if ("endsWith".equals(op)){
			bResult = left.endsWith(right);
		}else if ("contains".equals(op)){
			bResult = left.contains(right);
		}
		return bResult;
	}
	
	/*
	 * Evaluate an expression. override this to modify or extend comparisons
	 */
	protected boolean evaluate(InteractionContext ctx, String expression) {
		if (expression == null){
			throw new IllegalArgumentException("null expression passed to MatchCommand");
		}

	    /*
	     * So we have an expression.
	     * Currently, only simple expression are valid (=, >, <, <=, >=, !=, startsWith, endsWith, contains)
	     */
			
		String left = null;
		String right = null;
		String comparator = null;
		for (String sOneComparator : comparators()){
			int pos = expression.indexOf(sOneComparator);
			if (pos > 0){
				left = expression.substring(0,pos);
				right = expression.substring(pos + sOneComparator.length());
				comparator = sOneComparator;
				break;
			}
		}
			
		if (comparator == null){
			throw new IllegalArgumentException("No comparsion operator recognised in expression passed to MatchCommand");
		}
			
		left = resolveVariable(ctx, left);
		right = resolveVariable(ctx, right);

		/*
		 * Do the comparisons.
		 */
		return simpleExpression(comparator, left, right);
	}
	
	@Override
	public Result execute(InteractionContext ctx) throws InteractionException {
		/*
		 * Few assertions first ...
		 */
		try {
			assert ctx != null;
			assert ctx.getCurrentState() != null;
			assert ctx.getCurrentState().getEntityName() != null && !"".equals(ctx.getCurrentState().getEntityName());

			Properties properties = ctx.getCurrentState().getViewAction().getProperties();
			String sExpression = properties.getProperty("Expression");

			if (evaluate(ctx, sExpression)){
				return Result.SUCCESS;
			}else{
				return Result.FAILURE;
			}
		} catch (Exception e) {
		    LOGGER.error("There was an issue while evaluating the expression", e);
		    
			return Result.FAILURE;
		}
	}	

	/* Resolve an operand, which may be a variable */
	protected String resolveVariable(InteractionContext ctx, String var){
		if (var == null){
			return null;
		}
		String s = var.trim();
		String ret = s;
		
		if (s.startsWith("'") && s.endsWith("'")){
			ret = s.substring(1, s.length()-1).trim();
		}else{
			if (s.startsWith("\"") && s.endsWith("\"")){
				ret = s.substring(1, s.length()-1).trim();
			}else if (s.startsWith("{") && s.endsWith("}")){
				s = s.substring(1, s.length()-1).trim();
				ret = lookup(ctx, s);
				if (ret == null){
					ret = s; // the variable without the { } 
				}else{
					ret = ret.trim();
				}
			}
		}

		return ret;
	}
	
}
