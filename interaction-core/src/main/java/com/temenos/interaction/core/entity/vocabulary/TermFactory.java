package com.temenos.interaction.core.entity.vocabulary;

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


import com.temenos.interaction.core.entity.vocabulary.terms.TermComplexGroup;
import com.temenos.interaction.core.entity.vocabulary.terms.TermComplexType;
import com.temenos.interaction.core.entity.vocabulary.terms.TermIdField;
import com.temenos.interaction.core.entity.vocabulary.terms.TermLangType;
import com.temenos.interaction.core.entity.vocabulary.terms.TermListType;
import com.temenos.interaction.core.entity.vocabulary.terms.TermMandatory;
import com.temenos.interaction.core.entity.vocabulary.terms.TermRange;
import com.temenos.interaction.core.entity.vocabulary.terms.TermResourceManager;
import com.temenos.interaction.core.entity.vocabulary.terms.TermSemanticType;
import com.temenos.interaction.core.entity.vocabulary.terms.TermValueType;

/**
 * Factory class responsible for creating vocabulary terms
 */
public class TermFactory {
	
	/**
	 * Create a vocabulary Term
	 * @param name Term name
	 * @param value Term value
	 * @return The create term
	 * @throws Exception
	 */
	public Term createTerm(String name, String value) throws Exception {
		Term term;
		if(name == null || value == null) {
			throw new Exception("Unable to create vocabulary Term with null values.");
		}
		else if(name.equalsIgnoreCase(TermValueType.TERM_NAME)) {
			term = new TermValueType(value);
		}
		else if(name.equalsIgnoreCase(TermSemanticType.TERM_NAME)) {
			term = new TermSemanticType(value);
		}
		else if(name.equalsIgnoreCase(TermMandatory.TERM_NAME)) {
			term = new TermMandatory(value.equalsIgnoreCase("true"));
		}
		else if(name.equalsIgnoreCase(TermIdField.TERM_NAME)) {
			term = new TermIdField(value.equalsIgnoreCase("true"));
		}
		else if(name.equalsIgnoreCase(TermComplexType.TERM_NAME)) {
			term = new TermComplexType(value.equalsIgnoreCase("true"));
		}
		else if (name.equalsIgnoreCase(TermListType.TERM_NAME)) {
			term = new TermListType(value.equalsIgnoreCase("true"));
		}
		else if(name.equalsIgnoreCase(TermComplexGroup.TERM_NAME)) {
			term = new TermComplexGroup(value);
		}
		else if(name.equalsIgnoreCase(TermResourceManager.TERM_NAME)) {
			term = new TermResourceManager(value);
		}
		else if(name.equalsIgnoreCase(TermLangType.TERM_NAME)) {
			term = new TermLangType(value.equalsIgnoreCase("true"));
		}		
		else if(name.equalsIgnoreCase(TermRange.TERM_NAME)) {
			String[] minmax = value.split(",");
			if(minmax.length == 2) {
				int min = Integer.parseInt(minmax[0].trim());
				int max = Integer.parseInt(minmax[1].trim());
				term = new TermRange(min, max);
			}
			else {
				throw new Exception("Failed to parse range value: " + value);
			}
		}
		else {
			throw new Exception("Term " + name + " does not exist.");
		}
		return term;
	}

	/**
	 * Return the default string value of a vocabulary term or null for those that do not have a default value.
	 * @param name Term name
	 * @return the default value
	 */
	public String getTermDefaultValue(String name) {
		if(name != null) {
			if(name.equalsIgnoreCase(TermValueType.TERM_NAME)) {
				return TermValueType.TEXT;
			}
			else if(name.equalsIgnoreCase(TermMandatory.TERM_NAME) ||
					name.equalsIgnoreCase(TermComplexType.TERM_NAME) ||
					name.equalsIgnoreCase(TermIdField.TERM_NAME)) {
				return "false";
			}
		}
		return null;
	}
}
