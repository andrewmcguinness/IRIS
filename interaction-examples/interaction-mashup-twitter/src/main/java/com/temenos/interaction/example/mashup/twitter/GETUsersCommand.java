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
import java.util.List;

import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.InteractionContext;
import com.temenos.interaction.core.resource.CollectionResource;
import com.temenos.interaction.core.resource.EntityResource;
import com.temenos.interaction.example.mashup.twitter.model.User;

public class GETUsersCommand implements InteractionCommand {

	private Persistence persistence;
	
	public GETUsersCommand(Persistence p) {
		persistence = p;
	}
	
	/* Implement InteractionCommand interface */
	
	@Override
	public Result execute(InteractionContext ctx) {
		assert(ctx != null);
		List<EntityResource<User>> userEntities = new ArrayList<EntityResource<User>>();
		List<User> users = persistence.getUsers();
		for (User u : users) {
			userEntities.add(new EntityResource<User>(u));
		}
		CollectionResource<User> usersResource = new CollectionResource<User>("user", userEntities);
		ctx.setResource(usersResource);
		return Result.SUCCESS;
	}

}
