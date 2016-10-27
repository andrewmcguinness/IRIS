package com.temenos.interaction.core.rim;

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


import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.temenos.interaction.core.MultivaluedMapImpl;
import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.InteractionContext;
import com.temenos.interaction.core.hypermedia.Event;
import com.temenos.interaction.core.hypermedia.LazyCollectionResourceState;
import com.temenos.interaction.core.hypermedia.LazyResourceState;
import com.temenos.interaction.core.hypermedia.ResourceState;
import com.temenos.interaction.core.hypermedia.ResourceStateMachine;
import com.temenos.interaction.core.hypermedia.Transition;
import com.temenos.interaction.core.resource.EntityResource;
import com.temenos.interaction.core.resource.RESTResource;

/**
 * <p>Simply iterate through the transitions and get each one in turn.</p>
 * @author aphethean
 */
public class SequentialResourceRequestHandler implements ResourceRequestHandler {

	@Override
	public Map<Transition, ResourceRequestResult> getResources(HTTPHypermediaRIM rimHandler, HttpHeaders headers, InteractionContext ctx, EntityResource<?> resource, ResourceRequestConfig config) {
		return getResources(rimHandler, headers, ctx, resource, null, config);
	}

	public Map<Transition, ResourceRequestResult> getResources(HTTPHypermediaRIM rimHandler, HttpHeaders headers, InteractionContext ctx, EntityResource<?> resource, Object entity, ResourceRequestConfig config) {	
		assert(config != null);
		assert(config.getTransitions() != null);
		ResourceStateMachine hypermediaEngine = rimHandler.getHypermediaEngine();
		Map<Transition, ResourceRequestResult> resources = new HashMap<Transition, ResourceRequestResult>(); 
		for (Transition t : config.getTransitions()) {
			String method = t.getCommand().getMethod();
			if ((t.getCommand().getFlags() & Transition.AUTO) == Transition.AUTO) {
				method = t.getCommand().getMethod();
			}
	    	Event event = new Event("", method);
			// determine action
	    	ResourceState targetState = t.getTarget();
			if (targetState instanceof LazyResourceState || targetState instanceof LazyCollectionResourceState) {
				targetState = rimHandler.getHypermediaEngine().getResourceStateProvider().getResourceState(targetState.getName());
				t.setTarget(targetState);
			}
	    	
	    	InteractionCommand action = hypermediaEngine.buildWorkflow(event, targetState.getActions());
	    	
			MultivaluedMap<String, String> newPathParameters = new MultivaluedMapImpl<String>();
			newPathParameters.putAll(ctx.getPathParameters());
			
			if (resource != null) {
				Map<String,Object> transitionProperties = hypermediaEngine.getTransitionProperties(t, ((EntityResource<?>)resource).getEntity(), ctx.getPathParameters(), ctx.getQueryParameters());
				
				for (String key : transitionProperties.keySet()) {
					if (transitionProperties.get(key) != null) {
						newPathParameters.putSingle(key, transitionProperties.get(key).toString());
					}
				}				
			}

			MultivaluedMap<String, String> newQueryParameters = new MultivaluedMapImpl<String>();
			newQueryParameters.putAll(ctx.getQueryParameters());
						
			if (entity != null) {
				/* Handle cases where we may be embedding a resource that has filter criteria whose values are contained in the current resource's 
				 * entity properties.				
				 */				
				Map<String,Object> transitionProperties = hypermediaEngine.getTransitionProperties(t, entity, ctx.getPathParameters(), ctx.getQueryParameters());
				
				for (String key : transitionProperties.keySet()) {
					if (transitionProperties.get(key) != null) {
						/* Adding transition properties to both query parameters and path parameters,
						 * as they should take priority over either */
						newQueryParameters.putSingle(key, transitionProperties.get(key).toString());
						newPathParameters.putSingle(key, transitionProperties.get(key).toString());
					}
				}
			}
			
			
	    	InteractionContext newCtx = new InteractionContext(ctx, null, newPathParameters, newQueryParameters, targetState);
	    	newCtx.setResource(null);
			Response response = rimHandler.handleRequest(headers, 
					newCtx, 
					event, 
					action, 
					resource, 
					config,
					true); 
			RESTResource targetResource = null;
			if (response.getEntity() != null) {
				targetResource = (RESTResource) ((GenericEntity<?>) response.getEntity()).getEntity();
			}
			resources.put(t, new ResourceRequestResult(response.getStatus(), targetResource));
		}
		return resources;
	}

}
