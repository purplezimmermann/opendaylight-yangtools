/*
 * Copyright (c) 2016, 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.rfc6643.parser;

import java.util.Objects;
import org.opendaylight.yangtools.rfc6643.model.api.ObjectIdentifier;
import org.opendaylight.yangtools.rfc6643.model.api.OidEffectiveStatement;
import org.opendaylight.yangtools.rfc6643.model.api.OidSchemaNode;
import org.opendaylight.yangtools.rfc6643.model.api.OidStatement;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.UnknownEffectiveStatementBase;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;

final class OidEffectiveStatementImpl extends UnknownEffectiveStatementBase<ObjectIdentifier, OidStatement>
        implements OidEffectiveStatement, OidSchemaNode {

    private final SchemaPath path;

    OidEffectiveStatementImpl(final StmtContext<ObjectIdentifier, OidStatement, ?> ctx) {
        super(ctx);
        path = ctx.getParentContext().getSchemaPath().get().createChild(getNodeType());
    }

    @Override
    public ObjectIdentifier getArgument() {
        return argument();
    }

    @Override
    public QName getQName() {
        return getNodeType();
    }

    @Override
    public SchemaPath getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, getNodeType(), getNodeParameter());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OidEffectiveStatementImpl other = (OidEffectiveStatementImpl) obj;
        if (!Objects.equals(path, other.path)) {
            return false;
        }
        if (!Objects.equals(getNodeType(), other.getNodeType())) {
            return false;
        }
        if (!Objects.equals(getNodeParameter(), other.getNodeParameter())) {
            return false;
        }
        return true;
    }
}