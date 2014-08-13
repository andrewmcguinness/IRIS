package com.temenos.interaction.odataext.resource;

/*
 * #%L
 * interaction-odata4j-ext
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


import org.odata4j.edm.EdmDataServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temenos.interaction.core.entity.Metadata;
import com.temenos.interaction.core.entity.vocabulary.TermFactory;
import com.temenos.interaction.core.hypermedia.ResourceStateMachine;
import com.temenos.interaction.odataext.entity.MetadataOData4j;

// TODO what is this class even for???

/**
 * This class provides EDM metadata for the current service.
 */
public class ResourceMetadataManager extends com.temenos.interaction.core.resource.ResourceMetadataManager {
	private final static Logger logger = LoggerFactory.getLogger(ResourceMetadataManager.class);

	private EdmDataServices edmMetadata = null;

	/**
	 * Construct the metadata object
	 */
	public ResourceMetadataManager(Metadata metadata, ResourceStateMachine hypermediaEngine)
	{
		super(metadata, hypermediaEngine);
		edmMetadata = populateOData4jMetadata(metadata, hypermediaEngine);
	}

	/**
	 * Construct the metadata object
	 */
	public ResourceMetadataManager(String metdataXml, ResourceStateMachine hypermediaEngine)
	{
		super(metdataXml, hypermediaEngine);
		edmMetadata = populateOData4jMetadata(getMetadata(), hypermediaEngine);
	}
	
	/**
	 * Construct the metadata object
	 */
	public ResourceMetadataManager(ResourceStateMachine hypermediaEngine)
	{
		super(hypermediaEngine);
		edmMetadata = populateOData4jMetadata(getMetadata(), hypermediaEngine);
	}

	/**
	 * Construct the metadata object
	 */
	public ResourceMetadataManager(ResourceStateMachine hypermediaEngine, TermFactory termFactory)
	{
		super(hypermediaEngine, termFactory);
		edmMetadata = populateOData4jMetadata(getMetadata(), hypermediaEngine);
	}
	
	/**
	 * Return the odata4j metadata
	 * @return metadata
	 */
	public EdmDataServices getOData4jMetadata() {
		return this.edmMetadata;
	}
	
	/*
	 * Parse the XML metadata file with the default Vocabulary Term Factory
	 */
	protected Metadata parseMetadataXML() {
		return parseMetadataXML(new TermFactory());
	}

	/*
	 * Load the metadata from the metadata.xml file.
	 */
	protected EdmDataServices populateOData4jMetadata(Metadata metadata, ResourceStateMachine hypermediaEngine) {
		EdmDataServices edmMetadata;
		try {
			edmMetadata = new MetadataOData4j(metadata, hypermediaEngine).getMetadata();
		}
		catch(Exception e) {
			logger.error("Failed to create odata4j metadata", e);
			throw new RuntimeException("Failed to create odata4j metadata: " + e.getMessage());
		}
		return edmMetadata;
	}
}
