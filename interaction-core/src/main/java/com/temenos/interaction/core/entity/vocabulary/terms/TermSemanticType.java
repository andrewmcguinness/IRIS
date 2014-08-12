package com.temenos.interaction.core.entity.vocabulary.terms;

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

import com.temenos.interaction.core.entity.vocabulary.Term;

/**
 * This Term describes the semantic type of a property, 
 * e.g. currency code, person name, money amount, etc. 
 */
public class TermSemanticType implements Term {
	public final static String TERM_NAME = "TERM_SEMANTIC_TYPE";
	
	public static final String NAMESPACE = "http://iris.temenos.com/odata-extensions";
	public static final String PREFIX = "iris-ext";
	public static final String CSDL_NAME = "semanticType";
	
	private String value;
	
	public TermSemanticType(String val) {
		this.value = val;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public String getName() {
		return TERM_NAME;
	}	
}
