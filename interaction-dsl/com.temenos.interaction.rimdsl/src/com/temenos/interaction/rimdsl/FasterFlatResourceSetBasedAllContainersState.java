package com.temenos.interaction.rimdsl;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.containers.FlatResourceSetBasedAllContainersState;
import org.eclipse.xtext.resource.containers.IAllContainersState;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData;

import com.google.common.collect.Lists;

/**
 * FlatResourceSetBasedAllContainersState with a performance optimization.
 *
 * TODO Remove this class once we're on Xtext 2.9.0.
 *
 * @see http://rd.oams.com/browse/DS-8893
 * @see http://rd.oams.com/browse/DS-8896
 *
 * @author Michael Vorburger, based on advise from Sven Efftinge
 */
public class FasterFlatResourceSetBasedAllContainersState extends FlatResourceSetBasedAllContainersState {

	// This had to be copy/pasted :-( from FlatResourceSetBasedAllContainersState because it's private there
	private final static String HANDLE = "all";
	private ResourceSet resourceSet;

	public FasterFlatResourceSetBasedAllContainersState(ResourceSet rs) {
		super(rs);
		this.resourceSet = rs;
	}

	@Override
	public Collection<URI> getContainedURIs(String containerHandle) {
		if (!HANDLE.equals(containerHandle))
			return Collections.emptySet();
		if (resourceSet instanceof XtextResourceSet) {
			XtextResourceSet xtextResourceSet = (XtextResourceSet) resourceSet;
			ResourceDescriptionsData descriptionsData = ResourceDescriptionsData.ResourceSetAdapter.findResourceDescriptionsData(resourceSet);
			if (descriptionsData != null) {
				return descriptionsData.getAllURIs();
			}
			return newArrayList(xtextResourceSet.getNormalizationMap().values());
		}
		List<URI> uris = Lists.newArrayListWithCapacity(resourceSet.getResources().size());
		URIConverter uriConverter = resourceSet.getURIConverter();
		for (Resource r : resourceSet.getResources())
			uris.add(uriConverter.normalize(r.getURI()));
		return uris;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return IAllContainersState.class == type || FasterFlatResourceSetBasedAllContainersState.class == type;
	}
}