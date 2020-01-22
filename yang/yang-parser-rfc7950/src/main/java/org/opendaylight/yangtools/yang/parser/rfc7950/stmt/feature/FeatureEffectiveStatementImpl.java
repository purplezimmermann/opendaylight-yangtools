/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt.feature;

import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.FeatureDefinition;
import org.opendaylight.yangtools.yang.model.api.stmt.FeatureEffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.FeatureStatement;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.AbstractEffectiveSchemaNode;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;

final class FeatureEffectiveStatementImpl extends AbstractEffectiveSchemaNode<FeatureStatement>
        implements FeatureDefinition, FeatureEffectiveStatement {
    FeatureEffectiveStatementImpl(final StmtContext<QName, FeatureStatement, ?> ctx) {
        super(ctx);
    }

    @Override
    public String toString() {
        return FeatureEffectiveStatementImpl.class.getSimpleName() + "[name=" + getQName() + "]";
    }
}
