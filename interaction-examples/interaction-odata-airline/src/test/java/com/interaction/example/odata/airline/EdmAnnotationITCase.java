package com.interaction.example.odata.airline;

/*
 * #%L
 * interaction-example-odata-airline
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


import org.junit.Assert;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.NamespacedAnnotation;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.jersey.consumer.ODataJerseyConsumer;

/**
 * Test that semantic types represented as Annotation attributes
 * are passed over the OData interface 
 * @author Andrew McGuinness
 */
public class EdmAnnotationITCase {

	public EdmAnnotationITCase() throws Exception {
		super();
	}
	
	
	@Test
	public void testSemanticAnnotation() {
		ODataConsumer consumer = ODataJerseyConsumer.newBuilder(ConfigurationHelper.getTestEndpointUri(Configuration.TEST_ENDPOINT_URI)).build();
		boolean oldDump = ODataConsumer.dump.responseBody();
		ODataConsumer.dump.responseBody(true);
		
		EdmDataServices metadata = consumer.getMetadata();

		Assert.assertEquals(EdmSimpleType.STRING,
				metadata.findEdmEntitySet("Airports").getType()
						.findProperty("country").getType());
		
		NamespacedAnnotation<?> st = metadata.findEdmEntitySet("Airports").getType().findProperty("country").findAnnotation("http://iris.temenos.com/odata-extensions", "semanticType");
		Assert.assertNotNull(st);
		Assert.assertEquals("Geography:Country", st.getValue());
		
		ODataConsumer.dump.responseBody(oldDump);
	}
	
}
