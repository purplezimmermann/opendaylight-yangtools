/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.impl.schema.builder.impl;

import com.google.common.collect.Iterables;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.util.UnmodifiableCollection;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifier;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeWithValue;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.PathArgument;
import org.opendaylight.yangtools.yang.data.api.schema.LeafSetEntryNode;
import org.opendaylight.yangtools.yang.data.api.schema.LeafSetNode;
import org.opendaylight.yangtools.yang.data.api.schema.OrderedLeafSetNode;
import org.opendaylight.yangtools.yang.data.impl.schema.builder.api.ListNodeBuilder;
import org.opendaylight.yangtools.yang.data.impl.schema.builder.api.NormalizedNodeContainerBuilder;
import org.opendaylight.yangtools.yang.data.impl.schema.nodes.AbstractImmutableNormalizedNode;

public class ImmutableOrderedLeafSetNodeBuilder<T> implements ListNodeBuilder<T, LeafSetEntryNode<T>> {
    private Map<NodeWithValue, LeafSetEntryNode<T>> value;
    private NodeIdentifier nodeIdentifier;
    private boolean dirty;

    protected ImmutableOrderedLeafSetNodeBuilder() {
        value = new LinkedHashMap<>();
        dirty = false;
    }

    protected ImmutableOrderedLeafSetNodeBuilder(final ImmutableOrderedLeafSetNode<T> node) {
        nodeIdentifier = node.getIdentifier();
        value = node.getChildren();
        dirty = true;
    }

    public static <T> @NonNull ListNodeBuilder<T, LeafSetEntryNode<T>> create() {
        return new ImmutableOrderedLeafSetNodeBuilder<>();
    }

    public static <T> @NonNull ListNodeBuilder<T, LeafSetEntryNode<T>> create(final LeafSetNode<T> node) {
        if (!(node instanceof ImmutableOrderedLeafSetNode<?>)) {
            throw new UnsupportedOperationException(String.format("Cannot initialize from class %s", node.getClass()));
        }

        return new ImmutableOrderedLeafSetNodeBuilder<>((ImmutableOrderedLeafSetNode<T>) node);
    }

    private void checkDirty() {
        if (dirty) {
            value = new LinkedHashMap<>(value);
            dirty = false;
        }
    }

    @Override
    public ListNodeBuilder<T, LeafSetEntryNode<T>> withChild(final LeafSetEntryNode<T> child) {
        checkDirty();
        this.value.put(child.getIdentifier(), child);
        return this;
    }

    @Override
    public ListNodeBuilder<T, LeafSetEntryNode<T>> withoutChild(final PathArgument key) {
        checkDirty();
        this.value.remove(key);
        return this;
    }

    @Override
    public OrderedLeafSetNode<T> build() {
        dirty = true;
        return new ImmutableOrderedLeafSetNode<>(nodeIdentifier, value);
    }

    @Override
    public ListNodeBuilder<T, LeafSetEntryNode<T>> withNodeIdentifier(final NodeIdentifier withNodeIdentifier) {
        this.nodeIdentifier = withNodeIdentifier;
        return this;
    }

    @Override
    public ListNodeBuilder<T, LeafSetEntryNode<T>> withValue(final Collection<LeafSetEntryNode<T>> withValue) {
        checkDirty();
        for (final LeafSetEntryNode<T> leafSetEntry : withValue) {
            withChild(leafSetEntry);
        }
        return this;
    }

    @Override
    public ListNodeBuilder<T, LeafSetEntryNode<T>> withChildValue(final T childValue) {
        return withChild(ImmutableLeafSetEntryNodeBuilder.<T>create()
            .withNodeIdentifier(new NodeWithValue<>(nodeIdentifier.getNodeType(), childValue))
            .withValue(childValue).build());
    }

    protected static final class ImmutableOrderedLeafSetNode<T> extends
            AbstractImmutableNormalizedNode<NodeIdentifier, Collection<LeafSetEntryNode<T>>> implements
            OrderedLeafSetNode<T> {

        private final Map<NodeWithValue, LeafSetEntryNode<T>> children;

        ImmutableOrderedLeafSetNode(final NodeIdentifier nodeIdentifier,
                final Map<NodeWithValue, LeafSetEntryNode<T>> children) {
            super(nodeIdentifier);
            this.children = children;
        }

        @Override
        public Optional<LeafSetEntryNode<T>> getChild(final NodeWithValue child) {
            return Optional.ofNullable(children.get(child));
        }

        @Override
        public LeafSetEntryNode<T> getChild(final int position) {
            return Iterables.get(children.values(), position);
        }

        @Override
        protected int valueHashCode() {
            return children.hashCode();
        }

        @SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD",
                justification = "https://github.com/spotbugs/spotbugs/issues/811")
        private Map<NodeWithValue, LeafSetEntryNode<T>> getChildren() {
            return Collections.unmodifiableMap(children);
        }

        @Override
        protected boolean valueEquals(final AbstractImmutableNormalizedNode<?, ?> other) {
            return children.equals(((ImmutableOrderedLeafSetNode<?>) other).children);
        }

        @Override
        public int getSize() {
            return children.size();
        }

        @Override
        public Collection<LeafSetEntryNode<T>> getValue() {
            return UnmodifiableCollection.create(children.values());
        }
    }

    @Override
    public NormalizedNodeContainerBuilder<NodeIdentifier, PathArgument, LeafSetEntryNode<T>, LeafSetNode<T>> addChild(
            final LeafSetEntryNode<T> child) {
        return withChild(child);
    }

    @Override
    public NormalizedNodeContainerBuilder<NodeIdentifier, PathArgument, LeafSetEntryNode<T>, LeafSetNode<T>>
            removeChild(final PathArgument key) {
        return withoutChild(key);
    }
}
