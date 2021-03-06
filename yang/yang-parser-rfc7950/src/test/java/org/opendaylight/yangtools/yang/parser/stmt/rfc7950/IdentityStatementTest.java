/*
 * Copyright (c) 2017 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc7950;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import org.junit.Test;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.model.api.IdentitySchemaNode;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.parser.spi.meta.ReactorException;
import org.opendaylight.yangtools.yang.stmt.StmtTestUtils;

public class IdentityStatementTest {

    @Test
    public void testMultipleBaseIdentities() throws Exception {
        final SchemaContext schemaContext = StmtTestUtils.parseYangSource("/rfc7950/identity-stmt/foo.yang");
        assertNotNull(schemaContext);

        final Module foo = schemaContext.findModule("foo", Revision.of("2016-12-21")).get();
        for (final IdentitySchemaNode identity : foo.getIdentities()) {
            if ("derived-id".equals(identity.getQName().getLocalName())) {
                final Collection<? extends IdentitySchemaNode> baseIdentities = identity.getBaseIdentities();
                assertEquals(3, baseIdentities.size());
            }
        }
    }

    @Test
    public void testInvalidYang10() throws Exception {
        try {
            StmtTestUtils.parseYangSource("/rfc7950/identity-stmt/foo10.yang");
            fail("Test should fail due to invalid Yang 1.0");
        } catch (final ReactorException e) {
            assertTrue(e.getCause().getMessage().startsWith("Maximal count of BASE for IDENTITY is 1, detected 3."));
        }
    }
}
