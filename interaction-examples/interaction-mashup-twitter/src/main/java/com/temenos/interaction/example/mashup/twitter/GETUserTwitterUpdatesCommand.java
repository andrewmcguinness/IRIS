package com.temenos.interaction.example.mashup.twitter;

/*
 * #%L
 * interaction-example-mashup-twitter
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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.InteractionContext;
import com.temenos.interaction.core.resource.CollectionResource;
import com.temenos.interaction.core.resource.EntityResource;
import com.temenos.interaction.example.mashup.twitter.model.Tweet;

public class GETUserTwitterUpdatesCommand implements InteractionCommand {

	public GETUserTwitterUpdatesCommand() {}
	
	@Override
	public Result execute(InteractionContext ctx) {
		assert(ctx != null);
		List<EntityResource<Tweet>> tweetEntities = new ArrayList<EntityResource<Tweet>>();
		
		// this command designed to be used with {username} in the path e.g.  /tweets/{username]
		Collection<Tweet> tweets = new Twitter4JConsumer().requestTweetsByUser(ctx.getPathParameters().getFirst("username"));
		for (Tweet t : tweets) {
			tweetEntities.add(new EntityResource<Tweet>(t));
		}
		CollectionResource<Tweet> usersResource = new CollectionResource<Tweet>(tweetEntities);
		ctx.setResource(usersResource);
		return Result.SUCCESS;
	}

}
