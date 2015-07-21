package com.temenos.interaction.rimdsl;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.containers.IAllContainersState;
import org.eclipse.xtext.resource.containers.ResourceSetBasedAllContainersStateProvider;

/**
 * Provider for FasterFlatResourceSetBasedAllContainersState.
 *
 * TODO Remove this class once we're on Xtext 2.9.0.
 *
 * @see FasterFlatResourceSetBasedAllContainersState
 *
 * @author Michael Vorburger
 */
public class FasterResourceSetBasedAllContainersStateProvider extends ResourceSetBasedAllContainersStateProvider {

	@Override
	protected IAllContainersState handleAdapterNotFound(ResourceSet resourceSet) {
		return new FasterFlatResourceSetBasedAllContainersState(resourceSet);
	}

}