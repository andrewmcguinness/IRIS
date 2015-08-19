package com.temenos.interaction.winkext;

/*
 * #%L
 * interaction-winkext
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


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.DynamicResource;
import org.apache.wink.common.model.multipart.InMultiPart;

import com.temenos.interaction.core.command.CommandController;
import com.temenos.interaction.core.entity.Metadata;
import com.temenos.interaction.core.hypermedia.ResourceState;
import com.temenos.interaction.core.hypermedia.ResourceStateMachine;
import com.temenos.interaction.core.hypermedia.ResourceStateProvider;
import com.temenos.interaction.core.resource.EntityResource;
import com.temenos.interaction.core.rim.HTTPHypermediaRIM;
import com.temenos.interaction.core.rim.HTTPResourceInteractionModel;
import com.temenos.interaction.core.rim.ResourceInteractionModel;

public class LazyResourceDelegate implements HTTPResourceInteractionModel, DynamicResource {

	private ResourceStateMachine hypermediaEngine;
	private ResourceStateProvider resourceStateProvider;
	private CommandController commandController;
	private Metadata metadata;

	private Map<String, Set<String>> resourceNamesToMethods = new HashMap<String, Set<String>>();
	private String path = null;
	
	private HTTPHypermediaRIM realResource = null;

	
	/**
	 * The class binding an instance of a ResourceState to a path
	 * @param hypermediaEngine registry of all resources
	 * @param resourceStateProvider interface that is used to lazily lookup/create ResourceState instance
	 * @param resourceName the state name
	 * @param path the resource path
	 */
	public LazyResourceDelegate(ResourceStateMachine hypermediaEngine,
			ResourceStateProvider resourceStateProvider, 
			CommandController commandController,
			Metadata metadata,
			String resourceName, 
			String path,
			Set<String> methods) {
		this.hypermediaEngine = hypermediaEngine;
		this.commandController = commandController;
		this.metadata = metadata;
		this.resourceStateProvider = resourceStateProvider;
		this.resourceNamesToMethods.put(resourceName, methods);
		this.path = path;
	}

	HTTPHypermediaRIM getRealResource() {
		// work out if a reload is required (could be more effecient here as every request goes through this loop)
		boolean reload = false;
		for (String resourceName : resourceNamesToMethods.keySet()) {
			if (!resourceStateProvider.isLoaded(resourceName)) {
				reload = true;
			}			
		}
		
		if (realResource == null || reload) {
			for (String resourceName : resourceNamesToMethods.keySet()) {
				ResourceState currentState = resourceStateProvider.getResourceState(resourceName);
				for (String method : resourceNamesToMethods.get(resourceName)) {
					if (reload) {
						hypermediaEngine.unregister(currentState, method);
					}
					hypermediaEngine.register(currentState, method);
				}
			}
			realResource = new HTTPHypermediaRIM(null, commandController, hypermediaEngine, metadata, path, false);
		}
		return realResource;
	}
	
	public void addResource(String name, Set<String> newMethods) {
		synchronized (resourceNamesToMethods) {
			Set<String> methods = resourceNamesToMethods.get(name);
			if (methods == null) {
				methods = new HashSet<String>();
			}
			methods.addAll(newMethods);
			resourceNamesToMethods.put(name, methods);
		}
	}
	
	@Override
    public String getBeanName() {
		return resourceNamesToMethods.toString();
    }

	@Override
    public void setBeanName(String beanName) {
        throw new AssertionError("Not supported");
    }

    public void setWorkspaceTitle(String workspaceTitle) {
        throw new AssertionError("Not supported");
    }

	@Override
    public String getWorkspaceTitle() {
		return "DefaultWorkspace";
    }

    public void setCollectionTitle(String collectionTitle) {
        throw new AssertionError("Not supported");
    }

	@Override
    public String getCollectionTitle() {
		return path;
    }

	@Override
    public String getPath() {
		return path;
    }

	@Override
    public void setParent(Object parent) {
        throw new AssertionError("Not supported");
    }

	@Override
    public HTTPResourceInteractionModel getParent() {
		return null;
    }

	@Override
	public Response get(HttpHeaders headers, String id, UriInfo uriInfo) {
		return getRealResource().get(headers, id, uriInfo);
	}

	@Override
	public Response post(HttpHeaders headers, UriInfo uriInfo, InMultiPart inMP) {
		return getRealResource().post(headers, uriInfo, inMP);
	}	
	
	@Override
    public Response post( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo, 
    		MultivaluedMap<String, String> formParams) {
		return getRealResource().post(headers, id, uriInfo, formParams);
	}

	@Override
	public Response post(HttpHeaders headers, String id, UriInfo uriInfo, EntityResource<?> eresource) {
		return getRealResource().post(headers, id, uriInfo, eresource);
	}
	
	@Override
	public Response put(HttpHeaders headers, UriInfo uriInfo, InMultiPart inMP) {
		return getRealResource().put(headers, uriInfo, inMP);
	}	

	@Override
	public Response put(HttpHeaders headers, String id, UriInfo uriInfo, EntityResource<?> eresource) {
		return getRealResource().put(headers, id, uriInfo, eresource);
	}

	@Override
	public Response delete(HttpHeaders headers, String id, UriInfo uriInfo) {
		return getRealResource().delete(headers, id, uriInfo);
	}

	@Override
	public Response options(@Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo) {
		return getRealResource().options(headers, id, uriInfo);
	}

	@Override
	public ResourceState getCurrentState() {
		return getRealResource().getCurrentState();
	}

	@Override
	public String getResourcePath() {
		return path;
	}

	@Override
	public String getFQResourcePath() {
		return path;
	}

	@Override
	public Collection<ResourceInteractionModel> getChildren() {
		return null;
	}    
}
