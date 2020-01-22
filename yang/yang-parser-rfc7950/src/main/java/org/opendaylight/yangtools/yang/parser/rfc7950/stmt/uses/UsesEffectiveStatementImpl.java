/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt.uses;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.AugmentationSchemaNode;
import org.opendaylight.yangtools.yang.model.api.RevisionAwareXPath;
import org.opendaylight.yangtools.yang.model.api.SchemaNode;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.UsesNode;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.GroupingEffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.GroupingStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.SchemaNodeIdentifier;
import org.opendaylight.yangtools.yang.model.api.stmt.UsesEffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.UsesStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.WhenEffectiveStatement;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.AbstractEffectiveDocumentedNode;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.refine.RefineEffectiveStatementImpl;
import org.opendaylight.yangtools.yang.parser.spi.GroupingNamespace;
import org.opendaylight.yangtools.yang.parser.spi.meta.CopyType;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;

final class UsesEffectiveStatementImpl extends AbstractEffectiveDocumentedNode<QName, UsesStatement>
        implements UsesEffectiveStatement, UsesNode {
    private final @NonNull SchemaPath groupingPath;
    private final boolean addedByUses;
    private final @NonNull ImmutableMap<SchemaPath, SchemaNode> refines;
    private final @NonNull ImmutableSet<AugmentationSchemaNode> augmentations;
    private final @Nullable RevisionAwareXPath whenCondition;

    UsesEffectiveStatementImpl(final StmtContext<QName, UsesStatement, UsesEffectiveStatement> ctx) {
        super(ctx);

        // initGroupingPath
        final StmtContext<?, GroupingStatement, GroupingEffectiveStatement> grpCtx =
                ctx.getFromNamespace(GroupingNamespace.class, ctx.coerceStatementArgument());
        this.groupingPath = grpCtx.getSchemaPath().get();

        // initCopyType
        addedByUses = ctx.getCopyHistory().contains(CopyType.ADDED_BY_USES);

        // initSubstatementCollections
        final Set<AugmentationSchemaNode> augmentationsInit = new LinkedHashSet<>();
        final Map<SchemaPath, SchemaNode> refinesInit = new HashMap<>();
        for (final EffectiveStatement<?, ?> effectiveStatement : effectiveSubstatements()) {
            if (effectiveStatement instanceof AugmentationSchemaNode) {
                final AugmentationSchemaNode augmentationSchema = (AugmentationSchemaNode) effectiveStatement;
                augmentationsInit.add(augmentationSchema);
            }
            if (effectiveStatement instanceof RefineEffectiveStatementImpl) {
                final RefineEffectiveStatementImpl refineStmt = (RefineEffectiveStatementImpl) effectiveStatement;
                final SchemaNodeIdentifier identifier = refineStmt.argument();
                refinesInit.put(identifier.asSchemaPath(), refineStmt.getRefineTargetNode());
            }
        }
        this.augmentations = ImmutableSet.copyOf(augmentationsInit);
        this.refines = ImmutableMap.copyOf(refinesInit);

        whenCondition = findFirstEffectiveSubstatementArgument(WhenEffectiveStatement.class).orElse(null);
    }

    @Override
    public SchemaPath getGroupingPath() {
        return groupingPath;
    }

    @Override
    public Collection<? extends AugmentationSchemaNode> getAugmentations() {
        return augmentations;
    }

    @Deprecated
    @Override
    public boolean isAugmenting() {
        return false;
    }

    @Deprecated
    @Override
    public boolean isAddedByUses() {
        return addedByUses;
    }

    @Override
    public Map<SchemaPath, SchemaNode> getRefines() {
        return refines;
    }

    @Override
    public Optional<RevisionAwareXPath> getWhenCondition() {
        return Optional.ofNullable(whenCondition);
    }

    @Override
    public String toString() {
        return UsesEffectiveStatementImpl.class.getSimpleName() + "[groupingPath=" + groupingPath + "]";
    }
}
